import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class BeatenUpSnake {

	private BufferedImage image;
	private int x;
	private int y;
	private int speed;
	
	public BeatenUpSnake() {
		try {
			image = ImageIO.read(new File(
					"../res/gfx/beaten_up_snake.png"
			));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		speed = 3;
		init();
	}
	
	public void init() {
		x = 20;
		y = 100;
	}

	// Update: move the snake up
	public void update() {
		y -= speed;
	}

	public void render(Graphics g) {
		g.drawImage(image, x, y, null);
	}
	
	public boolean isDone() {
		return y <= -75;
	}
	
}
