import java.awt.Color;
import java.awt.Graphics;


public class Star extends Element{

	private double x;
	private double y;
	private double xSpeed;
	private Color clr;
	
	
	
	public Star(GameState s)
	{
		y = Math.random()*s.height;
		x = s.width;
		xSpeed = 3+(Math.random()*-4);
		clr = new Color(150+rInt(100),150+rInt(100),150+rInt(100));

	}
	
	public void render(Graphics g)
	{
		g.setColor(clr);
		g.drawLine((int)x, (int)y, (int)(x+xSpeed*4), (int)y);
	}
	
	public void tick(GameState s)
	{
		x += xSpeed;
		if(x < -100) s.elements.remove(this);
	}
	
}
