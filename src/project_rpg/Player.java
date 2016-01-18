package project_rpg;

import java.io.Serializable;
import java.util.ArrayList;

/** Class representing a player.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class Player implements Serializable {

    /** Initializes new player given GIVENNAME. */
    public Player(String givenName) {
        name = givenName;
    }

    /** Initializes player without a name. */
    public Player() {
        this("Jimmy");
    }

    /** Returns current HP. */
    public int getHP() {
        return currHP;
    }

    /** Returns max HP. */
    int getMaxHP() {
        return maxHP;
    }

    /** Returns current MP. */
    public int getMP() {
        return currMP;
    }

    /** Returns max MP. */
    int getMaxMP() {
        return maxMP;
    }

    /** Reduces my health by AMOUNT. */
    void reduceHealth(int amount) {
        currHP -= amount;
    }

    /** Reduces my mana by AMOUNT. */
    void reduceMana(int amount) {
        currMP -= amount;
    }

    /** Increases my max health by AMOUNT. */
    void increaseHealth(int amount) {
        maxHP += amount;
    }

    /** Increases my max mana by AMOUNT. */
    void increaseMana(int amount) {
        maxMP += amount;
    }

    /** Returns true iff I am dead. */
    public boolean isDead() {
        return currHP <= 0;
    }

    /** Returns true iff I am out of MP. */
    public boolean isOutOfMana() {
        return currMP <= 0;
    }

    /** Restores HP and MP to max values. */
    public void restore() {
        currHP = maxHP;
        currMP = maxMP;
    }

    /** Adds NEWSKILL to the player. */
    public void addSkill(Skill newSkill) {
        skills.add(newSkill);
    }

    /** Adds NEWCOURSE to a list of taken courses. */
    public void addCourse(Course newCourse) {
        pastCourses.add(newCourse);
    }

    /** Adds NEWASSIGNMENT to a list of taken courses. */
    public void addAssignment(Assignment newAssignment) {
        pastAssignments.add(newAssignment);
    }

    /** Prints a list of the player's skills. */
    void printSkills() {
        int index = 0;
        for (Skill skill : skills) {
            System.out.printf("%s: %s (%s MP)\n", index, skill.getName(),
                skill.getCost());
            index += 1;
        }
    }

    /** Returns the skill at INDEX. */
    Skill getSkill(int index) {
        return skills.get(index);
    }

    /** Returns the player's skills. */
    ArrayList<Skill> getSkills() {
        return skills;
    }

    /** Returns battleSkills. */
    ArrayList<Skill> getBattleSkills() {
        return battleSkills;
    }

    /** Prints the status of the player. */
    public void status() {
        System.out.printf("You have %s/%s HP remaining.\n", currHP, maxHP);
        System.out.printf("You have %s/%s MP remaining.\n", currMP, maxMP);
    }

    /** Initial parameters. */
    private int currHP = 100, maxHP = 100, currMP = 100, maxMP = 100;

    /** The player's name. */
    private String name;

    /** An array of the player's skills. */
    private ArrayList<Skill> skills = new ArrayList<Skill>();

    /** An array of courses the player has taken. */
    private ArrayList<Course> pastCourses = new ArrayList<Course>();

    /** An array of assignments the player has completed. */
    private ArrayList<Assignment> pastAssignments = new ArrayList<Assignment>();

    /** An array of skills that the player can bring to battle. */
    private ArrayList<Skill> battleSkills = new ArrayList<Skill>();

}
