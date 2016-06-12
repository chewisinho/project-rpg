package project_rpg.behaviors;

import project_rpg.BattleGrid;
import project_rpg.Monster;
import project_rpg.Skill;
import project_rpg.behaviors.Token;

/** Controls the behavior for a spell that moves in a straight line.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class SkillToken extends Token {

    /** Creates a new spell with IMAGE at (X, Y) which moves in the direction (DIRX, DIRY) representing a SKILL. */
    public SkillToken(
            String image,
            int x,
            int y,
            BattleGrid grid,
            int dirX,
            int dirY,
            Skill skill
        ) {
        super(grid, x, y, image);
        this.dirX = dirX;
        this.dirY = dirY;
        this.skill = skill;
    }

    @Override
    public void run() {
        if (skill.behavior.equals("straightLine")) {
            straightLine();
        }
    }

    /** Controls behavior for spells that move in a straight line. */
    void straightLine() {
        while (true) {
            if (System.currentTimeMillis() - lastAction > 250) {
                int newX = _x + dirX, newY = _y + dirY;
                if (!_grid.inBounds(newX, newY)) {
                    disappear();
                    return;
                } else if (_grid.monsters.containsKey(_grid.map[newX][newY])) {
                    Monster monster = _grid.monsters.get(_grid.map[newX][newY]);
                    int damage = skill.attack();
                    monster.reduceHealth(damage);
                    _grid._gui.updateMenuBar("The spell did " + damage + " damage to "
                        + monster.getName()
                        + "! " + monster.getName()
                        + " has " + monster.getHP() + " HP remaining.");
                    disappear();
                    return;
                } else {
                    move(newX, newY);
                    lastAction = System.currentTimeMillis();
                }
            }
        }
    }

    /** The direction of movement of the spell. */
    private int dirX, dirY;

    /** The skill that the token represents. */
    private Skill skill;

}
