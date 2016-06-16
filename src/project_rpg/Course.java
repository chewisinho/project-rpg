package project_rpg;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

/** Abstract class representing a class taken by the player.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class Course implements Serializable {

    /** Constructs a course from the NAME by reading from a JSON file. Initially, the course starts on week 1.  */
    public Course(String name) {
        try {
            BufferedReader input = new BufferedReader(new FileReader(new File(
                "project_rpg" + File.separator + "database" + File.separator + "courses" + File.separator + name + ".json"
            )));
            JsonParser parser = new JsonParser();
            JsonObject attrTree = (JsonObject) parser.parse(input);
            courseTitle = attrTree.get("courseTitle").getAsString();
            description = attrTree.get("description").getAsString();
            skill = new Skill(attrTree.get("skill").getAsString());
            for (int i = 1; i <= 10; i += 1) {
                JsonObject assignmentTree = (JsonObject) attrTree.get(((Integer) i).toString());
                Assignment assignment = new Assignment(
                    this,
                    assignmentTree.get("description").getAsString(),
                    assignmentTree.get("dungeon").getAsString(),
                    assignmentTree.get("name").getAsString()
                );
                assignments[i - 1] = assignment;
            }
            week = 1;
            ready = true;
            input.close();
        } catch (FileNotFoundException fileException) {
            Main.error("Cannot find the file " + name + ".json.");
        } catch (IOException exception) {
            Main.error("Error while reading the file " + name + ".json.");
        }
    }

    /** Returns a short description of the course. */
    public String description() {
        return courseTitle + ": " + description;
    }

    /** Finishes the current assignment. Returns the log message. */
    String finishAssignment() {
        if (week < 10) {
            week += 1;
            ready = false;
            return skill.completedAssignments(week);
        }
        return "Error: Finishing the course is not yet implemented.";
    }

    /** Returns the assignment for a particular WEEK. */
    protected Assignment getAssignment(int week) {
        return assignments[week - 1];
    }

    /** Returns the assignments of the course. */
    protected Assignment[] getAssignments() {
        return assignments;
    }

    /** Returns the assignment of the current week. */
    Assignment getCurrentAssignment() {
        return assignments[week - 1];
    }

    /** Returns the skill of the course. */
    Skill getSkill() {
        return skill;
    }

    /** Returns true iff the course is ready for another assignment. */
    boolean isReady() {
        return ready;
    }

    /** Sets the variable ready to true at the start of a new week. */
    void ready() {
        ready = true;
    }

    @Override
    public String toString() {
        return courseTitle;
    }

    /** The assignments of the course. */
    protected Assignment[] assignments = new Assignment[10];

    /** True iff the next assignment is ready to be started. */
    private boolean ready;

    /** The current assignment to finish. */
    private int week;

    /** The skill that this course will teach. */
    protected Skill skill;

    /** The title of the course and a short description. */
    protected String courseTitle, description;

    /** Comparator that compares two courses by their course titles. */
    public static final Comparator<Course> TITLE_COMPARATOR =
        new Comparator<Course>() {

        @Override
        public int compare(Course x, Course y) {
            return x.toString().compareTo(y.toString());
        }

    };

}

