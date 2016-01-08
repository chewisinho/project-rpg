package project_rpg;

import java.io.Serializable;

/** Abstract class representing assignments of a course.
 *  @author S. Chewi, A. Tran
 */
public class Assignment implements Serializable {

    /** Constructor for assignment that takes in a TITLE. */
    public Assignment(String title) {
        _title = title;
    }

    /** Returns title of the assignment. */
    public String getTitle() {
        return _title;
    }

    /** Title of the assignment. */
    private String _title;

}
