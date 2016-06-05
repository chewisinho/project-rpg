package project_rpg.behaviors.spells;

import project_rpg.BattleGrid;
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
	super(image, x, y, grid);
	this.dirX = dirX;
	this.dirY = dirY;
    }

    /** The direction of movement of the spell. */
    private int dirX, dirY;

}

