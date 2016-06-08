package project_rpg.behaviors.skills;

import project_rpg.BattleGrid;
import project_rpg.Monster;
import project_rpg.behaviors.Token;

/** Controls the behavior for a spell that moves in a straight line.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class StraightLine extends Token {

    /** Creates a new spell with IMAGE at (X, Y) which moves in the direction
     *  (DIRX, DIRY).
     */
    public StraightLine(
            String image,
            int x,
            int y,
            BattleGrid grid,
            int dirX,
	    int dirY
	) {
	super(grid, x, y, image);
	this.dirX = dirX;
	this.dirY = dirY;
    }

    @Override
    public void run() {
	while (true) {
	    if (System.currentTimeMillis() - lastAction > 250) {
		int newX = _x + dirX, newY = _y + dirY;
		if (!_grid.inBounds(newX, newY)) {
		    disappear();
		    return;
		} else if (_grid.monsters.containsKey(_grid.map[newX][newY])) {
		    Monster monster = _grid.monsters.get(_grid.map[newX][newY]);
		    monster.reduceHealth(5);
		    _grid._gui.updateMenuBar("The spell did 5 damage to "
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

}

