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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class EditSpecialist {
	final static double WINDOW_WIDTH = 600;
	final static double WINDOW_HEIGHT = 400;
	final int SPECIALIST_INFO_MAX_WIDTH = 500;
	
	@FXML
	private BorderPane rootNode;
	@FXML
	private ListView<Specialist> specialists;
	@FXML
	private Pane specialistInfo;
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
	private ToggleGroup contactPreference;
	@FXML
	private RadioButton emailPreference;
	@FXML
	private RadioButton phonePreference;
	@FXML
	private RadioButton neitherPreference;
	@FXML
	private TextArea bio;
	@FXML
	private Button confirm;
	
	private ObservableList<Specialist> specialistOList;
	
	public EditSpecialist() {
		// TODO Auto-generated constructor stub
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
		
		Collections.sort(Schedule.getInstance().specialists, new Comparator<Specialist>(){
		    public int compare(Specialist s1, Specialist s2){
		         return s1.firstName.toLowerCase().compareTo(s2.firstName.toLowerCase());
		    }
		});
		specialistOList = 
        		FXCollections.observableArrayList(Schedule.getInstance().specialists);
        specialists.setItems(specialistOList);
        
        
        specialists.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Specialist>() {
        	@Override
            public void changed(ObservableValue<? extends Specialist> observable, Specialist oldValue, Specialist newValue) {
        		specialistInfo.setVisible(true);
        		
        		try {
        			profileImage.setImage(GlobalHelperMethods.convertToFxImage(newValue.loadProfPic()));
        		}
        		catch(IOException iOException) {
        			System.out.println(iOException.getMessage());
        		}
                
                firstName.setText(newValue.firstName);
                lastName.setText(newValue.lastName);
                email.setText(newValue.getEmailAddr());
                phoneNo.setText(newValue.getPhoneNo());
                
        		switch(newValue.preference) {
        		case EMAIL:
        			emailPreference.setSelected(true);
        			break;
        		case PHONE:
        			phonePreference.setSelected(true);
        			break;
        		default:
        			neitherPreference.setSelected(true);
        			break;
        		}
        		
        		bio.setText(newValue.bio);
        		
        		confirm.setOnAction(e -> {
        			Specialist.ContactPref selectedPreference = 
        					Specialist.ContactPref.valueOf(
        					((RadioButton)contactPreference.getSelectedToggle()).getText().toUpperCase());
        			
        			Schedule.getInstance().updateSpecialist(newValue,firstName.getText(),
            				lastName.getText(),selectedPreference,email.getText(),phoneNo.getText(),bio.getText());
        			
        			try {
        				Stage currentWindow = (Stage)((Node) e.getSource()).getScene().getWindow();
        				Parent mainMenuRoot = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        				Scene mainMenuScreen = new Scene(mainMenuRoot,WINDOW_WIDTH,WINDOW_HEIGHT);
        				currentWindow.setScene(mainMenuScreen);
        				currentWindow.setTitle("Main Menu");
        			}
        			catch(Exception exception) {
        				System.out.println(exception.getMessage());
        			}
        		});
            }
        });
	}
}
