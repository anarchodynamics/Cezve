package com.anarchodynamics.cezvegateway;


import cezve.grpc.RegisterGrpc;
import cezve.grpc.SendRegisterReq;
import cezve.grpc.RegisterResponse;
import cezve.grpc.ServiceUpdate;
import cezve.grpc.UpdateResponse;
import io.grpc.stub.StreamObserver;

import java.time.Instant;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.CompletableFuture;

import cezve.grpc.ServiceType;


public class RegisterServiceImpl extends RegisterGrpc.RegisterImplBase {

    private ServiceRegistry registry;
    private Logger registerLogger = LogAgent.getLogger(RegisterServiceImpl.class);
    private final ExecutorService localexec;


    public RegisterServiceImpl(ServiceRegistry registryInstance, ExecutorService ex)
    {
        this.registry = registryInstance;
        this.localexec = ex;
    }

    private RegisterResponse createRegisterResponse(boolean status, String token) {
        return RegisterResponse.newBuilder()
            .setReceiverName("API Gateway")
            .setRegisteredStatus(status)
            .setServiceToken(token)
            .build();
    } 


    @Override
    public void registerRequest(SendRegisterReq request, StreamObserver<RegisterResponse> responseObserver) {
 
        ServiceType serviceType = request.getServiceType();
        String serviceName = request.getServiceName();
        String serviceAddress = request.getServiceAddress();
        int servicePort = request.getServicePort();


        CompletableFuture.supplyAsync(() -> {
        try
        {
            String token = UUID.randomUUID().toString().replace("-", "").substring(0, 24);
            this.registry.registerService(serviceName,serviceType,serviceAddress, servicePort, token);
            return createRegisterResponse(true, token);
        }
        catch(Exception e)
        {
            registerLogger.log(Level.WARNING,"Registration of: " + serviceName + " failed", e);
            return createRegisterResponse(false, "0");
        }
        
           // RegisterResponse response = RegisterResponse.newBuilder()
           // .setReceiverName("API Gateway") 
           // .setRegisteredStatus(registeredStatus)      
           // .setServiceToken("0") 
           // .build();

                    // Send the response
       // responseObserver.onNext(response);
       // responseObserver.onCompleted();


    }, localexec).whenComplete((response,throwable) -> 
    {
        if (throwable != null) {
            registerLogger.log(Level.WARNING, "Unexpected error during registration", throwable);
            responseObserver.onNext(createRegisterResponse(false, "0"));
        } else {
            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
    });
}
    

private UpdateResponse createUpdateResponse(boolean status)
{        return UpdateResponse.newBuilder()
    .setReceiverName("API Gateway")
    .setResponseStatus(status)
    .build();

}
@Override
public void updateServiceStatus(ServiceUpdate updateReq, StreamObserver<UpdateResponse> responseObserver) {
    String serviceToken = updateReq.getServiceToken();

    // Retrieve the ServiceInfo object from the registry
    ServiceInfo serviceInfo = registry.getServiceInfo(serviceToken);

    if (serviceInfo == null) {
        registerLogger.log(Level.WARNING,"Error finding service with token: " + serviceToken);
        return;
    }
    CompletableFuture.supplyAsync(() -> { 
    try
    {
    serviceInfo.setServiceName(updateReq.getServiceName());
    serviceInfo.setServiceVersion(updateReq.getServiceVersion());
    serviceInfo.setCpuUsagePercent(updateReq.getCpuUsagePercent());
    serviceInfo.setMemoryUsagePercent(updateReq.getMemoryUsagePercent());
    serviceInfo.setDiskUsagePercent(updateReq.getDiskUsagePercent());
    serviceInfo.setNetworkInboundKbps(updateReq.getNetworkInboundKbps());
    serviceInfo.setNetworkOutboundKbps(updateReq.getNetworkOutboundKbps());
    serviceInfo.setActiveThreads(updateReq.getActiveThreads());
    serviceInfo.setAvailableThreads(updateReq.getAvailableThreads());
    serviceInfo.setOpenConnections(updateReq.getOpenConnections());
    serviceInfo.setRequestsPerSecond(updateReq.getRequestsPerSecond());
    serviceInfo.setAvgRequestLatencyMs(updateReq.getAvgRequestLatencyMs());
    serviceInfo.setErrorRatePercent(updateReq.getErrorRatePercent());
    serviceInfo.setQueueLength(updateReq.getQueueLength());
    serviceInfo.setAvgQueueWaitTimeMs(updateReq.getAvgQueueWaitTimeMs());
    serviceInfo.setCompletedTasks(updateReq.getCompletedTasks());
    serviceInfo.setErrorCount(updateReq.getErrorCount());
    serviceInfo.setWarningCount(updateReq.getWarningCount());
    serviceInfo.setUptimeSeconds(updateReq.getUptimeSeconds());

    if (!updateReq.getLastRestartTime().isEmpty()) {
        serviceInfo.setLastRestartTime(Instant.parse(updateReq.getLastRestartTime()));
    }
    return createUpdateResponse(true);

    }

    catch(Exception e)
    {
        registerLogger.log(Level.WARNING, "Updating a service object failed with name " + updateReq.getServiceName(), e);
        return createUpdateResponse(false);

    }},localexec).whenComplete((response,throwable) ->
    {
        if (throwable != null) {
            registerLogger.log(Level.WARNING, "Unexpected error during update ", throwable);
            responseObserver.onNext(createUpdateResponse(false));
        } else {
            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
    });

}


}