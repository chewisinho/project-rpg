package project_rpg;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.StringJoiner;

/** Class representing a player.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class Player {

  /** Initializes player without a name. */
  public Player() {
    this("Jimmy");
  }

  /** Initializes new player given GIVENNAME. */
  public Player(String givenName) {
    name = givenName;
    skills.add(new Skill("meditate"));
  }

  /** Adds NEWCOURSE to a list of taken courses. */
  protected void addCourse(Course newCourse) {
    pastCourses.add(newCourse);
  }

  /** Adds NEWSKILL to the player. */
  protected void addSkill(Skill newSkill) {
    skills.add(newSkill);
  }

  /** Changes a battle SKILL at INDEX. */
  protected void changeBattleSkills(int index, Skill skill) {
    battleSkills[index] = skill;
  }

  /** Returns the current battleSkill. */
  public Skill getBattleSkill() {
    return battleSkills[skillIndex];
  }

  /** Returns the battleSkill at INDEX. */
  public Skill getBattleSkill(int index) {
    return battleSkills[index];
  }

  /** Returns an iterator over the player's selected skills. */
  public Iterator<Skill> getBattleSkillIterator() {
    return Arrays.asList(battleSkills).iterator();
  }

  /** Returns a course from the player's past courses by FILENAME. Used in JSON serialization. */
  protected Course getCourseByFileName(String fileName) {
    for (Course course : pastCourses) {
      if (course.getFileName().equals(fileName)) {
        return course;
      }
    }
    return null;
  }

  /** Returns a skill from the player's skills by NAME. Used in JSON serialization. */
  protected Skill getSkillByFileName(String fileName) {
    for (Skill skill : skills) {
      if (skill.getFileName().equals(fileName)) {
        return skill;
      }
    }
    return null;
  }

  /** Returns current HP. */
  public int getHp() {
    return currHp;
  }

  /** Returns max HP. */
  public int getMaxHp() {
    return maxHp;
  }

  /** Returns max MP. */
  public int getMaxMp() {
    return maxMp;
  }
  
  /** Returns current MP. */
  public int getMp() {
    return currMp;
  }

  /** Returns the skill at INDEX. */
  public Skill getSkill(int index) {
    return skills.get(index);
  }

  /** Returns the skill index. */
  public int getSkillIndex() {
    return skillIndex;
  }

  /** Returns an iterator over all of the player's skills. */
  public Iterator<Skill> getSkillIterator() {
    return skills.iterator();
  }

  /** Returns true iff the player has enough MP to use a certain skill with COST. */
  public boolean hasEnoughMp(int cost) {
    if (cost > currMp) {
      return false;
    } else {
      return true;
    }
  }

  /** Increases max HP by AMOUNT. */
  protected void increaseHealth(int amount) {
    maxHp += amount;
  }

  /** Increases max MP by AMOUNT. */
  protected void increaseMana(int amount) {
    maxMp += amount;
  }

  /** Returns true iff the player is dead. */
  public boolean isDead() {
    return currHp <= 0;
  }

  /** Returns true iff I am out of Mp. */
  public boolean isOutOfMana() {
    return currMp <= 0;
  }

  /** After deserialization, make sure each course is linked to the correct skill object. */
  protected void linkCoursesToSkills() {
    for (Course course : pastCourses) {
      String skillFileName = course.getSkill().getFileName();
      Skill skill = getSkillByFileName(skillFileName);
      course.setSkill(skill);
    }
  }

  /** Returns true iff the player has no battle skills set. */
  public boolean noSkillsSet() {
    for (int i = 0; i < 4; i += 1) {
      if (battleSkills[i] != null) {
        return false;
      }
    }
    return true;
  }

  /** Prepares the player for JSON serialization. */
  protected void prepareForSave() {
    updateSelectedSkills();
  }

  /** Reduces current HP by AMOUNT. */
  protected void reduceHealth(int amount) {
    currHp -= amount;
    if (currHp < 0) {
      currHp = 0;
    }
  }

  /** Reduces current MP by AMOUNT. */
  protected void reduceMana(int amount) {
    currMp -= amount;
    if (currMp < 0) {
      currMp = 0;
    } else if (currMp > maxMp) {
      currMp = maxMp;
    }
  }

  /** Restores HP and MP to max values. */
  protected void restore() {
    currHp = maxHp;
    currMp = maxMp;
  }

  /** Switches to Skill 1. */
  protected void switchAttack1() {
    skillIndex = 0;
  }

  /** Switches to Skill 2. */
  protected void switchAttack2() {
    skillIndex = 1;
  }

  /** Switches to Skill 3. */
  protected void switchAttack3() {
    skillIndex = 2;
  }

  /** Switches to Skill 4. */
  protected void switchAttack4() {
    skillIndex = 3;
  }

  /** Sets the battle skills from the deserialized string array selectedSkills. */
  protected void updateBattleSkills() {
    for (int i = 0; i < 4; i += 1) {
      String name = selectedSkills[i];
      if (name != null) {
        for (Skill skill : skills) {
          if (skill.getName().equals(name)) {
            battleSkills[i] = skill;
          }
        }
      }
    }
  }

  /** Updates selectedSkills to reflect battleSkills. Used to save the selected skills for another
   *  game session in serialization.
   */
  protected void updateSelectedSkills() {
    for (int i = 0; i < 4; i += 1) {
      if (battleSkills[i] != null) {
        selectedSkills[i] = battleSkills[i].getName();
      }
    }
  }
  
  /** An array of courses the player has taken. */
  @Expose private ArrayList<Course> pastCourses = new ArrayList<Course>();

  /** An array of the player's skills. */
  @Expose private ArrayList<Skill> skills = new ArrayList<Skill>();

  /** Initial parameters. */
  @Expose private int currHp = 100;
  @Expose private int currMp = 100;
  @Expose private int maxHp = 100;
  @Expose private int maxMp = 100;
  private int skillIndex = 0;

  /** An array of skills that the player can bring to battle. */
  private Skill[] battleSkills = new Skill[4];

  /** The player's name. */
  @Expose private String name;

  /** An array of the currently selected skills. */
  @Expose private String[] selectedSkills = new String[4];

}

