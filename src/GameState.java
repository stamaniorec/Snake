
public class GameState {

	/*
	 * This class was written because Java is always pass-by-value
	 * and I have felt the need to change the state of the GameStateEnum 
	 * object passed to a method but have not been able to
	 * e.g. in SnakeInputController
	 */
	
	private GameStateEnum state;
	
	public GameState(GameStateEnum state) {
		this.state = state;
	}
	
	public void setState(GameStateEnum state) { this.state = state; }
	public GameStateEnum getState() { return state; }
	
}
