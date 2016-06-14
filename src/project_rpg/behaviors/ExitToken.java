package project_rpg.behaviors;

import project_rpg.BattleGrid;
import project_rpg.behaviors.Token;

/** Class that represents the exit of a dungeon.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class ExitToken extends Token {

    public ExitToken(BattleGrid grid, int x, int y) {
        super(grid, x, y, "exit");
    }

    @Override
    public boolean isExit() {
        return true;
    }

}

