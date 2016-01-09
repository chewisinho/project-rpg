package project_rpg;

import java.io.Serializable;
import java.util.ArrayList;

/** Abstract class representing assignments of a course.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class Assignment implements Serializable {

    /** Constructor for assignment that takes in a TITLE. */
    public Assignment(String title) {
        _title = title;
    }
    
    /** Constructor for assignment that takes in an array of MONSTERS and
     * a Title. */
    public Assignment(ArrayList<Monster> monsters, String title) {
    	_monsters = monsters;
    	_title = title;
    }

    /** Returns title of the assignment. */
    public String getTitle() {
        return _title;
    }
    
    /** Returns the monsters of the assignment. */
    public ArrayList<Monster> getMonsters() {
    	return _monsters;
    }

    /** Title of the assignment. */
    private String _title;
    
    /** The monsters in this assignment. */
    private ArrayList<Monster> _monsters;

}
