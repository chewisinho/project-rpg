package project_rpg;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import project_rpg.enums.Day;
import project_rpg.enums.GameState;
import project_rpg.enums.Quarter;
import project_rpg.enums.Year;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

  /** Returns a String array of course names from the given COURSES list. */
  protected String[] mapCoursesToFileNames(ArrayList<Course> courses) {
    return courses
        .stream()
        .map(course -> course.getFileName())
        .toArray(size -> new String[size]);
  }


  /** Saves the game into SLOT. */
  protected void save(int slot) throws IOException {
    player.prepareForSave();
    availableCourseNames = mapCoursesToFileNames(availableCourses);
    enrolledCourseNames = mapCoursesToFileNames(enrolledCourses);
    String json = new GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .setPrettyPrinting()
        .setVersion(2.0)
        .create()
        .toJson(this);
    BufferedWriter writer = Files.newBufferedWriter(Paths.get("save" + slot + ".sav.json"));
    writer.write(json);
    writer.close();
  }

  /** Sets the current GameState to STATE. */
  protected void setGameState(GameState state) {
    gameState = state;
  }

  /** Returns the save file at INDEX. */
  public static Game loadGame(int index) {
    Game game = null;
    try {
      String json = new String(Files.readAllBytes(Paths.get("save" + index + ".sav.json")));
      game = new GsonBuilder()
          .excludeFieldsWithoutExposeAnnotation()
          .registerTypeAdapter(Course.class, new Course.CourseDeserializer())
          .create()
          .fromJson(json, Game.class);
      game.availableCourses = new ArrayList<Course>();
      for (String fileName : game.availableCourseNames) {
        game.availableCourses.add(new Course(fileName));
      }
      game.enrolledCourses = new ArrayList<Course>();
      for (String fileName : game.enrolledCourseNames) {
        game.enrolledCourses.add(game.player.getCourseByFileName(fileName));
      }
      game.player.linkCoursesToSkills();
      game.player.updateBattleSkills();
      game.gameState = GameState.SCHOOL;
    } catch (IOException exception) {
      Main.error("Sorry, could not load the save file.");
    }
    return game;
  }

  /** The temporal state of the game. */
  @Expose private Day day;
  @Expose private int week;
  @Expose private Quarter quarter;
  @Expose private Year year;

  /** Contains a list of available and enrolled courses. */
  private ArrayList<Course> availableCourses;
  private ArrayList<Course> enrolledCourses;

  /** Contains the names of the available and enrolled courses for serialization. */
  @Expose private String[] availableCourseNames;
  @Expose private String[] enrolledCourseNames;

  /** The state that this game is in. */
  private transient GameState gameState;

  /** The main character in the game. */
  @Expose private Player player;

  /** The number of courses to enroll in every semester. */
  public static final int NUM_COURSES = 4;

}

