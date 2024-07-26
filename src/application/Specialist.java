package application;

import java.util.Properties;
import java.util.Scanner;
import java.time.LocalDateTime;

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


    
public class Specialist {
	private static String USER_NAME = "podfan12345";  // GMail user name (just the part before "@gmail.com")
    private static String PASSWORD = "ugmnkdbgwwvnchsp "; // GMail password
	
    public enum ContactPref {
    	PHONE,EMAIL,NEITHER
    }
    
    private int id;
    public String firstName;
    public String lastName;
    public String bio;
    public ContactPref preference;
    private String phoneNo;
    private String emailAddr;
    private LocalDateTime createDate;
    
    public Specialist(int id, String first, String last, LocalDateTime create){
    	this.createDate = create;
    	this.id = id;
        this.firstName = first;
        this.lastName = last;
        
        preference = ContactPref.NEITHER;
    }
    
    public void setEmailAddr(String emailAddr) {
    	this.emailAddr = emailAddr;
    }
    
    public String getEmailAddr() {
    	return emailAddr;
    }
    
    public String getPhoneNo() {
    	return phoneNo;
    }
    
    public String toString(){
        return (firstName + " " + lastName);
    }
    
    public void notifySpecialist() {
    	if(this.preference == ContactPref.EMAIL){
    		String subject = "Your patient has checked in";
    		String body = this.firstName + " has checked in for their appointment";
    				
    		this.sendFromGMail(USER_NAME,PASSWORD,subject,body);
    	}
    	
    	if(this.preference == ContactPref.PHONE) {
    		String subject = "Your patient has checked in";
    		String body = this.firstName + " has checked in for their appointment";
    	}
    }
    
    private void sendFromGMail(String from, String pass, String subject, String body) {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            
            //set the recipient address
            InternetAddress toAddress = new InternetAddress(this.emailAddr);
            message.addRecipient(Message.RecipientType.TO, toAddress);

            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }
}


