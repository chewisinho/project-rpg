package project_rpg;

import java.io.Serializable;
/** Class representing a player.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class Player implements Serializable {

    /** Initializes new player given NAME. */
    public void Player(String name) {
        this.name = name;
    }

    /** Initializes player without name */
    public void Player() {
        this("Jimmy");
    }

    /** Returns current HP */
    public int getHP() {
        return curr_hp;
    }

    /** Returns current MP */
    public int getMP() {
        return curr_mp;
    }

    /** Restores HP and MP to max values */
    public void restore() {
        curr_hp = max_hp;
        curr_mp = max_mp;
    }

    /** Adds a skill to the player */
    public void addSkill(Skill newSkill) {
        skills.add(newSkill);
    }
    /** Adds a course to a list of taken courses */
    public void addCourse(Course newCourse) {
        pastCourses.add(newCourse);
    }

    /** Adds a course to a list of taken courses */
    public void addAssignment(Course newAssignment) {
        pastAssignments.add(newAssignment);
    }

    public void status() {
        System.out.println("HP: " + curr_hp + "/" + max_hp);
        System.out.println("MP: " + curr_mp + "/" + max_mp);
    }
    /** Initial stat values */
    private int curr_hp = 100, max_hp = 100, curr_mp = 100, max_mp = 100;

    /** The player's name*/
    private String name;

    /** An array of the player's skills*/
    private ArrayList<Skill> skills = new ArrayList<Skill>();

    /** An array of courses the player has taken */
    private ArrayList<Course> pastCourses = new ArrayList<course>();

    /** An array of assignments the player has completed */
    private ArrayList<Assignment> pastAssignments = new ArrayList<Assignment>();
}