package com.ajilesolutions.appointmenttracker.Appointment_Tracker;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;

public class AppointmentSignin {
	@FXML
	BorderPane rootNode;
	@FXML
	private ListView<Appointment> appointments;
	@FXML
	private Button signIn;
	
	public AppointmentSignin() {
		
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
		
		ObservableList<Appointment> todayAppointments = 
				FXCollections.observableArrayList();
		for(Appointment currentAppointment : Schedule.getInstance().appointments) {
			if(currentAppointment.apptTime.truncatedTo(ChronoUnit.DAYS)
					.isEqual(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS))) {
				todayAppointments.add(currentAppointment);
			}
		}
		
		appointments.setItems(todayAppointments);
	}
	
	public void signIntoAppointment() {
		Appointment selectedAppointment = appointments.getSelectionModel().getSelectedItem();
		selectedAppointment.apptSpecialist.notifySpecialist(selectedAppointment.apptPatient);
	}
}
