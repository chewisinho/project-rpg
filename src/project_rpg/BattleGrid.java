package project_rpg;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import project_rpg.behaviors.Token;
import project_rpg.behaviors.monsters.SimpleMelee;


/** Displays the battle screen.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class BattleGrid extends JPanel {

    /** Initializes the battle grid with a PLAYER and a GUI. */
    public BattleGrid(GUI gui, Player player, String dungeon) {
        setPreferredSize(new Dimension(WIDTH * SQ_WIDTH, HEIGHT * SQ_HEIGHT));
        _gui = gui;
        _player = player;
        addKeyListener(new PlayerControl());

        // Initialize the dungeon.
        map = new Token[WIDTH][HEIGHT];
        monsters = new HashMap<Token, Monster>();
        try {
            BufferedReader input = new BufferedReader(new FileReader(new File(
                "project_rpg" + File.separator + "database" + File.separator
                + "dungeons" + File.separator + dungeon + ".json")));
            JsonParser parser = new JsonParser();
            JsonObject attrTree = (JsonObject) parser.parse(input);
            JsonArray layout = attrTree.get("layout").getAsJsonArray();
            int col = 0, row = 0;
            for (JsonElement rowElem : layout) {
                col = 0;
                JsonArray rowLayout = (JsonArray) rowElem;
                for (JsonElement element : rowLayout) {
                    String identifier = element.getAsString();
                    if (identifier.equals("player")) {
                        playerToken = new Token(this, 0, 0, "player");
                    } else if (identifier.equals("null")) {
                        map[col][row] = null;
                    } else {
                        Monster monster = new Monster(identifier);
                        Token token = GetMonsterToken.getToken(this, col, row, monster);
                        monsters.put(token, monster);
                    }
                    col += 1;
                }
                row += 1;
            }
        } catch (IOException exception) {
            Main.error("Unable to load dungeon " + dungeon + ".json.");
        }

        // Start monster threads.
        for (Token token : monsters.keySet()) {
            new Thread(token).start();
        }

        // Set up display.
        rotation = makeRotation(RIGHT_ANGLE);
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
            switch (event.getKeyCode()) {
            case KeyEvent.VK_S:
                playerToken.down();
                break;
            case KeyEvent.VK_A:
                playerToken.left();
                break;
            case KeyEvent.VK_D:
                playerToken.right();
                break;
            case KeyEvent.VK_W:
                playerToken.up();
                break;
            case KeyEvent.VK_SPACE:
                playerToken.attack();
                break;
            case KeyEvent.VK_UP:
                _player.switchAttack1();
                break;
            case KeyEvent.VK_DOWN:
                _player.switchAttack2();
                break;
            case KeyEvent.VK_LEFT:
                _player.switchAttack3();
                break;
            case KeyEvent.VK_RIGHT:
                _player.switchAttack4();
            default:
                break;
            }
        }

    }

    /** Represents a pi/2 rotation. */
    AffineTransformOp rotation;

    /** Contains the GUI from before the start of the battle. */
    public GUI _gui;

    /** Contains the monsters/spells and their tokens. */
    public HashMap<Token, Monster> monsters;

    /** Contains the player token. */
    public Token playerToken;

    /** Contains the map. */
    public Token[][] map;

    /** Contains the player. */
    public Player _player;

    /** Contains the parameters of the grid. */
    public static final int HEIGHT = 8, RIGHT_ANGLE = 90, SQ_HEIGHT = 50, SQ_WIDTH = 50, WIDTH = 15;

}

