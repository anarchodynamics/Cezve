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


public class RegisterServiceImpl extends RegisterGrpc.RegisterImplBase {

    private ServiceRegistry registry;
    private Logger registerLogger = LogAgent.getLogger(RegisterServiceImpl.class);
    private final ExecutorService localexec;


    public RegisterServiceImpl(ServiceRegistry registryInstance, ExecutorService ex)
    {
        this.registry = registryInstance;
        this.localexec = ex;
    }

    private RegisterResponse createResponse(boolean status, String token) {
        return RegisterResponse.newBuilder()
            .setReceiverName("API Gateway")
            .setRegisteredStatus(status)
            .setServiceToken(token)
            .build();
    } 


    @Override
    public void registerRequest(SendRegisterReq request, StreamObserver<RegisterResponse> responseObserver) {
 
        String serviceType = request.getServiceType();
        String serviceName = request.getServiceName();
        String serviceAddress = request.getServiceAddress();
        int servicePort = request.getServicePort();

        try
        {
            String token = UUID.randomUUID().toString().replace("-", "").substring(0, 24);
            this.registry.registerService(serviceName,serviceType,serviceAddress, servicePort, token);


        }
        catch(Exception e)
        {
            registerLogger.log(Level.SEVERE,"Registration of: " + serviceName + " failed", e);
        }
        
           // RegisterResponse response = RegisterResponse.newBuilder()
           // .setReceiverName("API Gateway") 
           // .setRegisteredStatus(registeredStatus)      
           // .setServiceToken("0") 
           // .build();

                    // Send the response
       // responseObserver.onNext(response);
       // responseObserver.onCompleted();
    }
    
@Override
public void updateServiceStatus(ServiceUpdate updateReq, StreamObserver<UpdateResponse> response) {
    String serviceToken = updateReq.getServiceToken();

    // Retrieve the ServiceInfo object from the registry
    ServiceInfo serviceInfo = registry.getServiceInfo(serviceToken);

    if (serviceInfo == null) {
        registerLogger.log(Level.WARNING,"Error finding service with token: " + serviceToken);
        return;
    }

    // Update fields from the Protobuf message
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
    serviceInfo.setServiceStatus(updateReq.getServiceStatus());
    serviceInfo.setErrorCount(updateReq.getErrorCount());
    serviceInfo.setWarningCount(updateReq.getWarningCount());
    serviceInfo.setUptimeSeconds(updateReq.getUptimeSeconds());

    if (!updateReq.getLastRestartTime().isEmpty()) {
        serviceInfo.setLastRestartTime(Instant.parse(updateReq.getLastRestartTime()));
    }

    serviceInfo.setUptimeSeconds(updateReq.getUptimeSeconds());

    // Respond with success
    UpdateResponse responseMessage = UpdateResponse.newBuilder()
        .setReceiverName("API Gateway")
        .setResponseStatus("Update successful.")
        .build();
    response.onNext(responseMessage);
    response.onCompleted();
}


}