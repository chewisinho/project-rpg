package project_rpg.behaviors;

import project_rpg.BattleGrid;
import project_rpg.Monster;
import project_rpg.Skill;
import project_rpg.behaviors.Token;
import project_rpg.Player;

/** Controls the behavior for skills.
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
            _grid._player.reduceMana(skill.getCost());
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
                    int damage = skill.attack();
                    _grid.reduceHealth(newX, newY, damage, skill.getName());
                    disappear();
                    return;
                } else if (_grid.map[newX][newY] != null) {
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

