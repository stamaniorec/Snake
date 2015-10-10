import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class SnakeInputController implements KeyListener {

	private Snake snake;
	private GameState gameState;
	
	public SnakeInputController(Snake s, GameState state) {
		snake = s;
		gameState = state;
	}
	
	public void keyPressed(KeyEvent e) {
		if(gameState.getState() == GameStateEnum.PLAYING) {
			if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
				snake.setDirection(Direction.RIGHT);
			} else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				snake.setDirection(Direction.LEFT);
			} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
				snake.setDirection(Direction.DOWN);
			} else if(e.getKeyCode() == KeyEvent.VK_UP) {
				snake.setDirection(Direction.UP);
			} else if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				gameState.setState(GameStateEnum.PAUSE);
			}
		} else if(gameState.getState() == GameStateEnum.PAUSE) {
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				gameState.setState(GameStateEnum.PLAYING);
			}
		} 
	}

	public void keyReleased(KeyEvent e) { }
	public void keyTyped(KeyEvent e) { }

}
