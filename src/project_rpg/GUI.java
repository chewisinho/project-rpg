package project_rpg;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import static project_rpg.GameState.*;

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
                paintEnrollment();
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

    /** Class that listens for class selection. */
    public class ClassSelectionListener implements ItemListener {

        /** Constructor that takes in a TEXTBOX to update. */
        public ClassSelectionListener(JTextArea textBox) {
            text = textBox;
        }

        @Override
        public void itemStateChanged(ItemEvent event) {
            text.setText(((Course) event.getItem()).description());
        }

        /** The text box that I update. */
        private JTextArea text;

    }

    /** Class that listens for class enrollment. */
    public class EnrollmentListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ignored) {
        	_game.startSchool();
            repaint();
        }
        
        /** Contains the number of classes I have enrolled in. */
        private int number;

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
        if (_game == null || _game.getState() == lastSeen) {
            return;
        }
        switch (_game.getState()) {
        case BATTLE:
            break;
        case CLASS:
            break;
        case ENROLLMENT:
            paintEnrollment();
            break;
        case GYM:
        	paintGym();
            break;
        case SCHOOL:
        	paintSchool();
            break;
        default:
            TextInterpreter.error("Invalid game state.");
            break;
        }
    }

    /** Renders the game for the enrollment game state. */
    void paintEnrollment() {
        lastSeen = ENROLLMENT;
        removeAll();
        add(new JLabel("Please choose a course!"));
        Vector<Course> courseList = new Vector<Course>(
            _game.getAvailableCourses());
        JComboBox<Course> courses = new JComboBox<Course>(courseList);
        add(courses);
        JTextArea courseDescription = new JTextArea(4, 30);
        courseDescription.setLineWrap(true);
        courseDescription.setText(courseList.get(0).description());
        courses.addItemListener(new ClassSelectionListener(courseDescription));
        JScrollPane scroller = new JScrollPane(courseDescription);
        scroller.setHorizontalScrollBarPolicy(
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(scroller);
        JButton enroll = new JButton("Enroll in this course!");
        enroll.addActionListener(new EnrollmentListener());
        add(enroll);
        updateUI();
    }

    /** Renders the game for the class game state. */
    void paintClass() {
        // TODO
    }

    /** Renders the game for the battle game state. */
    void paintBattle() {
        // TODO
    }

    /** Renders the game for the gym game state. */
    void paintGym() {
        removeAll();
        add(new JLabel("What would you like to do?"));
        JButton workOutButton = new JButton("Work Out");
        JButton returnButton = new JButton("Return");
        workOutButton.addActionListener(new WorkOutListener());
        add(returnButton);
        add(workOutButton);
        updateUI();
    }

    /** Class that listens for the Work Out button. */
    public class WorkOutListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ignored) {
            _game.startGym();
            repaint();
        }

    }

    /** Class that listens for the Return button. */
    public class ReturnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ignored) {
            _game.startSchool();
            repaint();
        }

    }

    /** Renders the game for the school game state. */
    void paintSchool() {
        lastSeen = SCHOOL;
    	removeAll();
        System.out.println("I'm in school!");
        add(new JLabel("What would you like to do?"));
        JButton status = new JButton("Status");
        status.addActionListener(new StatusListener());
        add(status);
        JButton courses = new JButton("Courses");
        courses.addActionListener(new CoursesListener());
        add(courses);
        JButton go = new JButton("Go");
        go.addActionListener(new GoListener());
        add(go);
        JButton rest = new JButton("Rest");
        rest.addActionListener(new RestListener());
        add(rest);
        JButton save = new JButton("Save");
        save.addActionListener(new SaveListener());
        add(save);
        updateUI();
    }

    /** Class that listens for the Status button. */
    public class StatusListener implements ActionListener {
    	
    	@Override
    	public void actionPerformed(ActionEvent ignored) {
    		//TODO
    	}
    }
    
    /** Class that listens for the Courses button. */
    public class CoursesListener implements ActionListener {
    	
    	@Override
    	public void actionPerformed(ActionEvent ignored) {
    		//TODO
    	}
    }
    
    /** Class that listens for the Go button. */
    public class GoListener implements ActionListener {
    	
    	@Override
    	public void actionPerformed(ActionEvent ignored) {
    		//TODO
    	}
    }
    
    /** Class that listens for the Rest button. */
    public class RestListener implements ActionListener {
    	
    	@Override
    	public void actionPerformed(ActionEvent ignored) {
    		//TODO
    	}
    }
    
    /** Class that listens for the Save button. */
    public class SaveListener implements ActionListener {
    	
    	@Override
    	public void actionPerformed(ActionEvent ignored) {
    		//TODO
    	}
    }

    /** Contains the frame which displays everything. */
    private final JFrame frame;

    /** Contains the frame width and height. */
    private static final int WIDTH = 800, HEIGHT = 600;

    /** Contains the game I am displaying. */
    private Game _game;

    /** Contains the last seen game state. */
    private GameState lastSeen;

}
