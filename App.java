package com.ajilesolutions.appointmenttracker.Appointment_Tracker;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.Collections;
import java.util.Comparator;
import java.awt.image.BufferedImage;

import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import com.twilio.Twilio;

public class App extends Application {
	static String clinicName;
	
    public static final String ACCOUNT_SID = "MGb8d81efe74878e9fb126140eb35b92e3";
    public static final String AUTH_TOKEN = "48cc1a9467562cfc0ddaae97da8cea93";
	
	private static WebCamService service;
	
	@Override
	public void init() {
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

		//initialize Schedule class
		Schedule.getInstance();
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
			primaryStage.setTitle("Welcome");
			primaryStage.setScene(new Scene(root,600,400));
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void captureProfilePic(Stage primaryStage, Patient newPatient) {
		//open new window for photo capture
		Stage webcamWindow = new Stage();
		webcamWindow.setTitle("Smile!");
		//block input to new patient (parent) window while webcam
		//window is open
		webcamWindow.initModality(Modality.WINDOW_MODAL);
		webcamWindow.initOwner(primaryStage);
    	
    	WebCamView view = new WebCamView(service);
    	
    	//button to toggle display of webcam
    	Button startStop = new Button();
		startStop.textProperty().bind(Bindings.
				when(service.runningProperty()).
				then("Stop").
				otherwise("Start"));
		startStop.setOnAction(f -> {
			if (service.isRunning()) {
				service.cancel();
			} else {
				service.restart();
			}
		});
		
		//button to capture webcam image
		Button capture = new Button("Capture");
		capture.setOnAction(f -> {
			Webcam cam2 = Webcam.getDefault();
			
			try {
				if(service.isRunning()) {
					cam2.open();
    				ImageIO.write(
        				cam2.getImage(), "JPG", 
        				new File(newPatient.profPicImageName));
				}
			}
			catch(IOException ioe) {
				System.out.println("File error!");
			}
			finally {
				service.cancel();
				
				webcamWindow.close();
				
				cam2.close();
			}
		});
		
		//put webcam display in center
		BorderPane root = new BorderPane(view.getView());
		//put startstop and capture buttons on bottom
		HBox cameraButtons = new HBox(startStop,capture);
		
		BorderPane.setAlignment(cameraButtons, Pos.CENTER);
		BorderPane.setMargin(cameraButtons, new Insets(5));
		root.setBottom(cameraButtons);
		
		Scene webcamView = new Scene(root);
		
		webcamWindow.setScene(webcamView);
		webcamWindow.show();
	}
}