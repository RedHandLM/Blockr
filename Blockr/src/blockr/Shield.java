package blockr;

import processing.core.PApplet;
import processing.core.PVector;

public class Shield extends ProcessingObject {
	
	public PVector shieldPos;
	public float shieldW, shieldH;
	public int shieldCol;
	
	public Shield(PVector pos, float w, float h, int color) {
		super();
		this.shieldPos = pos;
		this.shieldW = w;
		this.shieldH = h;
		this.shieldCol = color;
	}
	
	public void display() {
		PApplet.println("making");
		p.fill(shieldCol);
		p.rect(shieldPos.x, shieldPos.y, shieldW, shieldH);
	}
}
