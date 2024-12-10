package com.ajilesolutions.appointmenttracker.Appointment_Tracker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author ajlso
 */
public class Appointment {
    public Patient apptPatient;
    public Specialist apptSpecialist;
    public LocalDateTime createTime;
    public LocalDateTime apptTime;
    public float cost;
    public boolean cancelled;
    public boolean signedIn;
    
    public Appointment(Patient p, Specialist s, LocalDateTime cTime, 
            LocalDateTime t, float cost, boolean cancelled){
        this.apptPatient = p;
        this.apptSpecialist = s;
        this.createTime = cTime;
        this.apptTime = t;
        this.cost = cost;
        this.cancelled = cancelled;
        this.signedIn = false;
    }
    
    @Override
    public String toString(){
        StringBuilder apptStr = new StringBuilder();
        DateTimeFormatter dtf;
        
        apptStr.append(apptSpecialist.firstName + " " + apptSpecialist.lastName);
        apptStr.append(" is meeting with patient ");
        apptStr.append(apptPatient.firstName + " " + 
                apptPatient.lastName);
        
        dtf =  DateTimeFormatter.ofPattern("MM-dd-yyyy");
        apptStr.append(" on " + apptTime.format(dtf));
        
        dtf = DateTimeFormatter.ofPattern("hh:mma");
        apptStr.append(" at " + apptTime.format(dtf));
        
        return apptStr.toString();
    }
    
    public void notifySpecialist() {
    	this.apptSpecialist.notifySpecialist(apptPatient);
    }
}
