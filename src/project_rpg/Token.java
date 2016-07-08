package project_rpg;

import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

/** Represents a token on the map.
 *  @author S. Chewi, A. Tran
 */
public class Token implements Runnable {

  /** Creates a new token with IMAGE at (POSITIONX, POSITIONY). */
  public Token(BattleGrid grid, int positionX, int positionY, String image) {
    this.grid = grid;
    this.image = grid.getImage(image);
    buffered = grid.toBufferedImage(this.image.getImage());
    lastMovement = System.currentTimeMillis();
    orientation = 0;
    this.positionX = positionX;
    this.positionY = positionY;
    grid.addTokenAt(this, positionX, positionY);
    takeAction();
  }

  /** Initiates the player's attack. */
  protected void attack() {
    int[] dir = orientationToArray(orientation);
    int newX = positionX + dir[0];
    int newY = positionY + dir[1];
    Skill currentSkill = grid.getPlayer().getBattleSkill();
    if (
        currentSkill != null
        && grid.valid(newX, newY)
        && grid.getPlayer().hasEnoughMp(currentSkill.getCost())
        && System.currentTimeMillis() - currentSkill.getLastUsed() > currentSkill.getCooldown()
    ) {
      Token token = new SkillToken(
          currentSkill.getImage(),
          newX,
          newY,
          grid,
          dir[0],
          dir[1],
          currentSkill
      );
      token.orientation = orientation;
      grid.addTokenAt(token, newX, newY);
      currentSkill.use(System.currentTimeMillis());
      new Thread(token).start();
    }
    grid.repaint();
  }

  /** Returns the buffered image of the token. */
  public BufferedImage bufferedImage() {
    return buffered;
  }

  /** Removes the token from the map. */
  protected void disappear() {
    grid.addTokenAt(null, positionX, positionY);
  }

  /** Move one space down. */
  protected void down() {
    move(positionX, positionY + 1);
    orientation = BattleGrid.RIGHT_ANGLE;
  }

  /** Returns the BattleGrid in which the Token is located. */
  protected BattleGrid getGrid() {
    return grid;
  }

  /** Returns the token's x-coordinate. */
  public int getX() {
    return positionX;
  }

  /** Returns the token's y-coordinate. */
  public int getY() {
    return positionY;
  }

  /** Returns the image of the token as an ImageIcon. */
  public ImageIcon image() {
    return image;
  }

  /** Returns false, since the token is not an exit. */
  public boolean isExit() {
    return false;
  }

  /** Returns true iff the time elapsed since the last action is greater than TIME. */
  public boolean isReadyForAction(long time) {
    return System.currentTimeMillis() - lastAction > time;
  }

  /** Move one space left. */
  protected void left() {
    move(positionX - 1, positionY);
    orientation = BattleGrid.RIGHT_ANGLE * 2;
  }

  /** Makes a movement to (NEWX, NEWY). */
  protected void move(int newX, int newY) {
    if (grid.inBounds(newX, newY)) {
      grid.exit(this, newX, newY);
    }
    if (grid.valid(newX, newY) && System.currentTimeMillis() - lastMovement > 100) {
      grid.addTokenAt(null, positionX, positionY);
      grid.addTokenAt(this, newX, newY);
      positionX = newX;
      positionY = newY;
      lastMovement = System.currentTimeMillis();
      grid.repaint();
    }
  }

  /** Returns my orientation. */
  public int orientation() {
    return orientation;
  }

  /** Move one space right. */
  protected void right() {
    move(positionX + 1, positionY);
    orientation = 0;
  }

  @Override
  public void run() {
    // Do nothing.
  }

  /** Creates a block at my position. */
  protected void block() {
    while (true) {
      if (System.currentTimeMillis() - lastMovement > 2000) {
        return;
      }
    }
    
  }
  
  /** Sets the time of the last action to the current time. */
  protected void takeAction() {
    lastAction = System.currentTimeMillis();
  }

  /** Move one space up. */
  protected void up() {
    move(positionX, positionY - 1);
    orientation = BattleGrid.RIGHT_ANGLE * 3;
  }

  /** Takes an ORIENTATION and returns an integer array representing the direction. */
  public static int[] orientationToArray(int orientation) {
    int[] direction = { 0, 0 };
    switch (orientation) {
      case 0:
        direction[0] = 1;
        break;
      case BattleGrid.RIGHT_ANGLE:
        direction[1] = 1;
        break;
      case BattleGrid.RIGHT_ANGLE * 2:
        direction[0] = -1;
        break;
      case BattleGrid.RIGHT_ANGLE * 3:
        direction[1] = -1;
        break;
      default:
        break;
    }
    return direction;
  }

  /** Contains my buffered image. */
  private BufferedImage buffered;

  /** Contains the BattleGrid in which the Token is found. */
  private BattleGrid grid;

  /** Contains the image of the token. */
  private ImageIcon image;

  /** The coordinates and orientation of the token. */
  private int orientation;
  private int positionX;
  private int positionY;

  /** Records the time of the last action and movement for cooldown tracking. */
  private long lastAction;
  private long lastMovement;

}

