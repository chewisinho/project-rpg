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
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
        initializeButtons();
        addKeyListener(new ShortcutKeyListener());
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

    /** Displays the load or save screen. CODE is either 0 for the load screen
     *  or -1 for the save screen.
     */
    void displaySaveSlots(int code) {
        removeAll();
        add(new JLabel("Please choose a save slot!"));
        for (JButton button : saveSlots) {
            add(button);
        }
        if (code == 0) {
            add(returnFromLoad);
        } else if (code == -1) {
            add(returnFromSave);
        }
        updateUI();
    }

    /** Returns true iff the game is already initialized. */
    boolean gameInitialized() {
        return _game == null;
    }

    /** Produces a Game Over if the player dies. */
    public void gameOver() {
        _game = null;
        frame.getContentPane().removeAll();
        removeAll();
        frame.getContentPane().add(BorderLayout.CENTER, this);
        start();
        JOptionPane.showMessageDialog(frame, "You died! Start a new game or "
            + "load an existing save file.", "Game Over!",
            JOptionPane.PLAIN_MESSAGE);
    }

    /** Allows the player to go to different locations. */
    void go() {
        removeAll();
        ArrayList<ShortcutButton> locations = new ArrayList<ShortcutButton>();
        locations.add(classroomButton);
        locations.add(gymButton);
        ImageIcon icon = new ImageIcon("project_rpg" + File.separator + "resources"
                + File.separator + "SchoolBackground" + ".jpg"); 
        Image image = icon.getImage();
        Image newImage = image.getScaledInstance(700, 500, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(newImage);
        JLabel background = new JLabel(icon, JLabel.CENTER);
        add(background);
        options.setOptions(locations);
    }

    /** Allows the player to go to the classroom. */
    void goClassroom() {
        _game.startClass();
        options.setOptions(new ArrayList<ShortcutButton>());
    }

    /** Allows the player to go to the gym. */
    void goGym() {
        _game.startGym();
        options.setOptions(new ArrayList<ShortcutButton>());
    }

    /** Hides all of the menu bars. */
    void hideMenu() {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(BorderLayout.CENTER, this);
        frame.repaint();
    }

    /** Initializes all buttons to their defaults. */
    void initializeButtons() {
        // DummyListener() is a listener for changing skills 
        // so that the button doesn't return null.
        changeSkill1 = new ShortcutButton("Change up skill (0)", '0');
        changeSkill1.addActionListener(new DummyListener());
        changeSkill2 = new ShortcutButton("Change down skill (1)", '1');
        changeSkill2.addActionListener(new DummyListener());
        changeSkill3 = new ShortcutButton("Change left skill (2)", '2');
        changeSkill3.addActionListener(new DummyListener());
        changeSkill4 = new ShortcutButton("Change right skill (3)", '3');
        changeSkill4.addActionListener(new DummyListener());
        
        classroomButton = new ShortcutButton("Classroom (0)", '0');
        classroomButton.addActionListener(new GoClassroomListener());
        courseButton = new ShortcutButton("Courses (C)", 'c');
        courseButton.addActionListener(new CourseListener());
        goButton = new ShortcutButton("Go (G)", 'g');
        goButton.addActionListener(new GoListener());
        gymButton = new ShortcutButton("Gym (1)", '1');
        gymButton.addActionListener(new GoGymListener());
        saveButton = new ShortcutButton("Save (S)", 's');
        saveButton.addActionListener(new SaveListener());
        skillsButton = new ShortcutButton("Skills (A)", 'a');
        skillsButton.addActionListener(new SkillsListener());
        restButton = new ShortcutButton("Rest (R)", 'r');
        restButton.addActionListener(new RestListener());
        returnButton = new ShortcutButton("Return (R)", 'r');
        returnButton.addActionListener(new ReturnListener());
        returnFromLoad = new JButton("Return");
        returnFromLoad.addActionListener(new SaveSlotListener(0));
        returnFromSave = new JButton("Return");
        returnFromSave.addActionListener(new SaveSlotListener(-1));
        testButton = new ShortcutButton("Test (T)", 't');
        testButton.addActionListener(new TestListener());
        workOutButton = new ShortcutButton("Work Out (W)", 'w');
        workOutButton.addActionListener(new WorkOutListener());
        saveSlots = new ArrayList<JButton>();
        for (int number = 1; number <= 10; number += 1) {
            JButton saveSlot = new JButton("Save Slot " + number);
            saveSlot.addActionListener(new SaveSlotListener(number));
            saveSlots.add(saveSlot);
        }
    }

    /** Responds to the press of a KEY. */
    void keyPress(char key) {
        options.pressedKey(key);
    }

    /** Loads DUNGEON and begins a battle. */
    void loadDungeon(String dungeon) {
        removeAll();
        hideMenu();
        displayMenuBar();
        BattleGrid battle = new BattleGrid(this, _game.getPlayer(), dungeon);
        add(battle);
        battle.requestFocusInWindow();
    }

    /** Refreshes the menu bar. */
    public void refreshMenu() {
        menu.repaint();
    }

    /** Allows the player to rest. */
    void rest() {
        _game.nextDay();
        _game.getPlayer().restore();
        menu.repaint();
    }

    /** Allows the player to save the game. */
    void save() {
        hideMenu();
        displaySaveSlots(-1);
    }

    /** Saves the current game in SLOT. */
    void saveGame(int slot) {
        try {
            _game.save(slot);
        } catch (IOException exception) {
            Main.error("Error while saving game.");
        }
    }

    /** Sets the game I am displaying to GAME. */
    void setGame(Game game) {
        _game = game;
    }

    /** Allows the player to view either battle skills or all skills. */
    void skills() {
        removeAll();
        add(new JLabel("Here are your skills."));
        Vector<Skill> skillList = new Vector<Skill>(
            _game.getPlayer().getSkills());
        JComboBox<Skill> skills = new JComboBox<Skill>(skillList);
        JComboBox<Skill> battleSkills = new JComboBox<Skill>(
                _game.getPlayer().getBattleSkills());
        add(skills);
        add(battleSkills);
        JTextArea skillDescription = new JTextArea(4, 30);
        skillDescription.setLineWrap(true);
        skillDescription.setEditable(false);
        skillDescription.setText(skillList.get(0).description() + "\n"
                + "Damage: " + skillList.get(0).getDamage()
                + "\n MP Cost:" + skillList.get(0).getCost());
        skills.addItemListener(new SkillSelectionListener(skillDescription));
        battleSkills.addItemListener(new SkillSelectionListener(
                skillDescription));
        JScrollPane scroller = new JScrollPane(skillDescription);
        scroller.setHorizontalScrollBarPolicy(
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(scroller);
        ArrayList<ShortcutButton> changeSkills = new ArrayList();
        changeSkills.add(changeSkill1);
        changeSkill1.removeActionListener(changeSkill1.getActionListeners()[0]);
        changeSkill1.addActionListener(new ChangeSkillListener(1, skills));
        changeSkills.add(changeSkill2);
        changeSkill2.removeActionListener(changeSkill2.getActionListeners()[0]);
        changeSkill2.addActionListener(new ChangeSkillListener(2, skills));
        changeSkills.add(changeSkill3);
        changeSkill3.removeActionListener(changeSkill3.getActionListeners()[0]);
        changeSkill3.addActionListener(new ChangeSkillListener(3, skills));
        changeSkills.add(changeSkill4);
        changeSkill4.removeActionListener(changeSkill4.getActionListeners()[0]);
        changeSkill4.addActionListener(new ChangeSkillListener(4, skills));
        options.setOptions(changeSkills);
        updateUI();
    }

    /** Starts a new game session. */
    void start() {
        JLabel titleScreen = new JLabel(BattleGrid.getImage("title_screen"));
        add(titleScreen);
        JButton newGame = new JButton("New Game");
        newGame.addActionListener(new NewGameListener());
        add(newGame);
        JButton loadGame = new JButton("Load Game");
        loadGame.addActionListener(new LoadGameListener());
        add(loadGame);
        frame.setVisible(true);
    }

    /** Used for testing purposes. The effect of this button changes. */
    void test() {
        removeAll();
        hideMenu();
        displayMenuBar();
        BattleGrid testDungeon = new BattleGrid(this, _game.getPlayer(), "test_dungeon");
        add(testDungeon);
        testDungeon.requestFocusInWindow();
    }

    /** Adds MESSAGE to the Menu Bar. */
    public void updateMenuBar(String message) {
    menu.addMessage(message);
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
            Main.error("Invalid game state.");
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
        ArrayList<ShortcutButton> classOptions = new ArrayList();
        add(courseButton);
        classOptions.add(courseButton);
        add(returnButton);
        classOptions.add(returnButton);
        ImageIcon icon = new ImageIcon("project_rpg" + File.separator + "resources"
                + File.separator + "ClassroomBackground" + ".jpg"); 
        Image image = icon.getImage();
        Image newImage = image.getScaledInstance(700, 500, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(newImage);
        JLabel background = new JLabel(icon, JLabel.CENTER);
        add(background);
        options.setOptions(classOptions);
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
        courseDescription.setEditable(false);
        courseDescription.setText(courseList.get(0).description());
        courses.addItemListener(new ClassSelectionListener(courseDescription));
        JScrollPane scroller = new JScrollPane(courseDescription);
        scroller.setHorizontalScrollBarPolicy(
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(scroller);
        ImageIcon icon = new ImageIcon("project_rpg" + File.separator + "resources"
                + File.separator + "DummyEnrollment" + ".jpg"); 
        Image image = icon.getImage();
        Image newImage = image.getScaledInstance(800, 400, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(newImage);
        JLabel background = new JLabel(icon, JLabel.CENTER);
        add(background);
        JButton enroll = new JButton("Enroll in this course!");
        enroll.addActionListener(new EnrollmentListener(courses, number));
        add(enroll);
        updateUI();
    }

    /** Renders the game for the gym game state. */
    void paintGym() {
        lastSeen = GYM;
        removeAll();
        ArrayList<ShortcutButton> gymOptions = new ArrayList();
        add(new JLabel("What would you like to do?"));
        gymOptions.add(workOutButton);
        gymOptions.add(returnButton);
        options.setOptions(gymOptions);
        updateUI();
    }

    /** Renders the game for the school game state. */
    void paintSchool() {
        lastSeen = SCHOOL;
        removeAll();
        ArrayList<ShortcutButton> schoolOptions = new ArrayList();
        add(new JLabel("What would you like to do?"));
        schoolOptions.add(goButton);
        schoolOptions.add(skillsButton);
        schoolOptions.add(restButton);
        schoolOptions.add(saveButton);
        schoolOptions.add(testButton);
        displayMenuBar();
        displayOptionBar();
        ImageIcon icon = new ImageIcon("project_rpg" + File.separator + "resources"
                + File.separator + "DummyDorm" + ".jpg"); 
        Image image = icon.getImage();
        Image newImage = image.getScaledInstance(650, 500, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(newImage);
        JLabel background = new JLabel(icon, JLabel.CENTER);
        add(background);
        options.setOptions(schoolOptions);
        updateUI();
    }

    /* LISTENER INNER CLASSES. */

    /** Class that listens for the ChangeSkill Button. */
    public class ChangeSkillListener implements ActionListener {

        /** Constructor that takes in battle skill NUMBER. */
        public ChangeSkillListener(int number, JComboBox<Skill> box) {
            _number = number - 1;
            _box = box;
        }

        @Override
        public void actionPerformed(ActionEvent ignored) {
            Skill skill = (Skill) _box.getSelectedItem();
            _game.getPlayer().changeBattleSkills(_number, skill);
            skills();
        }

        /** My skill number. */
        private int _number;

        /** My box. */
        private JComboBox<Skill> _box;

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

    /** Class that listens for the Courses button. */
    public class CourseListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ignored) {
            ArrayList<ShortcutButton> courses = new ArrayList();
            char i = '0';
            ShortcutButton button;
            for (Course course : _game.getEnrolledCourses()) {
                button = new ShortcutButton(course.toString() + " (" + i + ")", i);
                button.addActionListener(new CourseSelectionListener(course));
                courses.add(button);
                i += 1;
            }
            options.setOptions(courses);
        }

    }

    /** Class that listens for the Course Selection button. */
    public class CourseSelectionListener implements ActionListener {

        /** Basic constructor that takes in a COURSE. */
        CourseSelectionListener(Course course) {
            _course = course;
        }

        @Override
        public void actionPerformed(ActionEvent ignored) {
            if (_course.isReady()) {
                ArrayList<ShortcutButton> assignments = new ArrayList();
                Assignment assignment = _course.getCurrentAssignment();
                ShortcutButton button = new ShortcutButton(assignment.getName() + " (S)", (char) 'S');
                button.addActionListener(new LoadAssignmentListener(assignment));
                assignments.add(button);
                options.setOptions(assignments);
            } else {
                updateMenuBar("There are no more assignments for " + _course + " this week.");
            }
        }

        /** My course. */
        private Course _course;
    }
    
    /** Class that does nothing. */
    public class DummyListener implements ActionListener {
    	
    	@Override
    	public void actionPerformed(ActionEvent ignored) {
    	
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
            if (_number == Game.NUM_COURSES) {
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
            go();
        }

    }

    /** Class that listens for the Classroom button. */
    public class GoClassroomListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ignored) {
            goClassroom();
            repaint();
        }

    }

    /** Class that listens for the Gym button. */
    public class GoGymListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ignored) {
            goGym();
            repaint();
        }
    }

    /** Class that loads a selected assignment. */
    public class LoadAssignmentListener implements ActionListener {

        /** Loads ASSIGNMENT. */
        public LoadAssignmentListener(Assignment assignment) {
            _assignment = assignment;
        }

        @Override
        public void actionPerformed(ActionEvent ignored) {
            loadDungeon(_assignment.getDungeon());
        }

        /** The assignment that the listener will load. */
        private Assignment _assignment;

    }

    /** Class that listens for the Load Game button. */
    public class LoadGameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ignored) {
            displaySaveSlots(0);
        }

    }

    /** Class that listens for the New Game button. */
    public class NewGameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ignored) {
            Game game = new Game();
            setGame(game);
            paintEnrollment(0);
        }

    }

    /** Class that listens for the Rest button. */
    public class RestListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ignored) {
            rest();
            paintSchool();
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
            save();
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
            } else if (number == -1) {
                GUI.this.removeAll();
                displayMenuBar();
                displayOptionBar();
                paintSchool();
            } else {
                if (gameInitialized()) {
                    Game game = Game.loadGame(number);
                    setGame(game);
                } else {
                    saveGame(number);
                }
                GUI.this.removeAll();
                displayMenuBar();
                displayOptionBar();
                paintSchool();
            }
        }

        /** Contains the number of my button. */
        private int number;

    }

    /** Class that listens for the Return shortcut. */
    public class ShortcutKeyListener extends KeyAdapter {

        @Override
        public void keyTyped(KeyEvent event) {
            System.out.print("" + event.getKeyChar());
            if (event.getKeyChar() == '\u001B') {
                lastSeen = null;
                repaint();
            } else {
                keyPress(event.getKeyChar());
            }
        }

    }

    /** Class that listens for the Skills button. */
    public class SkillsListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ignored) {
            skills();
        }

    }
    
    /** Class that listens for class selection. */
    public class SkillSelectionListener implements ItemListener {

        /** Constructor that takes in a TEXTBOX to update. */
        public SkillSelectionListener(JTextArea textBox) {
            text = textBox;
        }

        @Override
        public void itemStateChanged(ItemEvent event) {
            text.setText(((Skill) event.getItem()).description() + "\n"
                    + "Damage: " + ((Skill) event.getItem()).getDamage()
                    + "\n MP Cost:" + ((Skill) event.getItem()).getCost());
        }

        /** The text box that I update. */
        private JTextArea text;

    }

    /** Class that listens for the Test button. */
    public class TestListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ignored) {
            test();
        }

    }

    /** Class that listens for the Work Out button. */
    public class WorkOutListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ignored) {
            test();
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
            notifications.setText("Welcome to Project RPG!\n");
            notifications.setEditable(false);
            notifications.setLineWrap(true);
            time = new JTextArea();
            JScrollPane scroller = new JScrollPane(notifications);
            scroller.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            scroller.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        new SmartScroller(scroller);
            add(status);
            add(scroller);
            add(time);
            repaint();
        }

    /** Adds MESSAGE to the text box. */
    void addMessage(String message) {
        notifications.append(message + "\n");
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
            setPreferredSize(new Dimension(150, 450));
        }

        /** Sets options to OPTIONSLIST. */
        public void setOptions(ArrayList<ShortcutButton> optionsList) {
            _options = optionsList;
            removeAll();
            for (ShortcutButton button : optionsList) {
                add(button);
            }
            updateUI();
        }

        /** Responds to a key press of KEY. */
        public void pressedKey(char key) {
            for (ShortcutButton button : _options) {
                if (key == button.getShortcut()) {
                    button.doEvent();
                }
            }
        }

        /** Contains my buttons. */
        private ArrayList<ShortcutButton> _options;

    }

    /** Same as an ActionListener, but with an additional field that contains
     *  the shortcut key for the button.
     */
    public class ShortcutButton extends JButton {

        /** Constructor that takes in a MESSAGE for the button and the character
         *  of its SHORTCUT key.
         */
        public ShortcutButton(String message, char shortcut) {
            super(message);
            _shortcut = shortcut;
        }

        /** Returns my shortcut key. */
        public char getShortcut() {
            return _shortcut;
        }

        /** Notifies the listener. */
        public void doEvent() {
            fireActionPerformed(new ActionEvent(this, 0, ""));
        }

        /** Contains my shortcut key. */
        private char _shortcut;

    }

    /* BUTTONS. */

    /** Individual buttons. */
    private JButton returnFromLoad, returnFromSave;

    /** Individual buttons with shortcut keys. */
    private ShortcutButton changeSkill1, changeSkill2, changeSkill3,
        changeSkill4, classroomButton, courseButton, goButton, gymButton,
        restButton, returnButton, saveButton, skillsButton, testButton,
        workOutButton;

    /** Lists of buttons. */
    private ArrayList<JButton> saveSlots;

    /* CLASS FIELDS. */

    /** Contains the frame which displays everything. */
    private final JFrame frame;

    /** Contains the game I am displaying. */
    private Game _game;

    /** Contains the last seen game state. */
    private GameState lastSeen;

    /** Contains my menu bar. */
    private MenuBar menu;

    /** Contains my option bar. */
    private OptionBar options;

    /** Contains the frame width and height. */
    private static final int WIDTH = 800, HEIGHT = 600;

}
