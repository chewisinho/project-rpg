package project_rpg;

/** Class that represents the exit of a dungeon.
 *  @author S. Chewi
 */
public class ExitToken extends Token {

  /** Creates a new exit on a GRID at position (POSITIONX, POSITIONY). */
  public ExitToken(BattleGrid grid, int positionX, int positionY) {
    super(grid, positionX, positionY, "exit");
  }

  @Override
  public boolean isExit() {
    return true;
  }

}

