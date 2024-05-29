package com.anarchodynamics.cezveserver;

import com.anarchodynamics.NetMessage;
import java.io.File;
import java.io.FileInputStream;
import java.util.logging.Level;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * @author anon
 */
public class DownloadRequestHandler implements IRequestHandler {
  private final DB dbInstance;
  private final SyncObjectOutputStream sout;

  public DownloadRequestHandler(DB dbInstance, SyncObjectOutputStream out) {
    this.dbInstance = dbInstance;
    this.sout = out;
  }

  @Override
  public void respond(NetMessage request) {
    String sessionId = Utils.MsgfindKey(request.msgArgs, "SESSION_ID");
    String relfilePath = Utils.MsgfindKey(request.msgArgs, "PATH");

    if (SessionHandler.isValidSession(sessionId)) {
      int queryid = SessionHandler.getUserId(sessionId);
      if (dbInstance.getFileAccess(queryid, relfilePath)) {

        String absfilePath =
            "UserStore" + "/" + dbInstance.getUserPath(queryid) + "/" + relfilePath;
        File file = new File(absfilePath);
        long fileSizeInBytes = file.length();
        long fileSizeInMB = fileSizeInBytes / (1024 * 1024);
        if (fileSizeInMB > 2000) {
          sendChunkedFile(file);
        } else {
          try {
            NetMessage response = new NetMessage();
            response.header = "UPLOAD";
            response.optionalData = Files.readAllBytes(file.toPath());
            this.sout.syncedWrite(response);
            requestLogger.log(Level.INFO,"A file was sent");
          } catch (IOException e) {
            requestLogger.log(Level.SEVERE, "Sending a file failed!", e);
          }
        }
      }
    }
  }

  private void sendChunkedFile(File file) {
    final int bufferSize = 2000 * 1024 * 1024;

    try (FileInputStream fileInputStream = new FileInputStream(file)) {
      byte[] buffer = new byte[bufferSize];
      int bytesRead;
      long filePtr = 0;

      // Read and send file data in chunks
      while ((bytesRead = fileInputStream.read(buffer)) != -1) {
        // Create a message with file data and FILE_PTR
        NetMessage message = new NetMessage();
        message.header = "UPLOAD";
        message.msgArgs = new String[] {"FILE_PTR:" + filePtr};
        message.optionalData = Arrays.copyOf(buffer, bytesRead);

        this.sout.syncedWrite(message);

        // Update the file pointer
        filePtr += bytesRead;
      }
      requestLogger.log(Level.INFO, "A large file was sent");
    } catch (IOException e) {
      requestLogger.log(Level.SEVERE, "Sending a file failed!", e);
    }
  }
}
