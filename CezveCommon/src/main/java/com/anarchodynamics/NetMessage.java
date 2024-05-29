/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anarchodynamics;

import java.io.Serializable;

/**
 * @author anon
 */
public class NetMessage implements Serializable 
{
  static final long serialVersionUID = 1L;

    public String header;
    public String[] msgArgs;
    public byte[] optionalData;

    

}
