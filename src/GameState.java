import java.util.Vector;


public class GameState {

	public int state = 0;
	public int lvlTick = 0;
	
	// Entities
	public Vector<Element> elements;
	
	// Camera / Dimensions
	public int height;
	public int width;
	
	// Input
	public boolean[] keyDown;
	public boolean mouseDown;
	public int mouseCoordX;
	public int mouseCoordY;
	
	public boolean sound = true;
	
	public int lives = 5;
	public int score = 0;
	
	public int sound_to_play = 0; // meh...
	
	public GameState()
	{
		keyDown = new boolean[65535];
		elements = new Vector<Element>();
	}
	
	
}
