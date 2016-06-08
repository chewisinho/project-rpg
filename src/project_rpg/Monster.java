package project_rpg;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Random;


/** Represents a monster class.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class Monster implements Serializable {

    /** Creates a new Monster with the following attributes: ATTACKNAME, DESCRIPTION, MONSTERDAMAGE, MONSTERHP,
     *  MONSTERMP, and NAME. Uses the spread method to introduce variability in its attributes.
     */
    public Monster(
            String attackName,
            String description,
            int monsterDamage,
            int monsterHP,
            int monsterMP,
            String name
        ) {
        _attackName = attackName;
        _description = description;
        _name = name;
        damage = spread(monsterDamage);
        hp = spread(monsterHP);
        mp = spread(monsterMP);
    }

    /** Returns damage caused by an attack. */
    public int attack() {
        return spread(damage);
    }

    /** Returns the attack name of the monster.. */
    String getAttackName() {
        return _attackName;
    }

    /** Returns the name of the monster. */
    public String getName() {
        return _name;
    }

    /** Returns the current HP level. */
    public int getHP() {
        return hp;
    }

    /** Returns true iff the monster is dead. */
    public boolean isDead() {
        return hp <= 0;
    }

    /** Causes DAMAGEAMOUNT to the monster. */
    public void reduceHealth(int damageAmount) {
        hp -= damageAmount;
    }

    /** Returns a monster created from the JSON file NAME. */
    public static Monster readFromJson(String name) throws IOException {
        BufferedReader input = new BufferedReader(new FileReader(new File(
            "project_rpg" + File.separator + "database" + File.separator
            + "monsters" + File.separator + name + ".json")));
        JsonParser parser = new JsonParser();
        JsonObject attrTree = (JsonObject) parser.parse(input);
        Monster monster = new Monster(
            attrTree.get("_attackName").getAsString(),
            attrTree.get("_description").getAsString(),
            attrTree.get("damage").getAsInt(),
            attrTree.get("hp").getAsInt(),
            attrTree.get("mp").getAsInt(),
            attrTree.get("_name").getAsString()
        );
        input.close();
        return monster;
    }

    /** Returns a sample from a Gaussian distribution with MEAN and STANDARDDEVIATION. */
    public static int spread(int mean, double standardDeviation) {
        return (int) (mean + standardDeviation * new Random().nextGaussian());
    }

    /** Returns a sample from a Gaussian distribution with MEAN, using the variability ratio. */
    public static int spread(int mean) {
        return spread(mean, VARIABILITY * mean);
    }

    /** Contains the monster's instance variables. */
    protected int damage, hp, mp;

    /** Contains the description of the monster. */
    protected String _attackName, _description, _name;

    /** Contains the innate variability for a typical monster. */
    protected static final double ATTACK_VARIABILITY = 0.25, VARIABILITY = 0.1;

}

