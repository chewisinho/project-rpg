package project_rpg.enums;

/** Contains the various days for Project RPG.
 *  @author S. Chewi, A. Tran
 */
public enum Day {

  MONDAY(),
  TUESDAY(),
  WEDNESDAY(),
  THURSDAY(),
  FRIDAY(),
  SATURDAY(),
  SUNDAY();

  /**Returns the next day. */
  public Day next() {
    return (this == SUNDAY) ? MONDAY : values()[ordinal() + 1];
  }

}

