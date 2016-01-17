package project_rpg;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/** Displays the battle screen.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class BattleGrid extends JPanel {

    /** Initializes the battle grid. */
    public BattleGrid() {
        setPreferredSize(new Dimension(WIDTH * SQ_WIDTH, HEIGHT * SQ_HEIGHT));
        playerX = 0;
        playerY = 0;
        addKeyListener(new PlayerControl());
        setFocusable(true);
        repaint();
    }

    @Override
    public synchronized void paintComponent(Graphics g) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                paintImage("default", x, y, g);
            }
        }
        paintImage("player", playerX, playerY, g);
    }

    /** Paints IMAGE at (X, Y) on G. */
    private void paintImage(ImageIcon image, int x, int y, Graphics g) {
        g.drawImage(image.getImage(), x * SQ_WIDTH, y * SQ_HEIGHT, null);
    }

    /** Paints IMAGE at (X, Y) on G (using the name of the resource. */
    private void paintImage(String image, int x, int y, Graphics g) {
        paintImage(getImage(image), x, y, g);
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
            System.out.printf("Player at (%s, %s).\n", playerX, playerY);
            switch (event.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                if (playerY < HEIGHT - 1) {
                    playerY += 1;
                }
                break;
            case KeyEvent.VK_LEFT:
                if (playerX > 0) {
                    playerX -= 1;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (playerX < WIDTH - 1) {
                    playerX += 1;
                }
                break;
            case KeyEvent.VK_UP:
                if (playerY > 0) {
                    playerY -= 1;
                }
                break;
            }
            repaint();
        }

    }

    /** Contains the location of the player. */
    private int playerX, playerY;

    /** Contains the parameters of the grid. */
    public static final int WIDTH = 15, HEIGHT = 8, SQ_WIDTH = 50,
        SQ_HEIGHT = 50;

}
