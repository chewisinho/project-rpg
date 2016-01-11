package project_rpg;

/** Main class for Project PRG.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class Main {

    /** Initializes Project RPG (with a GUI if ARGS[0] is --display). */
    public static void main(String[] args) {
        if (args[0].equals("--display")) {
            new GUI().start();
        } else {
            new TextInterpreter();
        }
    }

}
