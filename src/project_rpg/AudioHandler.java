package project_rpg;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
import java.util.StringJoiner;

/** Simple handler that provides utility methods for playing audio.
 *  @author S. Chewi
 */
public class AudioHandler {

  /** Stops the current clip and starts playing FILE. */
  public void playSong(String file) {
    if (clip != null && song != file) {
      clip.stop();
    }
    if (song == file) {
      return;
    }
    song = file;
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
  
  /** The current file that is playing. */
  private String song;

}

