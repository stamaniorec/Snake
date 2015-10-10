import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;


public class TailComponent extends SnakeComponent {

	private static BufferedImage image;
			
	static { 
		try {
			image = ImageIO.read(new File(
					"../res/gfx/red-tail.png"
			));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public TailComponent(int x, int y) {
		super(x, y);
	}

	public void render(Graphics g) {
		g.drawImage(image, x, y, null);
	}
	
	
	
}
