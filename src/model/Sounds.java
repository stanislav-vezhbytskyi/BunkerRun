package model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class Sounds {
    private static Sounds instance;

    public Sounds() {
        runningPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                runningPlayer.seek(Duration.ZERO);
            }
        });
    }

    public static Sounds getInstance() {
        if (instance == null) {
            instance = new Sounds();
        }
        return instance;
    }

    private Media runningMedia = new Media(new File("src/music/running.mp3").toURI().toString());
    private MediaPlayer runningPlayer = new MediaPlayer(runningMedia);
    private Media jumpMedia = new Media(new File("src/music/jump.mp3").toURI().toString());
    private MediaPlayer jumpPlayer = new MediaPlayer(jumpMedia);
    private Media buttonMedia = new Media(new File("src/music/buttonSound.mp3").toURI().toString());
    private MediaPlayer buttonPlayer = new MediaPlayer(buttonMedia);
    private double currentVolume;

    public void startRunning() {
        runningPlayer.play();
    }

    public void stopRunning() {
        if (runningPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            runningPlayer.pause();
        }
    }

    public void stopSounds() {

        runningPlayer.stop();
        jumpPlayer.stop();
        /*
        runningPlayer = new MediaPlayer(runningMedia);
        runningPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                runningPlayer.seek(Duration.ZERO);
            }
        });*/
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

    public void setVolume(Double aVolume) {
        runningPlayer.setVolume(aVolume);
        jumpPlayer.setVolume(aVolume);
    }
}
