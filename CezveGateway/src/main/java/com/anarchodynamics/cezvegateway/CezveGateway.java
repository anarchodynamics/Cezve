package com.anarchodynamics.cezvegateway;


import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;



/**
 * @author anon
 */
public class CezveGateway {

    private final ExecutorService userClientPool;
    private final ExecutorService serviceClientPool;
    private final ServiceRegistry registry;

    private final int port = 4444;
    

    private static final int USER_THREADS = Runtime.getRuntime().availableProcessors() * 2;
    private static final int SERVICE_THREADS = Runtime.getRuntime().availableProcessors();

    public CezveGateway(ServiceRegistry registry) {
        this.registry = registry;
        this.userClientPool = new ThreadPoolExecutor(
            USER_THREADS, USER_THREADS,
            9600, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());


        this.serviceClientPool = new ThreadPoolExecutor(
            SERVICE_THREADS, SERVICE_THREADS,
            9600, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());
    }
    


  public static void main(String[] args) throws IOException {

      
  }
}
