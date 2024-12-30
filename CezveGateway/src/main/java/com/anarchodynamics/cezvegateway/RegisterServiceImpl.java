package com.anarchodynamics.cezvegateway;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
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

public class RegisterServiceImpl extends RegisterGrpc.RegisterImplBase {

    private ServiceRegistry registry;
    private Logger registerLogger;


    public RegisterServiceImpl(ServiceRegistry registryInstance)
    {
        this.registry = registryInstance;
    }

    @Override
    public void registerRequest(SendRegisterReq request, StreamObserver<RegisterResponse> responseObserver) {
 
        String serviceType = request.getServiceType();
        String serviceName = request.getServiceName();

        boolean registeredStatus = false;


        String token = UUID.randomUUID().toString().replace("-", "").substring(0, 24);
        try
        {
            this.registry.registerService(serviceName,serviceType, token);

            registeredStatus = true;

        }
        catch(Exception e)
        {
            registerLogger.log(Level.SEVERE,"Registration of: " + serviceName + " failed", e);
        }
        

        //response will go ahead one way or another
        //this current implementation is to ensure the service knows whether or not to try again
        //WIP?
        if(registeredStatus == true)
        {
            RegisterResponse response = RegisterResponse.newBuilder()
            .setReceiverName("API Gateway") 
            .setRegisteredStatus(registeredStatus)      
            .setServiceToken(token) 
            .build();

                    // Send the response
        responseObserver.onNext(response);
        responseObserver.onCompleted();


        }
        else
        {
            RegisterResponse response = RegisterResponse.newBuilder()
            .setReceiverName("API Gateway") 
            .setRegisteredStatus(registeredStatus)      
            .setServiceToken("0") 
            .build();

                    // Send the response
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        }




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
    serviceInfo.serviceName = updateReq.getServiceName();
    serviceInfo.serviceVersion = updateReq.getServiceVersion();
    serviceInfo.cpuUsagePercent = updateReq.getCpuUsagePercent();
    serviceInfo.memoryUsagePercent = updateReq.getMemoryUsagePercent();
    serviceInfo.diskUsagePercent = updateReq.getDiskUsagePercent();
    serviceInfo.networkInboundKbps = updateReq.getNetworkInboundKbps();
    serviceInfo.networkOutboundKbps = updateReq.getNetworkOutboundKbps();
    serviceInfo.activeThreads = updateReq.getActiveThreads();
    serviceInfo.availableThreads = updateReq.getAvailableThreads();
    serviceInfo.openConnections = updateReq.getOpenConnections();
    serviceInfo.requestsPerSecond = updateReq.getRequestsPerSecond();
    serviceInfo.avgRequestLatencyMs = updateReq.getAvgRequestLatencyMs();
    serviceInfo.errorRatePercent = updateReq.getErrorRatePercent();
    serviceInfo.queueLength = updateReq.getQueueLength();
    serviceInfo.avgQueueWaitTimeMs = updateReq.getAvgQueueWaitTimeMs();
    serviceInfo.completedTasks = updateReq.getCompletedTasks();
    serviceInfo.serviceStatus = updateReq.getServiceStatus();
    serviceInfo.errorCount = updateReq.getErrorCount();
    serviceInfo.warningCount = updateReq.getWarningCount();

    if (!updateReq.getLastRestartTime().isEmpty()) {
        serviceInfo.lastRestartTime = Instant.parse(updateReq.getLastRestartTime());
    }

    serviceInfo.uptimeSeconds = updateReq.getUptimeSeconds();

    // Respond with success
    UpdateResponse responseMessage = UpdateResponse.newBuilder()
        .setReceiverName("API Gateway")
        .setResponseStatus("Update successful.")
        .build();
    response.onNext(responseMessage);
    response.onCompleted();
}


}