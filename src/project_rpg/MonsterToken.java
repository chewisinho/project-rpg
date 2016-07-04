package project_rpg;

/** Controls the behavior for monsters.
 *  @author S. Chewi
 */
public class MonsterToken extends Token {

  /** Creates a new token on a GRID at position (POSITIONX, POSITIONY) representing a MONSTER. */
  public MonsterToken(BattleGrid grid, int positionX, int positionY, Monster monster) {
    super(grid, positionX, positionY, monster.getImage());
    this.monster = monster;
  }

  /** Moves towards the player. */
  private void moveTowardsPlayer() {
    if (getGrid().playerToken.getX() > getX()) {
      right();
    } else if (getGrid().playerToken.getX() < getX()) {
      left();
    } else {
      if (getGrid().playerToken.getY() > getY()) {
        down();
      } else if (getGrid().playerToken.getY() < getY()) {
        up();
      }
    }
  }

  @Override
  public void run() {
    while (true) {
      if (monster.isDead()) {
        disappear();
        break;
      } else if (monster.getBehavior().equals("simpleMelee")) {
        simpleMelee();
      }
    }
  }

  /** Controls the behavior for a simple melee monster. */
  private void simpleMelee() {
    if (isReadyForAction(500)) {
      if (getGrid().playerAdjacentTo(getX(), getY())) {
        getGrid()._player.reduceHealth(getGrid().monsters.get(this).attack());
        if (getGrid()._player.isDead()) {
          disappear();
          getGrid()._gui.gameOver();
        }
      } else {
        moveTowardsPlayer();
      }
      getGrid()._gui.refreshMenu();
      takeAction();
    }
  }

  /** The monster that the token represents. */
  private Monster monster;

}

