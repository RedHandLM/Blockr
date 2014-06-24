package blockr;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
//import processing.video.Capture;


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
//	Capture cam;
	float meR,meG,meB;
	static float menowX = 0.0f;
	static float menowY = 0.0f;
	
	// ***Background
	//----------------------------------
	PImage bg;
	
	// ***Shield Setup
	//----------------------------------
	//Shield sh = new Shield();
	
	// ***Me Setup
	//----------------------------------
	PVector mePos;
	PVector meDir;
	float meAccel = 0.5f;
	float meSpeed = 0.2f;
	float meStartX, meStartY;
	

	public void setup() {
		// ***World Setup
		//----------------------------------
		size (1024, 768);
		noStroke();
		ellipseMode(CENTER);
		  
		bg = loadImage ("background.png");
		
//		pushMatrix();
//		translate(512,384);
		
		
		// ***Camera Setup
		//----------------------------------
//		String[] camNames = cam.list();
//		
//		meR = 255;
//		meG = 0;
//		meB = 0;
//		
//		cam = new Capture(this, 1024, 768);
//		
//		cam.start();
		
		// ***Me Setup
		//----------------------------------
		meStartX = width/2;
		meStartY = height/2;
		
		mePos = new PVector(meStartX, meStartY);
		meDir = new PVector(menowX, menowY);
		
		//----------------------------------
		  
	}

	public void draw() {
		// ***BG
		//----------------------------------
		background(0);
		image (bg, centerX + mePos.x/-10, centerY + mePos.y/-10);
		  
		// ***Shield
		//----------------------------------
		
		
		// ***Me
		//----------------------------------
		meDir = new PVector(menowX, menowY);
		meDir.sub(mePos);
		//meDir.normalize();
		PVector move = PVector.mult(meDir, meSpeed);
		mePos.add(move);
		
		// ***Camera
		//----------------------------------
		
//		if(cam.available()){
//			cam.read();
//			
//			//image(cam, 0, 0);
//			
//			fill(255);
//			float meY = meStartY;
//			float meX = meStartX;
//			
//			
//			int[] pixs = cam.pixels;
//			
//			float closestToColor = 100;
//			
//			for(int i = 0; i < pixs.length; i++){
//				int color = pixs[i];
//				
//				if(dist(meR, meG, meB, red(color), green(color), blue(color)) < closestToColor){
//					
//					closestToColor = dist(meR, meG, meB, red(color), green(color), blue(color));
//					
//					
//					meY = i/cam.width;
//					meX = i%cam.width;
//					
//					menowX = meX;
//					menowY = meY;
//				}
//			}
//		}
//		ellipse(mePos.x, mePos.y, 50, 50);
	}
	
	public static void instShield(){
		//sh.display();
	}
	
	public static void main(String _args[]) {
		PApplet.main(new String[] { blockr.Blockr.class.getName() });
	}
}
