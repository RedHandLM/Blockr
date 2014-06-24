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
	
	MidiBus nanoKontrol;
	
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
	
	// ***Shield Setup
	//----------------------------------
	Shield sh = new Shield();
	float aRed1 = 255;
	float aGreen1 = 255;
	float aBlue1 = 255;
	float aWeight1 = 2;
	float aAlpha1 = 100;
	float aDist1 = 0;
	
	float aRed2 = 255;
	float aGreen2 = 255;
	float aBlue2 = 255;
	float aWeight2 = 2;
	float aAlpha2 = 100;
	float aDist2 = 0;
	
	float aRed3 = 255;
	float aGreen3 = 255;
	float aBlue3 = 255;
	float aWeight3 = 2;
	float aAlpha3 = 100;
	float aDist3 = 0;
	
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
		image (bg, centerX + menowX/-1, centerY + menowY/-1);
		  
		// ***Shield
		//----------------------------------
		
		//if (mousePressed) sh.move();
		  
		//sh.decelerate();
		//sh.make();
		
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
			
			float closestToColor = 100;
			
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
		strokeWeight(21-((aDist1/PI)*20));
		stroke(aRed1, aGreen1, aBlue1, aAlpha1);
		arc(mePos.x, mePos.y, 200.0f, 200.0f, PI-aDist1, PI+aDist1);
		/*
		strokeWeight(aWeight2);
		stroke(aRed2, aGreen2, aBlue2, aAlpha2);
		arc(mePos.x, mePos.y, 200.0f-aDist2, 200.0f-aDist2, (2*PI)/3, (4*PI)/3);
		strokeWeight(aWeight3);
		stroke(aRed3, aGreen3, aBlue3, aAlpha3);
		arc(mePos.x, mePos.y, 200.0f-aDist3, 200.0f-aDist3, (4*PI)/3, 2*PI);
		*/
	}
	
	//------------------THIS IS THE FUNCTION GETTING CHANGES IN VALUES FROM THE MIDI CONTROLLER
	public void controllerChange(int channel, int number, int value){
		println("+------+");
		println("Channel: "+channel);
		println("Number: "+number);
		println("Value: "+value);
		
		//-------------------FIRST ARC
		if(number==33){//first fader
			aRed1 = map(value, 0, 127, 0, 255);
		}else if(number == 34){//second fader
			aGreen1 = map(value, 0, 127, 0, 255);
		}else if(number == 35){//third fader
			aBlue1 = map(value, 0, 127, 0, 255);
		}else if(number==17){//first knob
			aWeight1 = map(value, 127, 0, 1, 30); 
		}else if(number==18){//second knob
			aAlpha1 = map(value, 127, 0, 100, 255);
		}else if(number == 19){
			aDist1 = map(value, 127f, 0f, PI, 0f);
		}
		
		//-------------------SECOND ARC
		if(number==36){//first fader
			aRed2 = map(value, 0, 127, 0, 255);
		}else if(number == 37){//second fader
			aGreen2 = map(value, 0, 127, 0, 255);
		}else if(number == 38){//third fader
			aBlue2 = map(value, 0, 127, 0, 255);
		}else if(number==20){//first knob
			aWeight2 = map(value, 127, 0, 1, 30); 
		}else if(number==21){//second knob
			aAlpha2 = map(value, 127, 0, 100, 255);
		}else if(number == 22){
			aDist2 = map(value, 127, 0, 0, 50);
		}
		
		//-------------------THIRD ARC
		if(number==39){//first fader
			aRed3 = map(value, 0, 127, 0, 255);
		}else if(number == 40){//second fader
			aGreen3 = map(value, 0, 127, 0, 255);
		}else if(number == 41){//third fader
			aBlue3 = map(value, 0, 127, 0, 255);
		}else if(number==23){//first knob
			aWeight3 = map(value, 127, 0, 1, 30); 
		}else if(number==24){//second knob
			aAlpha3 = map(value, 127, 0, 100, 255);
		}else if(number == 25){
			aDist3 = map(value, 127, 0, 0, 50);
		}
		
	}
	
	public static void main(String _args[]) {
		PApplet.main(new String[] { blockr.Blockr.class.getName() });
	}
}
