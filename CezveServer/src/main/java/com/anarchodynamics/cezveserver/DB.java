package com.anarchodynamics.cezveserver;

import java.sql.Connection;
import java.util.Objects;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author anon
 */
public class DB {

  private String DBURL;
  private int timeout = 300;
  private String databaseName;
  private Logger DBLogger;

  DB(String URL, String DBName) {

    Objects.requireNonNull(URL, "Database URL cannot be empty!");
    Objects.requireNonNull(DBName, "Database name must be set.");
    this.DBLogger = Logger.getLogger(DB.class.getName());

    this.DBURL = URL;
    this.databaseName = DBName;

    this.DBLogger.info("Database name set to:" + this.databaseName);
    this.DBLogger.info("Database URL is:" + this.DBURL);
  }

  public void setQueryTimeout(int timeout) {
    this.timeout = timeout;
  }

  public boolean addFile(int userId, String filepath) {
    String query = "INSERT INTO Files (FileOwner,FilePath) VALUES (?)";
    try (Connection connection = DriverManager.getConnection(DBURL);
        PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setQueryTimeout(this.timeout);
      stmt.setInt(1, userId);
      stmt.setString(2, filepath);
      if (stmt.executeUpdate() == 1) {
        return true;
      }
    } catch (SQLException e) {
      this.DBLogger.log(Level.SEVERE, "Adding a file record failed.", e);
    }
    return false;
  }

  public boolean addUser(String username, String password, String dirpath) {
    String query = "INSERT INTO Users (User,Password,UserIsAdmin,TopLevelDirectoryPath) VALUES (?)";
    try (Connection connection = DriverManager.getConnection(DBURL);
        PreparedStatement stmt = connection.prepareStatement(query)) {

      stmt.setQueryTimeout(this.timeout);
      stmt.setString(1, username);
      stmt.setString(2, password);
      stmt.setBoolean(3, false);
      stmt.setString(4, dirpath);
      if (stmt.executeUpdate() == 1) {
        return true;
      }
    } catch (SQLException e) {
      this.DBLogger.log(Level.SEVERE, "Adding a user failed.", e);
    }
    return false;
  }

  public ResultSet getUserRecords() {
    try (Connection connection = DriverManager.getConnection(DBURL);
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Users"); ) {
      stmt.setQueryTimeout(this.timeout);
      try (ResultSet rs = stmt.executeQuery()) {
        return rs;
      }
    } catch (SQLException e) {
      this.DBLogger.log(Level.SEVERE, "Getting users failed.", e);
    }
    return null;
  }

  public ResultSet getFileRecords() {
    try (Connection connection = DriverManager.getConnection(DBURL);
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Files"); ) {

      stmt.setQueryTimeout(this.timeout);
      try (ResultSet rs = stmt.executeQuery()) {
        return rs;
      }
    } catch (SQLException e) {
      this.DBLogger.log(Level.SEVERE, "Getting file records failed.", e);
    }
    return null;
  }

  public ResultSet getFileSharingRecords() {
    try (Connection connection = DriverManager.getConnection(DBURL);
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM FileSharing"); ) {

      stmt.setQueryTimeout(this.timeout);
      try (ResultSet rs = stmt.executeQuery()) {
        return rs;
      }
    } catch (SQLException e) {
      this.DBLogger.log(Level.SEVERE, "Getting file records failed.", e);
    }
    return null;
  }

  public String getUserPath(int id) {
    try (ResultSet rs = getUserRecords(); ) {
      while (rs.next()) {
        int currentId = rs.getInt("UserID");
        if (currentId == id) {
          return rs.getString("TopLevelDirectoryPath");
        } else {
          return "NONE";
        }
      }

    } catch (SQLException e) {

    }
    return "ERROR";
  }

  public boolean getFileAccess(int userId, String filename) {
    try {
      if (userOwnsFile(userId, filename)) {
        return true;
      }

      // Check if the user has the file shared with them
      if (fileSharedWithUser(userId, filename)) {
        return true;
      }

    } catch (SQLException e) {

    }
    // User does not own the file and it's not shared with them
    return false;
  }

  private boolean userOwnsFile(int userId, String filename) throws SQLException {
    try (ResultSet filesResultSet = getFileRecords(); ) {
      while (filesResultSet.next()) {
        int ownerId = filesResultSet.getInt("FileOwner");
        String filePath = filesResultSet.getString("FilePath");
        if (ownerId == userId && filePath.equals(filename)) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean fileSharedWithUser(int userId, String filename) throws SQLException {
    try (ResultSet fileSharingResultSet = getFileSharingRecords();
        ResultSet filesResultSet = getFileRecords(); ) {
      while (fileSharingResultSet.next()) {
        int sharedToUserId = fileSharingResultSet.getInt("FileSharedToUser");
        int fileId = fileSharingResultSet.getInt("FileShared");
        if (sharedToUserId == userId) {
          // Check if the file ID matches and the file path matches
          while (filesResultSet.next()) {
            int fileIdFromFiles = filesResultSet.getInt("FileID");
            String filePath = filesResultSet.getString("FilePath");
            if (fileIdFromFiles == fileId && filePath.equals(filename)) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }
}
