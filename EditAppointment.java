package com.ajilesolutions.appointmenttracker.Appointment_Tracker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.Pane;

public class EditAppointment {
	final static String[] availTimes = {
			"08:00AM","08:30AM","09:00AM","09:30AM","10:00AM","10:30AM",
			"11:00AM","11:30AM","12:00PM","12:30PM","01:00PM","01:30PM",
			"02:00PM","02:30PM","03:00PM","03:30PM","04:00PM","04:30PM",
			"05:00PM","05:30PM","06:00PM","06:30PM","07:00PM","07:30PM",
			"08:00PM","08:30PM","09:00PM"
	};
	
	@FXML
	private Pane rootNode;
	@FXML
	private ChoiceBox<Patient> patient;
	@FXML
	private ChoiceBox<Specialist> specialist;
	@FXML
	private DatePicker date;
	@FXML
	private ChoiceBox<String> time;
	@FXML
	private Button confirm;
	
	private Appointment selectedAppointment;
	
	public EditAppointment(Appointment appt) {
		selectedAppointment = appt;
	}
	
	public void initialize() {
		patient.setValue(selectedAppointment.apptPatient);
		specialist.setValue(selectedAppointment.apptSpecialist);
		date.setValue(selectedAppointment.apptTime.toLocalDate());
		//disable past dates
		date.setDayCellFactory(picker -> new DateCell() {
	        public void updateItem(LocalDate date, boolean empty) {
	            super.updateItem(date, empty);
	            LocalDate today = LocalDate.now();

	            setDisable(empty || date.compareTo(today) < 0 );
	        }
	    });
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mma");
		time.setValue(selectedAppointment.apptTime.toLocalTime().format(timeFormatter));
	}
}
