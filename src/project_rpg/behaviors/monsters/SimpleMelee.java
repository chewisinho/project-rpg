package project_rpg.behaviors.monsters;

import project_rpg.BattleGrid;
import project_rpg.Monster;
import project_rpg.behaviors.Token;

/** Default behavior for simple melee-based monsters.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class SimpleMelee extends Token {

    public SimpleMelee(BattleGrid grid, int x, int y, Monster monster) {
	super(grid, x, y, monster.getImage());
	_monster = monster;
    }

    @Override
    public void run() {
        while (true) {
	    if (_monster.isDead()) {
		disappear();
		break;
	    }
            if (System.currentTimeMillis() - lastAction > 500) {
                if (_grid.playerAdjacentTo(_x, _y)) {
                    _grid._player.reduceHealth(_grid.monsters.get(this).attack());
                    if (_grid._player.isDead()) {
                        _grid._gui.gameOver();
                        break;
                    }
                } else {
                    moveTowardsPlayer();
                }
                _grid._gui.refreshMenu();
                lastAction = System.currentTimeMillis();
            }
        }
    }

    /** Contains the monster I represent. */
    private Monster _monster;

}

