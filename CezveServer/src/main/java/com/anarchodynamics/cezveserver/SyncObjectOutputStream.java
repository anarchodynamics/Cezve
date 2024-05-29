/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anarchodynamics.cezveserver;

import java.io.IOException;
import java.io.ObjectOutputStream;


/**
 * @author anon
 */
public class SyncObjectOutputStream 
{
  
  private final ObjectOutputStream oos;

    public SyncObjectOutputStream(ObjectOutputStream outputStream) {
        this.oos = outputStream;
    }
    
     public synchronized void syncedWrite(Object obj) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(this.oos);
        oos.writeObject(obj);
        oos.flush();
    }
    
    public void close() throws IOException
    {
      this.oos.close();
    }


  

}
