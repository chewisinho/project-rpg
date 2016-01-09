package project_rpg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import static project_rpg.Monster.*;

/** Basic representation of a skill.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class Skill {

    /** Creates a new skill starting at rank 0 with BASEDAMAGE and BASEMP. Also
     *  takes in a NAME and DESCRIPTION.
     */
    public Skill(int baseDamage, int baseMP, String name, String description) {
        rank = 0;
        _baseDamage = baseDamage;
        _baseMP = baseMP;
        _name = name;
        _description = _description;
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
        result = new Skill(line);
        return result;
    }

    /** Contains the description of the skill. */
    protected String _name, _description;

    /** Contains the parameters of the skill. */
    protected int rank, damage, mp, _baseDamage, _baseMP;

    /** Contains the scaling of the skill by rank. */
    public static final double SCALE = 1.2;

}
