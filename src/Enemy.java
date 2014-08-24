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
	
	private double xSpeed;
	private double ySpeed;
	
	private Color clr;
	
	public Enemy(GameState s)
	{
		x = s.width;
		y = Math.random()*s.height;
		w = 20+rInt(70);
		h = 20+rInt(70);
		
		xSpeed = -1*Math.random()*2;
		ySpeed = (Math.random()*2)-1;
		
		clr = new Color(55+rInt(200),55+rInt(200),55+rInt(200));
	}
	
	public void render(Graphics g)
	{
		g.setColor(clr);
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
		// explode =D
		for(int i = 0; i < 20; i++) s.elements.add(new Explosion(x+(w/2),y+(h/2)));
		
		s.elements.remove(this);
	}
	
}
