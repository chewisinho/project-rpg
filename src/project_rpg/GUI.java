package project_rpg;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** Displays an interactive GUI for the game.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class GUI extends JPanel {

    /** Default constructor for the GUI. Sets up the graphical display. */
    public GUI() {
        frame = new JFrame("Project RPG Version 2.0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.getContentPane().add(this);
    }

    /** Starts a new game session. */
    void start() {
        JLabel titleScreen = new JLabel(getImage("title_screen.png"));
        add(titleScreen);
        JButton newGame = new JButton("New Game");
        newGame.addActionListener(new NewGameListener());
        add(newGame);
        JButton loadGame = new JButton("Load Game");
        loadGame.addActionListener(new LoadGameListener());
        add(loadGame);
        frame.setVisible(true);
    }

    /** Returns an Image from NAME. */
    ImageIcon getImage(String name) {
        return new ImageIcon("project_rpg" + File.separator + "database"
            + File.separator + name);
    }

    /** Class that listens for the New Game button. */
    public class NewGameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ignored) {
            try {
                Game game = new Game();
                setGame(game);
            } catch (IOException exception) {
                TextInterpreter.error("Could not load courses.");
            }
        } 

    }

    /** Class that listens for a save slot button. */
    public class SaveSlotListener implements ActionListener {

        /** Sets my number to BUTTONNUMBER. */
        public SaveSlotListener(int buttonNumber) {
            number = buttonNumber;
        }

        @Override
        public void actionPerformed(ActionEvent ignored) {
            if (number == 0) {
                GUI.this.removeAll();
                start();
            } else {
                Game game = Game.loadGame(number);
            	setGame(game);
            }
        }

        /** Contains the number of my button. */
        private int number;

    }

    /** Class that listens for the Load Game button. */
    public class LoadGameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ignored) {
            displaySaveSlots();
        }

    }

    /** Sets the game I am displaying to GAME. */
    void setGame(Game game) {
        _game = game;
        if (game != null) {
            System.out.println("Successfully set game!");
        }
    }

    /** Displays the load or save screen. */
    void displaySaveSlots() {
        System.out.println("I entered here.");
        removeAll();
        add(new JLabel("Please choose a save slot!"));
        for (int number = 1; number <= 10; number += 1) {
            JButton saveSlot = new JButton("Save Slot " + number);
            saveSlot.addActionListener(new SaveSlotListener(number));
            add(saveSlot);
        }
        JButton returnButton = new JButton("Return");
        returnButton.addActionListener(new SaveSlotListener(0));
        add(returnButton);
        updateUI();
    }

    @Override
    public synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    /** Contains the frame which displays everything. */
    private final JFrame frame;

    /** Contains the frame width and height. */
    private static final int WIDTH = 800, HEIGHT = 600;

    /** Contains the game I am displaying. */
    private Game _game;

}
