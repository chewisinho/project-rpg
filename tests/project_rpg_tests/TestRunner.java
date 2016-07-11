import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/** Used to run all of the JUnit tests.
 *  @author S. Chewi
 */
public class TestRunner {

  /** Runs all JUnit tests and prints the results. */
  public static void main(String[] ignored) {
    Class[] classes  = {
        BattleGridTest.class,
        CutsceneTest.class,
        GuiTest.class,
        MonsterTest.class
    };
    for (Class testClass : classes) {
      Result result = JUnitCore.runClasses(testClass);
      for (Failure failure : result.getFailures()) {
        System.out.println(failure.toString());
      }
    }
  }

}

