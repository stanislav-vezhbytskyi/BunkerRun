package model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class BackgroundMusic {
    private static BackgroundMusic instance;

    public static BackgroundMusic getInstance() {
        if (instance == null) {
            instance = new BackgroundMusic();
        }
        return instance;
    }
    private double currentVolume = 1;
    private Media media;
    private MediaPlayer mediaPlayer;

    public void startSong(String fileName) {
        media = new Media(new File(fileName).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(currentVolume);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
            }
        });
        mediaPlayer.play();
    }

    public void setVolume(Double aVolume) {
        mediaPlayer.setVolume(aVolume);
        currentVolume = aVolume;
    }

    public void play() {
        mediaPlayer.play();
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public double getCurrentVolume() {
        return currentVolume;
    }
}
