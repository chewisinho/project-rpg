package project_rpg;

/** Controls the behavior for skills.
 *  @author S. Chewi, A. Tran
 */
public class SkillToken extends Token {

  /** Creates a new spell with IMAGE at (POSITIONX, POSITIONY) which moves in the direction
   *  (DIRX, DIRY) representing a SKILL.
   */
  public SkillToken(
        String image,
        int positionX,
        int positionY,
        BattleGrid grid,
        int dirX,
        int dirY,
        Skill skill
  ) {
    super(grid, positionX, positionY, image);
    this.dirX = dirX;
    this.dirY = dirY;
    this.skill = skill;
  }

  /** Controls behavior for spells that don't attack. */
  private void nothing() {
    disappear();
  }

  @Override
  public void run() {
    if (skill.getBehavior().equals("straightLine")) {
      getGrid()._player.reduceMana(skill.getCost());
      straightLine();
    }
    if (skill.getBehavior().equals("nothing")) {
      getGrid()._player.reduceMana(skill.getCost());
      nothing();
    }
  }

  /** Controls behavior for spells that move in a straight line. */
  private void straightLine() {
    while (true) {
      if (isReadyForAction(250)) {
        int newX = getX() + dirX;
        int newY = getY() + dirY;
        if (!getGrid().inBounds(newX, newY)) {
          disappear();
          return;
        } else if (getGrid().monsters.containsKey(getGrid().map[newX][newY])) {
          int damage = skill.attack();
          getGrid().reduceHealth(newX, newY, damage, skill.getName());
          disappear();
          return;
        } else if (getGrid().map[newX][newY] != null) {
          disappear();
          return;
        } else {
          move(newX, newY);
          takeAction();
        }
      }
    }
  }
  
  /** The direction of movement of the spell. */
  private int dirX;
  private int dirY;

  /** The skill that the token represents. */
  private Skill skill;

}
