package cujae.map.j2g.util;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;

public class AlertImageFilter extends RGBImageFilter {
	
	public final int alert;
	public final int width;
	public final int height;
	
	public AlertImageFilter(int alert, int width, int height) {
		super();
		this.alert = alert;
		this.width = width;
		this.height = height;
	}

	@Override
	public int filterRGB(int x, int y, int rgb) {
		
		int frgb = rgb;
		
		switch(Integer.signum(alert)){
		case -1:
			if(inside(x, y, width, height)){
				frgb = Color.RED.getRGB();
			}
			break;
		case  0: break;
		case  1: 
			if(inside(x, y, width, height)){
				frgb = Color.YELLOW.getRGB();
			}
			break;
		}
		
		return frgb;
	}
	
	public static boolean inside(int x, int y, int width, int height){
		return x < width && y < height;
	}
	
	public static Image createAlertImage(Image i, int alert, int width, int height){
		AlertImageFilter f = new AlertImageFilter(alert, width, height);
		ImageProducer p = new FilteredImageSource(i.getSource(), f);
		return Toolkit.getDefaultToolkit().createImage(p);
	}
	
	public static Image createAlertImage(Image i, int alert){
		return createAlertImage(i, alert, 8, 8);
	}

}
