package project_rpg;

import java.io.Serializable;
import java.util.ArrayList;

/** Class representing a player.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class Player implements Serializable {

    /** Initializes new player given GIVENNAME. */
    public Player(String givenName) {
        name = givenName;
    }

    /** Initializes player without a name. */
    public Player() {
        this("Jimmy");
    }

    /** Returns the current battleSkill. */
    public Skill getBattleSkill() {
    	return battleSkill;
    }
    
    /** Returns current HP. */
    public int getHP() {
        return currHP;
    }

    /** Returns max HP. */
    int getMaxHP() {
        return maxHP;
    }

    /** Returns current MP. */
    public int getMP() {
        return currMP;
    }

    /** Returns max MP. */
    int getMaxMP() {
        return maxMP;
    }
    
    /** Checks if the player has enough MP to use a certain skill with COST. */
    public boolean hasEnoughMP(int cost) {
    	if (cost > currMP) {
    		return false;
    	} else {
    		return true;
    	}
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
        }
    }

    /** Increases my max health by AMOUNT. */
    public void increaseHealth(int amount) {
        maxHP += amount;
    }

    /** Increases my max mana by AMOUNT. */
    public void increaseMana(int amount) {
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

    /** Restores HP and MP to max values. */
    public void restore() {
        currHP = maxHP;
        currMP = maxMP;
    }

    /** Adds NEWSKILL to the player. */
    public void addSkill(Skill newSkill) {
        skills.add(newSkill);
    }

    /** Adds NEWCOURSE to a list of taken courses. */
    public void addCourse(Course newCourse) {
        pastCourses.add(newCourse);
    }

    /** Adds NEWASSIGNMENT to a list of taken courses. */
    public void addAssignment(Assignment newAssignment) {
        pastAssignments.add(newAssignment);
    }

    /** Prints a list of the player's skills. */
    void printSkills() {
        int index = 0;
        for (Skill skill : skills) {
            System.out.printf("%s: %s (%s MP)\n", index, skill.getName(),
                skill.getCost());
            index += 1;
        }
    }

    /** Returns the skill at INDEX. */
    Skill getSkill(int index) {
        return skills.get(index);
    }

    /** Returns the player's skills. */
    ArrayList<Skill> getSkills() {
        return skills;
    }

    /** Returns battleSkills. */
    Skill[] getBattleSkills() {
        return battleSkills;
    }

    /** Changes a battle SKILL at INDEX. */
    void changeBattleSkills(int index, Skill skill) {
    	battleSkills[index] = skill;
    }

    /** Switches to Skill 1. */
    public void switchAttack1() {
        battleSkill = battleSkills[0];
    }

    /** Switches to Skill 2. */
    public void switchAttack2() {
        battleSkill = battleSkills[1];
    }

    /** Switches to Skill 3. */
    public void switchAttack3() {
        battleSkill = battleSkills[2];
    }

    /** Switches to Skill 4. */
    public void switchAttack4() {
        battleSkill = battleSkills[3];
    }
    
    /** Prints the status of the player. */
    public void status() {
        System.out.printf("You have %s/%s HP remaining.\n", currHP, maxHP);
        System.out.printf("You have %s/%s MP remaining.\n", currMP, maxMP);
    }

    /** Initial parameters. */
    private int currHP = 100, maxHP = 100, currMP = 100, maxMP = 100;

    /** The player's name. */
    private String name;

    /** An array of the player's skills. */
    private ArrayList<Skill> skills = new ArrayList<Skill>();

    /** An array of courses the player has taken. */
    private ArrayList<Course> pastCourses = new ArrayList<Course>();

    /** An array of assignments the player has completed. */
    private ArrayList<Assignment> pastAssignments = new ArrayList<Assignment>();

    /** An array of skills that the player can bring to battle. */
    private Skill[] battleSkills = new Skill[4];

    /** The current battle skill that the player is using. */
    private Skill battleSkill = battleSkills[0];

}

