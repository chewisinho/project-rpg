package project_rpg.monsters;

import project_rpg.Monster;

/** Simple ice cube monster for Fire I dungeon.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class IceCube extends Monster {

    /** No-argument constructor. */
    public IceCube() {
        super(25, 0, 5);
    }

    {
        name = "Ice Cube";
        description = "An animated ice cube that can tackle you.";
    }

}
