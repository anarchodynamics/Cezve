package com.anarchodynamics.cezvegateway;
import java.time.Instant;

public class ServiceInfo {
    // Private fields
    private String serviceName;
    private String serviceType;
    private String serviceVersion;
    private String serviceAddress;
    private int servicePort;

    private Double cpuUsagePercent;
    private Double memoryUsagePercent;
    private Double diskUsagePercent;
    private Double networkInboundKbps;
    private Double networkOutboundKbps;

    private Integer activeThreads;
    private Integer availableThreads;
    private Integer openConnections;

    private Double requestsPerSecond;
    private Double avgRequestLatencyMs;
    private Double errorRatePercent;

    private Integer queueLength;
    private Double avgQueueWaitTimeMs;
    private Long completedTasks;

    private String serviceStatus;
    private Integer errorCount;
    private Integer warningCount;
    private Instant lastRestartTime;
    private Long uptimeSeconds;

    // Constructor
    public ServiceInfo(String serviceNameInit, String serviceTypeInit, String serviceAddress, int port) {
        this.serviceName = serviceNameInit;
        this.serviceType = serviceTypeInit;
        this.serviceAddress = serviceAddress;
        this.servicePort = port;
    }

    // Synchronized getters and setters
    public synchronized String getServiceName() {
        return serviceName;
    }

    public synchronized void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public synchronized String getServiceType() {
        return serviceType;
    }

    public synchronized void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public synchronized String getServiceVersion() {
        return serviceVersion;
    }

    public synchronized void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public synchronized String getServiceAddress() {
        return serviceAddress;
    }

    public synchronized void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public synchronized int getServicePort() {
        return servicePort;
    }

    public synchronized void setServicePort(int servicePort) {
        this.servicePort = servicePort;
    }

    public synchronized Double getCpuUsagePercent() {
        return cpuUsagePercent;
    }

    public synchronized void setCpuUsagePercent(Double cpuUsagePercent) {
        this.cpuUsagePercent = cpuUsagePercent;
    }

    public synchronized Double getMemoryUsagePercent() {
        return memoryUsagePercent;
    }

    public synchronized void setMemoryUsagePercent(Double memoryUsagePercent) {
        this.memoryUsagePercent = memoryUsagePercent;
    }

    public synchronized Double getDiskUsagePercent() {
        return diskUsagePercent;
    }

    public synchronized void setDiskUsagePercent(Double diskUsagePercent) {
        this.diskUsagePercent = diskUsagePercent;
    }

    public synchronized Double getNetworkInboundKbps() {
        return networkInboundKbps;
    }

    public synchronized void setNetworkInboundKbps(Double networkInboundKbps) {
        this.networkInboundKbps = networkInboundKbps;
    }

    public synchronized Double getNetworkOutboundKbps() {
        return networkOutboundKbps;
    }

    public synchronized void setNetworkOutboundKbps(Double networkOutboundKbps) {
        this.networkOutboundKbps = networkOutboundKbps;
    }

    public synchronized Integer getActiveThreads() {
        return activeThreads;
    }

    public synchronized void setActiveThreads(Integer activeThreads) {
        this.activeThreads = activeThreads;
    }

    public synchronized Integer getAvailableThreads() {
        return availableThreads;
    }

    public synchronized void setAvailableThreads(Integer availableThreads) {
        this.availableThreads = availableThreads;
    }

    public synchronized Integer getOpenConnections() {
        return openConnections;
    }

    public synchronized void setOpenConnections(Integer openConnections) {
        this.openConnections = openConnections;
    }

    public synchronized Double getRequestsPerSecond() {
        return requestsPerSecond;
    }

    public synchronized void setRequestsPerSecond(Double requestsPerSecond) {
        this.requestsPerSecond = requestsPerSecond;
    }

    public synchronized Double getAvgRequestLatencyMs() {
        return avgRequestLatencyMs;
    }

    public synchronized void setAvgRequestLatencyMs(Double avgRequestLatencyMs) {
        this.avgRequestLatencyMs = avgRequestLatencyMs;
    }

    public synchronized Double getErrorRatePercent() {
        return errorRatePercent;
    }

    public synchronized void setErrorRatePercent(Double errorRatePercent) {
        this.errorRatePercent = errorRatePercent;
    }

    public synchronized Integer getQueueLength() {
        return queueLength;
    }

    public synchronized void setQueueLength(Integer queueLength) {
        this.queueLength = queueLength;
    }

    public synchronized Double getAvgQueueWaitTimeMs() {
        return avgQueueWaitTimeMs;
    }

    public synchronized void setAvgQueueWaitTimeMs(Double avgQueueWaitTimeMs) {
        this.avgQueueWaitTimeMs = avgQueueWaitTimeMs;
    }

    public synchronized Long getCompletedTasks() {
        return completedTasks;
    }

    public synchronized void setCompletedTasks(Long completedTasks) {
        this.completedTasks = completedTasks;
    }

    public synchronized String getServiceStatus() {
        return serviceStatus;
    }

    public synchronized void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public synchronized Integer getErrorCount() {
        return errorCount;
    }

    public synchronized void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public synchronized Integer getWarningCount() {
        return warningCount;
    }

    public synchronized void setWarningCount(Integer warningCount) {
        this.warningCount = warningCount;
    }

    public synchronized Instant getLastRestartTime() {
        return lastRestartTime;
    }

    public synchronized void setLastRestartTime(Instant lastRestartTime) {
        this.lastRestartTime = lastRestartTime;
    }

    public synchronized Long getUptimeSeconds() {
        return uptimeSeconds;
    }

    public synchronized void setUptimeSeconds(Long uptimeSeconds) {
        this.uptimeSeconds = uptimeSeconds;
    }
}



