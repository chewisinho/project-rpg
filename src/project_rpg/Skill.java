package project_rpg;

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

    /** Creates a new skill starting at rank 0 with BASEDAMAGE and BASEMP. Also
     *  takes in a NAME and DESCRIPTION.
     */
    public Skill(int baseDamage, int baseMP, String name, String description) {
        rank = 0;
        exp = (int) BASE_EXP;
        _baseDamage = baseDamage;
        _baseMP = baseMP;
        _name = name;
        _description = description;
        update();
    }

    /** Creates a new skill from a database LINE. */
    public Skill(String[] line) {
        this(Integer.parseInt(line[2]), Integer.parseInt(line[3]), line[0],
            line[1]);
    }

    /** Updates the damage of the skill. */
    protected void update() {
        damage = (int) (_baseDamage * Math.pow(SCALE, rank));
        mp = (int) (_baseMP * Math.pow(SCALE, rank));
    }

    /** Increases my rank. */
    private void rankUp() {
        rank += 1;
        System.out.printf("Congratulations, %s ranked up! It is now rank %s.\n",
            _name, rank);
        update();
    }

    /** Returns the amount of EXP needed for RANK. */
    private static int requiredEXP(int rank) {
        return (int) (BASE_EXP * Math.pow(EXP_SCALE, rank));
    }

    /** Adds POINTS to skill EXP, ranking up the skill if needed. */
    void increaseEXP(int points) {
        exp += points;
        if (rank <= 10) {
            if (exp >= requiredEXP(rank + 1)) {
                rankUp();
            }
        }
    }

    /** Allocates skill EXP after completing the assignments for WEEK. */
    void completedAssignments(int week) {
        increaseEXP(requiredEXP(week) - requiredEXP(week - 1));
    }

    /** Returns the damage caused by me. */
    public int attack() {
        return spread(damage);
    }

    /** Returns my name. */
    public String getName() {
        return _name;
    }

    /** Returns my MP cost. */
    public int getCost() {
        return mp;
    }

    /** Returns my description. */
    public String description() {
        return _name + ": " + _description;
    }

    /** Returns the skill NAME from the database. */
    public static Skill readSkill(String name) throws IOException {
        BufferedReader input = new BufferedReader(new FileReader(new File(
            "project_rpg" + File.separator + "database" + File.separator
            + "skills.db")));
        Skill result = null;
        String[] line;
        while (!(line = input.readLine().split("=="))[0].equals(name)) {
            continue;
        }
        input.close();
        result = new Skill(line);
        return result;
    }

    /** Contains the description of the skill. */
    protected String _name, _description;

    /** Contains the parameters of the skill. */
    protected int rank, damage, mp, _baseDamage, _baseMP, exp;

    /** Contains the scaling of the skill by rank. */
    public static final double SCALE = 1.2, BASE_EXP = 35.0, EXP_SCALE = 1.6;

}
