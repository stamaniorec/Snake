import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Background {

	private BufferedImage bgImage;
	
	public Background(String path) {
		try {
			bgImage = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void render(Graphics g) {
		g.drawImage(bgImage, 0, 0, null);
	}
	
}
