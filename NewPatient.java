package com.ajilesolutions.appointmenttracker.Appointment_Tracker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class NewPatient {
	private static final double CAPTURE_WINDOW_WIDTH = 400;
	private static final double CAPTURE_WINDOW_HEIGHT = 400;
	
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
	
	public NewPatient() {
		
	}
	
	public void onSubmitClick(ActionEvent e) {
		Patient newPatient = Schedule.getInstance().addPatient(firstName.getText(),
				lastName.getText(), email.getText(), phoneNo.getText());

		Stage currentWindow = (Stage) firstName.getScene().getWindow();
		Stage captureProfileImageWindow = new Stage();
		captureProfileImageWindow.setTitle("Smile!");
		//block input to parent window while profile picture capture
		//window is open
		captureProfileImageWindow.initModality(Modality.WINDOW_MODAL);
		captureProfileImageWindow.initOwner(currentWindow);
		
		try {
			Parent profileImageRoot = FXMLLoader.load(getClass()
					.getResource("CaptureProfileImage.fxml"));
			Scene profileImageScreen = new Scene(profileImageRoot,
					CAPTURE_WINDOW_WIDTH,CAPTURE_WINDOW_HEIGHT);
			
			//store new patient in profile image scene to save the image
			//using the patient's profPicImageName variable
			profileImageScreen.setUserData(newPatient);
			captureProfileImageWindow.setScene(profileImageScreen);
			captureProfileImageWindow.show();
		}
		catch(Exception exception) {
			System.out.println(exception.toString());
		}
	}
}
