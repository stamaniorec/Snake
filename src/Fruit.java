import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Fruit {

	private BufferedImage image;
	private static final int imageSize = 34;
	private int x;
	private int y;
	
	public Fruit() {
		int fruitNumber = (int)(Math.random() * 8);
		x = (int)(Math.random() * (SnakeGame.WIDTH-imageSize-40));
		y = (int)(Math.random() * (SnakeGame.HEIGHT-imageSize-40));
		
		try {
			image = ImageIO.read(new File(
					"../res/gfx/fruits/fruit" + fruitNumber + ".png"
			));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void render(Graphics g) {
		g.drawImage(image, x, y, null);
	}
	
	public boolean isEaten(Snake s) {
		return snakeComponentIntersectsFruit(s.getHead());
	}
	
	/**
	 * @param component - SnakeComponent
	 * @return whether the given SnakeComponent collides with the fruit
	 * The method utilizes standard rectangle collision detection.
	 */
	private boolean snakeComponentIntersectsFruit(SnakeComponent component) {
		if(component.getX() + SnakeComponent.imageSize >= x && 
			component.getX() <= x + imageSize && 
			component.getY() + SnakeComponent.imageSize >= y && 
			component.getY() <= y + imageSize) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * @return whether the fruit is on top of the snake
	 * Used outside the class to position the fruit properly
	 */
	public boolean intersects(Snake s) {
		for(SnakeComponent component : s.getBody()) {
			if(snakeComponentIntersectsFruit(component)) {
				return true;
			}
		}
		return false;
	}
	
}
