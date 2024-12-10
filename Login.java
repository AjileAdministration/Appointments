package com.ajilesolutions.appointmenttracker.Appointment_Tracker;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Login {
	private static final double WINDOW_WIDTH = 600;
	private static final double WINDOW_HEIGHT = 400;
	private static final String CONNECTION_STRING = 
			"jdbc:aws-wrapper:mysql://clinic-database.chqks0mo8cue.us-east-1.rds.amazonaws.com:3306/clinic";
	private static final String MYSQL_USERNAME = "admin";
	private static final String MYSQL_PASSWORD = "cat_Tower8*";

	@FXML
	private TextField email;
	@FXML
	private PasswordField password;
	@FXML
	private Button submit;
	

	public void onSubmitClick(ActionEvent e) {
		if(validateCredentials()) {
			try {
				Stage currentWindow = 
						(Stage)((Node) e.getSource()).getScene().getWindow();
				Parent newRoot = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
				Scene mainMenu = new Scene(newRoot,WINDOW_WIDTH,WINDOW_HEIGHT);
				currentWindow.setScene(mainMenu);
			}
			catch(Exception exception) {
				System.out.println(exception.getMessage());
			}
		}
		else {
			System.out.println("Invalid credentials");
		}
	}
	
	private boolean validateCredentials() {
		final Properties properties = new Properties();
    	
    	// Configuring connection properties for the underlying JDBC driver.
        properties.setProperty("user", MYSQL_USERNAME);
        properties.setProperty("password", MYSQL_PASSWORD);
        properties.setProperty("loginTimeout", "100");
        
        // Configuring connection properties for the JDBC Wrapper.
        properties.setProperty("wrapperPlugins", "failover,efm2");
        properties.setProperty("wrapperLogUnclosedConnections", "true");
        
        try {
        	Connection conn = DriverManager.getConnection(CONNECTION_STRING, properties);
		
        	PreparedStatement query = 
				conn.prepareStatement("SELECT * FROM specialist WHERE email=? AND password_hash=?;");
        	query.setString(1,email.getText());
        	query.setBytes(2,getPasswordHash(password.getText()));
        	
        	ResultSet results = query.executeQuery();
        	if(results.next()) {
        		conn.close();
        		return true;
        	}
        	else {
        		conn.close();
        		return false;
        	}
        }
        catch(SQLException e) {
        	System.out.println(e.getMessage());
        	return false;
        }
	}

	private byte[] getPasswordHash(String clearTextPassword) {
		final byte[] passwordBytes = clearTextPassword.getBytes(StandardCharsets.UTF_8);
		byte[] passwordHash;
		
		try {
		    passwordHash = MessageDigest.getInstance("SHA-512").digest(passwordBytes);
		} catch (NoSuchAlgorithmException e) {
		    throw new RuntimeException(e);
		}
		
		return passwordHash;
	}
	
	private byte[] getPasswordHashDB() {
		final Properties properties = new Properties();
    	
    	// Configuring connection properties for the underlying JDBC driver.
        properties.setProperty("user", MYSQL_USERNAME);
        properties.setProperty("password", MYSQL_PASSWORD);
        properties.setProperty("loginTimeout", "100");
        
        // Configuring connection properties for the JDBC Wrapper.
        properties.setProperty("wrapperPlugins", "failover,efm2");
        properties.setProperty("wrapperLogUnclosedConnections", "true");
        
        try {
        	Connection conn = DriverManager.getConnection(CONNECTION_STRING, properties);
		
        	PreparedStatement query = 
				conn.prepareStatement("SELECT password_hash FROM specialist WHERE email='tinaturner@aol.com'");
        	
        	ResultSet results = query.executeQuery();
        	if(results.next()) {
        		return results.getBytes("password_hash");
        	}
        	else {
        		return null;
        	}
        }
        catch(SQLException e) {
        	return null;
        }
	}
	
	private String byteArrayToHex(byte[] theByteArray) {
		StringBuilder sb = new StringBuilder();
		for (byte b : theByteArray) {
		    sb.append(String.format("%02X ", b));
		}
		return sb.toString();
	}
}
