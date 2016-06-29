package project_rpg;

import static project_rpg.Monster.spread;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.StringJoiner;

/** Basic representation of a skill.
 *  @author S. Chewi, A. Tran
 */
public class Skill implements Serializable {

  /** Creates a new skill from the JSON file with filename NAME. */
  public Skill(String name) {
    try {
      // Load and parse JSON file.
      String path = new StringJoiner(File.separator)
          .add("project_rpg")
          .add("database")
          .add("skills")
          .add(name + ".json")
          .toString();
      BufferedReader input = new BufferedReader(new FileReader(new File(path)));
      JsonParser parser = new JsonParser();
      JsonObject attrTree = (JsonObject) parser.parse(input);
      baseDamage = attrTree.get("baseDamage").getAsInt();
      baseMp = attrTree.get("baseMp").getAsInt();
      behavior = attrTree.get("behavior").getAsString();
      cooldown = attrTree.get("cooldown").getAsInt();
      description = attrTree.get("description").getAsString();
      image = attrTree.get("image").getAsString();
      name = attrTree.get("name").getAsString();
      input.close();
      // Set the skill EXP.
      exp = (int) BASE_EXP;
      rank = 0;
      update();
    } catch (IOException exception) {
      Main.error("Error while reading " + name + ".json.");
    }
  }

  /** Returns the damage caused by a use of the skill. */
  public int attack() {
    return spread(damage);
  }

  /** Allocates skill EXP after completing the assignments for WEEK. Returns the log message. */
  protected String completedAssignments(int week) {
    return increaseExp(requiredExp(week) - requiredExp(week - 1));
  }

  /** Returns the description of the skill. */
  public String description() {
    return name + ": " + description;
  }

  /** Returns the behavior of the skill. */
  public String getBehavior() {
    return behavior;
  }

  /** Returns the cooldown of the skill. */
  public int getCooldown() {
    return cooldown;
  }
  
  /** Returns the MP cost of the skill. */
  public int getCost() {
    return mp;
  }

  /** Returns the damage of the skill. */
  public int getDamage() {
    return damage;
  }
  
  /** Returns the image of the skill. */
  public String getImage() {
    return image;
  }

  /** Returns the time that the skill was last used. */
  public long getLastUsed() {
    return lastUsed;
  }

  /** Returns the name of the skill. */
  public String getName() {
    return name;
  }

  /** Returns the current rank of the skill. */
  public int getRank() {
    return rank;
  }

  /** Adds POINTS to skill EXP, ranking up the skill if needed. Returns the log message. */
  protected String increaseExp(int points) {
    exp += points;
    String message = "";
    if (rank <= 10 && exp >= requiredExp(rank + 1)) {
      message = rankUp();
    }
    return name + " gained " + points + " EXP points!" + message;
  }

  /** Increases the skill rank. Returns a log message. */
  private String rankUp() {
    rank += 1;
    update();
    return " Congratulations, " + name + " ranked up! " + name + " is now rank " + rank + ".";
  }

  @Override
  public String toString() {
    return name + " " + rank;
  }

  /** Updates the damage of the skill after ranking up. */
  protected void update() {
    damage = (int) (baseDamage * Math.pow(SCALE, rank));
    mp = (int) (baseMp * Math.pow(SCALE, rank));
  }

  /** Sets the lastUsed variable to TIME. */
  public void use(long time) {
    lastUsed = time;
  }

  /** Returns the amount of EXP needed for RANK. */
  private static int requiredExp(int rank) {
    return (int) (BASE_EXP * Math.pow(EXP_SCALE, rank));
  }

  /** Contains the parameters of the skill. */
  private int baseDamage;
  private int baseMp;
  private int cooldown;
  private int damage;
  private int exp;
  private int mp;
  private int rank;

  /** The last time that the skill was used. */
  private long lastUsed;

  /** Contains the description of the skill. */
  private String behavior;
  private String description;
  private String image;
  private String name;

  /** Contains the scaling of the skill by rank. */
  public static final double BASE_EXP = 35.0;
  public static final double EXP_SCALE = 1.6;
  public static final double SCALE = 1.2;

}

