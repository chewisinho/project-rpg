package project_rpg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

/** Abstract class representing a class taken by the player.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class Course implements Serializable {

    /** Constructs a course from the database LINE. */
    public Course(String[] line) {
        courseTitle = line[0];
        description = line[1];
        try {
            skill = Skill.readSkill(line[2]);
            for (int index = 3; index < 12; index += 1) {
                String[] week = line[index].split("~~");
                ArrayList<Assignment> weekList = new ArrayList<Assignment>();
                for (String assignment : week) {
                    weekList.add(new Assignment(assignment.split("--"), this));
                }
                assignments[index - 3] = weekList;
            }
        } catch (IOException exception) {
            TextInterpreter.error("Error loading course.");
        }
    }

    /** Returns a short description of the course. */
    public String description() {
        return courseTitle + ": " + description;
    }
    
    @Override
    public String toString() {
    	return courseTitle + ": " + description;
    }

    /** Returns the title of the course.*/
    public String getTitle() {
        return courseTitle;
    }

    /** Returns the skill of the course. */
    Skill getSkill() {
        return skill;
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

    /** Removes ASSIGNMENT from the list of assignments during WEEK. */
    void removeAssignment(Assignment assignment, int week) {
        ((ArrayList<Assignment>) assignments[week - 1]).remove(assignment);
    }

    /** Returns the course NAME read from the database. */
    public static Course readCourse(String name) throws IOException {
        BufferedReader input = new BufferedReader(new FileReader(new File(
            "project_rpg" + File.separator + "database" + File.separator
            + "courses.db")));
        Course result = null;
        String[] line;
        while (!(line = input.readLine().split("=="))[0].equals(name)) {
            continue;
        }
        input.close();
        result = new Course(line);
        return result;
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

    /** The skill that this course will teach. */
    protected Skill skill;

}
