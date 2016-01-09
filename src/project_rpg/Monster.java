package project_rpg;

import java.util.Random;

/** Represents a monster class.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class Monster {

    /** Simple no-argument constructor. */
    public Monster(int monsterHP, int monsterMP, int monsterDamage) {
        hp = spread(monsterHP);
        mp = spread(monsterMP);
        damage = spread(monsterDamage);
    }

    /** Returns a sample from a Gaussian distribution with MEAN and
     *  STANDARDDEVIATION.
     */
    public static int spread(int mean, double standardDeviation) {
        return (int) (mean + standardDeviation * new Random().nextGaussian());
    }

    /** Returns a sample from a Gaussian distribution with MEAN, using the
     *  variability ratio.
     */
    public static int spread(int mean) {
        return spread(mean, VARIABILITY * mean);
    }

    /** Returns damage caused by my attack. */
    public int attack() {
        return spread(damage);
    }

    /** Cause DAMAGEAMOUNT to me. */
    public void reduceHealth(int damageAmount) {
        hp -= damageAmount;
    }

    /** Returns true iff I am dead. */
    public boolean isDead() {
        return hp <= 0;
    }

    /** Returns my name. */
    String getName() {
        return name;
    }

    /** Contains the monster's instance variables. */
    protected int hp, mp, damage;

    /** Contains the description of the monster. */
    protected static String name, description;

    /** Contains the innate variability for a typical monster. */
    protected static final double VARIABILITY = 0.1, ATTACK_VARIABILITY = 0.25;

}
