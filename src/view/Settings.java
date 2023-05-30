package view;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import model.BackgroundMusic;
import model.Sounds;

import static view.ViewManager.HEIGHT;
import static view.ViewManager.WIDTH;

public class Settings {
    private AnchorPane settingsPane;
    private Scene settingsScene;
    private Slider musicVolumeSlider;
    private Slider soundsVolumeSlider;
    private Label musicLabel;
    private Label soundsLabel;

    public Settings() {
        settingsPane = new AnchorPane();
        settingsScene = new Scene(settingsPane, WIDTH, HEIGHT);
        createBackground();
        createButtons();
        createSliders();
    }

    private void createBackground() {
        Image backgroundImage = new Image("resources/settingsBackground.jpg", 1200, 675, false, true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        settingsPane.setBackground(new Background(background));
    }

    private void createButtons() {
        Button backButton = new Button();
        backButton.setLayoutX(34);
        backButton.setLayoutY(34);
        backButton.setPrefWidth(70);
        backButton.setPrefHeight(70);
        backButton.setStyle("-fx-background-color: #00f9ff;\n" +
                "    -fx-background-radius: 50;\n" +
                "    -fx-padding: 5;\n" +
                "    -fx-background-image: url('arrow.png');\n" +
                "    -fx-background-size: 50 50;\n" +
                "    -fx-background-repeat: no-repeat;\n" +
                "    -fx-background-position: center;");
        backButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    Sounds.getInstance().clickButtonSound();
                    ViewManager.getInstance().switchToMainMenu();
                }
            }
        });
        backButton.setOnMouseEntered(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                DropShadow effect = new DropShadow(BlurType.GAUSSIAN, Color.rgb(255, 255, 255, 0.6), 20, 0.5, 0, 0);
                backButton.setEffect(effect);

            }
        });

        backButton.setOnMouseExited(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                backButton.setEffect(null);

            }
        });
        settingsPane.getChildren().add(backButton);
    }

    public void createSliders() {
        musicLabel = new Label("Фонова музика");
        musicLabel.setLayoutX(80);
        musicLabel.setLayoutY(200);
        musicLabel.setTextFill(Color.WHITESMOKE);
        musicLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 32));
        settingsPane.getChildren().add(musicLabel);
        musicVolumeSlider = new Slider();
        musicVolumeSlider.setLayoutX(80);
        musicVolumeSlider.setLayoutY(250);
        musicVolumeSlider.setValue(100);
        musicVolumeSlider.setPrefWidth(350);

        settingsPane.getChildren().add(musicVolumeSlider);
        musicVolumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                BackgroundMusic.getInstance().setVolume(musicVolumeSlider.getValue()/100);
            }
        });
        soundsLabel = new Label("Звуки в грі");
        soundsLabel.setLayoutX(80);
        soundsLabel.setLayoutY(300);
        soundsLabel.setTextFill(Color.WHITESMOKE);
        soundsLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 32));
        settingsPane.getChildren().add(soundsLabel);
        soundsVolumeSlider = new Slider();
        soundsVolumeSlider.setLayoutX(80);
        soundsVolumeSlider.setLayoutY(350);
        soundsVolumeSlider.setValue(100);
        soundsVolumeSlider.setPrefWidth(350);
        settingsPane.getChildren().add(soundsVolumeSlider);
        soundsVolumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                Sounds.getInstance().setVolume(soundsVolumeSlider.getValue()/100);
            }
        });
    }

    public Scene getSettingsScene() {
        return settingsScene;
    }
}
