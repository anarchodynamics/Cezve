/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anarchodynamics.cezveserver;

import com.anarchodynamics.NetMessage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.io.RandomAccessFile;

/**
 * @author anon
 */
public class UploadRequestHandler implements IRequestHandler {
  private final DB dbInstance;
  private final SyncObjectOutputStream sout;
  

  public UploadRequestHandler(DB dbInstance, SyncObjectOutputStream out) {
    this.dbInstance = dbInstance;
    this.sout = out;
  }

  @Override
  public void respond(NetMessage request) {
    String sessionId = Utils.MsgfindKey(request.msgArgs, "SESSION_ID");
    String relfilePath = Utils.MsgfindKey(request.msgArgs, "PATH");
    long filepointer = 0;
    if (!Utils.MsgfindKey(request.msgArgs, "FILE_PTR").isEmpty()) {
      filepointer = Long.parseLong(Utils.MsgfindKey(request.msgArgs, "FILE_PTR"));
    }
    String absfilePath;

    if (SessionHandler.isValidSession(sessionId)) {
      int queryid = SessionHandler.getUserId(sessionId);
      if (!dbInstance.getUserPath(queryid).contains("NONE") && !dbInstance.getUserPath(queryid).isEmpty()) {
        absfilePath = "UserStore" + "/" + dbInstance.getUserPath(queryid) + "/" + relfilePath;
        saveFile(absfilePath, request.optionalData, filepointer);
        NetMessage response = new NetMessage();
        response.header = "OK";
        try
        {
          sout.syncedWrite(response);
        }
        catch(IOException e)
        {
          requestLogger.log(Level.SEVERE, "Sending an upload response failed!", e);
        }
      }
    }
  }

  private void saveFile(String path, byte[] data, long fileptr) {
    try {
      // Check if the file exists, create it if it doesn't
      File file = new File(path);
      if (!file.exists()) {
        file.createNewFile();
      }

      // Open the file in read-write mode
      try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
        // Move the file pointer to the specified position
        raf.seek(fileptr);
        // Write the data to the file
        raf.write(data);
      }
    } catch (IOException e) {
      requestLogger.log(Level.SEVERE, "Saving a file failed!", e);
    }
  }
}
