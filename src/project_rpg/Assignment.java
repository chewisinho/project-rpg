package project_rpg;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/** Abstract class representing assignments of a course. Essentially, assignments just hold
 *  a dungeon and useful metadata (such as the name and description).
 *  @author S. Chewi, A. Tran
 */
public class Assignment implements Serializable {

  /** Constructor for an assignment that takes in a COURSE, a DESCRIPTION, a DUNGEON, and a NAME. */
  public Assignment(Course course, String description, String dungeon, String name) {
    this.course = course;
    this.description = description;
    this.dungeon = dungeon;
    this.name = name;
  }

  /** Returns the description of the assignment. */
  public String description() {
    return name + ": " + description;
  }

  /** Returns the course in which the assignment is found. */
  public Course getCourse() {
    return course;
  }
  
  /** Returns the name of the dungeon for the assignment. */
  public String getDungeon() {
    return dungeon;
  }

  /** Returns the name of the assignment. */
  public String getName() {
    return name;
  }

  /** The course in which the assignment is found. */
  private Course course;

  /** Title and description of the assignment. */
  private String description;
  private String dungeon;
  private String name;

}

