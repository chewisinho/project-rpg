package project_rpg;

import java.io.Serializable;
import java.util.ArrayList;

/** Abstract class representing assignments of a course.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class Assignment implements Serializable {

    /** Constructor for assignment that takes in a TITLE and a DESCRIPTION. */
    public Assignment(String title, String description) {
        _title = title;
        _description = description;
    }

    /** Constructor for assignment that takes in an array of MONSTERS, TITLE,
     *  a DESCRIPTION, and my PARENT course. */
    public Assignment(ArrayList<Monster> monsters, String title,
        String description, Course parent) {
        _monsters = monsters;
        _title = title;
        _description = description;
        course = parent;
    }

    /** Returns title of the assignment. */
    public String getTitle() {
        return _title;
    }

    /** Returns my course. */
    Course getCourse() {
        return course;
    }

    /** Returns the description of the assignment. */
    public String description() {
        return _title + ": " + _description;
    }

    /** Returns the monsters of the assignment. */
    ArrayList<Monster> getMonsters() {
        return _monsters;
    }

    /** Prints out the assignments in ASSIGNMENTS. */
    public static void printAssignments(ArrayList<Assignment> assignments) {
        int index = 0;
        for (Assignment assignment : assignments) {
            System.out.println(index + ": " + assignment.getTitle());
            index += 1;
        }
    }

    /** Title and description of the assignment. */
    private String _title, _description;

    /** The monsters in this assignment. */
    private ArrayList<Monster> _monsters;

    /** I am an assignment within this course. */
    private Course course;

}
