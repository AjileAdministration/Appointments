package com.ajilesolutions.appointmenttracker.Appointment_Tracker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class NewSpecialist {
	@FXML
	private TextField firstName;
	@FXML
	private TextField lastName;
	@FXML
	private TextField email;
	@FXML
	private TextField phoneNo;
	@FXML
	private Button submit;
	@FXML
	private RadioButton phonePreference;
	@FXML
	private RadioButton emailPreference;
	@FXML
	private RadioButton neitherPreference;
	@FXML
	private ToggleGroup contactPrefGroup;
	@FXML
	private TextArea bio;
	
	public NewSpecialist() {
		
	}
	
	public void onSubmitClick(ActionEvent e) {
		Specialist.ContactPref selectedPreference;
		switch(contactPrefGroup.getSelectedToggle().toString()) {
		case "Phone":
			selectedPreference = Specialist.ContactPref.PHONE;
			break;
		case "Email":
			selectedPreference = Specialist.ContactPref.EMAIL;
			break;
		default:
			selectedPreference = Specialist.ContactPref.NEITHER;
			break;
		}
		
		Schedule.getInstance().addSpecialist(firstName.getText(),
				lastName.getText(),selectedPreference,email.getText(),phoneNo.getText(),bio.getText());

		Stage currentWindow = (Stage)((Node) e.getSource()).getScene().getWindow();
		currentWindow.close();
	}
}
