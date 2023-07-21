package snake;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener {
	
	
	//Screen size...
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	
	//Size of the cells that can be turned on or off in line 72 to 75...
	static final int UNIT_SIZE = 25;
	
	//Objects that display in screen: apples, snake...
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	
	//Speed option which works vice versa; the less delay is the faster snake be...
	static final int DELAY = 120;
	
	
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	
	
	int bodyParts = 6; //The snake's body which has length in 6 unit size and its first piece always has green color meanwhile its the last 5 parts will have random colors...
	int applesEaten;
	
	//It for spawns apples across the 2D game...
	int appleX;
	int appleY;
	
	
	char direction = 'R'; //Made for further settings to control the snake in the move method and in key adapter class...
	boolean running = false; //Made for further manipulations to move the snake 
	Timer timer; //Timer will be always go on until the snake's head touches body or borders...
	Random random; //Made for random spawning apples...
	
	
	GamePanel() {
		random = new Random();
		
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT)); 
		this.setBackground(Color.black); //Sets color of the display...
		this.setFocusable(true); //Allows to control the snake...
		this.addKeyListener(new MyKeyAdapter()); //Adds buttons to control the snake...
		
		startGame(); //Initially allows to play...
	}
	
	public void startGame() { 
		newApple(); //When the game is just started it will spawn apples...
		
		running = true; //Running game and snake...
		
		timer = new Timer(DELAY, this); 
		timer.start(); 
	;}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		
		if (running) {
/**			for (int i = 0; i< SCREEN_HEIGHT/UNIT_SIZE; i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}
**/	
			//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
			//      Adds grids...
			
			
			//Set apple color to red...
			g.setColor(Color.red);
			//Which size the apples will be...	
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
			for (int i = 0; i < bodyParts; i++) {
				if (i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				
				else {
					g.setColor(new Color(45, 180, 0));
					g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255))); //Makes the last 5 snake's parts randomly colorful... 
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			g.setColor(Color.white);
			g.setFont(new Font("sans serif", Font.BOLD, 25));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
		}
		
		else {
			gameOver(g);
		}
	}
	
	//Randomly spawns the apples across the screen...
	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE; //Spawns apples in axis X...
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE; //Spawns apples in axis Y...
	}
	
	public void move() {
		
		//For moving snake's body across the 2D screen...
		for(int i = bodyParts; i > 0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];	
		}
		//Decides where will go the snake; x is horizontal way meanwhile y is vertical one... 
		switch (direction) {
		case 'U': y[0] = y[0] - UNIT_SIZE; break; //Goes up...
		case 'D': y[0] = y[0] + UNIT_SIZE; break; //Goes down...
		case 'L': x[0] = x[0] - UNIT_SIZE; break; //Goes left...
		case 'R': x[0] = x[0] + UNIT_SIZE; break; //Goes right...
		}
	}
	
	public void checkApple() {
		if ((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++; //Adds unit size to the snake...
			applesEaten++; //Adds the score count... Score: 1, 2, 3...
			newApple(); //When an apple is eaten there'll be spawned another one...
		}
	}
	
	public void checkCollisions() {
		
		//Checks if head collides with body...
		for (int i = bodyParts; i > 0; i--) {
			if ((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		
		//Check if head touches left border...
		if (x[0] < 0) { running = false;  }
		
		
		//Check if head touches right border...
		if (x[0] > SCREEN_WIDTH) { running = false; }
		
		
		//Check if head touches top border...
		if (y[0] < 0) { running = false; }
		
		
		//Check if head touches bottom border...
		if (y[0] > SCREEN_HEIGHT) { running = false; }
		
		
		//Just stops the game whenever the snake's head touches any side of border... 
		if (!running) { timer.stop(); }
	}
	
	public void gameOver(Graphics g) {
		
		g.setColor(Color.white); //Sets the color to white...
		
		g.setFont(new Font("sans serif", Font.BOLD, 25)); //Sets the New font(%a font%, Font.%type of font(ex. italic, underline, etc)%, %font size%)
		
		FontMetrics metrics1 = getFontMetrics(g.getFont()); //Gets from java.awt that allows to display the letters...
		
		//Draws the score counter in the top display which shows the eaten apples...
		g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
		
	
		g.setColor(Color.red); //Sets red color in game over screen...
		g.setFont(new Font("sans serif", Font.BOLD, 75)); //Sets sans serif font, makes font bold and makes it size 75...
		
		FontMetrics metrics2 = getFontMetrics(g.getFont()); //Gets from java.awt that allows to display the letters in game over part...
		
		/**Draws the game over screen which is at the center of game screen because of 
		divided the width and height screen size by 2 and it located horizontally due to metrics2.stringWidth **/
		g.drawString("Game over...", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT / 2);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		//While running is true, these next methods will be go on...
		if (running) {
			move(); //Makes the snake to move. Check method move...
			checkApple(); //Makes the snake bigger after each eaten apple and spawns a new one. Check method checkApple...
			checkCollisions(); //Allows to snake hit its head at borders. Check method checkCollisions...
		}
		repaint(); //Allows change the snake's colors and move it...
	}
	
	//This class adds controls to make the snake move...
	public class MyKeyAdapter extends KeyAdapter { //Allows to move the snake with buttons in the keyboard...
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			
			/*It would be odd if snake moves right and we could move it to the right side again...
			VK is button in keyboard, and in this case there VK_LEFT, VK_RIGHT, VK_UP and VK_DOWDN which mean the arrows buttons left in keyboard...; 
			ex. VK_SPACE is space, VK_TAB is tab and so on... */
			
			//...............
			case KeyEvent.VK_LEFT: 
				if (direction != 'R' ) { 
					direction = 'L';
				}
				break;
				
			//...............
			case KeyEvent.VK_RIGHT:
				if (direction != 'L' ) {
					direction = 'R';
				}
				break;
				
			//...............
			case KeyEvent.VK_UP:
				if (direction != 'D' ) {
					direction = 'U';
				}
				break;
				
			//...............
			case KeyEvent.VK_DOWN:
				if (direction != 'U' ) {
					direction = 'D';
				}
				break;
			}
		}
	}

}
