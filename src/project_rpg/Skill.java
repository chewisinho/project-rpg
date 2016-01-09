package project_rpg;

import static project_rpg.Monster.*;

/** Basic representation of a skill.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class Skill {

    /** Creates a new skill starting at rank 0. */
    public Skill() {
        rank = 0;
        update();
    }

    /** Updates the damage of the skill. */
    protected void update() {
        damage = (int) (baseDamage * Math.pow(SCALE, rank));
        mp = (int) (baseMP * Math.pow(SCALE, rank));
    }

    /** Increases my rank. */
    protected void rankUp() {
        rank += 1;
        update();
    }

    /** Returns the damage caused by me. */
    public int attack() {
        return spread(damage);
    }

    /** Returns my name. */
    public String getName() {
        return name;
    }

    /** Returns my description. */
    public String description() {
        return name + ": " + description;
    }

    /** Contains the description of the skill. */
    private static String name, description;

    /** Contains the base damage of the skill. */
    private static int baseDamage, baseMP;

    /** Contains the parameters of the skill. */
    private int rank, damage, mp;

    /** Contains the scaling of the skill by rank. */
    public static final double SCALE = 1.2;

}
