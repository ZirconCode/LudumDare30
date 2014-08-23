import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;


public class Player extends Element {

	private double x, y;
	private double xVel, yVel;
	private double xSpeed = 0.05, ySpeed = 0.05;
	private double speedCap = 2.5;
	private double edgeModifier = 2.5;
	
	// TODO earths weapon; a moon on a rope like a wrecking ball
	private double mX, mY;
	private double mXVel, mYVel;
	
	
	public void render(Graphics g)
	{
		g.setColor(Color.red);
		g.drawOval((int)x, (int)y, 50, 50);
		
		g.setColor(Color.blue);
		g.drawOval((int)mXVel, (int)mYVel, 25, 25);
		
		g.setColor(Color.white);
		g.drawLine((int)x,(int)y,(int)mX,(int)mY);
	}
	
	public void tick(GameState s)
	{
		move(s);
		
		System.out.println(xVel);
	}
	
	private void move(GameState s)
	{
		// momentum
		x = x+xVel;
		y = y+yVel;
		
		// controls
		if(s.keyDown[KeyEvent.VK_DOWN])
			yVel += ySpeed;
		if(s.keyDown[KeyEvent.VK_UP])
			yVel -= ySpeed;
		if(s.keyDown[KeyEvent.VK_RIGHT])
			xVel += xSpeed;
		if(s.keyDown[KeyEvent.VK_LEFT])
			xVel -= xSpeed;
		
		// "bouncy" edge
		if(x < 0) xVel += xSpeed*edgeModifier;
		if(x > s.width) xVel -= xSpeed*edgeModifier;
		if(y < 0) yVel += ySpeed*edgeModifier;
		if(y > s.height) yVel -= ySpeed*edgeModifier;
		
		// TODO !!! moon "rubber band" "physics"
		// TODO !!! Gotta go to work now =(
		
		// speed caps
		xVel = Math.min(Math.max(xVel, -speedCap), speedCap);
		yVel = Math.min(Math.max(yVel, -speedCap), speedCap);
	}
	
}
