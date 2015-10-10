import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class FadeOut {
	private BufferedImage fader;
	private float opacity;
	private float fadingSpeed;
	private boolean isDone;
	private boolean activated;
	
	public FadeOut(float fadingSpeed) {
		fader = new BufferedImage(SnakeGame.WIDTH, SnakeGame.HEIGHT, 
				BufferedImage.TYPE_INT_RGB);
		Graphics g = fader.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, SnakeGame.WIDTH, SnakeGame.HEIGHT);
		g.dispose();
		
		this.fadingSpeed = fadingSpeed;
		opacity = 1f;
		isDone = activated = false;
	}
	
	public void init() {
		opacity = 1f;
		isDone = activated = false;
	}
	
	public void update() {
		opacity -= fadingSpeed;
		if(opacity <= 0) {
			opacity = 0;
			isDone = true;
		}		
	}
	
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
	    g2d.setComposite(AlphaComposite.getInstance(
	            AlphaComposite.SRC_OVER, opacity));
	    		    
	    g.drawImage(fader, 0, 0, null);
	}
	
	public boolean isDone() { return isDone; }
	public boolean hasStarted() { return opacity < 1f; }
	public void setDone(boolean b) { isDone = b; }
	public void setActivated(boolean b) { activated = b; }
	public boolean isActivated() { return activated; }
	
}
