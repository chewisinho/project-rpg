package project_rpg.enums;

/** Contains the various years for Project RPG.
 *  @author S. Chewi, A. Tran
 */
public enum Year {

  FRESHMAN(),
  SOPHOMORE(),
  JUNIOR(),
  SENIOR();

  /** Returns the next year. */
  public Year next() {
    return values()[ordinal() + 1];
  }

}

