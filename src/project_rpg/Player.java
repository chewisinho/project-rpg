package project_rpg;

import java.io.Serializable;
import java.util.ArrayList;

/** Class representing a player.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class Player implements Serializable {

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
    void addCourse(Course newCourse) {
        pastCourses.add(newCourse);
    }

    /** Adds NEWSKILL to the player. */
    void addSkill(Skill newSkill) {
        skills.add(newSkill);
    }

    /** Changes a battle SKILL at INDEX. */
    void changeBattleSkills(int index, Skill skill) {
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

    /** Returns battleSkills. */
    Skill[] getBattleSkills() {
        return battleSkills;
    }

    /** Returns current HP. */
    public int getHP() {
        return currHP;
    }

    /** Returns max HP. */
    public int getMaxHP() {
        return maxHP;
    }

    /** Returns max MP. */
    public int getMaxMP() {
        return maxMP;
    }
    
    /** Returns current MP. */
    public int getMP() {
        return currMP;
    }

    /** Returns the skill at INDEX. */
    public Skill getSkill(int index) {
        return skills.get(index);
    }

    /** Returns the skill index. */
    public int getSkillIndex() {
        return skillIndex;
    }

    /** Returns the player's skills. */
    ArrayList<Skill> getSkills() {
        return skills;
    }

    /** Checks if the player has enough MP to use a certain skill with COST. */
    public boolean hasEnoughMP(int cost) {
    	if (cost > currMP) {
    		return false;
    	} else {
    		return true;
    	}
    }

    /** Increases my max health by AMOUNT. */
    void increaseHealth(int amount) {
        maxHP += amount;
    }

    /** Increases my max mana by AMOUNT. */
    void increaseMana(int amount) {
        maxMP += amount;
    }

    /** Returns true iff I am dead. */
    public boolean isDead() {
        return currHP <= 0;
    }

    /** Returns true iff I am out of MP. */
    public boolean isOutOfMana() {
        return currMP <= 0;
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

    /** Reduces my health by AMOUNT. */
    public void reduceHealth(int amount) {
        currHP -= amount;
        if (currHP < 0) {
        	currHP = 0;
        }
    }

    /** Reduces my mana by AMOUNT. */
    public void reduceMana(int amount) {
        currMP -= amount;
        if (currMP < 0) {
        	currMP = 0;
        } else if (currMP > maxMP) {
        	currMP = maxMP;
        }
    }

    /** Restores HP and MP to max values. */
    void restore() {
        currHP = maxHP;
        currMP = maxMP;
    }

    /** Switches to Skill 1. */
    public void switchAttack1() {
        skillIndex = 0;
    }

    /** Switches to Skill 2. */
    public void switchAttack2() {
        skillIndex = 1;
    }

    /** Switches to Skill 3. */
    public void switchAttack3() {
        skillIndex = 2;
    }

    /** Switches to Skill 4. */
    public void switchAttack4() {
        skillIndex = 3;
    }
    
    /** An array of courses the player has taken. */
    private ArrayList<Course> pastCourses = new ArrayList<Course>();

    /** An array of the player's skills. */
    private ArrayList<Skill> skills = new ArrayList<Skill>();

    /** Initial parameters. */
    private int currHP = 100, currMP = 100, maxHP = 100, maxMP = 100, skillIndex = 0;

    /** An array of skills that the player can bring to battle. */
    private Skill[] battleSkills = new Skill[4];

    /** The player's name. */
    private String name;

}

