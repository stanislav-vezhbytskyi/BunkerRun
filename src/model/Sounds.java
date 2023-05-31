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

    private Media runningMedia = new Media(new File("src/resources/running.mp3").toURI().toString());
    private MediaPlayer runningPlayer = new MediaPlayer(runningMedia);
    private Media jumpMedia = new Media(new File("src/resources/jump.mp3").toURI().toString());
    private MediaPlayer jumpPlayer = new MediaPlayer(jumpMedia);
    private Media buttonMedia = new Media(new File("src/resources/buttonSound.mp3").toURI().toString());
    private MediaPlayer buttonPlayer = new MediaPlayer(buttonMedia);
    private Media attackMedia = new Media(new File("src/resources/epicWoosh.mp3").toURI().toString());
    private MediaPlayer attackPlayer = new MediaPlayer(attackMedia);
    private Media winMedia = new Media(new File("src/resources/winSound.mp3").toURI().toString());
    private MediaPlayer winPlayer = new MediaPlayer(winMedia);
    private Media looseMedia = new Media(new File("src/resources/looseSound.mp3").toURI().toString());
    private MediaPlayer loosePlayer = new MediaPlayer(looseMedia);
    private double currentVolume = 1;

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
    }

    public void jump() {
        if (jumpPlayer.getCurrentTime().greaterThan(new Duration(200)) || jumpPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
            jumpPlayer.stop();
            jumpPlayer.play();
        }
    }

    public void attack() {
        if (attackPlayer.getCurrentTime().greaterThan(new Duration(200)) || attackPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
            attackPlayer.stop();
            attackPlayer.play();
        }
    }

    public void win() {
        if (winPlayer.getCurrentTime().greaterThan(new Duration(200)) || winPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
            winPlayer.stop();
            winPlayer.play();
        }
    }

    public void notWin() {
        if (loosePlayer.getCurrentTime().greaterThan(new Duration(200)) || loosePlayer.getStatus() != MediaPlayer.Status.PLAYING) {
            loosePlayer.stop();
            loosePlayer.play();
        }
    }

    public void clickButtonSound() {
        if (buttonPlayer.getCurrentTime().greaterThan(new Duration(200)) || buttonPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
            buttonPlayer.stop();
            buttonPlayer.play();
        }
    }

    public void setVolume(double aVolume) {
        runningPlayer.setVolume(aVolume);
        jumpPlayer.setVolume(aVolume);
        attackPlayer.setVolume(aVolume);
        currentVolume = aVolume;
    }

    public double getCurrentVolume() {
        return currentVolume;
    }
}
