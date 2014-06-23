package blockr;

import processing.core.PApplet;

public class Shield extends ProcessingObject {
	float xPos;
	float yPos;
	float speedX;
	float speedY;
	float xLoc;
	
	public void setup() {
		xPos = p.width / 2;
		yPos = p.width / 2;
		speedX = 0;
	    speedY = 0;
	    xLoc = p.random(50, p.height/2);
	}
	public void draw() {
	    xPos = xPos - p.pmouseX/300 + speedX;
	    yPos = yPos - p.pmouseY/300 + speedY;
	}
	
	public void decelerate () {
		speedX = speedX * 0.9f;
	    speedY = speedY * 0.9f;
	}
	
	public void move () {
	    speedX = p.mouseX - p.pmouseX;
	    speedX = p.mouseY - p.pmouseY;
	}
	
	public void make (){
	    PApplet.println("making");
	    p.fill(100 - p.mouseX, 200 - p.mouseY, 100);
	    //rect(xPos - width/2,yPos,100,100);
	    p.rect(xPos - p.width/2 + xLoc, yPos + xLoc, xLoc/2, xLoc/2);
	}

}
