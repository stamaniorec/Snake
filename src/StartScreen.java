import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;


public class StartScreen implements KeyListener {

	private final String title = "SNAKE"; // Serves as a logo of sorts
	
	private ArrayList<StartMenuItem> menuItems;
	private static final int menuItemsCount = 2;
	private int curSelectedMenuItem; // changes with key presses
	
	private boolean isPlaySelected;
	private boolean isExitSelected;
	
	private Font smallerFont;
	private Font biggerFont;
		
	public StartScreen(Font smallerFont, Font biggerFont) {		
		menuItems = new ArrayList<StartMenuItem>();
		menuItems.ensureCapacity(menuItemsCount); // Do not create new array 
												  // and copy all the time 
		menuItems.add(new StartMenuItem("PLAY"));
		menuItems.add(new StartMenuItem("EXIT"));
		
		this.smallerFont = smallerFont;
		this.biggerFont = biggerFont;
		
		init();
	}
	
	public void init() {
		for(StartMenuItem item : menuItems) {
			item.init();
		}
		
		curSelectedMenuItem = 0; 			// The first option is 
		menuItems.get(0).setSelected(true); // selected by default
		
		isExitSelected = isPlaySelected = false;
	}
	
	public void render(Graphics g) {
		g.setColor(Color.RED);
		g.setFont(biggerFont);
		
		// Draw the title in the center of the screen with a bigger red font
		int curStringWidth = g.getFontMetrics().stringWidth(title);
		g.drawString(
				title, 
				SnakeGame.WIDTH / 2 - curStringWidth / 2, 
				200
		);
		
		// Draw the menu items in the center of the screen with a smaller font
		// The selected option is in red, the others in white
		g.setFont(smallerFont);
		for(int i = 0; i < menuItems.size(); ++i) {
			StartMenuItem item = menuItems.get(i);
			
			if(item.isSelected()) {
				g.setColor(Color.RED);
			} else {
				g.setColor(Color.WHITE);
			}
			
			curStringWidth = g.getFontMetrics().stringWidth(item.getTitle());
			g.drawString(
					item.getTitle(), 
					SnakeGame.WIDTH / 2 - curStringWidth / 2, 
					400 + i*75
			);
		}
	}

	// Update curSelectedMenuItem according to what the user pressed
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			if(++curSelectedMenuItem >= menuItemsCount) {
				curSelectedMenuItem = 0;
			}
			
			for(StartMenuItem item : menuItems) {
				item.setSelected(false);
			}
			
			menuItems.get(curSelectedMenuItem).setSelected(true);
		} else if(e.getKeyCode() == KeyEvent.VK_UP) {
			if(--curSelectedMenuItem < 0) {
				curSelectedMenuItem = menuItemsCount-1;
			}
			
			for(StartMenuItem item : menuItems) {
				item.setSelected(false);
			}
			
			menuItems.get(curSelectedMenuItem).setSelected(true);
		} else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(curSelectedMenuItem == 0) {
				isPlaySelected = true;
			} else if(curSelectedMenuItem == 1) {
				isExitSelected = true;
			}
		}
	}

	public void keyReleased(KeyEvent e) { }
	public void keyTyped(KeyEvent e) { }
	
	public boolean isPlaySelected() { return isPlaySelected; }
	public boolean isExitSelected() { return isExitSelected; }
	
}
