/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.anarchodynamics.cezveserver;

import java.io.File;
import java.io.IOException;

/**
 * @author anon
 */
public class CezveServer {

  private static Server serverInstance;

  public static void main(String[] args) throws IOException {
    if (args.length == 0) {
      serverInstance = new Server(4443);

    } else if (args.length == 2 && args[0].equals("--DBPath")) {
      String dbPath = args[1];
      File DBFile = new File(dbPath);
      String fileName = DBFile.getName();
      String DBURL = "jdbc:sqlite:" + fileName;
      int dotIndex = fileName.lastIndexOf('.');
      if (dotIndex > 0) {
        fileName = fileName.substring(0, dotIndex);
      }
      DB dbInstance = new DB(DBURL, fileName);
      serverInstance = new Server(4443);
      serverInstance.setDatabase(dbInstance);
      serverInstance.startServer();
    } else {
      System.out.println("Usage:");
      System.out.println("1. To start the server: java Cezveserver");
      System.out.println("2. To specify a file: java Cezveserver --DBPath <path>");
    }
  }
}
