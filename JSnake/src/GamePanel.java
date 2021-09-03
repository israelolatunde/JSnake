import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{

	/*
	 * constants to define width and height of the panel,
	 * size of game units and
	 * number of game units
	 */
	
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	
	int delay = 300;//the delay is going to be used in the timer

	//integer arrays x and y hold the x and y coordinates of each part of the snake
	final int [] x = new int[GAME_UNITS];
	final int [] y = new int[GAME_UNITS];
	
	int snakeSize = 4;//initial size of the snake
	int applesEaten = 0;//number of apples eaten 
	
	//X and Y coordinates of the apple
	int appleX;
	int appleY;
	
	char direction = 'R';//references the position the snake is moving 
	boolean running = false;//is the game running?
	Timer timer;//Instance of timer class declared
	Random r;//Instance of Random class declared

	//construct the panel
	public GamePanel() {
		r = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
		
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
		
	}
	
	public void draw(Graphics g) {
		
		//if game is running..
		if(running) {
			
			//draw the apple(red circle)
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
		
			//loops through each part of the snake draws it
			for(int i = 0; i < snakeSize; i++) {
				if(i == 0) {//draw the head(dark green circle)
					g.setColor(new Color(10, 128, 20));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}else {//draw body parts(lighter green square)
					g.setColor(new Color(0, 90, 20));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			
			//print score at the top of panel
			String message = "Score: " + applesEaten;
			
			g.setColor(Color.RED);
			g.setFont(new Font("Stencil", Font.ITALIC, 30));
			
			FontMetrics myFont = getFontMetrics(g.getFont());
			g.drawString(message, (SCREEN_WIDTH - myFont.stringWidth(message))/2, g.getFont().getSize());
			
		}else endGame(g);//if game is not running call end game function
	}
	
	//gives random coordinates for a new apple
	public void addApple() {
		
	appleX = r.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE))*UNIT_SIZE;
	appleY = r.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE))*UNIT_SIZE;
	}
	
	/*
	 * starts game;
	 * adds an apple, running set as true,
	 * timer is started with default delay
	 */
	public void startGame() {
		addApple();
		running = true;
		timer = new Timer(delay, this);
		timer.start();
		
	}
	
	public void moveSnake() {
		/*
		 * loops through each part of the snake and
		 *  moves it forward by one unit
		 */
		for(int i = snakeSize; i > 0  ;i--) {
			x[i]=x[i-1];
			y[i] = y[i-1];	
		}
		
		//changes direction the head is moving in
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
			
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
			
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
		
	}
	
	public void checkApple() {
		//if apple and snake head have the same coordinates
		if((appleX == x[0]) && (appleY == y[0])) {
			snakeSize ++;//snake given another unit
			applesEaten++;//score goes up
			/*
			 * after every 10 apples eaten a new timer is initialized,
			 * the delay is reduced and 
			 * the speed of the game increases
			 */
			if(applesEaten % 10 == 0) {
			delay--;
			timer = new Timer(delay, this);
			timer.start();
			}


			addApple();//new apple added
			
			
		}
		
	}
	
	public void collision() {
		//head collides with body
		for(int i = snakeSize; i > 0; i--) {
			if((x[0] == x[i]) && y[0] == y[i]) {
				running = false;//game stops running
			}
		}
		
	   /*
		 * head touches left border
		  * head touches right border
		   * head touches top border
		    * head touches bottom border
		     */
		
		if(x[0] < 0 ||
			 x[0] > SCREEN_WIDTH ||
			   y[0] > SCREEN_HEIGHT ||
			     y[0] < 0) {
			
			running = false;//game stops running
		}
		
		if(!running) {
			timer.stop();
		}
		

	}
	
	//prints final score and end game message

	public void endGame(Graphics g) {
		String message = "GAME OVER!";
		
		g.setColor(Color.RED);
		g.setFont(new Font("Stencil", Font.ITALIC, 75));
		
		FontMetrics myFont1 = getFontMetrics(g.getFont());
		g.drawString(message, (SCREEN_WIDTH - myFont1.stringWidth(message))/2, SCREEN_HEIGHT/2);
	
		
		String message2 = "Score: " + applesEaten;
		
		g.setColor(Color.RED);
		g.setFont(new Font("Stencil", Font.ITALIC, 30));
		
		FontMetrics myFont2 = getFontMetrics(g.getFont());
		g.drawString(message2, (SCREEN_WIDTH - myFont2.stringWidth(message2))/2, g.getFont().getSize());
		
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		/*
		 * when the game is running, each action involves
		 *  the following methods
		 */
		if(running) {
			moveSnake();
			checkApple();
			collision();
			
			
		}
		repaint();//repaint the panel in response to the action
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			/*
			 * direction is changed depending in which key is pressed
			 * as long as the snake is not already moving in the 
			 * opposite direction.
			 * prevents snake from doing a 180 degree turn
			 */
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
				
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
				
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
				
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			
			}
			
		}
		
	}

}
