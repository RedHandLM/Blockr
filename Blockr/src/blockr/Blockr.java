package blockr;

import processing.core.PApplet;
import processing.core.PImage;
import processing.video.Capture;


public class Blockr extends PApplet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// ***World-Coordinates
	//----------------------------------
	float centerX = width/2;
	float centerY = height/2;
	
	// ***Camera variables
	//----------------------------------
	Capture cam;
	float meR,meG,meB;
	
	// ***Background
	//----------------------------------
	PImage bg;
	
	// ***Shield Setup
	//----------------------------------
	Shield sh = new Shield();
	

	public void setup() {
		// ***World Setup
		//----------------------------------
		size (1024, 768);
		noStroke();
		ellipseMode(CENTER);
		  
		bg = loadImage ("background.png");
		
		
		// ***Camera Setup
		//----------------------------------
		String[] camNames = cam.list();
		
		meR = 255;
		meG = 0;
		meB = 0;
		
		cam = new Capture(this, 1024, 768);
		
		cam.start();
		
		//----------------------------------
		  
	}

	public void draw() {
		// ***BG
		//----------------------------------
		background(0);
		image (bg, centerX + pmouseX/-1, centerY + pmouseY/-1);
		  
		// ***Shield
		//----------------------------------
		
		//if (mousePressed) sh.move();
		  
		//sh.decelerate();
		//sh.make();
		
		// ***Camera
		//----------------------------------
		
		if(cam.available()){
			cam.read();
			//image(cam, 0, 0);
			
			fill(255);
			
			int[] pixs = cam.pixels;
			
			float closestToColor = 100;
			int x = 0;
			int y = 0;
			
			for(int i = 0; i < pixs.length; i++){
				int color = pixs[i];
				
				if(dist(meR, meG, meB, red(color), green(color), blue(color)) < closestToColor){
					closestToColor = dist(meR, meG, meB, red(color), green(color), blue(color));
					
					y = i/cam.width;
					x = i%cam.width;
				}
			}
			
			ellipse(x, y, 50, 50);
		}
	}
	
	public static void main(String _args[]) {
		PApplet.main(new String[] { blockr.Blockr.class.getName() });
	}
}
