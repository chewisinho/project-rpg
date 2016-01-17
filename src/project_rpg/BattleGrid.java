package project_rpg;

import java.awt.Dimension;
import java.awt.Graphics;
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
        repaint();
    }

    @Override
    public synchronized void paintComponent(Graphics g) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                paintImage("default", x, y, g);
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

    /** Returns an Image from NAME. */
    static ImageIcon getImage(String name) {
        return new ImageIcon("project_rpg" + File.separator + "resources"
            + File.separator + name + ".png");
    }

    /** Contains the parameters of the grid. */
    public static final int WIDTH = 30, HEIGHT = 16, SQ_WIDTH = 25,
        SQ_HEIGHT = 25;

}
