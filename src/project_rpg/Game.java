package project_rpg;

import java.util.ArrayList;
import project_rpg.courses.FireI;
import static project_rpg.Day.*;
import static project_rpg.GameState.*;
import static project_rpg.Quarter.*;
import static project_rpg.Year.*;

/** Contains the main game data for Project RPG.
 *  @author S. Chewi, A. Tran
 */
public class Game {

    /** Initializes a new game file. */
    public Game() {
        year = FRESHMAN;
        quarter = FALL;
        week = 1;
        day = MONDAY;
        gameState = ENROLLMENT;
        availableCourses = new ArrayList<Course>();
        availableCourses.add(new FireI());
        enrolledCourses = new ArrayList<Course>();
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

    /** Prints the available courses. */
    public void printAvailableCourses() {
        availableCourses.sort(Course.TITLE_COMPARATOR);
        int index = 0;
        for (Course course : availableCourses) {
            System.out.println(index + ": " + course.getTitle());
            index += 1;
        }
    }

    /** Registers the course at INDEX into the schedule. */
    void registerCourse(int index) {
        try {
            enrolledCourses.add(availableCourses.get(index));
            availableCourses.remove(index);
        } catch (IndexOutOfBoundsException exception) {
            throw new IllegalArgumentException();
        }
    }
    
    /** Views the COURSE's description. */
    void viewCourseDescription(int index) {
    	System.out.println(availableCourses.get(index).description());
    	System.out.println("Are you sure you want to enroll in this class?");
    }

    /** Starts school after the enrollment phase. */
    void startSchool() {
        gameState = SCHOOL;
    }

    /** Travels to classroom. */
    void startClass() {
        gameState = CLASS;
    }

    /** Returns the year the game is in. */
    public int getYear() {
        return year.ordinal();
    }

    /** Returns the quarter the game is in. */
    public int getQuarter() {
        return quarter.ordinal();
    }

    /** Returns the week the game is in. */
    public int getWeek() {
        return week;
    }

    /** Returns the day the game is in. */
    public int getDay() {
        return day.ordinal();
    }

    /** Returns the state of the game. */
    public GameState getState() {
        return gameState;
    }

    /** The year this game is in. */
    private Year year;

    /** The quarter this game is in. */
    private Quarter quarter;

    /** The week this game is in. */
    private Integer week;

    /** The day this game is in. */
    private Day day;

    /** The state that this game is in. */
    private GameState gameState;

    /** Contains a list of available and enrolled courses. */
    private ArrayList<Course> availableCourses, enrolledCourses;

}
