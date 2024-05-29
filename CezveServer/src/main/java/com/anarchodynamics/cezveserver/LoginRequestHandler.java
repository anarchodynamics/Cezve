/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anarchodynamics.cezveserver;

import com.anarchodynamics.NetMessage;
import static com.anarchodynamics.cezveserver.IRequestHandler.requestLogger;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 * @author anon
 */
public class LoginRequestHandler implements IRequestHandler {

  private final DB localdb;
  private final SyncObjectOutputStream sout;

  public LoginRequestHandler(DB dbInstance, SyncObjectOutputStream out) {
    this.localdb = dbInstance;
    this.sout = out;
  }

  @Override
  public void respond(NetMessage request) {
    String loginData[] = request.msgArgs;
    String username;
    String password;
    

    try(ResultSet userData = localdb.getUserRecords();) {
      username = Utils.MsgfindKey(loginData, "username");
      password = Utils.MsgfindKey(loginData, "password");

      while (userData.next()) {
        String userNameFound = userData.getString("User");
        String passwordFound = userData.getString("Password");

        if (username.equals(userNameFound) && password.equals(passwordFound)) {
          int userId = userData.getInt("UserID");
          String userID = Integer.toString(userId);
          NetMessage response = new NetMessage();
          String newSessionId = Utils.rdm24byteString();
          response.header = "OK";
          response.msgArgs = new String[]{"SESSION_ID:"+newSessionId};
          SessionHandler.appendSession(userID+":"+newSessionId);
        try
        {
          sout.syncedWrite(response);
        }
        catch(IOException e)
        {
          requestLogger.log(Level.SEVERE, "Sending a login response failed!", e);
        }
        }
      }

    } catch (RuntimeException r) {
       requestLogger.log(Level.SEVERE, "Login handler failed unexpectedly at runtime!", r);
    }
    catch(SQLException e)
    {
      requestLogger.log(Level.WARNING,"The login handler failed to carry out an SQL operation of some kind.", e);
    }

  }
}
