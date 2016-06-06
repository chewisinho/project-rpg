package project_rpg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Random;

import com.google.gson.Gson;

/** Represents a monster class.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class Monster implements Serializable {

    /** Creates a monster with MONSTERHP, MONSTERMP, and MONSTERDAMAGE. Also
     *  takes in a NAME, DESCRIPTION, and ATTACKNAME.
     */
    public Monster(int monsterHP, int monsterMP, int monsterDamage, String name,
        String description, String attackName) {
        hp = spread(monsterHP);
        mp = spread(monsterMP);
        damage = spread(monsterDamage);
        _name = name;
        _description = description;
        _attackName = attackName;
    }

    /** Creates a monster from the database LINE. */
    public Monster(String[] line) {
        this(Integer.parseInt(line[2]), Integer.parseInt(line[3]),
            Integer.parseInt(line[5]), line[0], line[1], line[4]);
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
    public String getName() {
        return _name;
    }

    /** Returns the name of my attack. */
    String getAttackName() {
        return _attackName;
    }

    /** Returns the current HP level. */
    public int getHP() {
	return hp;
    }

    /** Returns a monster created from the JSON file NAME. */
    public static Monster readFromJson(String name) throws IOException {
	BufferedReader input = new BufferedReader(new FileReader(new File(
            "project_rpg" + File.separator + "database" + File.separator
            + "monsters" + File.separator + name + ".json")));
	Gson parser = new Gson();
        Monster monster = parser.fromJson(input, Monster.class);
	System.out.println(monster.hp);
        input.close();
	return monster;
    }

    /** Returns a monster with NAME read from the database file. */
    public static Monster readMonster(String name) throws IOException {
        BufferedReader input = new BufferedReader(new FileReader(new File(
            "project_rpg" + File.separator + "database" + File.separator
            + "monsters.db")));
        Monster result = null;
        String[] line;
        while (!(line = input.readLine().split("=="))[0].equals(name)) {
            continue;
        }
        input.close();
        result = new Monster(line);
        return result;
    }

    /** Contains the monster's instance variables. */
    protected int hp, mp, damage;

    /** Contains the description of the monster. */
    protected String _name, _description, _attackName;

    /** Contains the innate variability for a typical monster. */
    protected static final double VARIABILITY = 0.1, ATTACK_VARIABILITY = 0.25;

}

