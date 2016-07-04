import static org.junit.Assert.assertEquals;

import org.junit.Test;
import project_rpg.Monster;

/** Unit tests for the Monster class.
 *  @author S. Chewi
 */
public class MonsterTest {

  @Test
  public void loadMonsterFromJsonTest() {
    Monster test = new Monster("ice_pig");
    assertEquals("Ice Pig", test.getName());
  }

}

