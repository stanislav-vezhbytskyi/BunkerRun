package model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class Sounds {
    private static Sounds instance;

    public static Sounds getInstance() {
        if (instance == null) {
            instance = new Sounds();
        }
        return instance;
    }

    private Media media = new Media(new File("src/music/running.mp3").toURI().toString());
    private MediaPlayer mediaPlayer = new MediaPlayer(media);
    private Media jumpMedia = new Media(new File("src/music/jump.mp3").toURI().toString());
    private MediaPlayer jumpPlayer = new MediaPlayer(jumpMedia);
    private Media buttonMedia = new Media(new File("src/music/buttonSound.mp3").toURI().toString());
    private MediaPlayer buttonPlayer = new MediaPlayer(buttonMedia);

    public void startRunning() {
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
            }
        });

        mediaPlayer.play();

    }

    public void stopRunning() {
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
        }
    }

    public void jump() {
        if (jumpPlayer.getCurrentTime().greaterThan(new Duration(200)) || jumpPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
            jumpPlayer.stop();
            jumpPlayer.play();
        }
    }

    public void clickButtonSound() {
        if (buttonPlayer.getCurrentTime().greaterThan(new Duration(200)) || buttonPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
            buttonPlayer.stop();
            buttonPlayer.play();
        }
    }
}
