import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;


public class Player extends Element {

	private double x, y;
	private double xVel, yVel;
	private double xSpeed = 0.05, ySpeed = 0.05;
	private double speedCap = 2.5;
	private double edgeModifier = 2.5;
	
	// TODO export moon to separate class, make recursive
	private double mX, mY;
	private double mXVel, mYVel;
	private double damper = 0.001;
	
	
	public void render(Graphics g)
	{
		g.setColor(Color.red);
		g.drawOval((int)x, (int)y, 50, 50);
		
		g.setColor(Color.blue);
		g.drawOval((int)mX, (int)mY, 25, 25);
		
		g.setColor(Color.white);
		g.drawLine((int)x,(int)y,(int)mX,(int)mY);
	}
	
	public void tick(GameState s)
	{
		move(s);
		
		System.out.println(xVel);
	}
	
	private double distance(double x1,double y1,double x2,double y2)
	{
		return Math.pow(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2), 0.5);
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
		
		// moon "rubber band" "physics"
		double d = distance(x,y,mX,mY);
		if(x > mX) mXVel += damper*d;
		if(x < mX) mXVel -= damper*d;
		if(y > mY) mYVel += damper*d;
		if(y < mY) mYVel -= damper*d;
		// moon momentum
		mX += mXVel;
		mY += mYVel;
		
		// speed caps
		xVel = Math.min(Math.max(xVel, -speedCap), speedCap);
		yVel = Math.min(Math.max(yVel, -speedCap), speedCap);
	}
	
}
