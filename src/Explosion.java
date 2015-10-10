import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Explosion {

	private static BufferedImage explosionSprite;
	private static int frameSize = 128;
	private final int spriteColumns = 8;
	
	private final int numFrames = 40;
	private int curFrame;
	
	private final int x;
	private final int y;
	
	private boolean isDone; // not used but it may come in handy
	private boolean isMostlyDone;
	
	static {
		explosionSprite = null;
		
		try {
			explosionSprite = ImageIO.read(new File(
					"../res/gfx/explosion_3_40_128.png"
			));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Explosion(int x, int y) {
		this.x = x;
		this.y = y;
		
		curFrame = 0;
		
		isMostlyDone = isDone = false;
	}
	
	public void update() {
		if(curFrame >= numFrames / 2) {
			isMostlyDone = true;
		}
		if(++curFrame >= numFrames) {
			isDone = true;
		}
	}
	
	public void render(Graphics g) {
		g.drawImage(
				explosionSprite.getSubimage(
						(curFrame % spriteColumns) * frameSize, 
						(curFrame / spriteColumns) * frameSize, 
						frameSize, 
						frameSize
				), 
				x, 
				y, 
				null
		);
	}
	
	public boolean isDone() { return isDone; }
	public boolean isMostlyDone() { return isMostlyDone; }
	
}
