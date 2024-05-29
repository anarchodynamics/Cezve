/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anarchodynamics.cezveserver;


import java.security.SecureRandom;
import java.util.Base64;
//import java.util.logging.Level;
//import java.util.logging.Logger;

/**
 * @author anon
 */
public class Utils {
  // private static Logger UtilsLogger = Logger.getLogger(Utils.class.getName());

  public static String MsgfindKey(String[] input, String key) {
    for (String str : input) {
      // Check if the string contains a colon
      if (str.contains(":")) {
        // Split the string into key and value parts
        String[] parts = str.split(":", 2);
        // Trim key part and compare it with the target key (case-insensitive)
        if (parts.length == 2 && parts[0].trim().equalsIgnoreCase(key)) {
          // Return the value if key matches
          return parts[1].trim();
        }
      }
    }
    // Return null if key is not found
    return null;
  }

  public static String rdm24byteString() {
    SecureRandom secureRandom = new SecureRandom();
    byte[] randombytes = new byte[24];
    secureRandom.nextBytes(randombytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(randombytes);
  }
  
  
  
  
}
