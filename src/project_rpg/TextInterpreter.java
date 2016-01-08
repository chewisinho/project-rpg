package project_rpg;

import java.util.ArrayList;
import java.util.Scanner;
import static project_rpg.GameState.*;

/** Text-based parser and interpreter for Project RPG.
 *  @author S. Chewi, A. Tran
 */
public class TextInterpreter {

    /** Initializes the TextInterpeter. */
    public TextInterpreter() {
        input = new Scanner(System.in);
        start();
    }

    /** Method that runs when the program is first started. */
    private void start() {
        System.out.println("Welcome to Project RPG Version 1.0!");
        System.out.println("Would you like to start a new game or load a "
            + "previous save file?");
        while (true) {
            String command = getInput();
            if (command.equals("new game")) {
                game = new Game();
                break;
            } else if (command.equals("load game")) {
                error("Sorry, not supported yet.");
            } else {
                System.out.println("Please enter 'new game' or 'load game.'");
            }
        }
        game.printGameInfo();
        play();
    }

    /** Plays the game. */
    private void play() {
        while (true) {
            GameState gameState = game.getState();
            switch (gameState) {
            case ENROLLMENT:
                playEnrollment();
                break;
            case SCHOOL:
                playSchool();
                break;
            case BATTLE:
                playBattle();
                break;
            case CLASS:
                playClass();
                break;
            default:
                error("Unknown game state!");
                break;
            }
        }
    }

    /** Plays the game during registration. */
    private void playEnrollment() {
        InputLoop:
        while (true) {
	        System.out.println("Please select a course from the list below to view"
	        		+ "its description.:");
	        game.printAvailableCourses();
	        int selection = Integer.parseInt(getInput());
	        while (true) {
		        game.viewCourseDescription(selection);
	        	String response = getInput();
	        	if (response.equals("yes")) {
	        		try {
	    	            game.registerCourse(selection);
	    	            break InputLoop;
	    	        } catch (IllegalArgumentException exception) {
	    	            System.out.println("Sorry, that is not a valid "
	    	            		+ "course.");
	    	        }
	        	} else if (response.equals("no")) {
	        		break;
	        	} else {
	        		System.out.println("Please enter yes or no.");
	        	}
	        }
        }
        game.startSchool();
    }

    /** Plays the game during school. */
    private void playSchool() {
        MainLoop:
        while (true) {
            System.out.println("What would you like to do?");
            String command = getInput();
            switch (command) {
            case "go":
                go();
                break MainLoop;
            case "courses":
            	courses();
            	break;
            default:
                System.out.println("Available commands:");
                System.out.println("go");
                System.out.println("courses");
                break;
            }
        }
    }

    /** Moves to a different location. */
    private void go() {
        MainLoop:
        while (true) {
            System.out.println("Where would you like to go?");
            System.out.println("0: Classroom");
            String command = getInput();
            switch (command) {
            case "0":
                game.startClass();
                break MainLoop;
            default:
                System.out.println("Sorry, that is an invalid location.");
                break;
            }
        }
    }
    
    /** Checks the courses the main character has. */
    private void courses() {
    	ArrayList<Course> courses = game.getEnrolledCourses();
    	for (Course course : courses) {
    		System.out.println(course.getTitle());
    	}
    }

    /** Plays the game during a battle. */
    private void playBattle() {
        // TODO
    }

    /** Plays the game during class. */
    private void playClass() {
        System.out.println("Congratulations, you're in class!");
    }

    /** Returns the player's command. */
    private String getInput() {
        System.out.print("-> ");
        return input.nextLine();
    }

    /** Errors out of the game with MESSAGE. */
    static void error(String message) {
        System.out.println(message);
        System.exit(1);
    }

    /** Reads in the user's input. */
    private Scanner input;

    /** The Game for which I interpret commands. */
    private Game game;

}
