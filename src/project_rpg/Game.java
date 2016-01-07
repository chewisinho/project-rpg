package project_rpg;

import java.util.ArrayList;
import static project_rpg.GameState.*;

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
        availableCourses.sort(Course.titleComparator);
        for (Course course : availableCourses) {
            System.out.println(course.getTitle());
        }
    }

    private enum Year {
    	FRESHMAN(), SOPHOMORE(), JUNIOR(), SENIOR();
    	
    	/** Returns the next year. */
    	Year next() {
    		return values()[ordinal() + 1];
    	}
    }

    private enum Quarter {
    	FALL(), WINTER(), SPRING(), SUMMER();
    	
    	/** Returns the next year. */
    	Quarter next() {
    		return (this == SUMMER) ? FALL : values()[ordinal() + 1];
    	}
    }

    private enum Day {
    	MONDAY(), TUESDAY(), WEDNESDAY(), THURSDAY(), FRIDAY(), SATURDAY(), SUNDAY();
    	
    	/**Returns the next day. */
    	Day next() {
    		return (this == SUNDAY) ? MONDAY : values()[ordinal() + 1];
    	}
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

    /** Contains a list of available classes. */
    private ArrayList<Course> availableCourses;

}
