import java.awt.Graphics;

public abstract class SnakeComponent {

	/*
	 * Base class for HeadComponent and TailComponent
	 */
	
	protected int x;
	protected int y;
	public static int imageSize = 25;
	
	protected Explosion explosion;
	private boolean hasExplosionStarted;
	
	public SnakeComponent(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public abstract void render(Graphics g); // HeadComponent and TailComponent
										     // render differently
	
	public void initExplosion() {
		explosion = new Explosion(
				x - imageSize/2 - 40, 
				y - imageSize/2 - 40
		);
	}
	
	public void startExplosion() { hasExplosionStarted = true; }
	public boolean hasExplosionStarted() { return hasExplosionStarted; }
	
	public boolean hasMostlyExploded() {
		if(explosion != null)
			return explosion.isMostlyDone();
		else
			return false;
	}
	
	public void updateExplosion() {
		explosion.update();
	}
	
	public void renderExplosion(Graphics g) {
		explosion.render(g);
	}
	
	/**
	 * @param component - a SnakeComponent
	 * @return whether two SnakeComponents intersect
	 * The method utilizes standard rectangle collision detection.
	 * Important note: 
	 * Make sure the signs don't include = 
 	 * because the SnakeComponents are right next to each other
	 * Used to check if the snake has bit itself
	 */
	public boolean intersects(SnakeComponent component) {
		if(component.getX() + imageSize > x && 
			component.getX() < x + imageSize && 
			component.getY() + imageSize > y && 
			component.getY() < y + imageSize) {
			return true;
		} else {
			return false;
		}
	}
	
	public int getX() { return x; }
	public int getY() { return y; }
		
}
