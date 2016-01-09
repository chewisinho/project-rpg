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

    /** Returns current MP. */
    public int getMP() {
        return currMP;
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

    /** Prints the status of the player. */
    public void status() {
        System.out.println("HP: " + currHP + "/" + maxHP);
        System.out.println("MP: " + currMP + "/" + maxMP);
    }

    /** Initial stat values. */
    private int currHP = 100, maxHP = 100, currMP = 100, maxMP = 100;

    /** The player's name. */
    private String name;

    /** An array of the player's skills. */
    private ArrayList<Skill> skills = new ArrayList<Skill>();

    /** An array of courses the player has taken. */
    private ArrayList<Course> pastCourses = new ArrayList<Course>();

    /** An array of assignments the player has completed. */
    private ArrayList<Assignment> pastAssignments = new ArrayList<Assignment>();

}
