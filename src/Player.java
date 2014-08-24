import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;


public class Player extends Element {

	private double x, y;
	private double xVel, yVel;
	private double xSpeed = 0.05, ySpeed = 0.05;
	private double speedCap = 2.0;
	private double edgeModifier = 2.5;
	private double radius = 50;
	
	// TODO export moon to separate class, make recursive
	private double mX, mY;
	private double mXVel, mYVel;
	private double damper = 0.001;
	private double mRadius = 25;
	private double mSpeedCap = 3;
	
	public void render(Graphics g)
	{
		g.setColor(Color.red);
		g.drawOval((int)x, (int)y, (int)radius, (int)radius);
		
		g.setColor(Color.blue);
		g.drawOval((int)mX, (int)mY, (int)mRadius, (int)mRadius);
		
		g.setColor(Color.white);
		g.drawLine((int)(x+radius*0.5),(int)(y+radius*0.5),(int)(mX+mRadius*0.5),(int)(mY+mRadius*0.5));
	}
	
	public void tick(GameState s)
	{
		move(s);
		collision(s);
	}
	
	private double distance(double x1,double y1,double x2,double y2)
	{
		return Math.pow(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2), 0.5);
	}
	
	private void collision(GameState s)
	{
		// "collision detection" -_-
		for(int i = 0; i<s.elements.size(); i++)
		{
			Element e = s.elements.get(i);
			if(e.getClass() != Enemy.class) continue;
			Enemy en = (Enemy)e;
			
			// earth
			boolean t = isCircleTouchingSquare(radius, x, y, en.x, en.y, en.h, en.w);
			if(t) 
			{
				en.destroy(s);
				s.lives--;
			}
			
			// moon
			t = isCircleTouchingSquare(mRadius, mX, mY, en.x, en.y, en.h, en.w);
			if(t)
			{
				en.destroy(s);
				s.score += 1000;
			}
			
		}	
	}
	
	// after-TODO put these funcs in BasicGameApp.git for next time...
	public double clamp(double val, double min, double max)
	{
		return Math.min(Math.max(val, min), max);
	}
	
	public boolean isCircleTouchingSquare(double cR, double cX, double cY, double sX, double sY, double sH, double sW)
	{
		// we don't need no physics engine
		// just another collision function in the collision-library-less walllll....
		
		// cX + cY not actually midpoints
		double cXM = cX+(cR/2);
		double cYM = cY+(cR/2);
		
		// closest point to square
		double closestX = clamp(cXM,sX,sX+sW);
		double closestY = clamp(cYM,sY,sY+sH);
		
		// within radius?
		return(distance(closestX,closestY,cXM,cYM) < cR);
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
		// moon cap
		mXVel = Math.min(Math.max(mXVel, -mSpeedCap), mSpeedCap);
		mXVel = Math.min(Math.max(mXVel, -mSpeedCap), mSpeedCap);
		
		// speed caps
		xVel = Math.min(Math.max(xVel, -speedCap), speedCap);
		yVel = Math.min(Math.max(yVel, -speedCap), speedCap);
	}
	
}
