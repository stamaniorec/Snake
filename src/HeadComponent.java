import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;


public class HeadComponent extends SnakeComponent {

	private static BufferedImage image;
	int degrees = 0; // The head is rotated according to 
					 // the direction of the snake 
	
	
	// A new HeadComponent is often constructed 
	// And loading the same image over and over again takes time 
	// Thus it has been made static 
	static { 
		try {
			image = ImageIO.read(new File(
					"../res/gfx/red-head.png"
			));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public HeadComponent(int x, int y, Direction d) {
		super(x, y);
		if(d == Direction.DOWN) {
			degrees = 180;
		} else if(d == Direction.UP) {
			degrees = 0;
		} else if(d == Direction.LEFT) {
			degrees = 270;
		} else if(d == Direction.RIGHT) {
			degrees = 90;
		}
	}
	
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		// Specify rotation center and rotation angle
		AffineTransform tx = AffineTransform.getRotateInstance(
				Math.toRadians(degrees), 
				image.getWidth() / 2, 
				image.getHeight() / 2);
		
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		
		// Draw a rotated Head
		g2d.drawImage(op.filter(image, null), x, y, null);
	}
	
	public int getX() { return x; }
	public int getY() { return y; }
	
}
