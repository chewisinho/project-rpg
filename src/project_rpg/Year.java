package project_rpg;

/** Contains the various years for Project RPG.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public enum Year {

    FRESHMAN(), SOPHOMORE(), JUNIOR(), SENIOR();

    /** Returns the next year. */
    Year next() {
        return values()[ordinal() + 1];
    }

}
