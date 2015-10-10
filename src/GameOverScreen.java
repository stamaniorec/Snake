import java.awt.Font;
import java.awt.Graphics;


public class GameOverScreen {
	
	private BeatenUpSnake beatenUpSnake;	   // Snake image
	private GameOverText gameOverText;		   // "Game Over" text
	private GameOverOptionsPanel optionsPanel; // Score + High Score + Options
	
	public GameOverScreen(int currentScore, int bestScore, Font font) {
		beatenUpSnake = new BeatenUpSnake();
		gameOverText = new GameOverText(font);
		optionsPanel = new GameOverOptionsPanel(currentScore, bestScore);
		// score = 0, bestScore read from file
	}
	
	public void init() {
		beatenUpSnake.init();
		gameOverText.init();
		optionsPanel.init();
	}
	
	public void update() {
		// Animate everything in a sequence
		if(!beatenUpSnake.isDone()) {
			beatenUpSnake.update();
		} else if(!gameOverText.isDone()) {
			gameOverText.update();
		} else {
			optionsPanel.update();
		}
	}
	
	public void render(Graphics g) {
		beatenUpSnake.render(g);
		gameOverText.render(g);
		optionsPanel.render(g);
	}
	
	public void setHighscore(int highscore) {
		optionsPanel.setHighscore(highscore);
	}
	
	public void setScore(int score) {
		optionsPanel.setScore(score);
	}
	
	public GameOverOptionsPanel getOptionsPanel() {
		return optionsPanel;
	}
	
}
