package com.ajilesolutions.appointmenttracker.Appointment_Tracker;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.mail.*;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Properties;


/**
 *
 * @author ajlso
 */
public class Schedule {
	private static final String CONNECTION_STRING = 
			"jdbc:aws-wrapper:mysql://clinic-database.chqks0mo8cue.us-east-1.rds.amazonaws.com:3306/clinic";
	private static final String USERNAME = "admin";
	private static final String PASSWORD = "cat_Tower8*";
	
	private static int numSpecialists = 0;
	private static int numPatients = 0;
	
	public ArrayList<Appointment> appointments;
    public ArrayList<Patient> patients;
    public ArrayList<Specialist> specialists;
    
    public String clinicName;
    
    public Schedule(){
    	patients = loadPatients();
    	specialists = loadSpecialists();
    	appointments = loadAppointments();
    }
    
    public void addAppt(Appointment newAppt)
    {
        appointments.add(newAppt);

        addAppointmentDB(newAppt);
    }
    
    public Patient addPatient(String first, String last, String email, String phone, LocalDate create){
    	//record date of patient's first visit
    	LocalDate creation = LocalDate.now();
    	
    	//increment numPatients and use it to set new patient's ID
    	numPatients++;
    	
        Patient newPatient = new Patient(numPatients,first,last,email,phone,creation);
        
        try {
	        File profPic = new File(newPatient.profPicImageName);
	        profPic.createNewFile();
        }
        catch(IOException e) {
        	System.out.println(e.getMessage());
        }
        
        patients.add(newPatient);
        
        addPatientDB(newPatient);
        
        return newPatient;
    }
    
    public Specialist addSpecialist(String first, String last, Specialist.ContactPref contPref, String phone, String email, String bio){
    	//record date of specialist's creation
    	LocalDate creation = LocalDate.now();
    	
    	//increment numSpecialists and use it to set new specialist's ID
    	numSpecialists++;
    	
        Specialist newSpecialist = new Specialist(numSpecialists,first,last,contPref,phone,email,bio,creation);
        
        try {
	        File profPic = new File(newSpecialist.profPicImageName);
	        profPic.createNewFile();
        }
        catch(IOException e) {
        	System.out.println(e.getMessage());
        }
        
        specialists.add(newSpecialist);
        
        addSpecialistDB(newSpecialist);
        
        return newSpecialist;
    }
    
    private ArrayList<Patient> loadPatients() {
    	ArrayList<Patient> loadedList = new ArrayList<>();
    	
    	final Properties properties = new Properties();
    	
    	// Configuring connection properties for the underlying JDBC driver.
        properties.setProperty("user", USERNAME);
        properties.setProperty("password", PASSWORD);
        properties.setProperty("loginTimeout", "100");
        
        // Configuring connection properties for the JDBC Wrapper.
        properties.setProperty("wrapperPlugins", "failover,efm2");
        properties.setProperty("wrapperLogUnclosedConnections", "true");
        
        try {
        	Connection conn = DriverManager.getConnection(CONNECTION_STRING, properties);
        	
        	Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM patient;");
            
            int newID;
            String newFirst, newLast, newEmail, newPhone;
            LocalDate newCreate;
            
            while(rs.next()) {
            	newID = rs.getInt("patient_id");
            	newFirst = rs.getString("first_name");
            	newLast = rs.getString("last_name");
            	newEmail = rs.getString("email");
            	newPhone = rs.getString("phone_num");
            	
            	String dateStr = rs.getDate("create_date").toString();
            	newCreate = LocalDate.parse(dateStr);
            	
            	loadedList.add(new Patient(newID,newFirst,newLast,newEmail,newPhone,newCreate));
            	
            	numPatients++;
            }
            
            conn.close();
        }
        catch(SQLException e) {
        	System.out.println(e.getMessage());
        }
        
    	return loadedList;
    }
    
    private ArrayList<Specialist> loadSpecialists() {
    	ArrayList<Specialist> loadedList = new ArrayList<>();
    	
    	final Properties properties = new Properties();
    	
    	// Configuring connection properties for the underlying JDBC driver.
        properties.setProperty("user", USERNAME);
        properties.setProperty("password", PASSWORD);
        properties.setProperty("loginTimeout", "100");
        
        // Configuring connection properties for the JDBC Wrapper.
        properties.setProperty("wrapperPlugins", "failover,efm2");
        properties.setProperty("wrapperLogUnclosedConnections", "true");
        
        try {
        	Connection conn = DriverManager.getConnection(CONNECTION_STRING, properties);
        	
        	Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM specialist;");
            
            int newID;
            Specialist.ContactPref newContactPref;
            String newFirst, newLast, newEmail, newPhone, newCreateDate, newBio;
            LocalDate newCreate;
            
            while(rs.next()) {
            	newID = rs.getInt("specialist_id");
            	newFirst = rs.getString("first_name");
            	newLast = rs.getString("last_name");
            	//enumerated value is stored as integer in specialist table
            	//convert with values() function
            	newContactPref = Specialist.ContactPref.values()[rs.getInt("contact_pref")];
            	newEmail = rs.getString("email");
            	newPhone = rs.getString("phone_num");
            	newBio = rs.getString("bio");
            	
            	newCreateDate = rs.getDate("create_date").toString();
            	newCreate = LocalDate.parse(newCreateDate);
            	
            	loadedList.add(new Specialist(newID,newFirst,newLast,newContactPref,newPhone,newEmail,newBio,newCreate));
            	
            	numSpecialists++;
            }
            
            conn.close();
        }
        catch(SQLException e) {
        	System.out.println(e.getMessage());
        }
        
    	return loadedList;
    }
    
    private ArrayList<Appointment> loadAppointments() {
    	ArrayList<Appointment> loadedList = new ArrayList<>();
    	
    	final Properties properties = new Properties();
    	
    	// Configuring connection properties for the underlying JDBC driver.
        properties.setProperty("user", USERNAME);
        properties.setProperty("password", PASSWORD);
        properties.setProperty("loginTimeout", "100");
        
        // Configuring connection properties for the JDBC Wrapper.
        properties.setProperty("wrapperPlugins", "failover,efm2");
        properties.setProperty("wrapperLogUnclosedConnections", "true");
    	
        try {
        	Connection conn = DriverManager.getConnection(CONNECTION_STRING, properties);
        	
        	Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM appointment;");
            
            Patient newPatient;
            Specialist newSpecialist;
            LocalDateTime newCreateTime, newApptTime;
            float newCost;
            boolean newCancelled;
            
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            
            while(rs.next()) {
            	//search patients ArrayList by index using the appointment's patient_id field
            	//decrement by 1 since ArrayList is zero-indexed
            	newPatient = patients.get(rs.getInt("patient_id") - 1);
            	
            	//search specialists ArrayList by index using appointment's specialist_id field
            	//increment by 1 since ArrayList is zero-indexed
            	newSpecialist = specialists.get(rs.getInt("specialist_id") - 1);
            	
            	newCreateTime = LocalDateTime.parse(rs.getString("create_time"),dtf);
            	newApptTime = LocalDateTime.parse(rs.getString("appt_time"),dtf);
            	
            	newCost = rs.getFloat("cost");
            	newCancelled = rs.getBoolean("cancelled");
            	
            	loadedList.add(new Appointment(newPatient,newSpecialist,newCreateTime,
            			newApptTime,newCost,newCancelled));
            }
            
            conn.close();
        }
        catch(SQLException e) {
        	System.out.println(e.getMessage());
        }
        
    	return loadedList;
    }
    
    private void addAppointmentDB(Appointment apt) {
    	final Properties properties = new Properties();
    	
    	// Configuring connection properties for the underlying JDBC driver.
        properties.setProperty("user", USERNAME);
        properties.setProperty("password", PASSWORD);
        properties.setProperty("loginTimeout", "100");
        
        // Configuring connection properties for the JDBC Wrapper.
        properties.setProperty("wrapperPlugins", "failover,efm2");
        properties.setProperty("wrapperLogUnclosedConnections", "true");
        
        try {
        	Connection conn = DriverManager.getConnection(CONNECTION_STRING, properties);
        	
        	Statement stmt = conn.createStatement();
        	//convert appt time to mysql DateTime format
        	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss");
        	final String UPDATE = 
        			"INSERT INTO appointment VALUES (" + apt.apptPatient.getID() + "," +
        			apt.apptSpecialist.getID() + ",\"" + apt.createTime.format(dtf) + "\",\"" +
        			apt.apptTime.format(dtf) + "\"," + apt.cost + "," + apt.cancelled + ");";
        	
            stmt.executeUpdate(UPDATE);
            
            conn.close();
        }
        catch(SQLException e) {
        	System.out.println(e.getMessage());
        }
    }
    
    private void addPatientDB(Patient p) {
    	final Properties properties = new Properties();
    	
    	// Configuring connection properties for the underlying JDBC driver.
        properties.setProperty("user", USERNAME);
        properties.setProperty("password", PASSWORD);
        properties.setProperty("loginTimeout", "100");
        
        // Configuring connection properties for the JDBC Wrapper.
        properties.setProperty("wrapperPlugins", "failover,efm2");
        properties.setProperty("wrapperLogUnclosedConnections", "true");
        
        try {
        	Connection conn = DriverManager.getConnection(CONNECTION_STRING, properties);
        	
        	Statement stmt = conn.createStatement();
        	final String UPDATE = 
        			"INSERT INTO patient VALUES (" + p.getID() + ",\"" +
        			p.profPicImageName + "\",\"" + p.firstName + "\",\"" +
        			p.lastName + "\",\"" + p.email + "\",\"" + p.phoneNo + "\",\"" +
        			p.createDate.toString() + "\");";
            stmt.executeUpdate(UPDATE);
            
            conn.close();
        }
        catch(SQLException e) {
        	System.out.println(e.getMessage());
        }
    }
    
    private void addSpecialistDB(Specialist s) {
    	final Properties properties = new Properties();
    	
    	// Configuring connection properties for the underlying JDBC driver.
        properties.setProperty("user", USERNAME);
        properties.setProperty("password", PASSWORD);
        properties.setProperty("loginTimeout", "100");
        
        // Configuring connection properties for the JDBC Wrapper.
        properties.setProperty("wrapperPlugins", "failover,efm2");
        properties.setProperty("wrapperLogUnclosedConnections", "true");
        
        try {
        	Connection conn = DriverManager.getConnection(CONNECTION_STRING, properties);
        	
        	Statement stmt = conn.createStatement();
        	final String UPDATE = 
        			"INSERT INTO patient VALUES (" + s.getID() + ",\"" +
        			s.profPicImageName + "\",\"" + s.firstName + "\",\"" +
        			s.lastName + "\"," + s.preference + ",\"" + s.getEmailAddr() + "\",\"" +
        			s.getPhoneNo() + "\",\"" + s.bio + "\",\"" + s.createDate.toString() + "\");";
            stmt.executeUpdate(UPDATE);
            
            conn.close();
        }
        catch(SQLException e) {
        	System.out.println(e.getMessage());
        }
        
    }
}
