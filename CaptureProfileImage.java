package com.ajilesolutions.appointmenttracker.Appointment_Tracker;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class CaptureProfileImage {
	@FXML
	private BorderPane layoutRoot;
	@FXML
	private Button capture;

	public CaptureProfileImage() {
		
	}

	public void capturePicture() {
		//retrieve newly created patient from NewPatient controller
		//and use its profPicImageName to create profile pic image file
		Patient newPatient = (Patient) layoutRoot.getScene().getUserData();
		
		Webcam displayCam = Webcam.getDefault();
		displayCam.open();
		try {
		ImageIO.write(
			displayCam.getImage(), "JPG", 
			new File(newPatient.profPicImageName));
		}
		catch(IOException ioe) {
			System.out.println("File error!");
		}
		
		Stage currentWindow = (Stage) layoutRoot.getScene().getWindow();
		Stage parentWindow = (Stage) currentWindow.getOwner();
		parentWindow.close();
		currentWindow.close();
		
		/*layoutRoot.setCenter(webCamView.getView());
		
		try {
			if(GlobalWebcam.getInstance().webCamService.isRunning()) {
				GlobalWebcam.getInstance().captureCam.open();
				ImageIO.write(
    				GlobalWebcam.getInstance().captureCam.getImage(), "JPG", 
    				new File(newPatient.profPicImageName));
			}
		}
		catch(IOException ioe) {
			System.out.println("File error!");
		}
		finally {
			GlobalWebcam.getInstance().webCamService.cancel();
			
			GlobalWebcam.getInstance().captureCam.close();
			
			Stage currentWindow = (Stage) layoutRoot.getScene().getWindow();
			currentWindow.close();
		}*/
	}
}
