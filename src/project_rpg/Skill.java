package project_rpg;

import static project_rpg.Monster.*;

/** Basic representation of a skill.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class Skill {

    /** Creates a new skill starting at rank 0. */
    public Skill(int baseDamage, int baseMP) {
        rank = 0;
        _baseDamage = baseDamage;
        _baseMP = baseMP;
        update();
    }

    /** Updates the damage of the skill. */
    protected void update() {
        damage = (int) (_baseDamage * Math.pow(SCALE, rank));
        mp = (int) (_baseMP * Math.pow(SCALE, rank));
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

    /** Returns my MP cost. */
    public int getCost() {
        return mp;
    }

    /** Returns my description. */
    public String description() {
        return name + ": " + description;
    }

    /** Contains the description of the skill. */
    protected String name, description;

    /** Contains the parameters of the skill. */
    protected int rank, damage, mp, _baseDamage, _baseMP;

    /** Contains the scaling of the skill by rank. */
    public static final double SCALE = 1.2;

}
