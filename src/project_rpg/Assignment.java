package project_rpg;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/** Abstract class representing assignments of a course.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class Assignment implements Serializable {

    /** Constructor for an assignment that takes in a COURSE, a DESCRIPTION, a DUNGEON, and a NAME. */
    public Assignment(Course course, String description, String dungeon, String name) {
        _course = course;
        _description = description;
        _dungeon = dungeon;
        _name = name;
    }

    /** Returns the description of the assignment. */
    public String description() {
        return _name + ": " + _description;
    }

    /** Returns the course in which the assignment is found. */
    Course getCourse() {
        return _course;
    }

    /** Returns the name of the assignment. */
    public String getName() {
        return _name;
    }

    /** The course in which the assignment is found. */
    private Course _course;

    /** Title and description of the assignment. */
    private String _description, _dungeon, _name;

}

