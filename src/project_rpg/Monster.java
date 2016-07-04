package project_rpg;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.StringJoiner;

/** Represents a monster class.
 *  @author S. Chewi, A. Tran
 */
public class Monster {

  /** Returns a monster created from the JSON file NAME. */
  public Monster(String fileName) {
    try {
      String path = new StringJoiner(File.separator)
          .add("project_rpg")
          .add("database")
          .add("monsters")
          .add(fileName + ".json")
          .toString();
      BufferedReader input = new BufferedReader(new FileReader(new File(path)));
      JsonParser parser = new JsonParser();
      JsonObject attrTree = (JsonObject) parser.parse(input);
      attackName = attrTree.get("attackName").getAsString();
      behavior = attrTree.get("behavior").getAsString();
      damage = spread(attrTree.get("damage").getAsInt());
      description = attrTree.get("description").getAsString();
      hp = spread(attrTree.get("hp").getAsInt());
      image = attrTree.get("image").getAsString();
      mp = spread(attrTree.get("mp").getAsInt());
      name = attrTree.get("name").getAsString();
      input.close();
    } catch (IOException exception) {
      Main.error("Unable to read monster file " + name + ".json.");
    }
  }

  /** Returns damage caused by an attack. */
  public int attack() {
    return spread(damage);
  }

  /** Returns the attack name of the monster. */
  public String getAttackName() {
    return attackName;
  }

  /** Returns the behavior of the monster. */
  public String getBehavior() {
    return behavior;
  }

  /** Returns the current HP level. */
  public int getHp() {
    return hp;
  }

  /** Returns the image of the monster. */
  public String getImage() {
    return image;
  }

  /** Returns the name of the monster. */
  public String getName() {
    return name;
  }

  /** Returns true iff the monster is dead. */
  public boolean isDead() {
    return hp <= 0;
  }

  /** Causes DAMAGEAMOUNT to the monster. */
  protected void reduceHealth(int damageAmount) {
    hp -= damageAmount;
    if (hp < 0) {
      hp = 0;
    }
  }

  /** Returns a sample from a Gaussian distribution with MEAN, using the variability ratio. */
  public static int spread(int mean) {
    return spread(mean, VARIABILITY * mean);
  }

  /** Returns a sample from a Gaussian distribution with MEAN and STANDARDDEVIATION. */
  public static int spread(int mean, double standardDeviation) {
    return (int) (mean + standardDeviation * new Random().nextGaussian());
  }

  /** Contains the monster's instance variables. */
  private int damage;
  private int hp;
  private int mp;

  /** Contains the description of the monster. */
  private String attackName;
  private String behavior;
  private String description;
  private String image;
  private String name;

  /** Contains the innate variability for a typical monster. */
  private static final double ATTACK_VARIABILITY = 0.25;
  private static final double VARIABILITY = 0.1;

}

