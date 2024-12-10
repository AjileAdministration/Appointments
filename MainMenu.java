package com.ajilesolutions.appointmenttracker.Appointment_Tracker;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;

public class MainMenu {
	@FXML
	private BorderPane landingScreen;

	public MainMenu() {
		
	}
	
	public void initialize() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenuBar.fxml"));
			MenuBar mainMenuBar = loader.load();
			landingScreen.setTop(mainMenuBar);
		}
		catch(Exception exception) {
			System.out.println(exception.toString());
		}
	}
}
