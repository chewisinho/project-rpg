package project_rpg;

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

    /** The title of the course and a short description. */
	String courseTitle, description;

	/** Comparator that compares two courses by their course titles. */
	public static Comparator<Course> titleComparator =
	    new Comparator<Course>() {

		@Override
	    public int compare(Course x, Course y) {
	        return x.getTitle().compareTo(y.getTitle());
	    }

	};

}
