
public class StartMenuItem {

	private String title;
	private boolean isSelected;
	
	public StartMenuItem(String title) {
		this.title = title;
	}
	
	public void init() {
		isSelected = false;
	}
	
	public void setSelected(boolean b) { isSelected = b; }
	public boolean isSelected() { return isSelected; }
	
	public String getTitle() { return title; }
	
}
