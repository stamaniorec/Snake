import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class GameOverOptionsPanel implements KeyListener {

	private int currentScore;
	private int bestScore;
	
	// The Panel utilizes a fade-in effect
	private float opacity;
	
	private boolean selectedOption; // 0 for Play Again, 1 for Menu, 
									// not going to add more options 
									// so this lazy approach is perfect
	
	private boolean hasEnterBeenPressed; // selectedOption is 0 by default 
										 // but this does not mean the user 
										 // actually wants to select it 
		
	public GameOverOptionsPanel(int currentScore, int bestScore) {
		this.currentScore = currentScore;
		this.bestScore = bestScore;
		
		init();
	}
	
	public void init() {
		opacity = 0.0f;
		selectedOption = false;
		hasEnterBeenPressed = false;
	}
	
	// Update the panel by increasing the opacity, simulating a fade-in
	public void update() {
		opacity += 0.05f;
	}
	
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
	
	    g2d.setComposite(AlphaComposite.getInstance(
	            AlphaComposite.SRC_OVER, opacity >= 1.0f ? 1.0f : opacity));
	    
	    // Render the current score in the middle of the screen
	    int curStringWidth = g.getFontMetrics().stringWidth(
	    		"Current Score: " + currentScore
	    );
		g.drawString(
				"Current Score: " + currentScore,
				SnakeGame.WIDTH / 2 - curStringWidth / 2, 
				330 
		);
		
		// Render the best score in the middle of the screen
		curStringWidth = g.getFontMetrics().stringWidth("Best Score: " + bestScore);
		g.drawString(
				"Best Score: " + bestScore,
				SnakeGame.WIDTH / 2 - curStringWidth / 2, 
				370
		);
	    
	    if(selectedOption == false) {
	    	g.setColor(Color.RED);
	    } else {
	    	g.setColor(Color.WHITE);
	    }
	    g.drawString("Play Again", 250, 500);
	    
	    if(selectedOption == false) {
	    	g.setColor(Color.WHITE);
	    } else {
	    	g.setColor(Color.RED);
	    }
	    
	    g.drawString("Go to Menu", 245, 575);
	}
	
	// Not used, but who knows, may come in handy
	public boolean isDone() {
		return opacity >= 1.0f;
	}
	
	public boolean isPlayAgainSelected() {
		if(hasEnterBeenPressed) {
			return selectedOption == false;
		} else {
			return false;
		}
	}
	
	public boolean isGoToMenuSelected() {
		if(hasEnterBeenPressed) {
			return selectedOption == true;
		} else {
			return false;
		}
	}

	public void setHighscore(int highscore) {
		bestScore = highscore;
	}
	
	public void setScore(int score) {
		currentScore = score;
	}
	
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_DOWN || 
				e.getKeyCode() == KeyEvent.VK_UP) {

			selectedOption = !selectedOption; // select the other option
		} else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			hasEnterBeenPressed = true;
		}
	}

	public void keyReleased(KeyEvent e) { }
	public void keyTyped(KeyEvent e) { }
	
}
