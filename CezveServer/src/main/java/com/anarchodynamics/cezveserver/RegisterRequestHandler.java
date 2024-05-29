/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anarchodynamics.cezveserver;

import com.anarchodynamics.NetMessage;
import java.util.logging.Level;
import java.io.IOException;

/**
 * @author anon
 */
public class RegisterRequestHandler implements IRequestHandler {
  private final DB dbInstance;
  private final SyncObjectOutputStream sout;
  public RegisterRequestHandler(DB dbInstance, SyncObjectOutputStream out)
  {
    this.dbInstance = dbInstance;
    this.sout = out;
  }
  
  @Override
  public void respond(NetMessage request) {
    String registerData[] = request.msgArgs;
    String username;
    String password;
    try {
      username = Utils.MsgfindKey(registerData, "username");
      password = Utils.MsgfindKey(registerData, "password");

      String TopLevelDirectoryName = username + Utils.rdm24byteString();

      if (this.dbInstance.addUser(username,password,TopLevelDirectoryName))
      {
        NetMessage response = new NetMessage();
        response.header="OK";
        try
        {
          sout.syncedWrite(response);
        }
        catch(IOException e)
        {
          requestLogger.log(Level.SEVERE, "Sending a register response failed!", e);
        }
      }

    } catch (RuntimeException r) {
      requestLogger.log(Level.SEVERE, "Register handler failed unexpectedly at runtime!", r);
    }

  }
}
