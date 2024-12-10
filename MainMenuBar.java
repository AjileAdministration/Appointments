package com.ajilesolutions.appointmenttracker.Appointment_Tracker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainMenuBar {
	private static final double WINDOW_WIDTH = 600;
	private static final double WINDOW_HEIGHT = 400;
	private static final double EDIT_SPECIALIST_WINDOW_HEIGHT = 600;
	private static final double EDIT_PATIENT_WINDOW_HEIGHT = 500;
	private static final double NEW_SPECIALIST_WINDOW_WIDTH = 400;
	private static final double NEW_PATIENT_WINDOW_WIDTH = 400;
	private static final double NEW_APPOINTMENT_WINDOW_WIDTH = 400;
	
	@FXML
	private MenuItem appointmentSignin;
	@FXML
	private MenuItem allAppointments;
	@FXML
	private MenuItem newAppointment;
	@FXML
	private MenuItem newPatient;
	@FXML
	private MenuItem editPatient;
	@FXML
	private MenuItem newSpecialist;
	@FXML
	private MenuItem editSpecialist;

	public MainMenuBar() {
		// TODO Auto-generated constructor stub
	}

	@FXML
	public void loadNewPatient(ActionEvent e) {
		Stage currentWindow = GlobalHelperMethods.getCurrentWindow();
		Stage newPatientWindow = new Stage();
		newPatientWindow.setTitle("New Patient");
		//block input to parent window while patient-creating
		//window is open
		newPatientWindow.initModality(Modality.WINDOW_MODAL);
		newPatientWindow.initOwner(currentWindow);
		
		try {
			Parent newPatientRoot = FXMLLoader.load(getClass().getResource("NewPatient.fxml"));
			Scene newPatientScreen = new Scene(newPatientRoot,NEW_PATIENT_WINDOW_WIDTH,WINDOW_HEIGHT);
			newPatientWindow.setScene(newPatientScreen);
			newPatientWindow.show();
		}
		catch(Exception exception) {
			System.out.println(exception.getMessage());
		}
	}
	
	@FXML
	public void loadEditPatient(ActionEvent e) {
		Stage currentWindow = GlobalHelperMethods.getCurrentWindow();
		currentWindow.setTitle("Edit Patient");
		
		try {
			Parent editPatientRoot = FXMLLoader.load(getClass().getResource("EditPatient.fxml"));
			Scene editPatientScreen = new Scene(editPatientRoot,WINDOW_WIDTH,EDIT_PATIENT_WINDOW_HEIGHT);
			currentWindow.setScene(editPatientScreen);
		}
		catch(Exception exception) {
			System.out.println(exception.toString());
		}
	}
	
	@FXML
	public void loadNewSpecialist(ActionEvent e) {
		Stage currentWindow = GlobalHelperMethods.getCurrentWindow();
		Stage newSpecialistWindow = new Stage();
		newSpecialistWindow.setTitle("New Patient");
		//block input to parent window while patient-creating
		//window is open
		newSpecialistWindow.initModality(Modality.WINDOW_MODAL);
		newSpecialistWindow.initOwner(currentWindow);
		
		try {
			Parent newSpecialistRoot = FXMLLoader.load(getClass().getResource("NewSpecialist.fxml"));
			Scene newPatientScreen = new Scene(newSpecialistRoot,NEW_SPECIALIST_WINDOW_WIDTH,WINDOW_HEIGHT);
			newSpecialistWindow.setScene(newPatientScreen);
			newSpecialistWindow.show();
		}
		catch(Exception exception) {
			System.out.println(exception.getMessage());
		}
	}
	
	@FXML
	public void loadEditSpecialist(ActionEvent e) {
		Stage currentWindow = GlobalHelperMethods.getCurrentWindow();
		currentWindow.setTitle("Edit Specialist");
		
		try {
			Parent editSpecialistRoot = FXMLLoader.load(getClass().getResource("EditSpecialist.fxml"));
			Scene editSpecialistScreen = new Scene(editSpecialistRoot,WINDOW_WIDTH,EDIT_SPECIALIST_WINDOW_HEIGHT);
			currentWindow.setScene(editSpecialistScreen);
		}
		catch(Exception exception) {
			System.out.println(exception.getMessage());
		}
	}
	
	@FXML
	public void loadAppointmentSignin(ActionEvent e) {
		Stage currentWindow = GlobalHelperMethods.getCurrentWindow();
		currentWindow.setTitle("Appointment Signin");
		
		try {
			Parent appointmentSigninRoot = 
					FXMLLoader.load(getClass().getResource("AppointmentSignin.fxml"));
			Scene appointmentSigninScreen = new Scene(appointmentSigninRoot,WINDOW_WIDTH,WINDOW_HEIGHT);
			currentWindow.setScene(appointmentSigninScreen);
		}
		catch(Exception exception) {
			System.out.println(exception.toString());
		}
	}
	
	@FXML
	public void loadNewAppointment(ActionEvent e) {
		Stage currentWindow = GlobalHelperMethods.getCurrentWindow();
		Stage newAppointmentWindow = new Stage();
		newAppointmentWindow.setTitle("New Patient");
		//block input to parent window while patient-creating
		//window is open
		newAppointmentWindow.initModality(Modality.WINDOW_MODAL);
		newAppointmentWindow.initOwner(currentWindow);
		
		try {
			Parent newAppointmentRoot = 
					FXMLLoader.load(getClass().getResource("NewAppointment.fxml"));
			Scene newAppointmentScreen = new Scene(newAppointmentRoot,NEW_APPOINTMENT_WINDOW_WIDTH,WINDOW_HEIGHT);
			newAppointmentWindow.setScene(newAppointmentScreen);
			newAppointmentWindow.show();
		}
		catch(Exception exception) {
			System.out.println(exception.toString());
		}
	}
	
	@FXML
	public void loadAllAppointments(ActionEvent e) {
		Stage currentWindow = GlobalHelperMethods.getCurrentWindow();
		currentWindow.setTitle("All Appointments");
		try {
			Parent allAppointmentsRoot = 
					FXMLLoader.load(getClass().getResource("AllAppointments.fxml"));
			
			Scene allAppointmentsScreen = 
					new Scene(allAppointmentsRoot,WINDOW_WIDTH,WINDOW_HEIGHT);
			currentWindow.setScene(allAppointmentsScreen);
		}
		catch(Exception exception) {
			System.out.println(exception.toString());
		}
	}
}
