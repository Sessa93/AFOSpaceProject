package JFXClient;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

/**
 * A wrap class to manage audio files and their reproduction
 * @author Andrea Sessa
 */

public class Sound {
    private String filename;
    private Media sound;
    private MediaPlayer player;

    public Sound(String filename) {
        this.filename = filename;
        this.sound = new Media(new File(filename).toURI().toString());
        this.player = new MediaPlayer(sound);
    }

    public void Play() {
        this.player.stop();
        this.player.play();
    }

    public  void Stop() {
        this.player.stop();
    }

    public void setVolume(double volume) {
        this.player.setVolume(volume);
    }

    public double getVolume() {
        return this.player.getVolume();
    }

    public void setStopTime(int milliseconds) {
        this.player.setStopTime(new Duration(milliseconds));
    }
}
