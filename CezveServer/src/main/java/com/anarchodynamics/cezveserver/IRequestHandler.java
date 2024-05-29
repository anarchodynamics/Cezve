/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.anarchodynamics.cezveserver;
import com.anarchodynamics.NetMessage;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author anon
 */
public interface IRequestHandler  {

    static Logger requestLogger = Logger.getLogger(IRequestHandler.class.getName());
   
    void respond(NetMessage request) throws IOException;
    
    
    
  }




