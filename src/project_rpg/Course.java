package project_rpg;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.StringJoiner;

/** A representation of a course taken by the player. Note: There is no need for serialization in
 *  this class because there are only two pieces of persistent information (the week of the next
 *  assignment and whether the course is ready for another assignment). However, care must be taken
 *  to ensure that the skill attached to the course correlates with the skill used by the player.
 *
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class Course {

  /** Constructs a course from the NAME by reading from a JSON file. Initially, the course
   *  starts on week 1 and advances when the player completes assignments.
   */
  public Course(String name) {
    try {
      String path = new StringJoiner(File.separator)
          .add("project_rpg")
          .add("database")
          .add("courses")
          .add(name + ".json")
          .toString();
      BufferedReader input = new BufferedReader(new FileReader(new File(path)));
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
      ready = true;
      week = 1;
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
  protected String finishAssignment() {
    String message = skill.completedAssignments(week);
    if (week < 10) {
      ready = false;
      week += 1;
    } else if (week == 10) {
      message += "\nCourse complete: " + courseTitle + " finished.";
    }
    return message;
  }

  /** Returns the assignment for a particular WEEK. */
  public Assignment getAssignment(int week) {
    return assignments[week - 1];
  }

  /** Returns the assignment of the current week. */
  public Assignment getCurrentAssignment() {
    return assignments[week - 1];
  }

  /** Returns the skill of the course. */
  public Skill getSkill() {
    return skill;
  }

  /** Returns true iff the course is ready for another assignment. */
  public boolean isReady() {
    return ready;
  }

  /** Sets the variable ready to true at the start of a new week. */
  protected void ready() {
    ready = true;
  }

  @Override
  public String toString() {
    return courseTitle;
  }

  /** The assignments of the course. */
  private Assignment[] assignments = new Assignment[10];

  /** True iff the next assignment is ready to be started. */
  private boolean ready;

  /** The current assignment to finish. */
  private int week;

  /** The skill that this course will teach. */
  private Skill skill;

  /** The title of the course and a short description. */
  private String courseTitle;
  private String description;

  /** Comparator that compares two courses by their course titles. */
  public static final Comparator<Course> TITLE_COMPARATOR =
      new Comparator<Course>() {

        @Override
        public int compare(Course courseOne, Course courseTwo) {
          return courseOne.toString().compareTo(courseTwo.toString());
        }

      };

}

