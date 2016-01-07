package project_rpg;

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
        
        
    }
    
    /** Prints the game state. */
    public void printGameState() {
    	System.out.println("Welcome back! Here's some info to refresh your "
    			+ "memory:");
    	System.out.println("Year: " + year);
    	System.out.println("Quarter: " + quarter);
    	System.out.println("Week: " + week);
    	System.out.println("Day: " + day);
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
    
    /** The year this game is in. */
    private Year year;
    
    /** The quarter this game is in. */
    private Quarter quarter;
    
    /** The week this game is in. */
    private Integer week;
    
    /** The day this game is in. */
    private Day day;

}
