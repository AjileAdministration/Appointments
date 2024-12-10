package com.ajilesolutions.appointmenttracker.Appointment_Tracker;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AllAppointments {
	private static final double EDIT_APPOINTMENT_WINDOW_WIDTH = 400;
	private static final double EDIT_APPOINTMENT_WINDOW_HEIGHT = 400;
	
	@FXML
	private BorderPane rootNode;
	@FXML
	private ListView<Appointment> appointmentDisplay;
	@FXML
	private Button edit;
	@FXML
	private Button delete;
	
	private ObservableList<Appointment> appointments;
	
	
	public AllAppointments() {
	}

	@FXML
	public void initialize() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenuBar.fxml"));
			MenuBar mainMenuBar = loader.load();
			rootNode.setTop(mainMenuBar);
		}
		catch(Exception exception) {
			System.out.println(exception.toString());
		}
		
		appointments = FXCollections.observableArrayList();
		for(Appointment currentAppointment : Schedule.getInstance().appointments) {
			if(currentAppointment.apptTime.truncatedTo(ChronoUnit.DAYS)
					.isEqual(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)) ||
					currentAppointment.apptTime.truncatedTo(ChronoUnit.DAYS)
					.isAfter((LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)))) {
				appointments.add(currentAppointment);
			}
		}
		appointmentDisplay.setItems(appointments);
	}
	
	@FXML
	public void deleteAppointment(ActionEvent e) {
		Appointment deleteAppointment = 
				appointmentDisplay.getSelectionModel().getSelectedItem();
		appointments.remove(deleteAppointment);
		
		Schedule.getInstance().deleteAppt(deleteAppointment);
	}
	
	@FXML
	public void editAppointment(ActionEvent e) {
		Stage currentWindow = (Stage)((Node) e.getSource()).getScene().getWindow();
		Stage editAppointmentWindow = new Stage();
		editAppointmentWindow.setTitle("Edit Appointment");
		//block input to parent window while profile picture capture
		//window is open
		editAppointmentWindow.initModality(Modality.WINDOW_MODAL);
		editAppointmentWindow.initOwner(currentWindow);
		
		try {
			FXMLLoader editAppointmentLoader = new FXMLLoader(
					getClass().getResource("EditAppointment.fxml"));
			EditAppointment editAppointmentController = new 
					EditAppointment(appointmentDisplay.getSelectionModel().getSelectedItem());
			editAppointmentLoader.setController(editAppointmentController);
			
			Parent editAppointmentRoot = editAppointmentLoader.load();
			Scene editAppointmentScreen = new Scene(editAppointmentRoot,
					EDIT_APPOINTMENT_WINDOW_WIDTH,EDIT_APPOINTMENT_WINDOW_HEIGHT);
			editAppointmentWindow.setScene(editAppointmentScreen);
			editAppointmentWindow.show();
		}
		catch(Exception exception) {
			System.out.println(exception.getMessage());
		}
	}
}
