package project_rpg;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

/** Provides an iterator method that iterates through the scenes in a cutscene.
 *  @author S. Chewi
 */
public class Cutscene
    implements Iterable<HashMap<String, String>>, Iterator<HashMap<String, String>> {

    /** Loads in the cutscene from the JSON file NAME.json. */
    public Cutscene(String name) {
        try {
            String file = new StringJoiner(File.separator)
                .add("project_rpg")
                .add("database")
                .add("cutscenes")
                .add(name + ".json")
                .toString();
            BufferedReader input = new BufferedReader(new FileReader(new File(file)));
            JsonParser parser = new JsonParser();
            JsonObject attrTree = (JsonObject) parser.parse(input);
            JsonArray sceneArray = (JsonArray) attrTree.get("scenes");
            Iterator<JsonElement> scenes = sceneArray.iterator();
        } catch (IOException error) {
            Main.error("Error while reading " + name + ".json.");
        }
    }

    @Override
    public boolean hasNext() {
        return scenes.hasNext();
    }

    @Override
    public Iterator<HashMap<String, String>> iterator() {
        return this;
    }

    @Override
    public HashMap<String, String> next() {
        if (hasNext()) {
            JsonObject scene = (JsonObject) scenes.next();
            HashMap<String, String> dict = new HashMap<String, String>();
            for (String field : fields) {
                dict.put(field, scene.get("field").getAsString());
            }
            return dict;
        } else {
            throw new NoSuchElementException();
        }
    }

    /** The parsed list of scenes from the JSON file. */
    private Iterator<JsonElement> scenes;

    /** The fields in a scene. */
    private String[] fields = { "image", "line", "speaker" };

}

