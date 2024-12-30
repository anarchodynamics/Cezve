package com.anarchodynamics.cezvegateway;
import java.time.Instant;

public class ServiceInfo
{
    public ServiceInfo(String serviceNameInit, String serviceTypeInit)
    {
        this.serviceName = serviceNameInit;
        this.serviceType = serviceTypeInit;

    }
    public String serviceName;
    public String serviceType; //descriptor for matching services to clients etc.
    public String serviceVersion;
    
    public Double cpuUsagePercent;
    public Double memoryUsagePercent;
    public Double diskUsagePercent;
    public Double networkInboundKbps;
    public Double networkOutboundKbps;

    //Thread and connection metrics

    public Integer activeThreads;
    public Integer availableThreads;
    public Integer openConnections;

    // Request and throughput metrics
    public Double requestsPerSecond;
    public Double avgRequestLatencyMs;
    public Double errorRatePercent;

    // Queue and task metrics
    public Integer queueLength;
    public Double avgQueueWaitTimeMs;
    public Long completedTasks;

    // Health and status
    public String serviceStatus; // "HEALTHY", "DEGRADED", "UNAVAILABLE"
    public Integer errorCount;
    public Integer warningCount;
    public Instant lastRestartTime; // ISO 8601 format
    public Long uptimeSeconds;

}