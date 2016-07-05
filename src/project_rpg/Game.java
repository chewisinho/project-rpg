package project_rpg;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import project_rpg.enums.Day;
import project_rpg.enums.GameState;
import project_rpg.enums.Quarter;
import project_rpg.enums.Year;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

/** Contains the main game data for Project RPG.
 *  @author S. Chewi, A. Tran
 */
public class Game {

  /** Initializes a new game file. */
  public Game() {

    // Initialize the date, time, and game state.
    year = Year.FRESHMAN;
    quarter = Quarter.FALL;
    week = 1;
    day = Day.MONDAY;
    gameState = GameState.ENROLLMENT;

    // Initialize the courses.
    availableCourses = new ArrayList<Course>();
    availableCourses.add(new Course("introduction_to_fire_magic"));
    availableCourses.add(new Course("introduction_to_water_magic"));
    availableCourses.add(new Course("introduction_to_ice_magic"));
    availableCourses.add(new Course("introduction_to_lightning_magic"));
    availableCourses.add(new Course("introduction_to_earth_magic"));
    availableCourses.add(new Course("introduction_to_wind_magic"));
    if (availableCourses.size() == 0) {
      Main.error("The game is initialized with no available courses.");
    }
    enrolledCourses = new ArrayList<Course>();
    player = new Player();
  }

  /** Removes COURSE from the list of enrolled courses. */
  protected void finishCourse(Course course) {
    enrolledCourses.remove(course);
  }

  /** Returns an iterator over the available courses. */
  public Iterator<Course> getAvailableCoursesIterator() {
    return availableCourses.iterator();
  }

  /** Returns the enrolled course at INDEX. */
  public Course getEnrolledCourse(int index) {
    try {
      return enrolledCourses.get(index);
    } catch (IndexOutOfBoundsException exception) {
      throw new IllegalArgumentException();
    }
  }

  /** Returns an iterator over the enrolled courses. */
  public Iterator<Course> getEnrolledCoursesIterator() {
    return enrolledCourses.iterator();
  }

  /** Returns the day the game is in. */
  public Day getDay() {
    return day;
  }

  /** Returns the player of the game. */
  public Player getPlayer() {
    return player;
  }

  /** Returns the quarter the game is in. */
  public Quarter getQuarter() {
    return quarter;
  }

  /** Returns the state of the game. */
  public GameState getState() {
    return gameState;
  }

  /** Returns the week the game is in. */
  public int getWeek() {
    return week;
  }

  /** Returns the year the game is in. */
  public Year getYear() {
    return year;
  }

  /** Increments to the next day. */
  protected void nextDay() {
    day = day.next();
    if (day == Day.MONDAY) {
      nextWeek();
    }
  }

  /** Increments to the next quarter. */
  private void nextQuarter() {
    quarter = quarter.next();
    if (quarter == Quarter.FALL) {
      nextYear();
    }
    gameState = GameState.ENROLLMENT;
    enrolledCourses.clear();
  }

  /** Increments to the next week. */
  private void nextWeek() {
    if (week < 10) {
      for (Course course : enrolledCourses) {
        course.ready();
      }
      week += 1;
    } else {
      week = 1;
      nextQuarter();
    }
  }

  /** Increments to the next year. */
  private void nextYear() {
    year = year.next();
  }

  /** Registers the course at INDEX into the schedule. */
  protected void registerCourse(int index) {
    try {
      Course addCourse = availableCourses.get(index);
      enrolledCourses.add(addCourse);
      availableCourses.remove(index);
      player.addCourse(addCourse);
      player.addSkill(addCourse.getSkill());
    } catch (IndexOutOfBoundsException exception) {
      throw new IllegalArgumentException();
    }
  }

  /** Registers the COURSE into the schedule. */
  protected void registerCourse(Course course) {
    enrolledCourses.add(course);
    player.addCourse(course);
    player.addSkill(course.getSkill());
    availableCourses.remove(course);
  }

  /** Saves the game into SLOT. */
  protected void save(int slot) throws IOException {
    System.out.println(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(this));
    // ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(
    //   new File("save" + slot + ".sav")));
    // writer.writeObject(this);
    // writer.close();
  }

  /** Sets the current GameState to STATE. */
  protected void setGameState(GameState state) {
    gameState = state;
  }

  /** Returns the save file at INDEX. */
  public static Game loadGame(int index) {
    // Game saveFile = null;
    // try {
    //   ObjectInputStream reader = new ObjectInputStream(
    //     new FileInputStream(new File("save" + Integer.toString(index) + ".sav")));
    //   saveFile = (Game) reader.readObject();
    //   reader.close();
    // } catch (IOException exception) {
    //   Main.error("Sorry, could not load file.");
    // } catch (ClassNotFoundException exception) {
    //   Main.error("Sorry, corrupt or outdated save file.");
    // }
    // return saveFile;
    return null;
  }

  /** The temporal state of the game. */
  @Expose private Day day;
  @Expose private int week;
  @Expose private Quarter quarter;
  @Expose private Year year;

  /** Contains a list of available and enrolled courses. */
  private ArrayList<Course> availableCourses;
  private ArrayList<Course> enrolledCourses;

  /** The state that this game is in. */
  private transient GameState gameState;

  /** The main character in the game. */
  @Expose private Player player;

  /** The number of courses to enroll in every semester. */
  public static final int NUM_COURSES = 4;

}

