import java.awt.Color;
import java.awt.Graphics;


public class Powerup extends Element{

	public double x;
	public double y;
	public double d = 25;
	
	private double xSpeed;
	private double ySpeed;
	
	// 0 = life, 1 = big moon, 2 = invincible earth
	public int type;
	
	public Powerup(GameState s)
	{
		y = Math.random()*s.height;
		x = s.width;
		
		xSpeed = 3+(Math.random()*-4);
		ySpeed = (Math.random()*1)-0.5;
		
		type = rInt(3);
		//System.out.println(""+type);
	}
	
	public void render(Graphics g)
	{
		String letter = "";
		Color clr = Color.black;
		if(type == 0)
		{
			letter = "L";
			clr = Color.red;
		} 
		else if (type == 1)
		{
			letter = "B";
			clr = Color.yellow;
		} 
		else if (type == 2)
		{
			letter = "I";
			clr = Color.blue;
		}
		
		g.setColor(clr);
		g.drawString(letter, (int)x+8, (int)y+18);
		g.drawOval((int)x, (int)y, (int)d, (int)d);
	}
	
	public void tick(GameState s)
	{
		move(s);
	}
	
	private void move(GameState s)
	{
		x += xSpeed;
		y += ySpeed;
		
		if(x < -d*1.2) s.elements.remove(this);
	}
	
	public void destroy(GameState s)
	{
		s.elements.remove(this);
	}
	
}
