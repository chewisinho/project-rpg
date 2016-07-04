package project_rpg;

/** Main class for Project PRG.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class Main {

  /** Initializes Project RPG. Ignores ARGS. */
  public static void main(String[] args) {
    new GUI().start();
  }

  /** Generic error used throughout Project RPG. Displays the error message MESSAGE. */
  public static void error(String message) {
    System.err.println(message);
    System.exit(1);
  }

}

