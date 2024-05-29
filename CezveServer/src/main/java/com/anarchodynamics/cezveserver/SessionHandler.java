/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.anarchodynamics.cezveserver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author anon
 */
public class SessionHandler {

  private static Logger SessionLogger = Logger.getLogger(SessionHandler.class.getName());

  public static void appendSession(String sessionId) {
    try (FileWriter writer = new FileWriter("Sessions", true)) {
      writer.write(sessionId + "\n");
    } catch (IOException e) {
      SessionLogger.log(Level.SEVERE, "Appending a session failed.", e);
    }
  }

  public static boolean isValidSession(String sessionId) {
    try (BufferedReader reader = new BufferedReader(new FileReader("Sessions"))) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.contains(sessionId)) {
          return true;
        }
      }

    } catch (IOException e) {
      SessionLogger.log(Level.SEVERE, "Validating a session failed.", e);
    }
    return false;
  }

  public static int getUserId(String sessionId) {
    int userId = -1; // Default value if session is not found or invalid
    try (BufferedReader reader = new BufferedReader(new FileReader("Sessions"))) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.contains("sessionId")) {
          // Split the line to extract the first character as integer
          userId = Character.getNumericValue(line.charAt(0));
          break;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return userId;
  }
}
