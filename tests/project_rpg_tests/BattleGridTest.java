import static org.junit.Assert.assertTrue;

import org.junit.Test;
import project_rpg.BattleGrid;

/** Unit tests for the BattleGrid.
 *  @author S. Chewi
 */
public class BattleGridTest {

  @Test
  public void testGameOver() {
    BattleGrid testGrid = new BattleGrid(null, null, "test_dungeon");
    testGrid.gameOver();
    assertTrue(testGrid.isGameOver());
  }

}

