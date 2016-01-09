package project_rpg;

import java.io.Serializable;
import java.util.ArrayList;

/** Abstract class representing assignments of a course.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class Assignment implements Serializable {

    /** Constructor for assignment that takes in a TITLE. */
    public Assignment(String title, String description) {
        _title = title;
        _description = description;
    }
    
    /** Constructor for assignment that takes in an array of MONSTERS, TITLE,
     * and a DESCRIPTION. */
    public Assignment(ArrayList<Monster> monsters, String title,
    		String description) {
    	_monsters = monsters;
    	_title = title;
    }

    /** Returns title of the assignment. */
    public String getTitle() {
        return _title;
    }
    
    /** Returns the description of the assignment. */
    public String description() {
    	return _title + ": " + _description;
    }
    
    /** Returns the monsters of the assignment. */
    public ArrayList<Monster> getMonsters() {
    	return _monsters;
    }

    /** Title and description of the assignment. */
    private String _title, _description;
    
    /** The monsters in this assignment. */
    private ArrayList<Monster> _monsters;

}
