/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.anarchodynamics.cezveserver;

import com.anarchodynamics.NetMessage;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Server {

  private ServerSocket serverSocket;
  private int port;
  private Logger serverLogger;
  private DB databaseInstance;
  private Map<String, IRequestHandler> requestHandlers;
  private ExecutorService writerPool;
  private ExecutorService readerPool;
  

  public Server(int port) {
    if (port <= 0) {
      throw new IllegalArgumentException("Invalid port number assigned.");
    }
    this.port = port;
    this.serverLogger = Logger.getLogger(Server.class.getName());

  }

  public void setDatabase(DB dbInstance) {
    this.databaseInstance = dbInstance;
  }
  

  public void stop() throws IOException {
    this.serverSocket.close();
  }

  public void startServer() throws IOException {
    this.serverSocket = new ServerSocket(this.port);
    Socket clientSocket = serverSocket.accept();
    ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
    SyncObjectOutputStream syncedout = new SyncObjectOutputStream (oos);
     this.writerPool = Executors.newFixedThreadPool(5);
     this.readerPool = Executors.newCachedThreadPool();
     requestHandlers = new HashMap<>();
     requestHandlers.put("REGISTER", new RegisterRequestHandler(this.databaseInstance,syncedout ));
     requestHandlers.put("LOGIN", new LoginRequestHandler(this.databaseInstance,syncedout ));
     requestHandlers.put("UPLOAD", new UploadRequestHandler(this.databaseInstance,syncedout ));
     requestHandlers.put("DOWNLOAD", new DownloadRequestHandler(this.databaseInstance,syncedout ));
     
     
 
  
  }

 
  
 
}
/*
     private Map<String, IRequestHandler> requestHandlers;
     requestHandlers = new HashMap<>();
     requestHandlers.put("REGISTER", new RegisterRequestHandler());
     requestHandlers.put("LOGIN", new LoginRequestHandler());
     requestHandlers.put("UPLOAD", new UploadRequestHandler());

         if (requestHandlers.containsKey(header)) {
            IRequestHandler handler = requestHandlers.get(header);

            
          }
*/

/* 
  
  

*/