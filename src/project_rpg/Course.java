package project_rpg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

/** Abstract class representing a class taken by the player.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public abstract class Course implements Serializable {

    /** Returns a short description of the course. */
    public String description() {
        return courseTitle + ": " + description;
    }

    /** Returns the title of the course.*/
    public String getTitle() {
        return courseTitle;
    }

    /** Returns a list of the course's assignments for the given WEEK. */
    protected ArrayList<Assignment> getAssignments(int week) {
        ArrayList<Assignment> assignmentList = null;
        if (week >= 1 && week < 10) {
            assignmentList = (ArrayList<Assignment>) assignments[week - 1];
        } else if (week == 10) {
            System.out.println("There are no assignments during tournament "
                + "week.");
        } else {
            System.out.println("Week out of range");
        }
        return assignmentList;
    }

    /** The title of the course and a short description. */
    protected String courseTitle, description;

    /** The assignments of the course. */
    protected ArrayList[] assignments = new ArrayList[9];

    /** Comparator that compares two courses by their course titles. */
    public static final Comparator<Course> TITLE_COMPARATOR =
        new Comparator<Course>() {

        @Override
        public int compare(Course x, Course y) {
            return x.getTitle().compareTo(y.getTitle());
        }

    };

}
