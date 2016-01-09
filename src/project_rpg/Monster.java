package project_rpg;

import java.util.Random;

/** Represents a monster class.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class Monster {

    /** Simple no-argument constructor. */
    public void Monster() {
        hp = spread(HP);
        mp = spread(MP);
        damage = spread(DAMAGE);
    }

    /** Samples from a distribution with MEAN and STANDARD DEVIATION. */
    public static int spread(int mean, double standardDeviation) {
        return (int) (mean + standardDeviation * new Random().nextGaussian());
    }

    /** Samples from a distribution with MEAN, using the variability ratio. */
    public static int spread(int mean) {
        return spread(mean, VARIABILITY * mean);
    }

    /** Returns damage caused by my attack. */
    public int attack() {
        return spread(damage);
    }

    /** Cause DAMAGE to me. */
    public void reduceHealth(int damage) {
        hp -= damage;
    }

    /** Returns true iff I am dead. */
    public boolean isDead() {
        return hp <= 0;
    }

    /** Contains the monster's instance variables. */
    protected int hp, mp, damage;

    /** Contains the description of the monster. */
    protected static String name, description;

    /** Contains the monster's parameters. */
    protected static int HP, MP, DAMAGE;

    /** Contains the innate variability for a typical monster. */
    protected static final double VARIABILITY = 0.1, ATTACK_VARIABILITY = 0.25;

}
