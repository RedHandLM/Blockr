package blockr;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.video.Capture;
import themidibus.MidiBus;


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
	static float menowX = 0.0f;
	static float menowY = 0.0f;
	
	// ***Background
	//----------------------------------
	PImage bg;
	
	// ***Shield Variables
	//----------------------------------
	float sRed;
	float sGreen;
	float sBlue;
	float sWeight;
	float sAlpha;
	
	// ***Me Variables
	//----------------------------------
	PVector mePos;
	PVector meDir;
	float meAccel = 0.5f;
	float meSpeed = 0.2f;
	float meStartX, meStartY;
	
	// ***MIDI Variables
	//----------------------------------
	MidiBus nanoKontrol;
	

	public void setup() {
		// ***World Setup
		//----------------------------------
		size (1280, 720);
		noStroke();
		ellipseMode(CENTER);
		
		MidiBus.list();
		
		nanoKontrol = new MidiBus(this, 0, 3);
		  
		bg = loadImage ("background.png");
		
		
		// ***Camera Setup
		//----------------------------------
		String[] camNames = cam.list();
		
		meR = 255;
		meG = 0;
		meB = 0;
		
		cam = new Capture(this, 1280, 720);
		
		cam.start();
		
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
		
		
		// ***Me
		//----------------------------------
		meDir = new PVector(menowX, menowY);
		meDir.sub(mePos);
		//meDir.normalize();
		PVector move = PVector.mult(meDir, meSpeed);
		mePos.add(move);
		
		// ***Camera
		//----------------------------------
		
		if(cam.available()){
			cam.read();
			//image(cam, 0, 0);
			
			fill(255);
			float meY = meStartY;
			float meX = meStartX;
			
			
			int[] pixs = cam.pixels;
			
			float closestToColor = 50;
			
			for(int i = 0; i < pixs.length; i++){
				int color = pixs[i];
				
				if(dist(meR, meG, meB, red(color), green(color), blue(color)) < closestToColor){
					
					closestToColor = dist(meR, meG, meB, red(color), green(color), blue(color));
					
					meY = i/cam.width;
					meX = i%cam.width;
					
					menowX = meX;
					menowY = meY;
				}
			}
		}
		fill(255);
		noStroke();
		ellipse(mePos.x, mePos.y, 50, 50);
		noFill();
		strokeWeight(sWeight);
		stroke(sRed, sGreen, sBlue, sAlpha);

		ellipse(mePos.x, mePos.y, 200, 200);
	}
	
	//------------------THIS IS THE FUNCTION GETTING CHANGES IN VALUES FROM THE MIDI CONTROLLER
	public void controllerChange(int channel, int number, int value){
		println("+------+");
		println("Channel: "+channel);
		println("Number: "+number);
		println("Value: "+value);
		
		if(number==33){//first fader
			sRed = map(value, 0, 127, 0, 255);
		}else if(number == 34){//second fader
			sGreen = map(value, 0, 127, 0, 255);
		}else if(number == 35){//third fader
			sBlue = map(value, 0, 127, 0, 255);
		}else if(number==17){//first knob
			sWeight = map(value, 127, 0, 1, 50); 
		}else if(number==18){//second knob
			sAlpha = map(value, 127, 0, 0, 255);
		}
		
	}
	
	public static void main(String _args[]) {
		PApplet.main(new String[] { blockr.Blockr.class.getName() });
	}
}
