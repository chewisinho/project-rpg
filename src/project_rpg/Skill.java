package project_rpg;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

import static project_rpg.Monster.*;


/** Basic representation of a skill.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class Skill implements Serializable {

    /** Creates a new skill from the JSON file with filename NAME. */
    public Skill(String name) {
        try {
            BufferedReader input = new BufferedReader(new FileReader(new File(
                "project_rpg" + File.separator + "database" + File.separator
                + "skills" + File.separator + name + ".json")));
            JsonParser parser = new JsonParser();
            JsonObject attrTree = (JsonObject) parser.parse(input);
            _baseDamage = attrTree.get("_baseDamage").getAsInt();
            _baseMP = attrTree.get("_baseMP").getAsInt();
            _description = attrTree.get("_description").getAsString();
            _name = attrTree.get("_name").getAsString();
            behavior = attrTree.get("behavior").getAsString();
            input.close();
            rank = 0;
            exp = (int) BASE_EXP;
            update();
        } catch (IOException exception) {
            Main.error("Error while reading " + name + ".json.");
        }
    }

    /** Returns the damage caused by me. */
    public int attack() {
        return spread(damage);
    }

    /** Allocates skill EXP after completing the assignments for WEEK. */
    void completedAssignments(int week) {
        increaseEXP(requiredEXP(week) - requiredEXP(week - 1));
    }

    /** Returns my description. */
    public String description() {
        return _name + ": " + _description;
    }
    
    /** Returns my MP cost. */
    public int getCost() {
        return mp;
    }

    /** Returns my damage. */
    public int getDamage() {
        return damage;
    }

    /** Returns my name. */
    public String getName() {
        return _name;
    }

    /** Returns my rank. */
    public int getRank() {
        return rank;
    }

    /** Adds POINTS to skill EXP, ranking up the skill if needed. */
    void increaseEXP(int points) {
        exp += points;
        if (rank <= 10 && exp >= requiredEXP(rank + 1)) {
            rankUp();
        }
    }

    /** Increases my rank. */
    private void rankUp() {
        rank += 1;
        System.out.printf("Congratulations, %s ranked up! It is now rank %s.\n",
            _name, rank);
        update();
    }

    @Override
    public String toString() {
        return _name + " " + rank;
    }

    /** Updates the damage of the skill. */
    protected void update() {
        damage = (int) (_baseDamage * Math.pow(SCALE, rank));
        mp = (int) (_baseMP * Math.pow(SCALE, rank));
    }

    /** Returns the amount of EXP needed for RANK. */
    private static int requiredEXP(int rank) {
        return (int) (BASE_EXP * Math.pow(EXP_SCALE, rank));
    }

    /** Contains the parameters of the skill. */
    protected int _baseDamage, _baseMP, damage, exp, mp, rank;

    /** Contains the description of the skill. */
    public String _description, _name, behavior;

    /** Contains the scaling of the skill by rank. */
    public static final double BASE_EXP = 35.0, EXP_SCALE = 1.6, SCALE = 1.2;

}

