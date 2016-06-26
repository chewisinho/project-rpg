import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import project_rpg.Cutscene;

import java.util.HashMap;

/** Unit tests for the Cutscene parser.
 *  @author S. Chewi
 */
public class CutsceneTest {

  @Test
  public void loadCutsceneFromJsonTest() {
    Cutscene test = new Cutscene("test");
    HashMap<String, String> scene = test.next();
    assertEquals("Failed to parse the first scene's image.", "test", scene.get("image"));
    assertEquals(
        "Failed to parse the first scene's line.",
        "The following is a set of sample quotes for testing.",
        scene.get("line")
    );
    assertEquals("Failed to parse the first scene's speaker.", "Player", scene.get("speaker"));
    assertTrue("Failed to parse second scene.", test.hasNext());
    test.next();
    assertTrue("Failed to parse third scene.", test.hasNext());
    test.next();
    assertTrue("Failed to parse fourth scene.", test.hasNext());
    test.next();
    assertTrue("Failed to parse fifth scene.", test.hasNext());
    test.next();
    assertFalse("Parsed too many scenes.", test.hasNext());
  }

}

