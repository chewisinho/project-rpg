package project_rpg;

import java.util.StringJoiner;

/** Main class for Project PRG.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class Main {

  /** Generic error used throughout Project RPG. Displays the error message MESSAGE. */
  public static void error(String message) {
    System.err.println(message);
    System.exit(1);
  }

  /** Initializes Project RPG. Ignores ARGS. */
  public static void main(String[] args) {
    new GUI().start();
  }

}

