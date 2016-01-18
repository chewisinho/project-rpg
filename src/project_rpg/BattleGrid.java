package project_rpg;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/** Displays the battle screen.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class BattleGrid extends JPanel {

    /** Initializes the battle grid with a PLAYER and a GUI. */
    public BattleGrid(Player player, GUI gui) {
        setPreferredSize(new Dimension(WIDTH * SQ_WIDTH, HEIGHT * SQ_HEIGHT));
        _gui = gui;
        _player = player;
        addKeyListener(new PlayerControl());
        map = new Token[WIDTH][HEIGHT];
        playerToken = new Token("player", 0, 0);
        monsters = new HashMap<Token, Monster>();
        try {
            monsters.put(new Token("monster", 4, 4),
                Monster.readMonster("Ice Pig"));
        } catch (IOException exception) {
            TextInterpreter.error("Error reading monster file.");
        }
        for (Token token : monsters.keySet()) {
            new Thread(token).start();
        }
        setFocusable(true);
        repaint();
    }

    @Override
    public synchronized void paintComponent(Graphics g) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                if (map[x][y] == null) {
                    paintImage("default", x, y, g);
                } else {
                    paintImage(map[x][y].image(), x, y, g);
                }
            }
        }
    }

    /** Paints IMAGE at (X, Y) on G. */
    private void paintImage(ImageIcon image, int x, int y, Graphics g) {
        g.drawImage(image.getImage(), x * SQ_WIDTH, y * SQ_HEIGHT, null);
    }

    /** Paints IMAGE at (X, Y) on G (using the name of the resource. */
    private void paintImage(String image, int x, int y, Graphics g) {
        paintImage(getImage(image), x, y, g);
    }

    /** Returns true iff the player is adjacent to (X, Y). */
    boolean playerAdjacentTo(int x, int y) {
        return (valid(x - 1, y) && playerToken == map[x - 1][y])
            || (valid(x, y - 1) && playerToken == map[x][y - 1])
            || (valid(x + 1, y) && playerToken == map[x + 1][y])
            || (valid(x, y + 1) && playerToken == map[x][y + 1]);
    }

    /** Returns true iff (X, Y) is a valid square. */
    boolean valid(int x, int y) {
        return (x >= 0) && (y >= 0) && (x < WIDTH) && (y < HEIGHT)
            && (map[x][y] == null);
    }

    /** Returns an Image from NAME. */
    static ImageIcon getImage(String name) {
        return new ImageIcon("project_rpg" + File.separator + "resources"
            + File.separator + name + ".png");
    }

    /** Class that listens for actions. */
    public class PlayerControl extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent event) {
            switch (event.getKeyChar()) {
            case '\u0073':
                playerToken.down();
                break;
            case '\u0061':
                playerToken.left();
                break;
            case '\u0064':
                playerToken.right();
                break;
            case '\u0077':
                playerToken.up();
                break;
            case '\u0020':
            	playerToken.attack();
            	break;
            case (char) KeyEvent.VK_UP:
            	playerToken.switchAttack1();
                break;
            case (char) KeyEvent.VK_DOWN:
            	playerToken.switchAttack2();
                break;
            case (char) KeyEvent.VK_LEFT:
            	playerToken.switchAttack3();
                break;
            case (char) KeyEvent.VK_RIGHT:
            	playerToken.switchAttack4();
            default:
                break;
            }
        }

    }

    /** Represents a token on the map. */
    public class Token implements Runnable {

        /** Creates a new token with IMAGE at (X, Y). */
        public Token(String image, int x, int y) {
            _image = getImage(image);
            _x = x;
            _y = y;
            map[x][y] = this;
            lastAction = System.currentTimeMillis();
        }

        /** Returns my x-coordinate. */
        public int x() {
            return _x;
        }

        /** Returns my y-coordinate. */
        public int y() {
            return _y;
        }

        /** Move one space down. */
        public void down() {
            move(_x, _y + 1);
        }

        /** Move one space left. */
        public void left() {
            move(_x - 1, _y);
        }

        /** Move one space right. */
        public void right() {
            move(_x + 1, _y);
        }

        /** Move one space up. */
        public void up() {
            move(_x, _y - 1);
        }

        /** Makes a movement to (X, Y). */
        public void move(int x, int y) {
            if (valid(x, y)) {
                map[_x][_y] = null;
                _x = x;
                _y = y;
                map[_x][_y] = this;
                repaint();
            }
        }
        
        /** Attacks. */
        public void attack() {
        	// TODO
        }
        
        /** Switches to Skill 1. */
        public void switchAttack1() {
        	// TODO
        }
        
        /** Switches to Skill 2. */
        public void switchAttack2() {
        	//TODO
        }
        
        /** Switches to Skill 3. */
        public void switchAttack3() {
        	//TODO
        }
        
        /** Switches to Skill 4. */
        public void switchAttack4() {
        	//TODO
        }

        /** Moves towards the player. */
        void moveTowardsPlayer() {
            if (playerToken.x() > _x) {
                right();
            } else if (playerToken.x() < _x) {
                left();
            } else {
                if (playerToken.y() > _y) {
                    down();
                } else if (playerToken.y() < _y) {
                    up();
                }
            }
        }

        /** Returns my image. */
        public ImageIcon image() {
            return _image;
        }

        @Override
        public void run() {
            while (true) {
                if (System.currentTimeMillis() - lastAction > 500) {
                    if (playerAdjacentTo(_x, _y)) {
                        _player.reduceHealth(monsters.get(this).attack());
                        if (_player.isDead()) {
                            _gui.gameOver();
                            break;
                        }
                    } else {
                        moveTowardsPlayer();
                    }
                    _gui.refreshMenu();
                    lastAction = System.currentTimeMillis();
                }
            }
        }

        /** My coordinates. */
        private int _x, _y;

        /** Contains my image. */
        private ImageIcon _image;

        /** Contains the time of my last action. */
        private long lastAction;

    }

    /** Contains the GUI from before the start of the battle. */
    private GUI _gui;

    /** Contains the map. */
    private Token[][] map;

    /** Contains the player token. */
    private Token playerToken;

    /** Contains the player. */
    private Player _player;

    /** Contains the monsters and their tokens. */
    private HashMap<Token, Monster> monsters;

    /** Contains the parameters of the grid. */
    public static final int WIDTH = 15, HEIGHT = 8, SQ_WIDTH = 50,
        SQ_HEIGHT = 50;

}
