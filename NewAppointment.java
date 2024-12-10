package com.ajilesolutions.appointmenttracker.Appointment_Tracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.Window;

public class NewAppointment {
	final String[] availTimes = {
			"08:00AM","08:30AM","09:00AM","09:30AM","10:00AM","10:30AM",
			"11:00AM","11:30AM","12:00PM","12:30PM","01:00PM","01:30PM",
			"02:00PM","02:30PM","03:00PM","03:30PM","04:00PM","04:30PM",
			"05:00PM","05:30PM","06:00PM","06:30PM","07:00PM","07:30PM",
			"08:00PM","08:30PM","09:00PM"
	};
	
	@FXML
	private ChoiceBox<Patient> patient;
	@FXML
	private ChoiceBox<Specialist> specialist;
	@FXML
	private DatePicker date;
	@FXML
	private ChoiceBox<String> time;
	@FXML
	private Button submit;
	
	public NewAppointment() {
		
	}
	
	@FXML
	public void initialize() {
		patient.setItems(FXCollections
				.observableArrayList(Schedule.getInstance().patients));
		specialist.setItems(FXCollections
				.observableArrayList(Schedule.getInstance().specialists));
		time.setItems(FXCollections.observableArrayList(availTimes));
		
		//disable past dates
		date.setDayCellFactory(picker -> new DateCell() {
	        public void updateItem(LocalDate date, boolean empty) {
	            super.updateItem(date, empty);
	            LocalDate today = LocalDate.now();

	            setDisable(empty || date.compareTo(today) < 0 );
	        }
	    });
	}
	
	public void createAppointment(ActionEvent e) {
		//convert time selection from string to DateTime
    	DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("h:mma");
    	LocalTime apptTime = LocalTime.parse(time.getValue(),timeFormat);
    	//combine apptDate and apptTime to get LocalDateTime object
    	LocalDateTime apptDateTime = apptTime.atDate(date.getValue());
    	
		Appointment newAppt = new Appointment(patient.getValue(),specialist.getValue(),
				LocalDateTime.now(),apptDateTime,100.0f,false);
		
		//check for conflicts
		if(Schedule.getInstance().hasSpecialistConflict(newAppt)) {
			String alertMessage = "Appointment time unavailable.";
    		Alert a = new Alert(AlertType.ERROR,alertMessage);
    		a.show();
		}
		else if(Schedule.getInstance().hasPatientConflict(newAppt)) {
			String alertMessage = "Patient already has an appointment for this time.";
    		Alert a = new Alert(AlertType.ERROR,alertMessage);
    		a.show();
		}
		else {
			Schedule.getInstance().addAppt(newAppt);
			
			Stage currentWindow = (Stage)((Node) e.getSource()).getScene().getWindow();
			currentWindow.close();
		}
	}
}
