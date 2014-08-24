
/*
 * 	Basic Game Applet Structure
 * 
 *  Version: 	1.0
 *  Author: 	Simon Gruening / ZirconCode
 */

import java.applet.*; 
import java.awt.*; 
import java.awt.event.*; 
import java.net.URL;
  
public class Main extends Applet implements MouseMotionListener, MouseListener, KeyListener, Runnable
{
  
	boolean running;
	Thread UpdaterThread;
	
	Graphics bufferg; 
	Image bufferi;
	Dimension bufferdim; 
	Font MyFont;
	
	int tickTime = 5;
	
	GameState state; // TODO next time just make GameState global...
	
	// sounds thanks to bfxr.net
	// TODO add sound & image thing to BasicGameApplet.git
	AudioClip snd_die; 
	AudioClip snd_hit; 
	AudioClip snd_hit_moon; 
	
	// images
	URL baseURL;
	MediaTracker mt;
	Image pic_world;
	Image pic_moon;
	
	
	// Setup
	
	public void init() 
    { 
   	    
		setSize(800, 600);
		this.resize(800,600);
		
        bufferdim = getSize(bufferdim);
	    bufferi = createImage(bufferdim.width,bufferdim.height); 
	    bufferg = bufferi.getGraphics();
	    
	    setBackground(Color.black);
	    MyFont = new Font("Arial",Font.ITALIC,16);
	    addMouseListener(this);
	    addMouseMotionListener(this); 
	    addKeyListener(this);
		
	    // load sounds
	    snd_die = getAudioClip(getDocumentBase(),"game_over.wav"); 
	    snd_hit = getAudioClip(getDocumentBase(),"hit.wav"); 
	    snd_hit_moon = getAudioClip(getDocumentBase(),"moon_hit.wav"); 
	    
	    // load images
	    mt = new MediaTracker(this);
	    try { baseURL = getDocumentBase(); } catch (Exception e) {}
	    
	    pic_world = getImage(baseURL,"earth2.png");
	    mt.addImage(pic_world,1);
	    pic_moon = getImage(baseURL,"moon.png");
	    mt.addImage(pic_moon,1);
	    
	    try { mt.waitForAll(); } catch (InterruptedException  e) {}
	    // ^- load images
	    
	    state = new GameState();
	    state.height = bufferdim.height;
	    state.width = bufferdim.width;
   	 	gameSetup();
	    
		running = true;
		UpdaterThread = new Thread(this);
   	    UpdaterThread.start();
    }
	
	public void gameSetup()
	{
		// TODO next time, make this easier by just creating new GameState and putting it all in initializer there~
		
		// Clear previous games if any...
		state.elements.clear();
		state.lvlTick = 0;
		
		// -- Setup Game
		state.state = 1337;  // play game
		
		state.lives = 3;
		state.score = 0;
		
		Player p = new Player(state,pic_world,pic_moon);
		state.elements.add(p);
		// --
	}
	
	// Render
	
    public void update(Graphics g) 
    { 
    	paint(g); 
    }
	
	public void paint(Graphics g) 
    { 
		bufferg.setColor(Color.black);
        bufferg.fillRect(0,0,bufferdim.width,bufferdim.height);
        
        bufferg.setFont(MyFont);
        
        
        renderGame(bufferg); // TODO-fixit; not renderGame(g);
        
        
		g.drawImage(bufferi,0,0,this); 
    }
	
	public void renderGame(Graphics g)
	{
		// -- Render Game
		
		if(state.state == 1337)
		{
			for(int i = 0; i<state.elements.size(); i++)
				state.elements.get(i).render(g);
			
			g.setColor(Color.white);
			g.drawString("Lives: "+state.lives, 25, 25);
			g.setColor(Color.white);
			g.drawString("Score: "+state.score, 25, 50);
			
			if(state.lvlTick < 10000)
			{
				
			}
		}
		
		if(state.state == 33)
		{
			g.setColor(Color.red);
			g.drawString("Game Over >_<", (int)(state.width/2), (int)(state.height/2));
			g.setColor(Color.green);
			g.drawString("Your Score: "+state.score, (int)(state.width/2), (int)(state.height/2)+50);
			g.setColor(Color.blue);
			g.drawString("hit the space bar to play again =)", (int)(state.width/2), (int)(state.height/2)+100);
		}
		
        
        // --
	}
	
	// Game Loop
	
	public void run() 
    { 
         while (running) 
         { 
        	 long nextTick = (System.currentTimeMillis() + tickTime);
        	 
        	 updateGameLogic();
        	
        	 while(nextTick > System.currentTimeMillis()) { /* blergh */ }
        	 repaint();
         }
    }
    
	public void updateGameLogic()
	{
		// -- Update Game State
   	 
		// playing
		if(state.state == 1337)
		{
			state.score++; // points for being alive
			
			for(int i = 0; i<state.elements.size(); i++)
	   	 		state.elements.get(i).tick(state);
			
			state.lvlTick++;
			if(state.lvlTick < 10000 && Math.random() < 0.01)
			{
				// spawn stuff in the messiest way possible ^_^
				Enemy e = new Enemy();
				
				e.x = state.width;
				e.y = Math.random()*state.height;
				e.w = 50;
				e.h = 50;
				
				e.xSpeed = -1*Math.random()*1;
				e.ySpeed = (Math.random()*2)-1;
				
				state.elements.add(e);
			}
			//System.out.println(state.lvlTick);
			
			playRequestedSounds();
			
			spawnStars();
			
			// game over?
			if(state.lives < 1)
			{
				snd_die.play();
				state.state = 33;
			}
		}
   	 
		// g over
		if(state.state == 33)
		{
			if(state.keyDown[KeyEvent.VK_SPACE])
			{
				gameSetup();
			}
		}
		
   	 	// --
	}
	
	private void spawnStars()
	{
		if(Math.random() < 0.2)
			state.elements.add(new Star(state));
	}
	
	private void playRequestedSounds()
	{
		// play necessary sound effects..
		// mehh...... quick fix for lack of sound management of any kind TODO
		if(state.sound_to_play == 1) snd_hit.play();
		if(state.sound_to_play == 2) snd_hit_moon.play();
		
		state.sound_to_play = 0;
	}
	
    // Teardown
    
	public void stop() 
    { 
		
    }
    
	public void destroy() 
    { 
		stop();
		running = false; 
		UpdaterThread = null; 
    }
	

	// Input
	
    public void mouseMoved(MouseEvent me)  
    {  
    	state.mouseCoordX = me.getX();
    	state.mouseCoordY = me.getY();
    }
    
    public void mouseDragged(MouseEvent me)  
    { 

    } 

    public void mouseClicked (MouseEvent me) 
    {

    } 
    
    public void mouseEntered (MouseEvent me) 
    {

    } 
    
    public void mousePressed (MouseEvent me) 
    {
    	state.mouseDown = true;	
    } 
    
    public void mouseReleased (MouseEvent me) 
    {
    	state.mouseDown = false;
    }  
    
    public void mouseExited (MouseEvent me) 
    {

    }

	public void keyPressed(KeyEvent e) {
		state.keyDown[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e) {
		state.keyDown[e.getKeyCode()] = false;
	}

	public void keyTyped(KeyEvent arg0) {
		
	}  	
	
}