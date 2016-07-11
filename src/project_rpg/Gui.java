package project_rpg;

import project_rpg.enums.GameState;

import utils.SmartScroller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

/** Displays an interactive GUI for the game.
 *  @author S. Chewi, A. Tran
 */
public class Gui extends JPanel {

  /* CONSTRUCTORS. */

  /** Default constructor for the GUI. Sets up the graphical display. */
  public Gui() {
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

  /** Constructor for the GUI that additionally loads the game from a given save SLOT. */
  public Gui(int slot) {
    this();
    loadGame(slot);
  }

  /* ORDINARY METHODS. */

  /** Displays a menu bar at the bottom of the screen. */
  private void displayMenuBar() {
    frame.getContentPane().add(BorderLayout.SOUTH, menu);
  }

  /** Displays an option bar at the right-hand side of the screen. */
  private void displayOptionBar() {
    frame.getContentPane().add(BorderLayout.EAST, options);
  }

  /** Displays the load or save screen. CODE is either 0 for the load screen or -1 for the save
   *  screen.
   */
  private void displaySaveSlots(int code) {
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
  public boolean gameInitialized() {
    return game != null;
  }

  /** Produces a Game Over if the player dies. */
  protected void gameOver() {
    game = null;
    frame.getContentPane().removeAll();
    removeAll();
    frame.getContentPane().add(BorderLayout.CENTER, this);
    start();
    JOptionPane.showMessageDialog(
        frame,
        "You died! Start a new game or load an existing save file.",
        "Game Over!",
        JOptionPane.PLAIN_MESSAGE
    );
  }

  /** Returns the current game. */
  protected Game getGame() {
    return game;
  }

  /** Allows the player to go to different locations. */
  private void go() {
    removeAll();
    ArrayList<ShortcutButton> locations = new ArrayList<ShortcutButton>();
    locations.add(classroomButton);
    locations.add(gymButton);
    locations.add(returnButton);
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
  private void goClassroom() {
    game.setGameState(GameState.CLASS);
    options.setOptions(new ArrayList<ShortcutButton>());
  }

  /** Allows the player to go to the gym. */
  private void goGym() {
    game.setGameState(GameState.GYM);
    options.setOptions(new ArrayList<ShortcutButton>());
  }

  /** Hides all of the menu bars. */
  private void hideMenu() {
    frame.getContentPane().removeAll();
    frame.getContentPane().add(BorderLayout.CENTER, this);
    frame.repaint();
  }

  /** Initializes all buttons to their defaults. */
  private void initializeButtons() {
    // DummyListener() is a listener for changing skills so that the button doesn't return null.
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
    returnButton = new ShortcutButton("Return to Dorm (R)", 'r');
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
  private void keyPress(char key) {
    options.pressedKey(key);
  }

  /** Loads DUNGEON in COURSE. */
  private void loadAssignment(String dungeon, Course course) {
    if (game.getPlayer().noSkillsSet()) {
      updateMenuBar("You cannot enter a dungeon without setting your battle skills!");
      return;
    }
    removeAll();
    hideMenu();
    displayMenuBar();
    BattleGrid battle = new BattleGrid(this, game.getPlayer(), dungeon, course);
    add(battle);
    battle.start();
    audioHandler.playSong("intensity_teaser");
    battle.requestFocusInWindow();
  }

  /** Loads and starts a CUTSCENE from the name of the JSON file. */
  private void loadCutscene(String cutscene) {
    removeAll();
    Cutscene scenes = new Cutscene(cutscene);
    ArrayList<ShortcutButton> nextOptions = new ArrayList();
    nextLineButton = new ShortcutButton("Next (n)", 'n');
    nextLineButton.addActionListener(new NextLineListener(scenes));
    nextOptions.add(nextLineButton);
    options.setOptions(nextOptions);
    updateUI();
  }

  /** Loads DUNGEON and begins a battle. */
  private void loadDungeon(String dungeon) {
    if (game.getPlayer().noSkillsSet()) {
      updateMenuBar("You cannot enter a dungeon without setting your battle skills!");
      return;
    }
    removeAll();
    hideMenu();
    displayMenuBar();
    audioHandler.playSong("intensity_teaser");
    BattleGrid battle = new BattleGrid(this, game.getPlayer(), dungeon);
    add(battle);
    battle.start();
    battle.requestFocusInWindow();
  }

  /** Loads the game at the given SLOT. */
  protected void loadGame(int slot) {
    setGame(Game.loadGame(slot));
  }
  
  /** Refreshes the menu bar. */
  public void refreshMenu() {
    menu.repaint();
  }

  /** Allows the player to rest. */
  private void rest() {
    game.nextDay();
    game.getPlayer().restore();
    menu.repaint();
  }

  /** Allows the player to save the game. */
  private void save() {
    hideMenu();
    displaySaveSlots(-1);
  }

  /** Saves the current game in SLOT. */
  private void saveGame(int slot) {
    try {
      game.save(slot);
    } catch (IOException exception) {
      Main.error("Error while saving game.");
    }
  }

  /** Sets the game I am displaying to GAME. */
  private void setGame(Game game) {
    this.game = game;
  }

  /** Starts a new game session. */
  protected void start() {
    JLabel titleScreen = new JLabel(BattleGrid.getImage("title_screen"));
    add(titleScreen);
    JButton newGame = new JButton("New Game");
    newGame.addActionListener(new NewGameListener());
    add(newGame);
    JButton loadGame = new JButton("Load Game");
    loadGame.addActionListener(new LoadGameListener());
    add(loadGame);
    audioHandler = new AudioHandler();
    audioHandler.playSong("opening_sequence");
    frame.setVisible(true);
  }

  /** Used for testing purposes. The effect of this button changes. */
  private void test() {
    loadCutscene("test");
  }

  /** Adds MESSAGE to the Menu Bar. */
  protected void updateMenuBar(String message) {
    menu.addMessage(message);
  }

  /* PAINT METHODS FOR EACH GAME STATE. */

  @Override
  public synchronized void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);
    if (game == null || game.getState() == lastSeen) {
      return;
    }
    switch (game.getState()) {
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
        audioHandler.playSong("reflection");
        paintSchool();
        break;
      case SKILLS:
        paintSkills();
        break;
      default:
        Main.error("Invalid game state.");
        break;
    }
  }

  /** Renders the game for the class game state. */
  private void paintClass() {
    lastSeen = GameState.CLASS;
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

  /** Renders the game for the enrollment game state. Takes in the NUMBER of enrolled courses. */
  private void paintEnrollment(int number) {
    lastSeen = GameState.ENROLLMENT;
    removeAll();
    hideMenu();
    add(new JLabel("Please choose a course!"));
    Vector<Course> courseList = new Vector<Course>();
    Iterator<Course> availableCoursesIterator = game.getAvailableCoursesIterator();
    while (availableCoursesIterator.hasNext()) {
      courseList.add(availableCoursesIterator.next());
    }
    JComboBox<Course> courses = new JComboBox<Course>(courseList);
    add(courses);
    JTextArea courseDescription = new JTextArea(4, 30);
    courseDescription.setLineWrap(true);
    courseDescription.setEditable(false);
    courseDescription.setText(courseList.get(0).description());
    courses.addItemListener(new ClassSelectionListener(courseDescription));
    JScrollPane scroller = new JScrollPane(courseDescription);
    scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
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
  private void paintGym() {
    lastSeen = GameState.GYM;
    removeAll();
    ArrayList<ShortcutButton> gymOptions = new ArrayList();
    add(new JLabel("What would you like to do?"));
    gymOptions.add(workOutButton);
    gymOptions.add(returnButton);
    options.setOptions(gymOptions);
    ImageIcon icon = new ImageIcon("project_rpg" + File.separator + "resources"
        + File.separator + "DummyGym" + ".jpg"); 
    Image image = icon.getImage();
    Image newImage = image.getScaledInstance(700, 500, java.awt.Image.SCALE_SMOOTH);
    icon = new ImageIcon(newImage);
    JLabel background = new JLabel(icon, JLabel.CENTER);
    add(background);
    updateUI();
  }

  /** Renders the game for the school game state. */
  private void paintSchool() {
    lastSeen = GameState.SCHOOL;
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
  
  /** Renders the game to the view skills state. */
  private void paintSkills() {
    lastSeen = GameState.SKILLS;
    removeAll();
    add(new JLabel("Here are your skills."));
    Vector<Skill> skillList = new Vector<Skill>();
    Iterator<Skill> skillIterator = game.getPlayer().getSkillIterator();
    while (skillIterator.hasNext()) {
      skillList.add(skillIterator.next());
    }
    JComboBox<Skill> skills = new JComboBox<Skill>(skillList);
    Vector<Skill> battleSkillList = new Vector<Skill>();
    Iterator<Skill> battleSkillIterator = game.getPlayer().getBattleSkillIterator();
    while (battleSkillIterator.hasNext()) {
      battleSkillList.add(battleSkillIterator.next());
    }
    JComboBox<Skill> battleSkills = new JComboBox<Skill>(battleSkillList);
    add(skills);
    add(battleSkills);
    JTextArea skillDescription = new JTextArea(4, 30);
    skillDescription.setLineWrap(true);
    skillDescription.setEditable(false);
    Skill skill = skillList.get(0);
    skillDescription.setText(String.join(
        "\n",
        skill.description(),
        "Damage: " + skill.getDamage(),
        "MP Cost:" + skill.getCost()
    ));
    skills.addItemListener(new SkillSelectionListener(skillDescription));
    battleSkills.addItemListener(new SkillSelectionListener(
        skillDescription));
    JScrollPane scroller = new JScrollPane(skillDescription);
    scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
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
    changeSkills.add(returnButton);
    options.setOptions(changeSkills);
    updateUI();
  }

  /* LISTENER INNER CLASSES. */

  /** Class that listens for the ChangeSkill Button. */
  public class ChangeSkillListener implements ActionListener {

    /** Constructor that takes in battle skill NUMBER. */
    public ChangeSkillListener(int number, JComboBox<Skill> box) {
      number = number - 1;
      box = box;
    }

    @Override
    public void actionPerformed(ActionEvent ignored) {
      Skill skill = (Skill) box.getSelectedItem();
      game.getPlayer().changeBattleSkills(number, skill);
      lastSeen = null;
      paintSkills();
    }

    /** My skill number. */
    private int number;

    /** My box. */
    private JComboBox<Skill> box;

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
      char courseIndex = '0';
      Iterator<Course> enrolledCourses = game.getEnrolledCoursesIterator();
      ShortcutButton button;
      while (enrolledCourses.hasNext()) {
        Course course = enrolledCourses.next();
        button = new ShortcutButton(course.toString() + " (" + courseIndex + ")", courseIndex);
        button.addActionListener(new CourseSelectionListener(course));
        courses.add(button);
        courseIndex += 1;
      }
      courses.add(returnButton);
      options.setOptions(courses);
    }

  }

  /** Class that listens for the Course Selection button. */
  public class CourseSelectionListener implements ActionListener {

    /** Basic constructor that takes in a COURSE. */
    public CourseSelectionListener(Course course) {
      this.course = course;
    }

    @Override
    public void actionPerformed(ActionEvent ignored) {
      if (course.isReady()) {
        ArrayList<ShortcutButton> assignments = new ArrayList();
        Assignment assignment = course.getCurrentAssignment();
        ShortcutButton button = new ShortcutButton(assignment.getName() + " (S)", (char) 'S');
        button.addActionListener(new LoadAssignmentListener(assignment, course));
        assignments.add(button);
        assignments.add(returnButton);
        options.setOptions(assignments);
      } else {
        updateMenuBar("There are no more assignments for " + course + " this week.");
      }
    }

    /** My course. */
    private Course course;
  }
  
  /** Class that does nothing. */
  public class DummyListener implements ActionListener {
    
    @Override
    public void actionPerformed(ActionEvent ignored) {
      // Do nothing.
    }

  }

  /** Class that listens for class enrollment. */
  public class EnrollmentListener implements ActionListener {

    /** Constructor that takes in a BOX of courses and the NUMBER of enrolled courses. */
    public EnrollmentListener(JComboBox<Course> box, int number) {
      this.box = box;
      this.number = number;
    }

    @Override
    public void actionPerformed(ActionEvent ignored) {
      Course course = (Course) box.getSelectedItem();
      game.registerCourse(course);
      number += 1;
      if (number == Game.NUM_COURSES) {
        game.setGameState(GameState.SCHOOL);
        repaint();
      } else {
        paintEnrollment(number);
      }
    }

    /** Contains the number of classes I have enrolled in. */
    private int number;

    /**Contains the course that I am enrolling for. */
    private final JComboBox<Course> box;

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

    /** Loads ASSIGNMENT from COURSE. */
    public LoadAssignmentListener(Assignment assignment, Course course) {
      this.assignment = assignment;
      this.course = course;
    }

    @Override
    public void actionPerformed(ActionEvent ignored) {
      loadAssignment(assignment.getDungeon(), course);
    }

    /** The assignment that the listener will load. */
    private Assignment assignment;

    /** The course of the assignment. */
    private Course course;

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
  
  /** Class that listens for the Next Line button. */
  public class NextLineListener implements ActionListener {
    
    public NextLineListener(Cutscene cutscene) {
      this.cutscene = cutscene;
    }
    
    @Override
    public void actionPerformed(ActionEvent ignored) {
      if (cutscene.hasNext()) {
        removeAll();
        HashMap<String, String> scene = cutscene.next();
        ImageIcon icon = new ImageIcon("project_rpg" + File.separator + "resources"
            + File.separator + scene.get("image") + ".png"); 
        Image image = icon.getImage();
        Image newImage = image.getScaledInstance(700, 500, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(newImage);
        JLabel background = new JLabel(icon, JLabel.CENTER);
        add(background);
        updateMenuBar(scene.get("speaker") + ": " + scene.get("line"));
        ArrayList<ShortcutButton> nextOptions = new ArrayList();
        nextLineButton = new ShortcutButton("Next (n)", 'n');
        nextLineButton.addActionListener(new NextLineListener(cutscene));
        nextOptions.add(nextLineButton);
        options.setOptions(nextOptions);
        updateUI();
      } else {
        ArrayList<ShortcutButton> returnOptions = new ArrayList();
        returnOptions.add(returnButton);
        options.setOptions(returnOptions);
      }

    }
    
    /** My Cutscene. */
    Cutscene cutscene;

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
      if (lastSeen == GameState.SCHOOL) {
        lastSeen = null;
        repaint();
      } else {
        game.setGameState(GameState.SCHOOL);
        repaint();
      }
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
        Gui.this.removeAll();
        start();
      } else if (number == -1) {
        Gui.this.removeAll();
        displayMenuBar();
        displayOptionBar();
        paintSchool();
      } else {
        if (!gameInitialized()) {
          loadGame(number);
        } else {
          saveGame(number);
        }
        Gui.this.removeAll();
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

  /** Class that listens for the Skills button. */
  public class SkillsListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent ignored) {
      game.setGameState(GameState.SKILLS);
      paintSkills();
    }

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
      scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
      new SmartScroller(scroller);
      add(status);
      add(scroller);
      add(time);
      repaint();
    }

    /** Adds MESSAGE to the text box. */
    private void addMessage(String message) {
      notifications.append(message + "\n");
      repaint();
    }

    @Override
    public synchronized void paintComponent(Graphics graphics) {
      super.paintComponent(graphics);
      Player player = game.getPlayer();
      status.setText(String.format("HP: %s/%s\nMP: %s/%s",
          player.getHp(),
          player.getMaxHp(),
          player.getMp(),
          player.getMaxMp()
      ));
      time.setText(String.format(
          "Year: %s\nQuarter: %s\nWeek: %s\nDay: %s\n",
          game.getYear(),
          game.getQuarter(),
          game.getWeek(),
          game.getDay()
      ));
    }

    /** Contains the components of the menu bar. */
    private JTextArea notifications;
    private JTextArea status;
    private JTextArea time;

  }

  /** Inner class that displays an option bar on the right-hand side of the screen. Updates the
   *  options list dynamically.
   */
  public class OptionBar extends JPanel {

    /** Constructor that sets up the option bar. */
    public OptionBar() {
      setLayout(new GridLayout(0, 1));
      setPreferredSize(new Dimension(150, 450));
    }

    /** Responds to a key press of KEY. */
    public void pressedKey(char key) {
      for (ShortcutButton button : options) {
        if (key == button.getShortcut()) {
          button.doEvent();
        }
      }
    }

    /** Sets options to OPTIONSLIST. */
    private void setOptions(ArrayList<ShortcutButton> optionsList) {
      options = optionsList;
      removeAll();
      for (ShortcutButton button : optionsList) {
        add(button);
      }
      updateUI();
    }

    /** Contains my buttons. */
    private ArrayList<ShortcutButton> options;

  }

  /** Same as an ActionListener, but with an additional field that contains the shortcut key for
   *  the button.
   */
  public class ShortcutButton extends JButton {

    /** Constructor that takes in a MESSAGE for the button and the character
     *  of its SHORTCUT key.
     */
    public ShortcutButton(String message, char shortcut) {
      super(message);
      this.shortcut = shortcut;
    }

    /** Notifies the listener. */
    public void doEvent() {
      fireActionPerformed(new ActionEvent(this, 0, ""));
    }

    /** Returns my shortcut key. */
    public char getShortcut() {
      return shortcut;
    }

    /** Contains my shortcut key. */
    private char shortcut;

  }

  /* BUTTONS. */

  /** Lists of buttons. */
  private ArrayList<JButton> saveSlots;

  /** Individual buttons. */
  private JButton returnFromLoad;
  private JButton returnFromSave;

  /** Individual buttons with shortcut keys. */
  private ShortcutButton changeSkill1;
  private ShortcutButton changeSkill2;
  private ShortcutButton changeSkill3;
  private ShortcutButton changeSkill4;
  private ShortcutButton classroomButton;
  private ShortcutButton courseButton;
  private ShortcutButton goButton;
  private ShortcutButton gymButton;
  private ShortcutButton nextLineButton;
  private ShortcutButton restButton;
  private ShortcutButton returnButton;
  private ShortcutButton saveButton;
  private ShortcutButton skillsButton;
  private ShortcutButton testButton;
  private ShortcutButton workOutButton;

  /* CLASS FIELDS. */
  
  /** Handles music-related logic. */
  private AudioHandler audioHandler;

  /** Contains the game I am displaying. */
  private Game game;

  /** Contains the last seen game state. */
  private GameState lastSeen;

  /** Contains the frame width and height. */
  private static final int HEIGHT = 600;
  private static final int WIDTH = 800;

  /** Contains the frame which displays everything. */
  private final JFrame frame;

  /** Contains my menu bar. */
  private MenuBar menu;

  /** Contains my option bar. */
  private OptionBar options;

}

