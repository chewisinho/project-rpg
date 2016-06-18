package project_rpg.behaviors;

import project_rpg.BattleGrid;
import project_rpg.Monster;
import project_rpg.behaviors.Token;

/** Controls the behavior for monsters.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class MonsterToken extends Token {

    public MonsterToken(BattleGrid grid, int x, int y, Monster monster) {
        super(grid, x, y, monster.getImage());
        _monster = monster;
    }

    @Override
    public void run() {
        while (true) {
            if (_monster.isDead()) {
                disappear();
                break;
            } else if (_monster.getBehavior().equals("simpleMelee")) {
                simpleMelee();
            }
        }
    }

    /** Moves towards the player. */
    void moveTowardsPlayer() {
        if (_grid.playerToken.x() > _x) {
            right();
        } else if (_grid.playerToken.x() < _x) {
            left();
        } else {
            if (_grid.playerToken.y() > _y) {
                down();
            } else if (_grid.playerToken.y() < _y) {
                up();
            }
        }
    }

    /** Controls the behavior for a simple melee monster. */
    void simpleMelee() {
        if (System.currentTimeMillis() - lastAction > 500) {
            if (_grid.playerAdjacentTo(_x, _y)) {
                _grid._player.reduceHealth(_grid.monsters.get(this).attack());
                if (_grid._player.isDead()) {
                    disappear();
                    _grid._gui.gameOver();
                }
            } else {
                moveTowardsPlayer();
            }
            _grid._gui.refreshMenu();
            lastAction = System.currentTimeMillis();
        }
    }

    /** The monster that the token represents. */
    private Monster _monster;

}

