package project_rpg;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/** Text-based parser and interpreter for Project RPG.
 *  @author S. Chewi, T. Nguyen, A. Tran
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
                try {
                    game = new Game();
                } catch (IOException exception) {
                    error("Error loading course data.");
                }
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
            case BATTLE:
                playAssignment();
                break;
            case CLASS:
                playClass();
                break;
            case ENROLLMENT:
                playEnrollment();
                break;
            case GYM:
                playGym();
                break;
            case SCHOOL:
                playSchool();
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
            case "status":
                status();
                break;
            default:
                System.out.println("Available commands: courses, go, rest, "
                    + "save, status.");
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
            System.out.println("1: Gym");
            String command = getInput();
            switch (command) {
            case "0":
                game.startClass();
                break MainLoop;
            case "1":
                game.startGym();
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
        game.getPlayer().restore();
        game.printGameInfo();
    }

    /** Shows the status of the player. */
    private void status() {
        game.getPlayer().status();
    }

    /** Plays the game during a battle consisting of MONSTERS. */
    private void playBattle(ArrayList<Monster> monsters) {
        Player player = game.getPlayer();
        while (!monsters.isEmpty()) {
            Battle battle = new Battle(player, monsters.get(0));
            while (true) {
                if (battle.isWon()) {
                    monsters.remove(0);
                    break;
                } else if (battle.isLost()) {
                    System.out.println("Sorry, you died! Tough luck.");
                    System.exit(0);
                } else if (player.isOutOfMana()) {
                    System.out.println("You're out of mana!");
                    System.out.println("You punched "
                        + monsters.get(0).getName() + " in the face.");
                    battle.punch();
                    break;
                }
                String index;
                SkillSelectionLoop:
                while (true) {
                    System.out.println("Select a skill:");
                    player.printSkills();
                    System.out.printf("You have %s/%s MP remaining.\n",
                        player.getMP(), player.getMaxMP());
                    index = getInput();
                    if (!index.matches("\\d+")) {
                        continue;
                    } else {
                        try {
                            battle.useSkill(Integer.parseInt(index));
                            break SkillSelectionLoop;
                        } catch (IndexOutOfBoundsException exception) {
                            continue SkillSelectionLoop;
                        }
                    }
                }
            }
        }
    }

    /** Plays an assignment. */
    private void playAssignment() {
        Assignment assignment = game.getAssignment();
        ArrayList<Monster> monsters = assignment.getMonsters();
        playBattle(monsters);
        game.startClass();
        assignment.getCourse().removeAssignment(assignment, game.getWeek());
        game.clearAssignment();
        System.out.println("Congratulations, you finished the assignment!");
    }

    /** Plays the game during class. */
    private void playClass() {
        MainLoop:
        while (true) {
            System.out.println("You are in class. What would you like to do?");
            String command = getInput();
            switch (command) {
            case "assignments":
                viewCourseAssignments();
                break MainLoop;
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
        MainLoop:
        while (true) {
            try {
                System.out.println("Which course's assignments would you like "
                    + "to view?");
                game.printEnrolledCourses();
                System.out.println("X: Return to previous screen.");
                String index = getInput();
                if (index.equals("x")) {
                    break MainLoop;
                } else if (!index.matches("\\d+")) {
                    continue MainLoop;
                }
                System.out.println("Would you like to complete an assignment?");
                ArrayList<Assignment> assignments = game.getEnrolledCourse(
                    Integer.parseInt(index)).getAssignments(game.getWeek());
                Assignment.printAssignments(assignments);
                System.out.println("X: Return to previous screen.");
                index = getInput();
                if (!index.matches("(\\d+)|X")) {
                    continue MainLoop;
                } else if (index.equals("x")) {
                    break MainLoop;
                } else {
                    game.startBattle(assignments.get(Integer.parseInt(index)));
                    break MainLoop;
                }
            } catch (IllegalArgumentException exception) {
                System.out.println("Sorry, that is not a valid course.");
            } catch (IndexOutOfBoundsException exception) {
                System.out.println("Sorry, that is not a valid assignment.");
            }
        }
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
                game.save(Integer.parseInt(selection));
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
            saveFile = Game.loadGame(Integer.parseInt(response));
            break;
        }
        return saveFile;
    }

    /** Plays the game during gym. */
    private void playGym() {
        MainLoop:
        while (true) {
            System.out.println("You are at the gym. "
                + "What would you like to do?");
            String command = getInput();
            switch (command) {
            case "return":
                goBack();
                break MainLoop;
            case "work out":
                workOut();
                break MainLoop;
            default:
                System.out.println("Available commands: return, work out.");
                break;
            }
        }
    }

    /** Increases the player's HP. */
    private void workOut() {
        Mainloop:
        while (true) {
            Player player = game.getPlayer();
            try {
                Monster arnold = Monster.readMonster("Arnold The Governator");
                ArrayList<Monster> monsters = new ArrayList<Monster>();
                monsters.add(arnold);
                playBattle(monsters);
            } catch (IOException exception) {
                error("Can't find Arnie");
            }
            player.increaseHealth(20);
            player.increaseMana(20);
            System.out.println("Workout completed! "
                    + "You increased your max health by 20 "
                    + "and your max mana by 20!");
            break Mainloop;
        }
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
