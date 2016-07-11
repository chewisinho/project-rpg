import static org.junit.Assert.assertTrue;

import org.junit.Test;
import project_rpg.Gui;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/** Unit tests for the GUI.
 *  @author S. Chewi
 */
public class GuiTest {

  @Test
  public void loadGameTest() throws IOException {
    Path tempFile = Paths.get("save0.sav.json");
    BufferedWriter writer = Files.newBufferedWriter(tempFile);
    String saveGame = String.join(
        "\n",
        "{",
        "  \"day\": \"MONDAY\",",
        "  \"week\": 4,",
        "  \"quarter\": \"FALL\",",
        "  \"year\": \"FRESHMAN\",",
        "  \"availableCourseNames\": [",
        "    \"introduction_to_lightning_magic\",",
        "    \"introduction_to_wind_magic\"",
        "  ],",
        "  \"enrolledCourseNames\": [",
        "    \"introduction_to_earth_magic\",",
        "    \"introduction_to_fire_magic\",",
        "    \"introduction_to_water_magic\",",
        "    \"introduction_to_ice_magic\"",
        "  ],",
        "  \"player\": {",
        "    \"pastCourses\": [",
        "      {",
        "        \"ready\": false,",
        "        \"week\": 5,",
        "        \"fileName\": \"introduction_to_earth_magic\",",
        "        \"skillFileName\": \"earth_spike\"",
        "      },",
        "      {",
        "        \"ready\": true,",
        "        \"week\": 1,",
        "        \"fileName\": \"introduction_to_fire_magic\",",
        "        \"skillFileName\": \"fireball\"",
        "      },",
        "      {",
        "        \"ready\": true,",
        "        \"week\": 1,",
        "        \"fileName\": \"introduction_to_water_magic\",",
        "        \"skillFileName\": \"fireball\"",
        "      },",
        "      {",
        "        \"ready\": true,",
        "        \"week\": 1,",
        "        \"fileName\": \"introduction_to_ice_magic\",",
        "        \"skillFileName\": \"iceball\"",
        "      }",
        "    ],",
        "    \"skills\": [",
        "      {",
        "        \"baseDamage\": 0,",
        "        \"baseMp\": -30,",
        "        \"cooldown\": 30000,",
        "        \"damage\": 0,",
        "        \"exp\": 35,",
        "        \"mp\": -30,",
        "        \"rank\": 0,",
        "        \"behavior\": \"nothing\",",
        "        \"description\": \"Restores mana.\",",
        "        \"fileName\": \"meditate\",",
        "        \"image\": \"exit\",",
        "        \"name\": \"Meditate\"",
        "      },",
        "      {",
        "        \"baseDamage\": 10,",
        "        \"baseMp\": 3,",
        "        \"cooldown\": 1000,",
        "        \"damage\": 20,",
        "        \"exp\": 229,",
        "        \"mp\": 6,",
        "        \"rank\": 4,",
        "        \"behavior\": \"earthAttack\",",
        "        \"description\": \"A spike made of Earth!\",",
        "        \"fileName\": \"earth_spike\",",
        "        \"image\": \"EarthSpike\",",
        "        \"name\": \"Earth Spike\"",
        "      },",
        "      {",
        "        \"baseDamage\": 10,",
        "        \"baseMp\": 3,",
        "        \"cooldown\": 1000,",
        "        \"damage\": 10,",
        "        \"exp\": 35,",
        "        \"mp\": 3,",
        "        \"rank\": 0,",
        "        \"behavior\": \"straightLine\",",
        "        \"description\": \"A ball of fire!\",",
        "        \"fileName\": \"fireball\",",
        "        \"image\": \"Fireball\",",
        "        \"name\": \"Fireball\"",
        "      },",
        "      {",
        "        \"baseDamage\": 10,",
        "        \"baseMp\": 3,",
        "        \"cooldown\": 1000,",
        "        \"damage\": 10,",
        "        \"exp\": 35,",
        "        \"mp\": 3,",
        "        \"rank\": 0,",
        "        \"behavior\": \"straightLine\",",
        "        \"description\": \"A ball of fire!\",",
        "        \"fileName\": \"fireball\",",
        "        \"image\": \"Fireball\",",
        "        \"name\": \"Fireball\"",
        "      },",
        "      {",
        "        \"baseDamage\": 10,",
        "        \"baseMp\": 3,",
        "        \"cooldown\": 1000,",
        "        \"damage\": 10,",
        "        \"exp\": 35,",
        "        \"mp\": 3,",
        "        \"rank\": 0,",
        "        \"behavior\": \"straightLine\",",
        "        \"description\": \"A ball of ice!\",",
        "        \"fileName\": \"iceball\",",
        "        \"image\": \"Fireball\",",
        "        \"name\": \"Iceball\"",
        "      }",
        "    ],",
        "    \"currHp\": 100,",
        "    \"currMp\": 10,",
        "    \"maxHp\": 100,",
        "    \"maxMp\": 100,",
        "    \"name\": \"Jimmy\",",
        "    \"selectedSkills\": [",
        "      \"Meditate\",",
        "      \"Fireball\",",
        "      \"Iceball\",",
        "      \"Earth Spike\"",
        "    ]",
        "  }",
        "}"
    );
    writer.write(saveGame);
    writer.close();
    Gui gui = new Gui(0);
    assertTrue(gui.gameInitialized());
    Files.delete(tempFile);
  }

}

