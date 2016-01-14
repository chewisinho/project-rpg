package project_rpg;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

    /* CONSTRUCTORS. */

    /** Default constructor for the GUI. Sets up the graphical display. */
    public GUI() {
        frame = new JFrame("Project RPG Version 2.0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.getContentPane().add(BorderLayout.CENTER, this);
        menu = new MenuBar();
        options = new OptionBar();
        addKeyListener(new RestKeyListener());
        setFocusable(true);
        requestFocusInWindow();
    }

    /* ORDINARY METHODS. */

    /** Displays a menu bar at the bottom of the screen. */
    void displayMenuBar() {
        frame.getContentPane().add(BorderLayout.SOUTH, menu);
    }

    /** Displays an option bar at the right-hand side of the screen. */
    void displayOptionBar() {
        frame.getContentPane().add(BorderLayout.EAST, options);
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

    /** Sets the game I am displaying to GAME. */
    void setGame(Game game) {
        _game = game;
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

    /* PAINT METHODS FOR EACH GAME STATE. */

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
            paintClass();
            break;
        case ENROLLMENT:
            paintEnrollment(0);
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

    /** Renders the game for the battle game state. */
    void paintBattle() {
        // TODO
    }

    /** Renders the game for the class game state. */
    void paintClass() {
        lastSeen = CLASS;
        removeAll();
        JButton courses = new JButton("Courses");
        courses.addActionListener(new CoursesListener());
        add(courses);
        System.out.print("I'm in class!");
        updateUI();
    }

    /** Renders the game for the enrollment game state. Takes in the NUMBER of
     *  enrolled courses.
     */
    void paintEnrollment(int number) {
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
        enroll.addActionListener(new EnrollmentListener(courses, number));
        add(enroll);
        updateUI();
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

    /** Renders the game for the school game state. */
    void paintSchool() {
        lastSeen = SCHOOL;
        removeAll();
        ArrayList<JButton> schoolOptions = new ArrayList<JButton>();
        add(new JLabel("What would you like to do?"));
        JButton go = new JButton("Go");
        go.addActionListener(new GoListener());
        schoolOptions.add(go);
        JButton rest = new JButton("Rest");
        rest.addActionListener(new RestListener());
        schoolOptions.add(rest);
        JButton save = new JButton("Save");
        save.addActionListener(new SaveListener());
        schoolOptions.add(save);
        displayMenuBar();
        displayOptionBar();
        options.setOptions(schoolOptions);
        updateUI();
    }

    /* LISTENER INNER CLASSES. */

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

    /** Class that listens for the Courses button. */
    public class CoursesListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ignored) {
            //TODO
        }

    }

    /** Class that listens for class enrollment. */
    public class EnrollmentListener implements ActionListener {

        /** Constructor that takes in a BOX of courses and the NUMBER of
         *  enrolled courses.
         */
        EnrollmentListener(JComboBox<Course> box, int number) {
            _box = box;
            _number = number;
        }

        @Override
        public void actionPerformed(ActionEvent ignored) {
            Course course = (Course) _box.getSelectedItem();
            _game.registerCourse(course);
            _number += 1;
            if (_number == 4) {
                _game.startSchool();
                repaint();
            } else {
                paintEnrollment(_number);
            }
        }

        /** Contains the number of classes I have enrolled in. */
        private int _number;

        /**Contains the course that I am enrolling for. */
        private final JComboBox<Course> _box;

    }

    /** Class that listens for the Go button. */
    public class GoListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ignored) {
            ArrayList<JButton> locations = new ArrayList<JButton>();
            JButton classroom = new JButton("Classroom");
            classroom.addActionListener(new GoClassroomListener());
            locations.add(classroom);
            options.setOptions(locations);
        }

    }

    /** Class that listens for the Classroom button. */
    public class GoClassroomListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ignored) {
            _game.startClass();
            options.setOptions(new ArrayList<JButton>());
            repaint();
        }

    }

    /** Class that listens for the Load Game button. */
    public class LoadGameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ignored) {
            displaySaveSlots();
        }

    }

    /** Class that listens for the New Game button. */
    public class NewGameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ignored) {
            try {
                Game game = new Game();
                setGame(game);
                paintEnrollment(0);
            } catch (IOException exception) {
                TextInterpreter.error("Could not load courses.");
            }
        }

    }

    /** Class that listens for the Rest button. */
    public class RestListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ignored) {
            _game.nextDay();
            _game.getPlayer().restore();
            paintSchool();
        }

    }
    
    /** Class that listens for the Rest shortcut. */
    public class RestKeyListener extends KeyAdapter {

        @Override
        public void keyTyped(KeyEvent event) {
            if (event.getKeyChar() == '\u0072') {
                _game.nextDay();
                _game.getPlayer().restore();
                menu.repaint();
                paintSchool();
            }
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

    /** Class that listens for the Save button. */
    public class SaveListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ignored) {
            //TODO
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

    /** Class that listens for the Work Out button. */
    public class WorkOutListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ignored) {
            _game.startGym();
            repaint();
        }

    }

    /* OTHER INNER CLASSES. */

    /** Inner class that displays a menu bar at the bottom of the screen. */
    public class MenuBar extends JPanel {

        /** No-argument constructor. */
        public MenuBar() {
            status = new JTextArea();
            notifications = new JTextArea(6, 40);
            notifications.setText("Welcome to Project RPG!");
            time = new JTextArea();
            JScrollPane scroller = new JScrollPane(notifications);
            scroller.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            scroller.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            add(status);
            add(scroller);
            add(time);
            repaint();
        }

        @Override
        public synchronized void paintComponent(Graphics g) {
            super.paintComponent(g);
            Player player = _game.getPlayer();
            status.setText(String.format("HP: %s/%s\nMP: %s/%s",
                player.getHP(), player.getMaxHP(), player.getMP(),
                player.getMaxMP()));
            time.setText(String.format("Year: %s\nQuarter: %s\nWeek: %s\nDay:"
                + " %s", _game.getYear(), _game.getQuarter(), _game.getWeek(),
                _game.getDay()));
        }

        /** Contains the components of the menu bar. */
        private JTextArea status, notifications, time;

    }

    /** Inner class that displays an option bar on the right-hand side of the
     *  screen. Updates the options list dynamically.
     */
    public class OptionBar extends JPanel {

        /** Constructor that sets up the option bar. */
        public OptionBar() {
            setLayout(new GridLayout(0, 1));
            setPreferredSize(new Dimension(100, 450));
        }

        /** Sets options to OPTIONSLIST. */
        public void setOptions(ArrayList<JButton> optionsList) {
            removeAll();
            for (JButton button : optionsList) {
                add(button);
            }
            updateUI();
        }

    }

    /* STATIC HELPER METHODS. */

    /** Returns an Image from NAME. */
    ImageIcon getImage(String name) {
        return new ImageIcon("project_rpg" + File.separator + "database"
            + File.separator + name);
    }

    /* CLASS FIELDS. */

    /** Contains the frame which displays everything. */
    private final JFrame frame;

    /** Contains the frame width and height. */
    private static final int WIDTH = 800, HEIGHT = 600;

    /** Contains the game I am displaying. */
    private Game _game;

    /** Contains the last seen game state. */
    private GameState lastSeen;

    /** Contains my menu bar. */
    private MenuBar menu;

    /** Contains my option bar. */
    private OptionBar options;

}
