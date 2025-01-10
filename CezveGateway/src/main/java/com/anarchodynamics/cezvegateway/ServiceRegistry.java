package com.anarchodynamics.cezvegateway;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import cezve.grpc.ServiceType;


//import java.util.UUID;

public class ServiceRegistry {

    private final Map<String, ServiceInfo> rpcServices;
    private static final Logger registryLogger = LogAgent.getLogger(ServiceRegistry.class);


    public ServiceRegistry() {
        rpcServices = new ConcurrentHashMap<>();
    }

    /**
     * Registers a service with a unique token.
     *
     * @param serviceName the name of the service to register.
     */
    public boolean registerService(String serviceName, ServiceType serviceType, String serviceAddress, int servicePort, String token) {
        try{
        rpcServices.put(token, new ServiceInfo(serviceName, serviceType, serviceAddress,servicePort));
        return true;
        }
        catch(Exception e)
        {
            registryLogger.log(Level.WARNING, () -> "Registration of service " + serviceName + "with address: " + serviceAddress + " failed.");
            registryLogger.log(Level.WARNING, "Exception caught: ", e);
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

