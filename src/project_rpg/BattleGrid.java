package project_rpg;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import project_rpg.behaviors.Token;

/** Displays the battle screen.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class BattleGrid extends JPanel {

    /** Initializes the battle grid with a PLAYER and a GUI. */
    public BattleGrid(Player player, GUI gui) {
        setPreferredSize(new Dimension(WIDTH * SQ_WIDTH, HEIGHT * SQ_HEIGHT));
        _gui = gui;
        _player = player;
        rotation = makeRotation(RIGHT_ANGLE);
        addKeyListener(new PlayerControl());
        map = new Token[WIDTH][HEIGHT];
        playerToken = new Token("player", 0, 0, this);
        monsters = new HashMap<Token, Monster>();
        try {
            monsters.put(new Token("monster", 4, 4, this),
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

    /** Returns true iff (X, Y) is in bounds. */
    public boolean inBounds(int x, int y) {
        return (x >= 0) && (y >= 0) && (x < WIDTH) && (y < HEIGHT);
    }

    @Override
    public synchronized void paintComponent(Graphics g) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                if (map[x][y] == null) {
                    paintImage("default", x, y, g);
                } else {
                    paintImage(map[x][y], x, y, g);
                }
            }
        }
    }

    /** Paints IMAGE at (X, Y) on G. */
    void paintImage(Image image, int x, int y, Graphics g) {
        g.drawImage(image, x * SQ_WIDTH, y * SQ_HEIGHT, null);
    }

    /** Paints IMAGE at (X, Y) on G. */
    void paintImage(ImageIcon image, int x, int y, Graphics g) {
        paintImage(image.getImage(), x, y, g);
    }

    /** Paints IMAGE at (X, Y) on G (using the name of the resource). */
    void paintImage(String image, int x, int y, Graphics g) {
        paintImage(getImage(image), x, y, g);
    }

    /** Paints a TOKEN at (X, Y) on G. */
    void paintImage(Token token, int x, int y, Graphics g) {
        BufferedImage image = token.bufferedImage();
        switch (token.orientation()) {
        case RIGHT_ANGLE * 3:
            image = rotation.filter(image, null);
        case RIGHT_ANGLE * 2:
            image = rotation.filter(image, null);
        case RIGHT_ANGLE:
            image = rotation.filter(image, null);
        default:
            break;
        }
        paintImage(image, x, y, g);
    }

    /** Returns true iff the player is adjacent to (X, Y). */
    public boolean playerAdjacentTo(int x, int y) {
        return (inBounds(x - 1, y) && playerToken == map[x - 1][y])
            || (inBounds(x, y - 1) && playerToken == map[x][y - 1])
            || (inBounds(x + 1, y) && playerToken == map[x + 1][y])
            || (inBounds(x, y + 1) && playerToken == map[x][y + 1]);
    }

    /** Returns true iff (X, Y) is a valid square. */
    public boolean valid(int x, int y) {
        return inBounds(x, y) && (map[x][y] == null);
    }

    /** Returns an Image from NAME. */
    public static ImageIcon getImage(String name) {
        return new ImageIcon("project_rpg" + File.separator + "resources"
            + File.separator + name + ".png");
    }

    /** Returns an AffineTransformOp which rotates by DEGREES. */
    static AffineTransformOp makeRotation(int degrees) {
        return new AffineTransformOp(AffineTransform.getRotateInstance(
            Math.toRadians(degrees), SQ_WIDTH / 2.0, SQ_HEIGHT / 2.0),
            AffineTransformOp.TYPE_BILINEAR);
    }

    /** Returns a given IMAGE converted into a BufferedImage. */
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        BufferedImage bimage = new BufferedImage(image.getWidth(null),
            image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bgr = bimage.createGraphics();
        bgr.drawImage(image, 0, 0, null);
        bgr.dispose();
        return bimage;
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

    /** Contains the GUI from before the start of the battle. */
    public GUI _gui;

    /** Contains the map. */
    public Token[][] map;

    /** Contains the player token. */
    public Token playerToken;

    /** Contains the player. */
    public Player _player;

    /** Contains the monsters and their tokens. */
    public HashMap<Token, Monster> monsters;

    /** Represents a pi/2 rotation. */
    AffineTransformOp rotation;

    /** Contains the parameters of the grid. */
    public static final int WIDTH = 15, HEIGHT = 8, SQ_WIDTH = 50,
        SQ_HEIGHT = 50;

    /** Contains the number of degrees in a right angle. */
    public static final int RIGHT_ANGLE = 90;

}

