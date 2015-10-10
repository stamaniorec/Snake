import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;

public class SnakeGame extends Canvas {

	private static final long serialVersionUID = 1L;
	public static int WIDTH = 707;
	public static int HEIGHT = 707;
	
	private Thread gameThread;
	private boolean isRunning;
	
	private GameState gameState;
	
	private JFrame frame;
	private BufferedImage blackScreenRenderer;
	
	private StartScreen startScreen;
	private Background background;
	private Snake snake;
	private SnakeInputController input;
	private Fruit fruit;
	private GameOverScreen gameOverScreen;
	
	private Font medievalSharp40;
	private Font medievalSharp80;
	
	private FadeIn fadeIn;
	private FadeOut fadeOut;
	
	private int score;
	private int bestScore;
		
	/*
	 * Tuesday - started working on the game, I think I had the actual game done 
	 * Wednesday - got to a working menu, game, pause and end 
	 * Thursday - started implementing fade in, fade out -> got really pissed off
	 *    			because it didn't work, tried doing FadeInOut as one class 
	 * Friday - made the fade in, out work, code organization, commenting, etc.
	 * Saturday - snake explosion
	 * Sunday - code organization
	 */
	
	private class GameLoop implements Runnable {
		public void run() {
			Timer timer = new Timer();
			
			while(isRunning) {
				if(timer.update()) {
					update();
					render();
				}
			}
			
			stop();
		}
	}
		
	private void update() {
		if(gameState.getState() == GameStateEnum.MENU) {
			
			// Transition into the Menu State by fading out 
			if(fadeOut.isActivated()) {
				fadeOut.update();
			}
			
			// If done fading out, then we are in the Menu State 
			if(fadeOut.isDone()) {
				
				// If the Play option was selected 
				if(startScreen.isPlaySelected()) {
					// Start fading in
					fadeIn.setActivated(true);
					fadeIn.update();
					
					removeKeyListener(startScreen);
					
					// If done fading in
					if(fadeIn.isDone()) {
						// Start a fade out and change the state to Playing
						fadeIn.init();
						fadeOut.init();
						fadeOut.setActivated(true);
						changeStateToPlaying();
					}
				} else if(startScreen.isExitSelected()) {
					isRunning = false;
				}
			}
		} else if(gameState.getState() == GameStateEnum.PLAYING) {
			
			// Transition into the Playing State by fading out
			if(fadeOut.isActivated()) {
				fadeOut.update();
			}
			
			// If done fading out, then we are in the Playing State
			if(fadeOut.isDone()) {
				
				// If the game is not over
				if(!snake.isDead()) {
					if(snake.hasBitItself() || snake.isOutOfBounds()) {
						snake.setDead(true);
					} else { 
						snake.update(); 
						
						// If the fruit has been eaten
						if(fruit.isEaten(snake)) {
							// Add a new fruit, making sure the fruit 
							// does not land on the snake
							while(fruit.intersects(snake)) {
								fruit = new Fruit();
							}
							
							snake.expand();
							score += 5;
						}
					}
				} else {
					if(!snake.hasSnakeExplosionStarted()) {
						snake.initExplosions();
						snake.startExplosion();
					} else {
						snake.updateExplosions();
					}
					
					if(snake.hasExploded()) {
						// Start fading in
						fadeIn.setActivated(true);
						fadeIn.update();
						
						// If done fading in
						if(fadeIn.isDone()) {
							// Start a fade out and change the state to Game Over
							fadeIn.init();
							fadeOut.init();
							fadeOut.setActivated(true);
							
							if(score > bestScore) {
								writeHighscoreToFile(
										"../res/highscore.txt", 
										score
								);
								bestScore = score;
							}
							changeStateToGameOver();
						}	
					}	
				} 
			}
		} else if(gameState.getState() == GameStateEnum.PAUSE) {
			// Do not update anything
		} else if(gameState.getState() == GameStateEnum.GAME_OVER) {
			
			// Transition into the Game Over State by fading out
			if(fadeOut.isActivated()) {
				fadeOut.update();
			}
			
			// If done fading out, then we are in the Game Over State
			if(fadeOut.isDone()) {
				
				gameOverScreen.update();
				
				if(gameOverScreen.getOptionsPanel().isPlayAgainSelected()) {
					// Start a fade in
					fadeIn.setActivated(true);
					fadeIn.update();
					
					removeKeyListener(gameOverScreen.getOptionsPanel());
					
					// If done fading in
					if(fadeIn.isDone()) {
						// Start a fade out and change the state to Playing 
						fadeIn.init();
						fadeOut.init();
						fadeOut.setActivated(true);
						changeStateToPlaying();
					}
				} else if(gameOverScreen.getOptionsPanel().isGoToMenuSelected()) {
					// Start a fade in 
					fadeIn.update();
					fadeIn.setActivated(true);
					
					removeKeyListener(gameOverScreen.getOptionsPanel());
					
					// If done fading in
					if(fadeIn.isDone()) {
						// Start a fade out and change the state to Menu
						fadeIn.init();
						fadeOut.init();
						fadeOut.setActivated(true);
						changeStateToMenu();
					}
				}
			}
		}
	}

	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		
		if(gameState.getState() == GameStateEnum.MENU) {
			g.drawImage(blackScreenRenderer, 0, 0, null);
			
			background.render(g);
			startScreen.render(g);
			
			if(fadeIn.isActivated()) { // transition from Menu State to Playing State
				fadeIn.render(g);
			}
			
			if(fadeOut.isActivated()) { // transition from Game Over to Menu State
				fadeOut.render(g);
			}
		} else if(gameState.getState() == GameStateEnum.PLAYING) {
			g.drawImage(blackScreenRenderer, 0, 0, null);
			
			background.render(g);
			
			g.setColor(Color.RED);
			g.setFont(medievalSharp40);
			g.drawString("Score: " + Integer.toString(score), 10, 40);
			
			snake.render(g);
			fruit.render(g);
			
			if(snake.hasSnakeExplosionStarted()) {
				snake.renderExplosions(g);
			}
			
			if(fadeIn.isActivated()) { // when leaving the Playing state
				fadeIn.render(g);
			}
			if(fadeOut.isActivated()) { // when transitioning from the previous 
										// state to the Playing state
				fadeOut.render(g);
			}
		} else if(gameState.getState() == GameStateEnum.PAUSE) {
			Graphics2D g2d = (Graphics2D) g;
					
		    // Set the opacity down to 0.1f
		    g2d.setComposite(AlphaComposite.getInstance(
		            AlphaComposite.SRC_OVER, 0.1f));
			
		    // And draw the things from the Playing State
			g.drawImage(blackScreenRenderer, 0, 0, null);
			g.setColor(Color.RED);
			
			background.render(g);
			snake.render(g);
			fruit.render(g);
			
			g.setFont(medievalSharp40);
			g.drawString("Score: " + Integer.toString(score), 10, 40); 
			
			if(snake.hasSnakeExplosionStarted()) {
				snake.renderExplosions(g);
			}
			
			// Set the opacity back to normal
			g2d.setComposite(AlphaComposite.getInstance(
		            AlphaComposite.SRC_OVER, 1f));
				
			// And draw the things from the current Pause State
			g.setFont(medievalSharp80);
			g.drawString("PAUSE", 230, 200);
			g.setFont(medievalSharp40);
			g.drawString("Press ESCAPE to continue playing", 45, 250);
		} else if(gameState.getState() == GameStateEnum.GAME_OVER) {
			g.drawImage(blackScreenRenderer, 0, 0, null);
			
			background.render(g);
			
			if(!fadeOut.isDone()) { // When entering the Game Over State
				fadeOut.render(g);
			} else {
				gameOverScreen.render(g); // only render when done fading out
										  // because an animation follows
			}
			
			// It is very important to render the fadeIn  
			// ON TOP OF the gameOverScreen! 
			if(fadeIn.isActivated()) { // When leaving the Game Over State
				fadeIn.render(g);
			}
		}
		
		g.dispose();
		bs.show();
	}
	
	private void changeStateToMenu() {
		startScreen.init();
		
		addKeyListener(startScreen);
		
		gameState.setState(GameStateEnum.MENU);
	}
	
	private void changeStateToPlaying() {
		snake = new Snake();
		fruit = new Fruit();
		while(fruit.intersects(snake))
			fruit = new Fruit();
		
		score = 0;
		
		input = new SnakeInputController(snake, gameState);

		removeKeyListener(gameOverScreen.getOptionsPanel()); // if going from 
															// Game Over to Playing
		addKeyListener(input);
		
		gameState.setState(GameStateEnum.PLAYING);
	}
	
	private void changeStateToGameOver() {
		gameOverScreen.init();
		gameOverScreen.setScore(score);
		gameOverScreen.setHighscore(bestScore);
		
		removeKeyListener(input);
		addKeyListener(gameOverScreen.getOptionsPanel());
		
		gameState.setState(GameStateEnum.GAME_OVER);
	}
	
	// Initialize game
	public SnakeGame() {
		// Initialize Canvas
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
		
		// Initialize JFrame
		frame = new JFrame("Snake by Stamaniorec");
		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.add(this);
		frame.setVisible(true);
		
		// Initialize black screen renderer
		blackScreenRenderer = new BufferedImage(WIDTH, HEIGHT, 
				BufferedImage.TYPE_INT_RGB);
		Graphics g = blackScreenRenderer.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.dispose();
		
		// Initialize Game State
		gameState = new GameState(GameStateEnum.MENU);
		
		// Initialize background
		background = new Background(
				"../res/gfx/background.jpg"
		);
		
		// Load a custom font - Medieval Sharp, size 40
		try {
			medievalSharp40 = Font.createFont(
					Font.TRUETYPE_FONT, 
					new File(
							"../res/fonts/MedievalSharp.ttf")
					).deriveFont(40f);
		} catch (FontFormatException | IOException e1) {
			e1.printStackTrace();
		}
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			ge.registerFont(Font.createFont(
					Font.TRUETYPE_FONT, 
					new File(
							"../res/fonts/MedievalSharp.ttf"
					)
			));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		
		// Load a custom font - Medieval Sharp, size 80
		try {
			medievalSharp80 = Font.createFont(
					Font.TRUETYPE_FONT, 
					new File(
							"../res/fonts/MedievalSharp.ttf")
					).deriveFont(80f);
		} catch (FontFormatException | IOException e1) {
			e1.printStackTrace();
		}
		
		// Initialize score and high score
		score = 0;
		bestScore = readHighscoreFromFile(
				"../res/highscore.txt"
		);
		
		// Initialize the Start Screen
		startScreen = new StartScreen(medievalSharp40, medievalSharp80);
		addKeyListener(startScreen);
		
		// Initialize the Game Over Screen
		gameOverScreen = new GameOverScreen(score, bestScore, medievalSharp40);
		
		// Initialize the Fade In and Fade Out effects
		fadeIn = new FadeIn(0.02f);
		fadeOut = new FadeOut(0.02f);
		fadeOut.setDone(true); // so that the update and render work correctly
		
		// Initialize Game Objects
		snake = new Snake();
		fruit = new Fruit();
		while(fruit.intersects(snake)) {
			// make sure the fruit does not land on the snake
			fruit = new Fruit();
		}
		
		input = new SnakeInputController(snake, gameState);
		
		isRunning = true;
		gameThread = new Thread(new GameLoop());
		gameThread.start();
	}
	
	private int readHighscoreFromFile(String path) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(path));
			String line = reader.readLine();
			return Integer.parseInt(line);	
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return -1;
	}
	
	private void writeHighscoreToFile(String path, int highscore) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(path));
			writer.write(Integer.toString(highscore));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stop() {
		if(!isRunning) {
			frame.setVisible(false);
			System.exit(0);
		}
	}
	
	public static void main(String[] args) {
		new SnakeGame();
	}
	
}
