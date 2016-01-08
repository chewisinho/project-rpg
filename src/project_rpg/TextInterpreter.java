package project_rpg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
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
                game = loadFile();
                break;
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
            try {
                System.out.println("Please select a course from the list below "
                    + "to view its description and enroll:");
                game.printAvailableCourses();
                int selection = Integer.parseInt(getInput());
                while (true) {
                    game.viewCourseDescription(selection);
                    String response = getInput();
                    if (response.equals("yes")) {
                        game.registerCourse(selection);
                        break InputLoop;
                    } else if (response.equals("no")) {
                        break;
                    } else {
                        System.out.println("Please enter yes or no.");
                    }
                }
            } catch (IllegalArgumentException exception) {
                System.out.println("Sorry, that is not a valid course.");
            }
        }
        game.startSchool();
    }

    /** Plays the game during school. */
    private void playSchool() {
        MainLoop:
        while (true) {
            System.out.println("You are at school. What would you like to do?");
            String command = getInput();
            switch (command) {
            case "courses":
                courses();
                break;
            case "go":
                go();
                break MainLoop;
            case "rest":
                rest();
                break;
            case "save":
                save();
                break MainLoop;
            default:
                System.out.println("Available commands: courses, go, rest, "
                    + "save.");
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

    /** Returns to the main screen. */
    private void goBack() {
        game.startSchool();
    }

    /** Checks the courses the main character has. */
    private void courses() {
        ArrayList<Course> courses = game.getEnrolledCourses();
        for (Course course : courses) {
            System.out.println(course.description());
        }
    }

    /** Rests for a day. */
    private void rest() {
        game.nextDay();
        if (game.getDay() == 0) {
            game.nextWeek();
            if (game.getWeek() == 1) {
                game.nextQuarter();
                if (game.getQuarter() == 0) {
                    game.nextYear();
                }
            }
        }
        game.printGameInfo();
    }

    /** Plays the game during a battle. */
    private void playBattle() {
        // TODO
    }

    /** Plays the game during class. */
    private void playClass() {
        MainLoop:
        while (true) {
            System.out.println("You are in class. What would you like to do?");
            String command = getInput();
            switch (command) {
            case "go":
                go();
                break MainLoop;
            case "assignments":
                viewCourseAssignments();
                break;
            case "return":
                goBack();
                break MainLoop;
            default:
                System.out.println("Available commands: assignments, return.");
                break;
            }
        }
    }

    /** Allows the player to view assignments for this course. */
    private void viewCourseAssignments() {
        System.out.println("No assignments yet");
    }

    /** Saves the state of the game. */
    private void save() {
        boolean[] usedSlots = new boolean[10];
        for (int slot = 0; slot < 10; slot += 1) {
            usedSlots[slot] = Files.exists(Paths.get("save" + slot + ".sav"));
        }
        MainLoop:
        while (true) {
            System.out.println("Please choose a save slot.");
            for (int slot = 0; slot < 10; slot += 1) {
                System.out.printf("SAVE %s: %s\n", slot, usedSlots[slot]
                    ? "SAVE FILE EXISTS" : "EMPTY SLOT");
            }
            String selection = getInput();
            if (!selection.matches("[0-9]")) {
                continue;
            } else if (usedSlots[Integer.parseInt(selection)]) {
                while (true) {
                    System.out.println("Are you sure you want to override your "
                        + "previous save file?");
                    String response = getInput();
                    if (response.equals("no")) {
                        continue MainLoop;
                    } else if (!response.equals("yes")) {
                        System.out.println("Please enter yes or no.");
                    } else {
                        break;
                    }
                }
            }
            try {
                ObjectOutputStream writer = new ObjectOutputStream(
                    new FileOutputStream(new File("save"
                    + Integer.parseInt(selection)) + ".sav"));
                writer.writeObject(game);
                writer.close();
                System.out.println("Quit now?");
                while (true) {
                    String response = getInput();
                    if (response.equals("yes")) {
                        System.exit(0);
                    } else if (response.equals("no")) {
                        return;
                    } else {
                        System.out.println("Please enter yes or no.");
                    }
                }
            } catch (IOException exception) {
                error("Error saving game file.");
            }
        }
    }

    /** Returns the game object by loading in a file. */
    private Game loadFile() {
        boolean[] usedSlots = new boolean[10];
        for (int slot = 0; slot < 10; slot += 1) {
            usedSlots[slot] = Files.exists(Paths.get("save" + slot + ".sav"));
        }
        Game saveFile = null;
        while (true) {
            System.out.println("Choose a save file to load.");
            for (int slot = 0; slot < 10; slot += 1) {
                System.out.printf("SAVE %s: %s\n", slot, usedSlots[slot]
                    ? "SAVE FILE EXISTS" : "EMPTY SLOT");
            }
            String response = getInput();
            if (!response.matches("[0-9]")
                || !usedSlots[Integer.parseInt(response)]) {
                System.out.println("Not a valid save file.");
                continue;
            }
            try {
                ObjectInputStream reader = new ObjectInputStream(
                    new FileInputStream(new File("save"
                    + Integer.parseInt(response)) + ".sav"));
                saveFile = (Game) reader.readObject();
                reader.close();
                break;
            } catch (IOException exception) {
                error("Sorry, could not load file.");
            } catch (ClassNotFoundException exception) {
                error("Sorry, corrupt or outdated save file.");
            }
        }
        return saveFile;
    }

    /** Returns the player's command. */
    private String getInput() {
        System.out.print("-> ");
        return input.nextLine().toLowerCase();
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
