package com.anarchodynamics.cezvegateway;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
//import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
//import java.util.UUID;

public class ServiceRegistry {

    private final Map<String, ServiceInfo> rpcServices;
    private final ManagedChannel channel;

    public ServiceRegistry(String address, int port) {
        // Create a single channel for all services
        channel = ManagedChannelBuilder
                .forAddress(address, port)
                .usePlaintext() // Disable SSL for local testing; enable in production
                .build();
        rpcServices = new ConcurrentHashMap<>();
    }

    /**
     * Registers a service with a unique token.
     *
     * @param serviceName the name of the service to register.
     */
    public boolean registerService(String serviceName, String serviceType, String serviceAddress, int servicePort, String token) {
        try{
        rpcServices.put(token, new ServiceInfo(serviceName, serviceType, serviceAddress,servicePort));
        return true;
        }
        catch(Exception e)
        {
            
        }
        return false;
    }

    /**
     * Retrieves the ServiceInfo associated with a given token.
     *
     * @param token the 24-character token string.
     * @return the ServiceInfo object, or null if not found.
     */
    public ServiceInfo getServiceInfo(String token) {
        return rpcServices.get(token);
    }

    //may not be needed
    /*
    public boolean updateServiceInfo(String token, ServiceInfo updatedInfo)
    {
            ServiceInfo existingInfo = rpcServices.get(token);
        if (existingInfo == null) {
            System.err.println("Service with token " + token + " not found in registry.");
            return false;
        }
    
        // Update fields in the existing ServiceInfo instance
        try{
        existingInfo.serviceName = updatedInfo.serviceName;
        existingInfo.serviceVersion = updatedInfo.serviceVersion;
        existingInfo.cpuUsagePercent = updatedInfo.cpuUsagePercent;
        existingInfo.memoryUsagePercent = updatedInfo.memoryUsagePercent;
        existingInfo.diskUsagePercent = updatedInfo.diskUsagePercent;
        existingInfo.networkInboundKbps = updatedInfo.networkInboundKbps;
        existingInfo.networkOutboundKbps = updatedInfo.networkOutboundKbps;
        existingInfo.activeThreads = updatedInfo.activeThreads;
        existingInfo.availableThreads = updatedInfo.availableThreads;
        existingInfo.openConnections = updatedInfo.openConnections;
        existingInfo.requestsPerSecond = updatedInfo.requestsPerSecond;
        existingInfo.avgRequestLatencyMs = updatedInfo.avgRequestLatencyMs;
        existingInfo.errorRatePercent = updatedInfo.errorRatePercent;
        existingInfo.queueLength = updatedInfo.queueLength;
        existingInfo.avgQueueWaitTimeMs = updatedInfo.avgQueueWaitTimeMs;
        existingInfo.completedTasks = updatedInfo.completedTasks;
        existingInfo.serviceStatus = updatedInfo.serviceStatus;
        existingInfo.errorCount = updatedInfo.errorCount;
        existingInfo.warningCount = updatedInfo.warningCount;
        existingInfo.lastRestartTime = updatedInfo.lastRestartTime;
        existingInfo.uptimeSeconds = updatedInfo.uptimeSeconds;

        return true;
        }
        catch(Exception e)
        {

        }
        return false;
        


    }
    */


    /**
     * Shuts down the shared channel.
     */
    public void shutdownChannel() {
        channel.shutdown();
    }

    /**
     * Gets the shared ManagedChannel.
     *
     * @return the ManagedChannel.
     */
    public ManagedChannel getChannel() {
        return channel;
    }

    /**
     * Removes a service from the registry using its token.
     *
     * @param token the 24-character token string.
     * @return true if the service was removed successfully, false otherwise.
     */
    public boolean removeService(String token) {
        if (rpcServices.containsKey(token)) {
            rpcServices.remove(token);
            return true;
        } else {
            System.err.println("Service with token " + token + " not found in registry.");
            return false;
        }
    }
}

