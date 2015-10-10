import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;


public class GameOverText {

	private final String text = "Game Over";
	private Font font;
	
	private final int x = 250;
	private final int y = 250;
	
	private Timer timer; // limits the speed of the effect
	private int i;
	
	public GameOverText(Font font) {
		this.font = font;
		timer = new Timer(30, false);
		init();
	}
	
	public void init() {
		i = 0; 
	}
	
	public void update() {
		if(timer.update()) {
			++i;
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString(text.subSequence(0, i).toString(), x, y);
	}
	
	public boolean isDone() {
		return i >= text.length();
	}
	
}
