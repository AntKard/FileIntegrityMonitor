package com.example.fim;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.sql.Timestamp;

//import java.awt.Color;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;



public class FIMApp extends Application {
	protected static fimGUI gui;
	protected static databaseController db;
	protected static hashedFile hashedFile;
	
	
    @Override
    public void start(Stage primaryStage) throws IOException {
    	db = new databaseController();
    	db.initializeDatabase();

    	gui = new fimGUI();
    	gui.initialize(primaryStage, this);
    	
    }

    
    public static void main(String[] args) {
        launch(args);
    }
    
    
    public static class hashedFile {
    	private String filePath;
    	private String hashValue;
    	private long lastMod;
    	private Timestamp monitorTimeStamp;
    	
    	private String comparisonHash;
    	
    	
    	public hashedFile() {
    		this.filePath = null;
    		this.hashValue = null;
    		this.lastMod = 0;
    		this.monitorTimeStamp = null;
    	}
    	
    	
    	public void setFilePath(String filePath) {
    		this.filePath = filePath;
    	}
    	
    	
    	public String getFilePath() {
    		return this.filePath;
    	}
    	
    	
    	public void setHashValue(String hashValue) {
    		this.hashValue = hashValue;
    	}
    	
    	
    	public String getHashValue() {
    		return this.hashValue;
    	}
    	
    	
    	public void setLastMod(long lastMod) {
    		this.lastMod = lastMod;
    	}
    	
    	
    	public long getLastMod() {
    		return this.lastMod;
    	}
    	
    	
    	public void setMonitorTimeStamp(Timestamp monitorTimestamp) {
    		this.monitorTimeStamp = monitorTimestamp;
    	}
    	
    	
    	public Timestamp getMonitorTimeStamp() {
    		return this.monitorTimeStamp;
    	}
     	
    	public String getComparisonHash(File file) {
    		
    		hashFile(file, true);
    		return this.comparisonHash;
    	}
    	
    	private void hashFile(File file, boolean isComparisonHash) {
    		
    		try {
    			MessageDigest digest = MessageDigest.getInstance("SHA-256");
    			FileInputStream fis = new FileInputStream(file);
    			byte[] byteArray = new byte[1024];
    			int bytesCount;
    			while ((bytesCount = fis.read(byteArray)) != -1) {
    				digest.update(byteArray, 0, bytesCount);
    			}
    			fis.close();
    			byte[] bytes = digest.digest();
    			
    			StringBuilder sb = new StringBuilder();
    			for (byte b : bytes) {
    				sb.append(String.format("%02x", b));
    			}
    			 
    			
    			if (!isComparisonHash) {
    				db.saveFileHash(file.getAbsolutePath(), sb.toString() , file.lastModified(), new Timestamp(System.currentTimeMillis()));
    			}
    			
    			else {
    				
    				this.comparisonHash = sb.toString();
    			}

    			    			
    		} catch (Exception e) {
    			e.printStackTrace();
    			System.out.print("Error");
    		}
    	}
    	
    	public void accessHashFile(File file) {

    		hashFile(file, false);
    		
    	}
    }
}
