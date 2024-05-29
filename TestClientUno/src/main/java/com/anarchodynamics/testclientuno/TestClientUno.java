/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.anarchodynamics.testclientuno;

import com.anarchodynamics.NetMessage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author anon
 */
public class TestClientUno {

  private static final String SESSION_ID_FILE = "Session_ID";
  private static final Logger testLogger = Logger.getLogger(TestClientUno.class.getName());
  private static final String SERVER_ADDRESS = "localhost";
  private static final int PORT = 4443;

  public static void main(String[] args) {

      try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
          ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
          ObjectInputStream in = new ObjectInputStream(socket.getInputStream()); ) {
        
        NetMessage request = sendRegisterMsg("wojciekMann","BadPassword103");
        
        out.writeObject(request);
        
       Object serverResponse = in.readObject();
        
        if(serverResponse instanceof NetMessage)
        {
          if(((NetMessage) serverResponse).getHeader() == "OK")
          {
            testLogger.log(Level.INFO,"You did it!");
          }
          
        }

      } catch (IOException ie) {
        testLogger.log(Level.SEVERE, "Something went wrong.", ie);
      }
      catch(ClassNotFoundException ce)
      {
      testLogger.log(Level.SEVERE, "Something went wrong.", ce);
      }
    }
  

  private static NetMessage sendRegisterMsg(String username, String password) {
    String[] data = {"username:" + username, "password:" + password};
    NetMessage msg = new NetMessage();

    msg.setHeader("REGISTER");
    msg.setMsgArgs(data);

    return msg;
  }
}
