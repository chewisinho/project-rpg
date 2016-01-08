package project_rpg;

import java.util.ArrayList;
import java.util.Comparator;

/** Abstract class representing a class taken by the player.
 *  @author S. Chewi, A. Tran
 */
public abstract class Course {

    /** Returns a short description of the course. */
    public String description() {
        return courseTitle + ": " + description;
    }

    /** Returns the title of the course.*/
    public String getTitle() {
        return courseTitle;
    }

    /** Returns a list of the course's assignments. */
    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }

    /** The title of the course and a short description. */
    protected String courseTitle, description;

    /** The assignments of the course. */
    protected ArrayList<Assignment> assignments;

    /** Comparator that compares two courses by their course titles. */
    public static final Comparator<Course> TITLE_COMPARATOR =
        new Comparator<Course>() {

        @Override
        public int compare(Course x, Course y) {
            return x.getTitle().compareTo(y.getTitle());
        }

    };

}
