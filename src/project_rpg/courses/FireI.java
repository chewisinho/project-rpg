package project_rpg.courses;

import java.util.ArrayList;
import project_rpg.Assignment;
import project_rpg.Monster;
import project_rpg.monsters.*;

/** The first course in fire magic.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class FireI extends project_rpg.Course {

    {
        courseTitle = "Introduction to Fire Magic";
        description = "Covers the basics of handling fire magic, culminating "
            + "in the production of a fireball.";
        
        /** Creates the monsters for assignment 1. */
        ArrayList<Monster> monsters1 = new ArrayList<Monster>();
        Monster monster1 = new IceCube();
        monsters1.add(monster1);
        
        /** The first assignment of the class. */
        Assignment assignment1 = new Assignment(monsters1, "0", "Using a fireball");
        
        ArrayList<Assignment> week1 = new ArrayList<Assignment>();
        week1.add(assignment1);
        assignments[0] = week1;
        
    }

}
