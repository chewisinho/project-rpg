package project_rpg.audio;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
import java.util.StringJoiner;

import project_rpg.Main;

/** Simple handler that provides utility methods for playing audio.
 *  @author S. Chewi, T. Nguyen, A. Tran
 */
public class AudioHandler {

    /** Stops the current clip and starts playing FILE. */
    public void playSong(String file) {
        if (clip != null) {
            clip.stop();
        }
        String filePath = new StringJoiner(File.separator)
            .add("project_rpg")
            .add("resources")
            .add("audio")
            .add(file + ".wav")
            .toString();
        File audioFile = new File(filePath);
        try {
            clip = Applet.newAudioClip(audioFile.toURL());
            clip.loop();
        } catch (MalformedURLException exception) {
            Main.error("Malformed audio file path: " + filePath + ".");
        }
    }

    /** The current clip that is playing. */
    private AudioClip clip;

}
