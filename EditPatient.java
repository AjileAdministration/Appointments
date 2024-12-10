package com.ajilesolutions.appointmenttracker.Appointment_Tracker;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EditPatient {
	private static final double WINDOW_WIDTH = 600;
	private static final double WINDOW_HEIGHT = 400;
	
	@FXML
	private BorderPane rootNode;
	@FXML
	private ListView<Patient> patients;
	@FXML
	private VBox patientInfo;
	@FXML
	private ImageView profileImage;
	@FXML
	private TextField firstName;
	@FXML
	private TextField lastName;
	@FXML
	private TextField email;
	@FXML
	private TextField phoneNo;
	@FXML
	private Button confirm;
	@FXML
	private Button delete;
	
	private ObservableList<Patient> patientOList;
	
	public EditPatient() {
		
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
		
		Collections.sort(Schedule.getInstance().patients, new Comparator<Patient>(){
		    public int compare(Patient p1, Patient p2){
		         return p1.firstName.toLowerCase().compareTo(p2.firstName.toLowerCase());
		    }    
		});
		patientOList = FXCollections.observableArrayList(Schedule.getInstance().patients);
        patients.setItems(patientOList);
        
        patients.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Patient>() {
        	@Override
            public void changed(ObservableValue<? extends Patient> observable, Patient oldValue, Patient newValue) {
        		patientInfo.setVisible(true);
        		
        		try {
        			profileImage.setImage(GlobalHelperMethods.convertToFxImage(newValue.loadProfPic()));
        		}
        		catch(IOException iOException) {
        			System.out.println(iOException.getMessage());
        		}
                
                firstName.setText(newValue.firstName);
                lastName.setText(newValue.lastName);
                email.setText(newValue.email);
                phoneNo.setText(newValue.phoneNo);
                
                confirm.setOnAction(e -> {  
        			Schedule.getInstance().updatePatient(newValue,firstName.getText(),
        					lastName.getText(),email.getText(),phoneNo.getText());
        			
        			try {
        				Stage currentWindow = GlobalHelperMethods.getCurrentWindow();
        				Parent mainMenuRoot = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        				Scene mainMenuScreen = new Scene(mainMenuRoot,WINDOW_WIDTH,WINDOW_HEIGHT);
        				currentWindow.setScene(mainMenuScreen);
        				currentWindow.setTitle("Main Menu");
        			}
        			catch(Exception exception) {
        				System.out.println(exception.getMessage());
        			}
                } );  
            }
        });
	}
	
	public void onDeleteClick(ActionEvent e) {
		Patient selected = patients.getSelectionModel().getSelectedItem();
		Schedule.getInstance().removePatient(selected);
	}
}


