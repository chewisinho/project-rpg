package project_rpg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import static project_rpg.Day.*;
import static project_rpg.GameState.*;
import static project_rpg.Quarter.*;
import static project_rpg.Year.*;

/** Contains the main game data for Project RPG.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class Game implements Serializable {

    /** Initializes a new game file. */
    public Game() {

        // Initialize the date, time, and game state.
        year = FRESHMAN;
        quarter = FALL;
        week = 1;
        day = MONDAY;
        gameState = ENROLLMENT;

        // Initialize the courses.
        availableCourses = new ArrayList<Course>();
        availableCourses.add(new Course("introduction_to_fire_magic"));
        if (availableCourses.size() == 0) {
            Main.error("The game is initialized with no available courses.");
        }
        enrolledCourses = new ArrayList<Course>();
        player = new Player();
        for (int i = 0; i < player.getSkills().size() && i < player.getBattleSkills().length; i += 1) {
        	player.changeBattleSkills(i, player.getSkill(i));
        }
    }

    /** Clears the current assignment. */
    void clearAssignment() {
        currentAssignment = null;
    }

    /** Returns the current assignment. */
    Assignment getAssignment() {
        return currentAssignment;
    }

    /** Returns an array of available courses. */
    ArrayList<Course> getAvailableCourses() {
        return availableCourses;
    }

    /** Returns the enrolled course at INDEX. */
    Course getEnrolledCourse(int index) {
        try {
            return enrolledCourses.get(index);
        } catch (IndexOutOfBoundsException exception) {
            throw new IllegalArgumentException();
        }
    }

    /** Returns the list of enrolled courses of the game. */
    ArrayList<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    /** Returns the day the game is in. */
    public Day getDay() {
        return day;
    }

    /** Returns the player of the game. */
    Player getPlayer() {
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
    void nextDay() {
        day = day.next();
        if (day == MONDAY) {
            nextWeek();
        }
    }

    /** Increments to the next quarter. */
    void nextQuarter() {
        quarter = quarter.next();
        if (quarter == FALL) {
            nextYear();
        }
    }

    /** Increments to the next week. */
    void nextWeek() {
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
    void nextYear() {
        year = year.next();
    }

    /** Prints the available courses. */
    public void printAvailableCourses() {
        printCourses(availableCourses);
    }

    /** Prints the game state. */
    public void printGameInfo() {
        System.out.println("Welcome back! Here's some info to refresh your "
            + "memory:");
        System.out.println("Year: " + year);
        System.out.println("Quarter: " + quarter);
        System.out.println("Week: " + week);
        System.out.println("Day: " + day);
    }

    /** Prints the enrolled courses. */
    public void printEnrolledCourses() {
        printCourses(enrolledCourses);
    }

    /** Registers the course at INDEX into the schedule. */
    void registerCourse(int index) {
        try {
            Course addCourse = availableCourses.get(index);
            enrolledCourses.add(addCourse);
            availableCourses.remove(index);
            player.addCourse(addCourse);
            player.addSkill(addCourse.skill);
        } catch (IndexOutOfBoundsException exception) {
            throw new IllegalArgumentException();
        }
    }

    /** Registers the COURSE into the schedule. */
    void registerCourse(Course course) {
        enrolledCourses.add(course);
        player.addCourse(course);
        player.addSkill(course.skill);
        availableCourses.remove(course);
    }

    /** Saves the game into SLOT. */
    void save(int slot) throws IOException {
        ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(
            new File("save" + slot + ".sav")));
        writer.writeObject(this);
        writer.close();
    }

    /** Starts ASSIGNMENT. */
    void startBattle(Assignment assignment) {
        gameState = BATTLE;
        currentAssignment = assignment;
    }

    /** Travels to classroom. */
    void startClass() {
        gameState = CLASS;
    }

    /** Travels to Gym. */
    void startGym() {
        gameState = GYM;
    }

    /** Starts school after the enrollment phase. */
    void startSchool() {
        gameState = SCHOOL;
    }
    
    /** Game moves to change skills. */
    void startSkills() {
    	gameState = SKILLS;
    }

    /** Views the course description at INDEX. */
    void viewCourseDescription(int index) {
        try {
            System.out.println(availableCourses.get(index).description());
            System.out.println("Are you sure you want to enroll in this "
                + "class?");
        } catch (IndexOutOfBoundsException exception) {
            throw new IllegalArgumentException();
        }
    }

    /** Returns the save file at INDEX. */
    public static Game loadGame(int index) {
        Game saveFile = null;
        try {
            ObjectInputStream reader = new ObjectInputStream(
                new FileInputStream(new File("save"
                + Integer.toString(index) + ".sav")));
            saveFile = (Game) reader.readObject();
            reader.close();
        } catch (IOException exception) {
            Main.error("Sorry, could not load file.");
        } catch (ClassNotFoundException exception) {
            Main.error("Sorry, corrupt or outdated save file.");
        }
        return saveFile;
    }

    /** Prints the courses in COURSES. */
    public static void printCourses(ArrayList<Course> courses) {
        courses.sort(Course.TITLE_COMPARATOR);
        int index = 0;
        for (Course course : courses) {
            System.out.println(index + ": " + course.toString());
            index += 1;
        }
    }

    /** The year this game is in. */
    private Year year;

    /** The quarter this game is in. */
    private Quarter quarter;

    /** The week this game is in. */
    private int week;

    /** The day this game is in. */
    private Day day;

    /** The state that this game is in. */
    private GameState gameState;

    /** Contains a list of available and enrolled courses. */
    private ArrayList<Course> availableCourses, enrolledCourses;

    /** Contains the current assignment in the game. */
    private transient Assignment currentAssignment;

    /** The main character in the game. */
    private Player player;

    /** The number of courses to enroll in every semester. */
    public static final int NUM_COURSES = 1;

}

