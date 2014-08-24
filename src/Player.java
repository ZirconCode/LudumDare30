import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;


public class Player extends Element {
	
	private double x, y;
	private double xVel, yVel;
	private double xSpeed = 0.15, ySpeed = 0.15;
	//private double xSpeed = 2, ySpeed = 2; 
		// TODO resistance (for moon too...)
	private double speedCap = 2.0;
	private double edgeModifier = 2.5;
	private double radius = 50; // actually diameter.... >___< *shhh*
	private double friction = 0.05;
	
	// TODO export moon to separate class, make recursive
	private double mX, mY;
	private double mXVel, mYVel;
	private double damper = 0.001;
	private double mRadius = 25; // actually diameter....
	private double mSpeedCap = 7;
	private double mFriction = 0.01; // TODO testing...
	
	// pics because resource management is TODO =p
	private Image earth;
	private Image moon;
	
	// powerup timers
	private double invincibleEarth = 0;
	private double bigMoon = 0;
	
	public Player(GameState s, Image e, Image m)
	{
		x = 75;
		y = s.height/2;
		mX = x-50;
		mY = y+50;
		
		earth = e;
		moon = m;
	}

	public void render(Graphics g)
	{
		g.setColor(Color.white);
		g.drawLine((int)(x+radius*0.5),(int)(y+radius*0.5),(int)(mX+mRadius*0.5),(int)(mY+mRadius*0.5));
		
		//g.setColor(Color.red);
		//g.drawOval((int)x, (int)y, (int)radius, (int)radius);
		g.drawImage(earth,(int)x, (int)y,null);
		
		//g.setColor(Color.blue);
		//g.drawOval((int)mX, (int)mY, (int)mRadius, (int)mRadius);
		g.drawImage(moon,(int)mX, (int)mY, (int)mRadius, (int)mRadius, null);
		
		if(invincibleEarth > 0)
		{
			g.setColor(Color.blue);
			g.drawOval((int)x-10, (int)y-10, (int)radius+20, (int)radius+20);
			g.drawString("INVINCIBLE", (int)x-20, (int)y-25);
			g.drawString(""+(int)invincibleEarth, (int)x, (int)y-10);
		}
		if(bigMoon > 0)
		{
			g.setColor(Color.yellow);
			g.drawString("BIG MOON", (int)x-20, (int)(y+radius+3));
			g.drawString(""+(int)bigMoon, (int)x, (int)(y+radius+18));
		}
		
	}
	
	public void tick(GameState s)
	{
		move(s);
		collision(s);
		powerups(s);
	}
	
	private void powerups(GameState s)
	{
		if(invincibleEarth>0)
		{
			invincibleEarth--;
		}
		
		if(bigMoon>0)
		{
			mRadius = 150;
			bigMoon--;
		}
		else
		{
			mRadius = 25;
		}
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
			
			// powerups
			if(e.getClass() == Powerup.class)
			{
				Powerup p = (Powerup)e;
				
				boolean t = isCircleTouchingCircle(x, y, radius/2, p.x, p.y, p.d/2);
				if(t)
				{
					if(p.type == 0) s.lives++;
					if(p.type == 1) bigMoon = 1111;
					if(p.type == 2) invincibleEarth = 1111;
					
					s.sound_to_play = 3;
					
					p.destroy(s);
				}
				
				continue;
			}
			
			// enemies
			if(e.getClass() != Enemy.class) continue;
			Enemy en = (Enemy)e;
			
			// earth
			boolean t = isCircleTouchingSquare(radius/2, x, y, en.x, en.y, en.h, en.w);
			if(t) 
			{
				en.destroy(s);
				if(!(invincibleEarth > 0)) s.lives--;
				s.sound_to_play = 1;
			}
			
			// moon
			t = isCircleTouchingSquare(mRadius/2, mX, mY, en.x, en.y, en.h, en.w);
			if(t)
			{
				en.destroy(s);
				s.score += 5000;
				s.sound_to_play = 2;
			}
			
		}	
	}
	
	// after-TODO put these funcs in BasicGameApp.git for next time...
	public double clamp(double val, double min, double max)
	{
		return Math.min(Math.max(val, min), max);
	}
	
	// creative function naming =p
	public boolean isCircleTouchingCircle(double x1, double y1, double r1, double x2, double y2, double r2)
	{
		return (distance(x1,y1,x2,y2) < (r1+r2));
	}
	
	public boolean isCircleTouchingSquare(double cR, double cX, double cY, double sX, double sY, double sH, double sW)
	{
		// we don't need no physics engine
		// just another collision function in the collision-library-less walllll....
		
		// cX + cY not actually midpoints
		double cXM = cX+(cR);
		double cYM = cY+(cR);
		
		// closest point to square
		double closestX = clamp(cXM,sX,sX+sW);
		double closestY = clamp(cYM,sY,sY+sH);
		
		// within radius?
		return(distance(closestX,closestY,cXM,cYM) < cR);
	}
	
	// TODO stick in lib...
	private double approach(double value, double target, double amount)
	{
		double newValue = value;
		if(value>target) newValue = value-amount;
		if(value<target) newValue = value+amount;
		if(Math.abs(value-target)<amount) newValue = target;
		return newValue;
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
		
		// friction
		xVel = approach(xVel, 0, friction);
		yVel = approach(yVel, 0, friction);
		
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
		// moon friction?
		mXVel = approach(mXVel, 0, mFriction);
		mYVel = approach(mYVel, 0, mFriction);
		
		// speed caps
		xVel = Math.min(Math.max(xVel, -speedCap), speedCap);
		yVel = Math.min(Math.max(yVel, -speedCap), speedCap);
	}
	
}
