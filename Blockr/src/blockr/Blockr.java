package blockr;

import java.io.IOException;
import java.util.ArrayList;

import ddf.minim.AudioPlayer;
import ddf.minim.AudioSample;
import ddf.minim.Minim;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.SamplePlayer;
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
	
	float continuedX = 0.1f;
	float continuedY = 0.1f;
	
	// ***Camera variables
	//----------------------------------
	Capture cam;
	float meR,meG,meB;
	static float menowX = 0.0f;
	static float menowY = 0.0f;
	
	// ***Background
	//----------------------------------
	PImage bg;
	ArrayList<PVector> stars = new ArrayList<PVector>();
	
	int randStar;
	float bri = 100.0f;
	float bridir = random(1,3);
	
	
	// ***Shield Variables
	//----------------------------------

	float aRed1 = 255;
	float aGreen1 = 255;
	float aBlue1 = 255;
	float aWeight1 = 2;
	float aAlpha1 = 100;
	float aDist1 = 0;
	float aRotSpeed1;
	
	int angleOfShape;
	
	//Collision Stuff
	float shieldRad = 200.0f;
	int colCheck;
	float bulletAngle;
	float shieldAngle;
	boolean entered = false;
	
	int errorMargin = 50;
	
	// ***Me Variables
	//----------------------------------
	PVector mePos;
	PVector refPos;
	PVector meDir;
	float meAccel = 0.5f;
	float meSpeed = 0.2f;
	float meStartX, meStartY;
	
	PVector meCol;
	int meRad;
	int meC;
	
	float health;
	float healthBar;
	
	// ***Juice Variables
	//----------------------------------
		//Shield Juice
		float [] jX = new float[100];
		float [] jY = new float[100];
		float [] jA = new float[100];
		float [] jB = new float[100];
		float [] jDOTx = new float[500];
		float [] jDOTy = new float [500];
		
		float jRad = 3;
		
		float jRand = random (-250,250);
		float jRand2 = random (-250,250);
		float jRand3 = random (-250,250);
		
		float jCenterX = width/2;
		float jCenterY = height/2;
		
		float jRed = random (100,200);
		float jGreen = random (100, 200);
		float jBlue= random (100, 200);
	
	// ***MIDI Variables
	//----------------------------------
	MidiBus nanoKontrol;
	
	// ***Bullet Variables
	//----------------------------------
	Bullet bull = new Bullet(400,200,50,255,0,0);
	public boolean colliding = false;
	
	//DEBUG
	PVector testPos;
	int move = 1;
	
	// ***SOUND VARIABLES
	//---------------------------------
	/*
	AudioContext ac;
	
	SamplePlayer engine;
	SamplePlayer shield;
	SamplePlayer shield_invert;
	
	Gain gEngine;
	Gain gShield;
	Gain gShieldInv;
	
	String sourceEngine;
	*/
	
	Minim minim;
	AudioSample sfx_shield;
	AudioSample sfx_shieldInv;
	AudioSample sfx_absorb;
	AudioSample sfx_get;
	AudioSample sfx_hit_lo;
	AudioSample sfx_hit_mid;
	AudioSample sfx_hit_tre;
	
	AudioPlayer sfx_engine;
	
	boolean isPlaying_Absorb = false;
	boolean isPlaying_Hit = false;

	public void setup() {
		
		
		// ***World Setup
		//----------------------------------
		size (1280, 720);
		noStroke();
		ellipseMode(CENTER);
		
		MidiBus.list();
		
		nanoKontrol = new MidiBus(this, 3, 6);
		
		
		// ***BG Setup
		//----------------------------------
		for (int s = 0; s < 10000; s++) {
			pushMatrix();
			translate(-width*5, -height*5);
			PVector sP = new PVector(random(10*width), random(10*height));
			stars.add(sP);
			popMatrix();
		}
		randStar = (int) random(-100,100);
		
		// ***AudioContext
		//----------------------------------
		/*
		ac = new AudioContext();
		
		sourceEngine = sketchPath("")+"data/Engine_1.wav";
		
		try {
			engine = new SamplePlayer(ac, new Sample(sketchPath("")+"data/Engine_1.wav"));
			shield = new SamplePlayer(ac, new Sample(sketchPath("")+"data/Shield_Pad.wav"));
			shield_invert = new SamplePlayer(ac, new Sample(sketchPath("")+"data/Shield_Pad_Inverted.wav"));
		} catch (IOException e) {
			println("COULDN'T LOAD AUDIO FILE");
			e.printStackTrace();
			exit();
		}
		*/
		
		minim = new Minim(this);
		
		sfx_engine = minim.loadFile("data/Engine_1.wav");
		sfx_engine.setGain(-5f);
		sfx_engine.loop();
		
		sfx_absorb = minim.loadSample("data/Absorb_1.wav"); //Absorb is when the shield deflects the missiles
		
		sfx_get = minim.loadSample("data/Get_1.wav"); //Get is when the ship gets bonuses
		
		sfx_hit_lo = minim.loadSample("data/Hit_1_Lo.wav"); //Hit is when the ship is hit
		sfx_hit_lo.setGain(-2f);
		sfx_hit_mid = minim.loadSample("data/Hit_1_Mid.wav");
		sfx_hit_mid.setGain(-2f);
		sfx_hit_tre = minim.loadSample("data/Hit_1_Tre.wav");
		sfx_hit_tre.setGain(-2f);
		
		sfx_shield = minim.loadSample("data/Shield_Pad.wav"); //Shield is when the shield is increased/decreased
		sfx_shieldInv = minim.loadSample("data/Shield_Pad_Inverted.wav");
		
		
		
		
		
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
		
		meRad = 50;
		
		health = 100.0f;
		healthBar = 100.0f;
		
		// ***Bullet Setup
		//----------------------------------
				
		// ***Juice Setup
		//----------------------------------
		for (int jCount = 0; jCount < 500; jCount++){
			jDOTx[jCount] = random (500);
			jDOTy[jCount] = random(50);
		}
				
		for (int jI = 0; jI < 100; jI++){
			jX[jI] = 250;
			jY[jI] = 250;
			jA[jI] = jRand2;
			jB[jI] = jRand3;
		}
		
		  
	}

	public void draw() {
		// ***BG
		//----------------------------------
		background(0);
		noStroke();
		fill(255);
		
		for (int i = 0; i < stars.size(); i++){
			PVector sP = stars.get(i);
			PVector sM = new PVector((mePos.x-meDir.x)/800, (mePos.y-meDir.y)/800);
			
			if (mePos.x > width/2){
				sP.x -= sM.x;
			} else if (mePos.x < width/2){
				sP.x += sM.x;
			}
			
			if (mePos.y < height/2){
				sP.y += sM.y;
			}else if (mePos.y > height/2){
				sP.y -= sM.y;
			}
			
			float d = dist(sP.x, sP.y, width/2, height/2);
			d = map(d, 0, width/2, 0, 3);
			ellipse(sP.x, sP.y, d, d);
			//ellipse(sP.x+randStar, sP.y+randStar, d+2, d+2);
		}
		if (bri < 0){
			bri = 0;
		} else if (bri > 255) {
			bri = 255;
			bridir = random(-1, -3);
		}
		
		// ***DEBUG
		//----------------------------------
		pushMatrix();
		translate(mePos.x, mePos.y);
			testPos = new PVector(bull.bX-mePos.x, bull.bY-mePos.y);
			stroke(0, 0, 255, 255);
			strokeWeight(100);
			point(testPos.x, testPos.y);
		popMatrix();
		
		
		
		// ***Me
		//----------------------------------
		meDir = new PVector(menowX, menowY);
		meDir.sub(mePos);
		//meDir.normalize();
		PVector move = PVector.mult(meDir, meSpeed);
		mePos.add(move);
		
		
		
		// ***Enemy
		//----------------------------------
		//CHECK COLLISION
		if (dist(mePos.x, mePos.y, bull.bX, bull.bY) < bull.bR/2 + meRad){
			colliding = true;
			if (entered){
				PApplet.println("HURT");
				health -= 1.0f;
				healthBar -= 1.0f;
				if(!isPlaying_Hit){
					int i = (int)random(3);
					switch(i) {
					case 0:
						sfx_hit_lo.trigger();
						break;
					case 1:
						sfx_hit_mid.trigger();
						break;
					case 2:
						sfx_hit_tre.trigger();
						break;
					}
					isPlaying_Hit = true;
					
				}
			}
			meC = 0;
		} else {
			colliding = false;
			isPlaying_Hit = false;
			sfx_hit_lo.stop();
			sfx_hit_mid.stop();
			sfx_hit_tre.stop();
			meC = 255;
		}
		
		//MAKE BULLET
		bull.render();
		//MOVE BULLET
		bull.bX +=0.01f;
		
		
		
		// ***Shield
		//----------------------------------
		//CHECK SHIELD COLLISION
		fill (255,0,0);
		rect(mePos.x+100, mePos.y+10, healthBar, 6);
		
		fill(colCheck);
		ellipse(mePos.x, mePos.y, shieldRad, shieldRad);
		
		if (dist(mePos.x, mePos.y, bull.bX, bull.bY) < bull.bR + shieldRad/2){
			
			//IF ENEMY IS HITTING THE SHIELD OR NOT
			if (bulletAngle >= shieldAngle){
				colCheck = 255;
				
				//IF SHIELD COLOUR MATCHES ENEMY COLOUR
				if ((bull.bRed - errorMargin) < aRed1 || aRed1 > (bull.bRed + errorMargin) 
					&& (bull.bGreen - errorMargin) < aGreen1 || aGreen1 > (bull.bGreen + errorMargin) 
					&& (bull.bBlue - errorMargin) < aBlue1 || aBlue1 > (bull.bBlue + errorMargin)){
				PApplet.println("BLOCKED");
				sfx_absorb.trigger();
				entered = false;
			} else {
				PApplet.println("NOT BLOCKED");
				entered = true;
			}
				
		} else {
			//PApplet.println("ENTERED SHIELD");
			colliding = true;
			entered = true;
			colCheck = 50;
			}
		} else {
			colCheck = 10;
		}	
		
		// ***Camera
		//----------------------------------
		
		if(cam.available()){
			cam.read();
			//image(cam, 0, 0);
			
			fill(255);
			float meY = meStartY;
			float meX = meStartX;
			
			
			int[] pixs = cam.pixels;
			
			float closestToColor = 98;
			
			for(int i = 0; i < pixs.length; i++){
				int color = pixs[i];
				
				if(dist(meR, meG, meB, red(color), green(color), blue(color)) < closestToColor){
					
					closestToColor = dist(meR, meG, meB, red(color), green(color), blue(color));
					
					meY = i/cam.width;
					meX = i%cam.width;
					
					meStartX = meX;
					meStartY = meY;
					
					menowX = meStartX;
					menowY = meStartY;
					
					
				}else{
					//engineMinim.stop();
				
				}
			}
		}
		
		
		//Checking when the player is moving
		PVector isMoving = PVector.sub(mePos, meDir);
		println(move);
		if(abs(move.x) > 2.3f && abs(move.x) < 2.5f && abs(move.y) > 1.1f && abs(move.y) < 1.3f){
			sfx_engine.mute();
		}else {
			sfx_engine.unmute();			
		}
		
		
		// MAKE MEEEE
		fill(meC);
		noStroke();
		ellipse(mePos.x, mePos.y, 50, 50);
		
		
		
		// MAKE SHIELD
		noFill();
		
		//aDist1 = PI/2;
		
		pushMatrix();
		translate(mePos.x, mePos.y);
			rotate(PApplet.radians(angleOfShape));
			
			strokeWeight(21-((aDist1/PI)*20));
			stroke(aRed1, aGreen1, aBlue1, aAlpha1);
			arc(0, 0, 200.0f, 200.0f, PI-aDist1, PI+aDist1);
			
			popMatrix();
			pushMatrix();
			translate(mePos.x, mePos.y);
				float angleIncoming = PVector.angleBetween(new PVector(testPos.x, testPos.y), new PVector(50, 0));
			popMatrix();
		
		//ANGLE TEST
//		stroke(255, 0, 0, 255);
//		strokeWeight(2);
		
//		line(mePos.x, mePos.y, testPos.x+mePos.x, testPos.y+mePos.y); //line from the origin to the bullet
//		stroke(0, 255, 0, 255);
//		line(mePos.x, mePos.y, mePos.x+50, mePos.y+0); //line from the origin to the reference
		
		bulletAngle = (degrees(angleIncoming));
		shieldAngle = degrees(PI-aDist1);
		
//		PApplet.println("bulletAngle = " +bulletAngle);
//		PApplet.println("shieldAngle = " +shieldAngle);
		
		
		//PUT ROTATION INFO HERE
		//angleOfShape +=aRotSpeed1;
		
		/*
		strokeWeight(aWeight2);
		stroke(aRed2, aGreen2, aBlue2, aAlpha2);
		arc(mePos.x, mePos.y, 200.0f-aDist2, 200.0f-aDist2, (2*PI)/3, (4*PI)/3);
		strokeWeight(aWeight3);
		stroke(aRed3, aGreen3, aBlue3, aAlpha3);
		arc(mePos.x, mePos.y, 200.0f-aDist3, 200.0f-aDist3, (4*PI)/3, 2*PI);
		*/
		
		
		
		
		
		
		// ***Juice
		//-------------------------
						
		fill (jRed, jGreen, jBlue);
		noStroke();
						
						
		if (colliding == true) {
							
			for (int i = 0; i < 100; i++) {
				jX[i]= jX[i] + ((mePos.x-jX[i]) / (5.0f + (i*5.0f) ));
				jY[i]= jY[i] + ((mePos.y-jY[i]) / (5.0f + (i*5.0f) ));
				}
						 
			int newDot = 0;
			int arms=7;
			jCenterX = jCenterX + ((mePos.x-jCenterX)/100.0f);
			jCenterY = jCenterY+((mePos.y-jCenterY)/100.0f);
			
			while (newDot<arms) {
				pushMatrix();
				translate(mePos.x, mePos.y);
				rotate((TWO_PI/arms)*newDot);
			  	translate(-jCenterX, -jCenterY);
						 
			  	for (int i = 0; i < 100; i++) {
		    		ellipse (jX[i], jY[i], jRad, jRad);
		    		//ellipse (mouseX-a[i], mouseY-b[i], 2,2);
			  	}
			  	popMatrix();
						 
			  	newDot++;
			}
						 
			//ellipse(jCenterX, jCenterY, jRad, jRad);
		}
						   
				else {
						 
				   float r = 5;
				   float t = 5;
				   for (int i = 0; i < 100; i++) {
					   jX[i]= jX[i] + ((mePos.x-jX[i]) / (5.0f + (i*5.0f) ));
					   jX[i]= jY[i] + ((mePos.y-jY[i]) / (5.0f + (i*5.0f) ));
				   }
						 
				   int newDot = 0;
				   int arms=7;
				   //jCenterX=jCenterX+((mePos.x-centerX)/50.0f);
				   //jCenterY=jCenterY+((mePos.y-centerY)/50.0f);
						 
				   while (newDot<arms) {
					   pushMatrix();
					   translate(mePos.x, mePos.y);
					   rotate((TWO_PI/arms)*newDot);
					   translate(-jCenterX, -jCenterY);
						 
					   for (int i = 0; i < 100; i++) {
						   fill(jRed, jGreen, jBlue, t);
						   ellipse (jX[i], jY[i], jRad, jRad);
					   }
						 
					   popMatrix();
						 
					   newDot++;
				   }
						    
				   fill(jRed, jGreen, jBlue, t);
				   ellipse(mePos.x, mePos.y, r, r);
				   
				}
	}
	
	//------------------THIS IS THE FUNCTION GETTING CHANGES IN VALUES FROM THE MIDI CONTROLLER
	public void controllerChange(int channel, int number, int value){
		println("+------+");
		println("Channel: "+channel);
		println("Number: "+number);
		println("Value: "+value);
		
		//-------------------FIRST ARC
		if(number==33){//first fader - RED
			aRed1 = map(value, 0, 127, 0, 255);
		}else if(number == 34){//second fader - GREEN
			aGreen1 = map(value, 0, 127, 0, 255);
		}else if(number == 35){//third fader - BLUE
			aBlue1 = map(value, 0, 127, 0, 255);
		}else if(number==17){//first knob
			aRotSpeed1 = map(value, 127, 0, -9.0f, 9.0f); 
		}else if(number==18){//second knob
			aAlpha1 = map(value, 127, 0, 100, 255);
		}
		if(number == 19){// third knob - SHIELD
			aDist1 = map(value, 127f, 0f, PI, 0f);
			if(!isPlaying_Absorb){
				sfx_shield.trigger();
				isPlaying_Absorb = true;
			}
		}else{
			sfx_shield.stop();
			isPlaying_Absorb = false;
		}
		/*
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
		*/
	}
	
	

	public void mousePressed (){
		PApplet.println("Mouse Pressed");
		jCenterX=mePos.x;
		jCenterY=mePos.y;
	}
	
	
	
//CLASSES
	
	//Bullet Class
	class Bullet {
		float bX;
		float bY;
		// the Bullet Radius
		float bR;
		int bRed;
		int bGreen;
		int bBlue;
		
		Bullet(float xpos, float ypos, float radius, int r, int g, int b){
			bX = xpos;
			bY = ypos;
			bR = radius;
			bRed = r;
			bGreen = g;
			bBlue = b;
		}
		
		void render() {
			fill(bRed, bGreen, bBlue);
			noStroke();
			ellipse (bX, bY, bR*2, bR*2);
		}
	}
	
	public static void main(String _args[]) {
		PApplet.main(new String[] { blockr.Blockr.class.getName() });
	}
}