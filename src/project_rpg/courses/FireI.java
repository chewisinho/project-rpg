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
        
        ArrayList<Monster> monsters0 = new ArrayList<Monster>();
        Monster monster0 = new IceCube();
        monsters0.add(monster0);
        
        /** The first assignment of the class. */
        Assignment assignment0 = new Assignment(monsters0, "0", "Using a fireball");
        
        
    }

}
