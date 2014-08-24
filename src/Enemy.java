import java.awt.Color;
import java.awt.Graphics;


public class Enemy extends Element {

	// collission detection?
	// sigh~ let's violate some object privacies
	// --> see Player
	
	// PUBLIC oh yeah o.o/
	public double x;
	public double y;
	public double w;
	public double h;
	
	public double xSpeed;
	public double ySpeed;
	
	
	public void render(Graphics g)
	{
		g.setColor(Color.blue);
		g.drawRect((int)x, (int)y, (int)w, (int)h);
	}
	
	public void tick(GameState s)
	{
		move(s);
	}
	
	private void move(GameState s)
	{
		x += xSpeed;
		y += ySpeed;
	}
	
	public void destroy(GameState s)
	{
		s.elements.remove(this);
		// TODO particles or stuff...
	}
	
}
