package project_rpg;

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
        }
    }

    /** Plays the game during registration. */
    private void playEnrollment() {
        System.out.println("Please select a course from the list below:");
        game.printAvailableCourses();
    }

    /** Plays the game during school. */
    private void playSchool() {
        // TODO
    }

    /** Plays the game during a battle. */
    private void playBattle() {
        // TODO
    }

    /** Retrieves the player's command. */
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
