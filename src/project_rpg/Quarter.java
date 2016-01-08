package project_rpg;

/** Contains the various quarters for Project RPG.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public enum Quarter {

    FALL(), WINTER(), SPRING(), SUMMER();

    /** Returns the next year. */
    Quarter next() {
        return (this == SUMMER) ? FALL : values()[ordinal() + 1];
    }

}
