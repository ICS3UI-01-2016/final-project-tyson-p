
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 *
 * @author petet9087
 */
public class FinalGame extends JComponent implements KeyListener {

    // Height and Width of our game
    static final int WIDTH = 1200;
    static final int HEIGHT = 500;
    // game variables
    // set color for background
    Color SkyPurple = new Color(161, 144, 212);
    // create character for player to control
    Rectangle person = new Rectangle(40, 425, 25, 75);
    //create the obstacle
    Rectangle[] rock = new Rectangle[4];
    //add jump velocity
    int jumpVelocity = -10;
    int dy = 0;
    int gravity = 1;
    //jump key variable
    boolean jump = false;
    boolean lastJump = false;
    // wait to start
    boolean start = false;
    boolean dead = false;
    // distance between the rocks
    int rockSpacing = 300;
    // width on a single rock
    int rockWidth = 75;
    // the height of the rock
    int rockHeight = HEIGHT - 50;
    // minimun distance from edge
    int minDistance = 600;
    //speed of the game 
    int speed = 3;
    //load background image
//    BufferedImage background = loadImage ("sky-background.png");
//    //load obstacle image
//    BufferedImage obstacle = loadImage ("rock.png");
//    //load person image
//    BufferedImage player = loadImage ("groot.png");
    // sets the framerate and delay for our game
    // you just need to select an approproate framerate
    long desiredFPS = 60;
    long desiredTime = (1000) / desiredFPS;

    // drawing of the game happens in here
    // we use the Graphics object, g, to perform the drawing
    // NOTE: This is already double buffered!(helps with framerate/speed)
    @Override
    public void paintComponent(Graphics g) {
        // always clear the screen first!
        g.clearRect(0, 0, WIDTH, HEIGHT);
        // change the color of the sky
        g.setColor(SkyPurple);
        // set the color of the sky
        g.fillRect(0, 0, WIDTH, HEIGHT);
        //g.drawImage(background, 0, 0, WIDTH, HEIGHT, null);
        
        //draw the bird into the game
       
        g.setColor(Color.white);
        // set coordinates for player
        g.fillRect(person.x, person.y, person.width, person.height);
        
        //g.drawImage(player, person.x, person.y, person.width, person.height, null);

        // GAME DRAWING GOES HERE 
        g.setColor(Color.GRAY);
        for (int i = 0; i < rock.length; i++) {
            g.fillRect(rock[i].x, rock[i].y, rock[i].width, rock[i].height);
            //g.drawImage(obstacle, rock[i].x, rock[i].y,rock[i].width, rock[i].height, null);
        }
        // GAME DRAWING ENDS HERE
    }
    
//    public BufferedImage loadImage(String filename) {
//        BufferedImage img = null;
//        try {
//            File file = new File(filename);
//            img = ImageIO.read(file);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return img;
//    }
    
    
    public int randInt(int min, int max){
        return (int)(Math.random()*(max-min+1) + min);
    }
    

    public void setRock(int rockPosition) {
        // generate a random number
        Random randGen = new Random();
        // generate new rock y coordinate
        int rockY = randInt(minDistance, 150);
        // generate the new rock x coordinates
        int rockX = rock[rockPosition].x;
        rock[rockPosition].setBounds(rockX, rockY, rockWidth, rockHeight);




    }

    // The main game loop
    // In here is where all the logic for my game will go
    public void run() {
        // Used to keep track of time used to draw and update the game
        // This is used to limit the framerate later on
        long startTime;
        long deltaTime;

        // set up the rocks
        int rockX = 600;
//        Random randGen = new Random();
        for (int i = 0; i < rock.length; i++) {
            // generating a y position
            int rockY = randInt ( minDistance, 150);
            // create one rock
            rock[i] = new Rectangle(rockX, rockY, rockWidth, rockHeight);
            // move rock x vaule over
            rockX = rockX + rockWidth + rockSpacing;
        }



        // the main game loop section
        // game will end if you set done = false;
        boolean done = false;
        while (!done) {
            // determines when we started so we can keep a framerate
            startTime = System.currentTimeMillis();

            // all your game rules and move is done in here
            // GAME LOGIC STARTS HERE 

            if (start) {
                for (int i = 0; i < rock.length; i++) {
                    rock[i].x = rock[i].x - speed;
                    //check if a rock is off the screen
                    if (rock[i].x + rockWidth < 0) {
                        // move the rock
                        setRock(i);
                    }
                }


                // get the person to jump
                //apply gravity to the person
                dy = dy + gravity;
                if (jump && !lastJump) {
                    dy = jumpVelocity;
                }
                lastJump = jump;
                // apply the changes above to the player
                person.y = person.y + dy;

                // make walls so the player does not fall through the ground
                if (person.y < 0) {
                    person.y = 0;
                } else if (person.y + person.height > HEIGHT) {
                    person.y = HEIGHT - person.height;
                }

            }

            // GAME LOGIC ENDS HERE 

            // update the drawing (calls paintComponent)
            repaint();



            // SLOWS DOWN THE GAME BASED ON THE FRAMERATE ABOVE
            // USING SOME SIMPLE MATH
            deltaTime = System.currentTimeMillis() - startTime;
            try {
                if (deltaTime > desiredTime) {
                    //took too much time, don't wait
                    Thread.sleep(1);
                } else {
                    // sleep to make up the extra time
                    Thread.sleep(desiredTime - deltaTime);
                }
            } catch (Exception e) {
            };
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // creates a windows to show my game
        JFrame frame = new JFrame("My Game");

        // creates an instance of my game
        FinalGame game = new FinalGame();
        // sets the size of my game
        game.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // adds the game to the window
        frame.add(game);

        // sets some options and size of the window automatically
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        // shows the window to the user
        frame.setVisible(true);
        frame.addKeyListener(game);
        // starts my game loop
        game.run();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // check to see if key is pressed
        int key = e.getKeyCode();
        //if key pressed is the space bar
        if (key == KeyEvent.VK_UP) {
            // jump is correct
            jump = true;
        }
        if (key == KeyEvent.VK_LEFT) {
            // forward is correct
            person.x = person.x - 30;
        }
        if (key == KeyEvent.VK_RIGHT) {
            // forward is correct
            person.x = person.x + 30;

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // check to see if key is pressed
        int key = e.getKeyCode();
        //if key pressed is the space bar
        if (key == KeyEvent.VK_UP) {
            // jump is correct
            jump = false;
        }
    }
}
