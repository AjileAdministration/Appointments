package application;

import java.time.LocalDateTime;

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
    public LocalDateTime createDate;
    public BufferedImage profPic;
    
    public Patient(int id, String first, String last, String email, String phone, LocalDateTime create){
    	this.id = id;
        this.firstName = first;
        this.lastName = last;
        this.email = email;
        this.phoneNo = phone;
        this.createDate = create;
        
        setProfPicImageName();
        
        try{
        	profPic = loadProfPic();
        }
        catch(IOException e) {
        	profPic = null;
        }
    }
    
    public String toString(){
        return (firstName + " " + lastName);
    }
    
    private BufferedImage loadProfPic() throws IOException {
    	try {
    		setProfPicImageName();
    		
    		BufferedImage profPic = ImageIO.read(
    			new File(this.profPicImageName));
    		
    		return profPic;
    	}
    	catch(IOException e) {
    		throw e;
    	}
    }
    
    private void setProfPicImageName() {
    	String fullName = "C:\\Users\\ajlso\\eclipse-workspace\\Appointments\\Resources\\"
    			+  this.firstName + this.lastName + ".jpg";
    	
    	this.profPicImageName = fullName;
    }
    
}
