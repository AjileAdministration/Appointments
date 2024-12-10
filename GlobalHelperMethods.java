package com.ajilesolutions.appointmenttracker.Appointment_Tracker;

import java.awt.image.BufferedImage;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import javafx.stage.Window;

public final class GlobalHelperMethods {

	private GlobalHelperMethods() {
		// TODO Auto-generated constructor stub
	}

	public static Stage getCurrentWindow() {
		return (Stage) Stage.getWindows().stream()
				.filter(Window::isShowing).findFirst().orElse(null);
	}
	
	public static Image convertToFxImage(BufferedImage image) {
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
