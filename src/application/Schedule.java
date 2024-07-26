package application;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import javax.mail.*;

/**
 *
 * @author ajlso
 */
public class Schedule {
	private static int numSpecialists = 0;
	private static int numPatients = 0;
	
	public ArrayList<Appointment> appointments;
    public ArrayList<Patient> patients;
    public ArrayList<Specialist> specialists;
    
    public String clinicName;
    
    public Schedule(){
    	//patients = loadPatients();
    	//specialists = loadSpecialists();
    	//appointments = loadAppointments();
        
        appointments = new ArrayList<>();
        patients = new ArrayList<>();
        specialists = new ArrayList<>();
    }
    
    public void addAppt(Appointment newAppt)
    {
        appointments.add(newAppt);
        
        
    }
    
    public void addPatient(String first, String last, String email, String phone, LocalDateTime create){
    	//record date of patient's first visit
    	LocalDateTime creation = LocalDateTime.now();
    	
    	//increment numPatients and use it to set new patient's ID
    	numPatients++;
    	
        Patient newPatient = new Patient(numPatients,first,last,email,phone,creation);
        
        patients.add(newPatient);
    }
    
    public void addSpecialist(String first, String last, LocalDateTime create){
    	//record date of specialist's creation
    	LocalDateTime creation = LocalDateTime.now();
    	
    	//increment numSpecialists and use it to set new specialist's ID
    	numSpecialists++;
    	
        Specialist newSpecialist = new Specialist(numSpecialists,first,last,creation);
        
        specialists.add(newSpecialist);
    }
    
    public ArrayList<Appointment> showAppointments(LocalDate apptsDate){
    	ArrayList<Appointment> dateAppts = new ArrayList<>();
    	
        for(int i=0; i < appointments.size(); i++){
        	//check if current appointment date is same as apptsDate
        	if(apptsDate.getDayOfYear() == appointments.get(i).apptTime.getDayOfYear() &&
        			apptsDate.getYear() == appointments.get(i).apptTime.getYear()) {
        		dateAppts.add(appointments.get(i));
        	}
        }
        
        return dateAppts;
    }
    
    public void showPatients(){
        for(int i=0; i < patients.size(); i++){
            System.out.print((i+1) + ". ");
            System.out.print(patients.get(i));
            System.out.print("\n");
        }
    }
    
    public void showSpecialists(){
        for(int i=0; i < specialists.size(); i++){
            System.out.print((i+1) + ". ");
            System.out.print(specialists.get(i));
            System.out.print("\n");
        }
    }
    
    private ArrayList<Patient> loadPatients() {
    	ArrayList<Patient> patients = new ArrayList<>();
    	
    	return patients;
    }
    
    private ArrayList<Specialist> loadSpecialists() {
    	ArrayList<Specialist> specialists = new ArrayList<>();
    	
    	return specialists;
    }
    
    private ArrayList<Appointment> loadAppointments() {
    	ArrayList<Appointment> appointments = new ArrayList<>();
    	
    	return appointments;
    }
}
