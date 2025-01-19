package com.example.fim;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;



public class databaseController {
	private static final String DB_URL = "jdbc:sqlite:fim.db";
	protected Connection connection;

	
	public Connection connect() {
		
		connection = null;
		
		try {
			connection = DriverManager.getConnection(DB_URL);
			System.out.println("Connected to the database!");
		} catch (SQLException e) {
			System.err.println("Database connection failed: " + e.getMessage());
		}
		return connection;
	}
	
	
	public void initializeDatabase() {
		
		connection = connect();
		
//		dropTable("files");   USED FOR TESTING
		
		String createTableSQL = "CREATE TABLE IF NOT EXISTS files ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "file_path TEXT NOT NULL,"
				+ "hash_value TEXT NOT NULL,"
				+ "last_modified TIMESTAMP NOT NULL,"
				+ "monitor_date TIMESTAMP NOT NULL)";

	
		try (Statement statement = connection.createStatement()) {
			statement.execute(createTableSQL);
			System.out.println("Database initialized.");
		} catch (Exception e) {
			System.err.println("Failed to initialize the database: " + e.getMessage());
		}
	}
	
	/*
	 * 	Method is used for testing purposes to clear database.
	 */
	private void dropTable(String tableName) {
		String query = "DROP TABLE IF EXISTS " + tableName;
		try (Statement stmt = connection.createStatement()) {
			stmt.execute(query);
			System.out.println("Table dropped.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 	 Method is used to save file path, hash value, last modified date, and monitor date to database.
	 */
	public void saveFileHash(String filePath, String hashValue, long lastModified, Timestamp monitorTimeStamp) {
		
		String insertSQL = "INSERT OR REPLACE INTO files (file_path, hash_value, last_modified, monitor_date) VALUES (?, ?, ?, ?)";
		
		try (PreparedStatement prepStatement = connection.prepareStatement(insertSQL)) {
			
			prepStatement.setString(1,  filePath);
			prepStatement.setString(2, hashValue);
			prepStatement.setTimestamp(3, new Timestamp(lastModified));
			prepStatement.setTimestamp(4,  monitorTimeStamp);
			
			prepStatement.executeUpdate();
			
			
		} catch (Exception e) {
			System.err.println("Failed to store file hash: " + e.getMessage());
		}
	}
	
	/*
	 * 	Method is used to access hashedFiles list in database
	 * 	and returns list.
	 */
	private List<FIMApp.hashedFile> accessHashedFiles() {
		List<FIMApp.hashedFile> hashedFileList = new ArrayList<>();
		
		String query = "SELECT file_path, hash_value, last_modified, monitor_date FROM files";
		
		try (Statement stmt = connection.createStatement();
			 ResultSet rs = stmt.executeQuery(query)) {
			
			// While loop iterates through results and adds each hashed file object to hashed file list
			while (rs.next()) {

				FIMApp.hashedFile hashedFile = new FIMApp.hashedFile();
				hashedFile.setFilePath(rs.getString("file_path"));
				hashedFile.setHashValue(rs.getString("hash_value"));
				hashedFile.setLastMod(rs.getLong("last_modified"));
				hashedFile.setMonitorTimeStamp(rs.getTimestamp("monitor_date"));
				hashedFileList.add(hashedFile);
			}
			return hashedFileList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * 	Method is used to call accessHashedFile method.
	 * 	Returns list of hashed files.
	 */
	public List<FIMApp.hashedFile> getHashedFiles() {
		List<FIMApp.hashedFile> returnList = accessHashedFiles();
		
		return returnList;
	}
	
}
