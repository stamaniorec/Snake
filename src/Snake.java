import java.awt.Graphics;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

public class Snake {

	private LinkedList<SnakeComponent> snake;
	
	private Direction currentDirection; // direction set by SnakeInputController 
	private Direction snakeDirection; // The actual direction of the snake
	
	private Timer snakeUpdateTimer; // slows down the movement of the snake
	
	private boolean hasSnakeExplosionStarted;
	
	private boolean isDead;
	
	private Timer timer2;
	
	public Snake() {
		// Create the snake
		snake = new LinkedList<SnakeComponent>();
		
		// Add four TailComponents
		for(int i = 1; i < 5; ++i) {
			snake.add(new TailComponent(i * SnakeComponent.imageSize, 50));
		}
		
		// And a fifth HeadComponent
		snake.add(new HeadComponent(5 * SnakeComponent.imageSize, 50, Direction.RIGHT));
		
		currentDirection = Direction.RIGHT;
		snakeDirection = Direction.RIGHT;
		
		snakeUpdateTimer = new Timer(10, false);
		
		hasSnakeExplosionStarted = isDead = false;
		
		timer2 = new Timer(15, false);
	}
	
	// The head of the snake is the LAST element of the snake list
	
	/**
	 * Method to handle the movement of the snake
	 */
	public void update() {
		if(snakeUpdateTimer.update()) {
			preventMovementInOppositeDirection();
			
			snake.removeFirst(); // "last" element of the snake
			convertHeadToTailComponent();
			addNewHead();
		}
	}
	
	/**
	 * Method to prevent the snake from going in the opposite direction 
	 * e.g. trying to go left when the snake is moving right 
	 * -> invalid, ignore request
	 */
	private void preventMovementInOppositeDirection() {
		if(currentDirection == Direction.RIGHT && 
				snakeDirection == Direction.LEFT) {
			
			currentDirection = Direction.LEFT;
		} else if(currentDirection == Direction.LEFT && 
				snakeDirection == Direction.RIGHT) {
			
			currentDirection = Direction.RIGHT;
		} else if(currentDirection == Direction.DOWN && 
				snakeDirection == Direction.UP) {
			
			currentDirection = Direction.UP;
		} else if(currentDirection == Direction.UP && 
				snakeDirection == Direction.DOWN) {
			
			currentDirection = Direction.DOWN;
		}
	}
	
	/**
	 * Converts the head of the snake from a HeadComponent to a TailComponent
	 */
	private void convertHeadToTailComponent() {
		HeadComponent curHead = (HeadComponent) snake.getLast();
		snake.removeLast();
		snake.addLast(new TailComponent(curHead.getX(), curHead.getY()));
	}
	
	/**
	 * Adds a new element to the front of the snake according to the snake's direction
	 */
	private void addNewHead() {		
		if(currentDirection == Direction.RIGHT) {
			snake.addLast(new HeadComponent(
					snake.getLast().getX() + SnakeComponent.imageSize, 
					snake.getLast().getY(),
					currentDirection
			));
			snakeDirection = Direction.RIGHT;
			
		} else if(currentDirection == Direction.LEFT) {
			snake.addLast(new HeadComponent(
					snake.getLast().getX() - SnakeComponent.imageSize, 
					snake.getLast().getY(), 
					currentDirection
			));
			snakeDirection = Direction.LEFT;
			
		} else if(currentDirection == Direction.DOWN) {
			snake.addLast(new HeadComponent(
					snake.getLast().getX(), 
					snake.getLast().getY()+SnakeComponent.imageSize, 
					currentDirection
			));
			snakeDirection = Direction.DOWN;
			
		} else if(currentDirection == Direction.UP) {
			snake.addLast(new HeadComponent(
					snake.getLast().getX(), 
					snake.getLast().getY()-SnakeComponent.imageSize,
					currentDirection
			));
			snakeDirection = Direction.UP;
		}
	}
	
	public void render(Graphics g) {
		for(SnakeComponent component : snake) {
			component.render(g);
		}
	}
	
	public void setDirection(Direction d) { // used by SnakeInputController
		currentDirection = d;
	}
	
	public HeadComponent getHead() { // used by Fruit for collision detection
		return (HeadComponent) snake.getLast();
	}
	
	// The snake expands when it eats a fruit
	public void expand() {
		convertHeadToTailComponent();
		addNewHead();
	}
	
	/**
	 * @return whether the HeadComponent intersects with any TailComponents
	 */
	public boolean hasBitItself() {
		HeadComponent head = (HeadComponent) snake.getLast();
		for(int i = 0; i < snake.size() - 1; ++i) {
			if(head.intersects(snake.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isOutOfBounds() {
		HeadComponent head = (HeadComponent) snake.getLast();
		if(head.getX() <= -5 || // don't know why the 5 is needed
								// but it works better like that
			head.getX() + SnakeComponent.imageSize >= SnakeGame.WIDTH || 
			head.getY() <= -5 || 
			head.getY() + SnakeComponent.imageSize >= SnakeGame.HEIGHT-10) {
			return true;
		} else {
			return false;
		}
	}
	
	public void initExplosions() {
		for(SnakeComponent component : snake) {
			component.initExplosion();
		}
	}
	
	public void updateExplosions() {
		for(SnakeComponent component : snake) {
			if(component.hasExplosionStarted())
				component.updateExplosion();
		}
		
		Collections.reverse(snake);		
		Iterator<SnakeComponent> itr = snake.iterator();
		if(timer2.update()) {
			while(itr.hasNext()) {
				SnakeComponent component = itr.next();
				
				if(!component.hasExplosionStarted()) {
					component.startExplosion();
					break;
				}
				
				if(component.hasMostlyExploded()) {
					itr.remove();
				}
			}
		}
		
		Collections.reverse(snake);
	}
	
	public void renderExplosions(Graphics g) {
		for(SnakeComponent component : snake) {
			if(component.hasExplosionStarted()) {
				component.renderExplosion(g);
			}
		}
	}
	
	public boolean hasSnakeExplosionStarted() { 
		return hasSnakeExplosionStarted; 
	}
	
	public void startExplosion() {
		hasSnakeExplosionStarted = true;
	}
	
	/**
	 * @return if the snake exists, i.e. if it has at least one element
	 */
	public boolean hasExploded() {
		return snake.size() < 1;
	}
	
	public boolean isDead() { return isDead; }
	public void setDead(boolean b) { isDead = b; }
	
	public LinkedList<SnakeComponent> getBody() { return snake; }
}
