package com.ajilesolutions.appointmenttracker.Appointment_Tracker;

import java.time.LocalDate;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;

/**
 *
 * @author ajlso
 */
public class Patient {
	private int id;
	public String profPicImageName;
    public String firstName;
    public String lastName;
    public String email;
    public String phoneNo;
    public LocalDate createDate;
    
    public Patient(int id, String first, String last, String email, String phone, LocalDate create){
    	this.id = id;
        this.firstName = first;
        this.lastName = last;
        this.email = email;
        this.phoneNo = phone;
        this.createDate = create;
        
        setProfPicImageName();
    }
    
    public String toString(){
        return (firstName + " " + lastName);
    }
    
    public int getID() {
    	return this.id;
    }
    
    public BufferedImage loadProfPic() throws IOException {
    	try {
    		BufferedImage profPic = ImageIO.read(
    			new File(this.profPicImageName));
    		
    		return profPic;
    	}
    	catch(IOException e) {
    		throw e;
    	}
    }
    
    private void setProfPicImageName() {
    	String fullName = 
    		"C:\\Users\\ajlso\\eclipse-workspace\\Appointment-Tracker\\src\\main\\Resources\\"
    		+  this.firstName + this.lastName + ".jpg";
    	
    	this.profPicImageName = fullName;
    }
}