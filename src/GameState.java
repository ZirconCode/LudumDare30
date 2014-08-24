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
	
	public int lives = 3; // reduces if earth touches something +destr
	public int score = 0; // increased if moon touches something +destr
	
	
	public GameState()
	{
		keyDown = new boolean[65535];
		elements = new Vector<Element>();
	}
	
	
}
