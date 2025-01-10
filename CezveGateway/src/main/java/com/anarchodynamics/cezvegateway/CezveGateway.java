package com.anarchodynamics.cezvegateway;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.grpc.Grpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;

/**
 * @author anon
 */
public class CezveGateway {

    private final ExecutorService serviceClientPool;
    private final ServiceRegistry registry;

    private final int port;

    private final Server gatewayServer;

    private static final int THREAD_MAX = Runtime.getRuntime().availableProcessors() * 2;

    public CezveGateway(ServiceRegistry registry, int port) throws IOException {

        this.gatewayServer = ServerBuilder.forPort(port)
                                        .addService(new RegisterServiceImpl(registry))
                                        .build()
                                        .start();

        this.port = port;
        this.registry = registry;

        this.serviceClientPool = new ThreadPoolExecutor(
                THREAD_MAX, THREAD_MAX,
                9600, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());

    }

    public static void main(String[] args) throws IOException {

    }
}
