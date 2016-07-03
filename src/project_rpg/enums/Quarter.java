package project_rpg.enums;

/** Contains the various quarters for Project RPG.
 *  @author S. Chewi, A. Tran
 */
public enum Quarter {

  FALL(),
  WINTER(),
  SPRING(),
  SUMMER();

  /** Returns the next year. */
  public Quarter next() {
    return (this == SUMMER) ? FALL : values()[ordinal() + 1];
  }

}

