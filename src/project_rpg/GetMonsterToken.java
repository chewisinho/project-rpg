package project_rpg;

import project_rpg.behaviors.Token;
import project_rpg.behaviors.monsters.SimpleMelee;


public class GetMonsterToken {
    /** Utility class that returns a token corresponding to a monster.
     *  @author S. Chewi, T. Nguyen, A. Tran
     */

    public static Token getToken(BattleGrid grid, int x, int y, Monster monster) {
        /** Returns a token corresponding to the behavior of MONSTER. The token is initialized in GRID at (X, Y). */
        String behavior = monster.getBehavior();
        if (behavior == null) {
            return null;
        } else if (behavior.equals("SimpleMelee")) {
            return new SimpleMelee(grid, x, y, monster);
        } else {
            return null;
        }
    }
}

