import java.awt.Color;
import java.awt.Graphics;


// explosions o.o/
public class Explosion extends Element{

	private int life;
	private double x, y, r;
	private double xSpeed, ySpeed;
	private Color clr;
	
	public Explosion(double x, double y)
	{
		this.x = x;
		this.y = y;
		xSpeed = (Math.random()*2.5)-1.25;
		ySpeed = (Math.random()*2.5)-1.25;
		life = rInt(450)+50;
		r = rInt(30)+3;
		clr = new Color(rInt(255),rInt(255),rInt(255),200);
	}
	
	public void render(Graphics g)
	{
		g.setColor(clr);
		g.drawOval((int)(x-r), (int)(y-r), (int)(r), (int)(r));
	}
	
	public void tick(GameState s)
	{
		x += xSpeed;
		y += ySpeed;
		life--;
		
		if(life < 0) s.elements.remove(this);
		
		if(Math.random() < 0.05) clr = new Color(rInt(255),rInt(255),rInt(255),200);
 	}
	
}
