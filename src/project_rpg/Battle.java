package project_rpg;

/** Simulates a battle between the player and a monster.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class Battle {

    /** Constructs a new battle between PLAYER and MONSTER. */
    public Battle(Player player, Monster monster) {
        _player = player;
        _monster = monster;
        System.out.println(_monster.getName() + " has appeared!");
        System.out.printf("You have %s/%s HP remaining\n", _player.getHP(), _player.getMaxH());
    }

    /** Returns true iff the battle is won. */
    boolean isWon() {
        return _monster.isDead();
    }

    /** Returns true iff the battle is lost. */
    boolean isLost() {
        return _player.isDead();
    }

    /** Plays a turn where the player uses skill at INDEX. */
    void useSkill(int index) {
        int playerDamage = _player.getSkill(index).attack();
        int playerManaUsage = _player.getSkill(index).getCost();
        _monster.reduceHealth(playerDamage);
        _player.reduceMana(playerManaUsage);
        System.out.printf("You did %s damage! The monster has %s HP "
            + "remaining.\n", playerDamage, Math.max(_monster.hp, 0));
        if (!_monster.isDead()) {
            int monsterDamage = _monster.attack();
            _player.reduceHealth(monsterDamage);
            System.out.printf("The enemy did %s damage! You have %s/%s HP "
                + "remaining.\n", monsterDamage, _player.getHP(),
                _player.getMaxHP());
        }
    }

    /** The player in the battle. */
    private Player _player;

    /** The monster in the battle. */
    private Monster _monster;

}
