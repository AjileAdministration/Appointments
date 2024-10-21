package com.ajilesolutions.appointmenttracker.Appointment_Tracker;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import java.awt.image.BufferedImage;

import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class App extends Application {
	static String clinicName;
	final static Schedule clinicCalendar = new Schedule();
	
	private static WebCamService service;
	
	@Override
	public void init() {
		// note this is in init as it **must not** be called on the FX Application Thread:
		Webcam cam = Webcam.getWebcams().get(0);
		service = new WebCamService(cam);
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			createClinicScene(primaryStage);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public VBox createMainMenu(Stage primaryStage) {
		Menu apptsMenu = new Menu("Appointments");
		Menu patientMenu = new Menu("Patients");
		Menu specialistMenu = new Menu("Specialists");
		MenuBar menuBar = new MenuBar();
		
		MenuItem m1 = new MenuItem("Appointments");
		m1.setOnAction(e -> {
			createAppointmentsWindow(primaryStage);
		});
		
		MenuItem m2 = new MenuItem("Create Appointment");
		m2.setOnAction(e -> {
			createNewAppointmentWindow(primaryStage);
		});
		
		MenuItem m3 = new MenuItem("New Patient");
		m3.setOnAction(e -> {
			createNewPatientWindow(primaryStage);
		});
		
		MenuItem m4 = new MenuItem("Edit Patient");
		m4.setOnAction(e -> {
			createEditPatientWindow(primaryStage);
		});
		
		MenuItem m5 = new MenuItem("New Specialist");
		m5.setOnAction(e -> {
			createNewSpecialistWindow(primaryStage);
		});
		
		MenuItem m6 = new MenuItem("Edit Specialist");
		m6.setOnAction(e -> {
			createEditSpecialistWindow(primaryStage);
		});
		
		apptsMenu.getItems().addAll(m1,m2);
		patientMenu.getItems().addAll(m3,m4);
		specialistMenu.getItems().addAll(m5,m6);
		
		menuBar.getMenus().addAll(apptsMenu,patientMenu,specialistMenu);
		
		return new VBox(menuBar);
	}
	
	public void createClinicScene(Stage primaryStage) {
		//CLINIC NAME FORM
		HBox clinicFormHB = new HBox();
		
		Label clinicNameLabel = new Label("Clinic name:");
		TextField clinicNameField = new TextField();
		Button submitBtn = new Button("Submit");
		submitBtn.setDefaultButton(true);
		submitBtn.setOnAction(e -> {  
			clinicName = clinicNameField.getText();
			
			createMainScene(primaryStage);
        } );  
		
		clinicCalendar.clinicName = clinicName;
		
		clinicFormHB.setSpacing(5);
		clinicFormHB.getChildren().addAll(clinicNameLabel,clinicNameField,submitBtn);
		
		Scene clinicNameScene = new Scene(clinicFormHB,400,400);
		
		primaryStage.setScene(clinicNameScene);
		primaryStage.show();
		//END CLINIC NAME FORM
	}
	
	public void createMainScene(Stage primaryStage) {
		VBox menu = createMainMenu(primaryStage);
		Scene mainScene = new Scene(menu,400,400);
		
		primaryStage.setScene(mainScene);
		primaryStage.setTitle(clinicName);
		primaryStage.show();
	}
	
	public void createAppointmentsWindow(Stage primaryStage) {
		BorderPane view = new BorderPane();
		
		VBox menu = createMainMenu(primaryStage);
		view.setTop(menu);
		
		//only display appointments that occur on the current date
		ListView<Appointment> appts = new ListView<>();
		ArrayList<Appointment> apptAL = new ArrayList<>();
		for(Appointment curr : clinicCalendar.appointments) {
			if(curr.apptTime.truncatedTo(ChronoUnit.DAYS).isEqual(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS))) {
				apptAL.add(curr);
			}
		}
		
		ObservableList<Appointment> apptsList = FXCollections.observableArrayList(apptAL);
		appts.setItems(apptsList);
		view.setCenter(appts);
		
        Scene apptScene = new Scene(view,400,400);
        
        primaryStage.setScene(apptScene);
        primaryStage.show();
	}
	
	public void createNewAppointmentWindow(Stage primaryStage) {
		final String[] availTimes = {
				"08:00AM","08:30AM","09:00AM","09:30AM","10:00AM","10:30AM",
				"11:00AM","11:30AM","12:00PM","12:30PM","01:00PM","01:30PM",
				"02:00PM","02:30PM","03:00PM","03:30PM","04:00PM","04:30PM",
				"05:00PM","05:30PM","06:00PM","06:30PM","07:00PM","07:30PM",
				"08:00PM","08:30PM","09:00PM"
		};
		
		//open new window for appointment creating form
		Stage apptWindow = new Stage();
		apptWindow.setTitle(clinicName);
		
		//block input to parent window while appointment creating
		//window is open
		apptWindow.initModality(Modality.WINDOW_MODAL);
		apptWindow.initOwner(primaryStage);
		
		VBox newApptVBox = new VBox(5);
		
		//display specialists
        Label specialistLabel = new Label("Specialist:");
        newApptVBox.getChildren().add(specialistLabel);
        
        ChoiceBox<Specialist> specialistCB = new ChoiceBox<>();
        ObservableList<Specialist> specialistOList = 
        		FXCollections.observableArrayList(clinicCalendar.specialists);
        specialistCB.setItems(specialistOList);
        newApptVBox.getChildren().add(specialistCB);
        
        //display patients
        Label patientLabel = new Label("Patient:");
        newApptVBox.getChildren().add(patientLabel);

        ChoiceBox<Patient> patientCB = new ChoiceBox<>();
        ObservableList<Patient> patientOList = 
        		FXCollections.observableArrayList(clinicCalendar.patients);
        patientCB.setItems(patientOList);
        
        newApptVBox.getChildren().add(patientCB);
        
        //display available dates
        Label dateLabel = new Label("Appointment Date:");
        newApptVBox.getChildren().add(dateLabel);
		
		DatePicker apptDP = new DatePicker();
		//disable past dates
		apptDP.setDayCellFactory(picker -> new DateCell() {
	        public void updateItem(LocalDate date, boolean empty) {
	            super.updateItem(date, empty);
	            LocalDate today = LocalDate.now();

	            setDisable(empty || date.compareTo(today) < 0 );
	        }
	    });	

        newApptVBox.getChildren().add(apptDP);
        
        //appointment time selection
        Label apptTimeLabel = new Label("Appointment time:");
        newApptVBox.getChildren().add(apptTimeLabel);
        
        ChoiceBox<String> apptTimeCB = new ChoiceBox<>(FXCollections.observableArrayList(
        	    availTimes) );
        newApptVBox.getChildren().add(apptTimeCB);
        
        //submit button
        Button submit = new Button("Submit");
        submit.setDefaultButton(true);
        submit.setOnAction(e -> { 
        	if((apptDP.getValue() != null) && (specialistCB.getValue() != null) && 
        			(patientCB.getValue() != null) && (apptTimeCB.getValue() != null)) {
	        	LocalDate apptDate = apptDP.getValue();
	        	
	        	//convert time selection from string to DateTime
	        	DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("h:mma");
	        	LocalTime apptTime = LocalTime.parse(apptTimeCB.getValue(),timeFormat);
	        	//combine apptDate and apptTime to get LocalDateTime object
	        	LocalDateTime apptDateTime = apptTime.atDate(apptDate);
	        	
				Appointment newAppt = new Appointment(patientCB.getValue(),specialistCB.getValue(),
						LocalDateTime.now(),apptDateTime,100.0f,false);
				clinicCalendar.addAppt(newAppt);
				
				apptWindow.close();
        	}
        	else {
        		String alertMessage = "Please make a selection for all fields.";
        		Alert a = new Alert(AlertType.ERROR,alertMessage);
        		a.show();
        	}
        } );
        
        newApptVBox.getChildren().add(submit);
        
        Scene newApptScene = new Scene(newApptVBox,400,400);
		apptWindow.setScene(newApptScene);
        apptWindow.show();
	}
	
	public void createNewPatientWindow(Stage primaryStage) {
		//open new window for appointment creating form
		Stage newPatientWindow = new Stage();
		newPatientWindow.setTitle(clinicName);
		
		//block input to parent window while appointment creating
		//window is open
		newPatientWindow.initModality(Modality.WINDOW_MODAL);
		newPatientWindow.initOwner(primaryStage);
		
		VBox newPatientVBox = new VBox(5);
		
		Label firstNameLabel = new Label("First Name:");
        newPatientVBox.getChildren().add(firstNameLabel);
		TextField firstNameInput = new TextField();
		newPatientVBox.getChildren().add(firstNameInput);
		
		Label lastNameLabel = new Label("Last Name:");
        newPatientVBox.getChildren().add(lastNameLabel);
        TextField lastNameInput = new TextField();
        newPatientVBox.getChildren().add(lastNameInput);
        
        Label emailLabel = new Label("Email:");
        newPatientVBox.getChildren().add(emailLabel);
        TextField emailInput = new TextField();
        newPatientVBox.getChildren().add(emailInput);
        
        Label phoneLabel = new Label("Phone Number:");
        newPatientVBox.getChildren().add(phoneLabel);
        TextField phoneInput = new TextField();
        newPatientVBox.getChildren().add(phoneInput);
		
        //submit button
        Button submit = new Button("Submit");
        submit.setDefaultButton(true);
        submit.setOnAction(e -> {
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
    					
    					
    					Patient newPatient = clinicCalendar.addPatient(firstNameInput.getText(), lastNameInput.getText(), 
	    						emailInput.getText(), phoneInput.getText(), LocalDate.now());
    					
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
        	
			newPatientWindow.close();
        } );
        
        newPatientVBox.getChildren().add(submit);
        
        Scene newPatientScene = new Scene(newPatientVBox,400,400);
		newPatientWindow.setScene(newPatientScene);
        
        newPatientWindow.setScene(newPatientScene);
        newPatientWindow.show();
	}
	
	public void createEditPatientWindow(Stage primaryStage) {
		//root container
		BorderPane view = new BorderPane();
		
		VBox menu = createMainMenu(primaryStage);
		view.setTop(menu);
		
		//Patient info on the right
        VBox patientInfoView = new VBox();
        //hide patient info section until a patient is selected
        patientInfoView.setVisible(false);
        patientInfoView.setPrefWidth(Double.MAX_VALUE);
      	//Creating the image view
        ImageView imageView = new ImageView();
        //Create name fields
        Label firstLabel = new Label("First:"), lastLabel = new Label("Last:");
        TextField firstNameTF = new TextField(), lastNameTF = new TextField();
        firstNameTF.setMaxWidth(150);
        lastNameTF.setMaxWidth(150);
        //
        Button submitBtn = new Button("Confirm");
        
        patientInfoView.getChildren().add(imageView);
        patientInfoView.getChildren().add(firstLabel);
        patientInfoView.getChildren().add(firstNameTF);
        patientInfoView.getChildren().add(lastLabel);
        patientInfoView.getChildren().add(lastNameTF);
        patientInfoView.getChildren().add(submitBtn);
		
		//List of patients on left
		ListView<Patient> patientsView = new ListView<>();
		patientsView.setPrefWidth(100);
		view.setLeft(patientsView);
		
		ObservableList<Patient> patientOList = 
        		FXCollections.observableArrayList(clinicCalendar.patients);
        patientsView.setItems(patientOList);
        
        //display selected patient's info in patient info section
        patientsView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Patient>() {
        	@Override
            public void changed(ObservableValue<? extends Patient> observable, Patient oldValue, Patient newValue) {
        		patientInfoView.setVisible(true);
        		
        		try {
        			System.out.println(newValue.profPicImageName);
        			
	                imageView.setImage(convertToFxImage(newValue.loadProfPic()));
	                imageView.setFitWidth(120);
	                imageView.setFitHeight(150);
	                imageView.setPreserveRatio(true);
        		}
        		catch(IOException e) {
        			System.out.println(e);
        		}
                
                firstNameTF.setText(newValue.firstName);
                lastNameTF.setText(newValue.lastName);
                
                submitBtn.setOnAction(e -> {  
        			newValue.firstName = firstNameTF.getText();
        			newValue.lastName = lastNameTF.getText();
        			
        			createMainScene(primaryStage);
                } );  
            }
        });
        
        Scene editPatientScene = new Scene(view,400,400);
        
        primaryStage.setScene(editPatientScene);
        primaryStage.show();
        
        view.setCenter(patientInfoView);
	}
	
	public void createNewSpecialistWindow(Stage primaryStage) {
		//open new window for appointment creating form
		Stage newSpecialistWindow = new Stage();
		newSpecialistWindow.setTitle(clinicName);
		
		//block input to parent window while appointment creating
		//window is open
		newSpecialistWindow.initModality(Modality.WINDOW_MODAL);
		newSpecialistWindow.initOwner(primaryStage);
		
		VBox newSpecialistVBox = new VBox(5);
		
		Label firstNameLabel = new Label("First Name:");
        newSpecialistVBox.getChildren().add(firstNameLabel);

		TextField firstNameInput = new TextField();
		newSpecialistVBox.getChildren().add(firstNameInput);
		
		Label lastNameLabel = new Label("Last Name:");
        newSpecialistVBox.getChildren().add(lastNameLabel);

        TextField lastNameInput = new TextField();
        newSpecialistVBox.getChildren().add(lastNameInput);
        
        Label phoneLabel = new Label("Phone Number:");
        newSpecialistVBox.getChildren().add(phoneLabel);
        
        TextField phoneInput = new TextField();
        newSpecialistVBox.getChildren().add(phoneInput);
        
        Label emailLabel = new Label("Email:");
        newSpecialistVBox.getChildren().add(emailLabel);
        
        TextField emailInput = new TextField();
        newSpecialistVBox.getChildren().add(emailInput);
        
        //Contact preference controls
        Label contactPrefLabel = new Label("Contact Preference:");
        newSpecialistVBox.getChildren().add(contactPrefLabel);
        
        final ToggleGroup contactPrefGroup = new ToggleGroup();

        RadioButton phoneButton = new RadioButton("Phone");
        phoneButton.setToggleGroup(contactPrefGroup);
        phoneButton.setSelected(true);
        newSpecialistVBox.getChildren().add(phoneButton);

        RadioButton emailButton = new RadioButton("Email");
        emailButton.setToggleGroup(contactPrefGroup);
        newSpecialistVBox.getChildren().add(emailButton);
         
        RadioButton neitherButton = new RadioButton("Both");
        neitherButton.setToggleGroup(contactPrefGroup);
        newSpecialistVBox.getChildren().add(neitherButton);
        
        //Creating a pagination
        TextArea area = new TextArea();
        //Setting number of pages
        area.setText("Enter your bio here");
        area.setPrefColumnCount(15);
        area.setPrefHeight(120);
        area.setPrefWidth(300);
        
        
        //submit button
        Button submit = new Button("Submit");
        submit.setDefaultButton(true);
        submit.setOnAction(e -> {
        	Specialist.ContactPref newContactPref = null;
            if(phoneButton.isSelected()) {
        		newContactPref = Specialist.ContactPref.PHONE;
        	}
        	else if(emailButton.isSelected()) {
        		newContactPref = Specialist.ContactPref.EMAIL;
        	}
        	else if(neitherButton.isSelected()) {
        		newContactPref = Specialist.ContactPref.NEITHER;
        	}
        	else {
        		
        	}
            //create final contact preference to be used for camera event handler
            final Specialist.ContactPref finalContactPref = newContactPref;
            
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
    					Specialist newSpecialist = clinicCalendar.addSpecialist(firstNameInput.getText(), lastNameInput.getText(), 
	    						finalContactPref,phoneInput.getText(),emailInput.getText(),area.getText());
    					
	    				cam2.open();
	    				ImageIO.write(
	        				cam2.getImage(), "JPG", 
	        				new File(newSpecialist.profPicImageName));
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
        	
			newSpecialistWindow.close();
        } ); 
        
        newSpecialistVBox.getChildren().add(submit);
        
        Scene newSpecialistScene = new Scene(newSpecialistVBox,400,400);
		newSpecialistWindow.setScene(newSpecialistScene);
        
        newSpecialistWindow.setScene(newSpecialistScene);
        newSpecialistWindow.show();
	}
	
	public void createEditSpecialistWindow(Stage primaryStage) {
		//root container
		BorderPane view = new BorderPane();
		
		VBox menu = createMainMenu(primaryStage);
		view.setTop(menu);
		
		//Specialist info on the right
        VBox specialistInfoView = new VBox();
        specialistInfoView.setVisible(false);
        specialistInfoView.setPrefWidth(Double.MAX_VALUE);
      	//Creating the image view
        ImageView imageView = new ImageView();
        
        //Create name fields
        Label firstLabel = new Label("First:"), lastLabel = new Label("Last:");
        TextField firstNameTF = new TextField(), lastNameTF = new TextField();
        firstNameTF.setMaxWidth(150);
        lastNameTF.setMaxWidth(150);
        
        //Create email field
        Label emailLabel = new Label("Email:");
        TextField emailTF = new TextField();
        emailTF.setMaxWidth(150);
        
        //Create phone number field
        Label phoneLabel = new Label("Phone:");
        TextField phoneTF = new TextField();
        phoneTF.setMaxWidth(140);
        
        Button submitBtn = new Button("Confirm");
        
        specialistInfoView.getChildren().add(imageView);
        specialistInfoView.getChildren().add(firstLabel);
        specialistInfoView.getChildren().add(firstNameTF);
        specialistInfoView.getChildren().add(lastLabel);
        specialistInfoView.getChildren().add(lastNameTF);
        specialistInfoView.getChildren().add(emailLabel);
        specialistInfoView.getChildren().add(emailTF);
        specialistInfoView.getChildren().add(phoneLabel);
        specialistInfoView.getChildren().add(phoneTF);
        specialistInfoView.getChildren().add(submitBtn);
		
		//List of specialists on left
		ListView<Specialist> specialistsView = new ListView<>();
		specialistsView.setPrefWidth(100);
		view.setLeft(specialistsView);
		
		ObservableList<Specialist> specialistOList = 
        		FXCollections.observableArrayList(clinicCalendar.specialists);
        specialistsView.setItems(specialistOList);
        specialistsView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Specialist>() {
        	@Override
            public void changed(ObservableValue<? extends Specialist> observable, Specialist oldValue, Specialist newValue) {
                specialistInfoView.setVisible(true);
        		
                try {
	        		imageView.setImage(convertToFxImage(newValue.loadProfPic()));
	                imageView.setFitWidth(120);
	                imageView.setFitHeight(150);
	                imageView.setPreserveRatio(true);
                }
                catch(IOException e) {
                	System.out.println(e);
                }
                
                firstNameTF.setText(newValue.firstName);
                lastNameTF.setText(newValue.lastName);
                emailTF.setText(newValue.getEmailAddr());
                phoneTF.setText(newValue.getPhoneNo());
                
                submitBtn.setOnAction(e -> {  
        			newValue.firstName = firstNameTF.getText();
        			newValue.lastName = lastNameTF.getText();
        			newValue.setEmailAddr(emailTF.getText());
        			
        			createMainScene(primaryStage);
                } );  
            }
        });
        
        Scene editSpecialistScene = new Scene(view,400,400);
        
        primaryStage.setScene(editSpecialistScene);
        primaryStage.show();
        
        view.setCenter(specialistInfoView);
	}
	
	private static Image convertToFxImage(BufferedImage image) {
	    WritableImage wr = null;
	    if (image != null) {
	        wr = new WritableImage(image.getWidth(), image.getHeight());
	        PixelWriter pw = wr.getPixelWriter();
	        for (int x = 0; x < image.getWidth(); x++) {
	            for (int y = 0; y < image.getHeight(); y++) {
	                pw.setArgb(x, y, image.getRGB(x, y));
	            }
	        }
	    }

	    return new ImageView(wr).getImage();
	}
}