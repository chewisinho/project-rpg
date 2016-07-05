package project_rpg;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringJoiner;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/** Displays the battle screen.
 *  @author S. Chewi, A. Tran
 */
public class BattleGrid extends JPanel implements Runnable {

  /** Initializes a DUNGEON with a PLAYER and a GUI. */
  public BattleGrid(GUI gui, Player player, String dungeon) {
    setPreferredSize(new Dimension(WIDTH * SQ_WIDTH, (HEIGHT + 1) * SQ_HEIGHT));
    this.gui = gui;
    this.player = player;
    addKeyListener(new PlayerControl());

    // Initialize the dungeon.
    map = new Token[WIDTH][HEIGHT];
    monsters = new HashMap<Token, Monster>();
    try {
      String path = new StringJoiner(File.separator)
          .add("project_rpg")
          .add("database")
          .add("dungeons")
          .add(dungeon + ".json")
          .toString();
      BufferedReader input = new BufferedReader(new FileReader(new File(path)));
      JsonParser parser = new JsonParser();
      JsonObject attrTree = (JsonObject) parser.parse(input);
      JsonArray layout = attrTree.get("layout").getAsJsonArray();
      int col = 0;
      int row = 0;
      for (JsonElement rowElem : layout) {
        col = 0;
        JsonArray rowLayout = (JsonArray) rowElem;
        for (JsonElement element : rowLayout) {
          String identifier = element.getAsString();
          if (identifier.equals("player")) {
            playerToken = new Token(this, 0, 0, "player");
          } else if (identifier.equals("null")) {
            map[col][row] = null;
          } else if (identifier.equals("exit")) {
            map[col][row] = new ExitToken(this, col, row);
          } else {
            Monster monster = new Monster(identifier);
            Token token = new MonsterToken(this, col, row, monster);
            monsters.put(token, monster);
          }
          col += 1;
        }
        row += 1;
      }
    } catch (IOException exception) {
      Main.error("Unable to load dungeon " + dungeon + ".json.");
    }
  }

  /** Initializes a DUNGEON with a PLAYER and a GUI. Also uses the COURSE when exiting the dungeon.
   */
  public BattleGrid(GUI gui, Player player, String dungeon, Course course) {
    this(gui, player, dungeon);
    this.course = course;
  }

  /** Adds the given TOKEN to the map at (POSITIONX, POSITIONY). */
  protected void addTokenAt(Token token, int positionX, int positionY) {
    map[positionX][positionY] = token;
  }

  /** Uses the monster's (from a MONSTERTOKEN) attack to damage the player. */
  protected void attackPlayer(MonsterToken monsterToken) {
    Monster monster = monsters.get(monsterToken);
    player.reduceHealth(monster.attack());
    if (player.isDead()) {
      gameOver();
    }
  }
 
  /** Exits the dungeon if TOKEN is the player token and there are no more monsters. Also, the
   *  given position (POSITIONX, POSITIONY) must be the exit token.
   */
  protected void exit(Token token, int positionX, int positionY) {
    Token exit = map[positionX][positionY];
    if (token == playerToken && monsters.isEmpty() && exit != null && exit.isExit()) {
      if (course != null) {
        String message = course.finishAssignment();
        gui.updateMenuBar(message);
        if (message.contains("Course complete")) {
          // The course is complete, so remove the course from the enrolled list.
          gui._game.finishCourse(course);
        }
      }
      gui.paintSchool();
    }
  }

  /** Sets the boolean flag gameOver to true, ending the repaint loop (and the game). Also stops
   *  every token thread.
   */
  public void gameOver() {
    for (int i = 0; i < WIDTH; i += 1) {
      for (int j = 0; j < HEIGHT; j += 1) {
        if (map[i][j] != null) {
          map[i][j].disappear();
        }
      }
    }
    gameOver = true;
  }

  /** Returns the player. */
  public Player getPlayer() {
    return player;
  }

  /** Returns the token located at (POSITIONX, POSITIONY). */
  public Token getTokenAt(int positionX, int positionY) {
    return map[positionX][positionY];
  }

  /** Returns true iff (POSITIONX, POSITIONY) is in bounds. */
  public boolean inBounds(int positionX, int positionY) {
    return (positionX >= 0) && (positionY >= 0) && (positionX < WIDTH) && (positionY < HEIGHT);
  }

  /** Returns true iff the game is over. */
  public boolean isGameOver() {
    return gameOver;
  }

  /** Returns true iff there is a monster at (POSITIONX, POSITIONY). */
  public boolean monsterAt(int positionX, int positionY) {
    return monsters.containsKey(map[positionX][positionY]);
  }

  /** Moves the given MONSTERTOKEN towards the player token. */
  protected void moveMonsterTokenTowardsPlayer(MonsterToken monsterToken) {
    if (playerToken.getX() > monsterToken.getX()) {
      monsterToken.right();
    } else if (playerToken.getX() < monsterToken.getX()) {
      monsterToken.left();
    } else {
      if (playerToken.getY() > monsterToken.getY()) {
        // Higher y-value means lower vertically on the grid.
        monsterToken.down();
      } else if (playerToken.getY() < monsterToken.getY()) {
        monsterToken.up();
      }
    }
  }

  @Override
  public synchronized void paintComponent(Graphics graphics) {
    // Move the player.
    if (down) {
      playerToken.down();
    }
    if (left) {
      playerToken.left();
    }
    if (right) {
      playerToken.right();
    }
    if (up) {
      playerToken.up();
    }
    for (int x = 0; x < WIDTH; x += 1) {
      for (int y = 0; y < HEIGHT; y += 1) {
        if (map[x][y] == null) {
          paintImage("default", x, y, graphics);
        } else {
          paintImage(map[x][y], x, y, graphics);
        }
      }
    }
    for (int num = 0; num < 4; num += 1) {
      Skill skill = player.getBattleSkill(num);
      String cooldownImage;
      if (skill == null) {
        cooldownImage = "cooldown_none";
      } else if (System.currentTimeMillis() - skill.getLastUsed() > skill.getCooldown()) {
        cooldownImage = "cooldown_ready";
      } else {
        cooldownImage = "cooldown_not_ready";
      }
      paintCooldown(cooldownImage, num, graphics);
    }
    paintCooldown("cooldown_selected", player.getSkillIndex(), graphics);
    gui.refreshMenu();
  }

  /** Paints the cooldown indicator with IMAGE for skill NUM on GRAPHICS. */
  private void paintCooldown(String image, int num, Graphics graphics) {
    graphics.drawImage(
        getImage(image).getImage(),
        COOLDOWN_BUFFER + num * COOLDOWN_WIDTH,
        HEIGHT * SQ_HEIGHT,
        null
    );
  }

  /** Paints IMAGE at (POSITIONX, POSITIONY) on GRAPHICS. */
  private void paintImage(Image image, int positionX, int positionY, Graphics graphics) {
    graphics.drawImage(image, positionX * SQ_WIDTH, positionY * SQ_HEIGHT, null);
  }

  /** Paints IMAGE at (POSITIONX, POSITIONY) on GRAPHICS. */
  private void paintImage(ImageIcon image, int positionX, int positionY, Graphics graphics) {
    paintImage(image.getImage(), positionX, positionY, graphics);
  }

  /** Paints IMAGE at (POSITIONX, POSITIONY) on GRAPHICS (using the name of the resource). */
  private void paintImage(String image, int positionX, int positionY, Graphics graphics) {
    paintImage(getImage(image), positionX, positionY, graphics);
  }

  /** Paints a TOKEN at (POSITIONX, POSITIONY) on GRAPHICS. */
  private void paintImage(Token token, int positionX, int positionY, Graphics graphics) {
    BufferedImage image = token.bufferedImage();
    switch (token.orientation()) {
      case RIGHT_ANGLE * 3:
        image = rotation.filter(image, null);
        image = rotation.filter(image, null);
        image = rotation.filter(image, null);
        break;
      case RIGHT_ANGLE * 2:
        image = rotation.filter(image, null);
        image = rotation.filter(image, null);
        break;
      case RIGHT_ANGLE:
        image = rotation.filter(image, null);
        break;
      default:
        break;
    }
    paintImage(image, positionX, positionY, graphics);
  }

  /** Returns true iff the player is adjacent to (POSITIONX, POSITIONY). */
  public boolean playerAdjacentTo(int positionX, int positionY) {
    return (inBounds(positionX - 1, positionY) && playerToken == map[positionX - 1][positionY])
      || (inBounds(positionX, positionY - 1) && playerToken == map[positionX][positionY - 1])
      || (inBounds(positionX + 1, positionY) && playerToken == map[positionX + 1][positionY])
      || (inBounds(positionX, positionY + 1) && playerToken == map[positionX][positionY + 1]);
  }

  /** Damages the token at (POSITIONX, POSITIONY) by DAMAGE using a SKILL. */
  protected void reduceHealth(int positionX, int positionY, int damage, String skill) {
    Token monsterToken = map[positionX][positionY];
    Monster monster = monsters.get(monsterToken);
    monster.reduceHealth(damage);
    String message = new StringJoiner(" ")
        .add(skill)
        .add("did " + damage)
        .add("damage to")
        .add(monster.getName() + "!")
        .add(monster.getName())
        .add("has " + monster.getHp() + " HP remaining.")
        .toString();
    gui.updateMenuBar(message);
    if (monster.isDead()) {
      monsters.remove(monsterToken);
    }
  }

  @Override
  public void run() {
    while (true) {
      if (gameOver) {
        break;
      }
      repaint();
    }
    gui.gameOver();
  }

  /** Starts executing the actions of the BattleGrid. */
  public void start() {
    // Start monster threads.
    for (Token token : monsters.keySet()) {
      new Thread(token).start();
    }

    // Set up display.
    rotation = makeRotation(RIGHT_ANGLE);
    setFocusable(true);
    repaint();

    new Thread(this).start();
  }

  /** Returns true iff (POSITIONX, POSITIONY) is a valid square. */
  public boolean valid(int positionX, int positionY) {
    return inBounds(positionX, positionY) && (map[positionX][positionY] == null);
  }

  /** Returns an Image from NAME. */
  public static ImageIcon getImage(String name) {
    if (name.equals("")) {
      return null;
    } else {
      String path = new StringJoiner(File.separator)
          .add("project_rpg")
          .add("resources")
          .add(name + ".png")
          .toString();
      return new ImageIcon(path);
    }
  }

  /** Returns an AffineTransformOp which rotates by DEGREES. */
  public static AffineTransformOp makeRotation(int degrees) {
    return new AffineTransformOp(AffineTransform.getRotateInstance(
      Math.toRadians(degrees), SQ_WIDTH / 2.0, SQ_HEIGHT / 2.0),
      AffineTransformOp.TYPE_BILINEAR);
  }

  /** Returns a given IMAGE converted into a BufferedImage. */
  public static BufferedImage toBufferedImage(Image image) {
    if (image instanceof BufferedImage) {
      return (BufferedImage) image;
    }
    BufferedImage bimage = new BufferedImage(
        image.getWidth(null),
        image.getHeight(null),
        BufferedImage.TYPE_INT_ARGB
    );
    Graphics2D bgr = bimage.createGraphics();
    bgr.drawImage(image, 0, 0, null);
    bgr.dispose();
    return bimage;
  }

  /** Class that listens for actions. */
  public class PlayerControl extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent event) {
      switch (event.getKeyCode()) {
        case KeyEvent.VK_S:
          down = true;
          break;
        case KeyEvent.VK_A:
          left = true;
          break;
        case KeyEvent.VK_D:
          right = true;
          break;
        case KeyEvent.VK_W:
          up = true;
          break;
        case KeyEvent.VK_SPACE:
          playerToken.attack();
          break;
        case KeyEvent.VK_UP:
          player.switchAttack1();
          break;
        case KeyEvent.VK_DOWN:
          player.switchAttack2();
          break;
        case KeyEvent.VK_LEFT:
          player.switchAttack3();
          break;
        case KeyEvent.VK_RIGHT:
          player.switchAttack4();
          break;
        default:
          break;
      }
    }

    @Override
    public void keyReleased(KeyEvent event) {
      switch (event.getKeyCode()) {
        case KeyEvent.VK_S:
          down = false;
          break;
        case KeyEvent.VK_A:
          left = false;
          break;
        case KeyEvent.VK_D:
          right = false;
          break;
        case KeyEvent.VK_W:
          up = false;
          break;
        default:
          break;
      }
    }
  }

  /** Represents a pi/2 rotation. */
  private AffineTransformOp rotation;

  /** Boolean values that control whether the player is moving or not. */
  private boolean down;
  private boolean left;
  private boolean right;
  private boolean up;

  /** Flag that is set to true when the player dies. Used to end the GUI loop. */
  private boolean gameOver = false;

  /** The course in which the dungeon is found. */
  private Course course = null;

  /** Contains the GUI from before the start of the battle. */
  private GUI gui;

  /** Contains the monsters/spells and their tokens. */
  private HashMap<Token, Monster> monsters;

  /** Contains the player token. */
  private Token playerToken;

  /** Contains the map. */
  private Token[][] map;

  /** Contains the player. */
  private Player player;

  /** Contains the parameters of the grid. */
  public static final int COOLDOWN_BUFFER = 75;
  public static final int COOLDOWN_WIDTH = 150;
  public static final int HEIGHT = 16;
  public static final int RIGHT_ANGLE = 90;
  public static final int SQ_HEIGHT = 25;
  public static final int SQ_WIDTH = 25;
  public static final int WIDTH = 30;

}

