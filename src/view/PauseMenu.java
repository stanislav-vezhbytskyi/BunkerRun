package view;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import model.BackgroundMusic;
import model.BunkerRunButton;
import model.Sounds;

import static view.ViewManager.getInstance;

public class PauseMenu {
    public static void openPauseMenu(GameField gameField){
        Stage stage = new Stage();

        Pane settingsPane = new Pane();
        settingsPane.setBackground(Background.fill(Color.color(48/255.0, 78/255.0, 78/255.0)));

        Label musicLabel = new Label("Фонова музика");
        musicLabel.setLayoutX(100);
        musicLabel.setLayoutY(15);
        musicLabel.setTextFill(Color.WHITESMOKE);
        musicLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 14));
        settingsPane.getChildren().add(musicLabel);
        Slider musicVolumeSlider = new Slider();
        musicVolumeSlider.setLayoutX(100);
        musicVolumeSlider.setLayoutY(45);
        musicVolumeSlider.setValue(BackgroundMusic.getInstance().getCurrentVolume() * 100);
        musicVolumeSlider.setPrefWidth(200);
        musicVolumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                BackgroundMusic.getInstance().setVolume(musicVolumeSlider.getValue()/100);
            }
        });
        Label soundsLabel = new Label("Звуки в грі");
        soundsLabel.setLayoutX(100);
        soundsLabel.setLayoutY(75);
        soundsLabel.setTextFill(Color.WHITESMOKE);
        soundsLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 14));
        settingsPane.getChildren().add(soundsLabel);
        Slider soundsVolumeSlider = new Slider();
        soundsVolumeSlider.setLayoutX(100);
        soundsVolumeSlider.setLayoutY(105);
        soundsVolumeSlider.setValue(Sounds.getInstance().getCurrentVolume() * 100);
        soundsVolumeSlider.setPrefWidth(200);
        soundsVolumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                Sounds.getInstance().setVolume(soundsVolumeSlider.getValue()/100);
            }
        });

        Button backButton = new Button();
        backButton.setLayoutX(15);
        backButton.setLayoutY(15);
        backButton.setPrefWidth(70);
        backButton.setPrefHeight(70);
        //backButton.getStyleClass().add("back-button");
        backButton.setStyle("-fx-background-color: #00f9ff;\n" +
                "    -fx-background-radius: 50;\n" +
                "    -fx-padding: 5;\n" +
                "    -fx-background-image: url('arrow.png');\n" +
                "    -fx-background-size: 50 50;\n" +
                "    -fx-background-repeat: no-repeat;\n" +
                "    -fx-background-position: center;");
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

        settingsPane.getChildren().addAll(musicVolumeSlider, soundsVolumeSlider, backButton);
        Scene settingsScene = new Scene(settingsPane,450,250);

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                gameField.setGameOnPause(false);
            }
        });

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);

        Rectangle bg = new Rectangle(0,0,450,250);

        bg.setFill(Color.color(48/255.0, 78/255.0, 78/255.0));
        Pane pane = new Pane();

        BunkerRunButton backToGame = new BunkerRunButton("Повернутись до гри",1);
        backToGame.setLayoutY(30);
        backToGame.setLayoutX(20);
        backToGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gameField.setGameOnPause(false);
                stage.close();
            }
        });

        BunkerRunButton settingButton = new BunkerRunButton("Налаштування",3);
        settingButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.close();
                stage.setScene(settingsScene);
                stage.show();
            }
        });
        settingButton.setLayoutY(100);
        settingButton.setLayoutX(20);

        BunkerRunButton backToMainMenu = new BunkerRunButton("Вихід",2);
        backToMainMenu.setLayoutY(170);
        backToMainMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.close();
                BackgroundMusic.getInstance().stop();
                ViewManager.getInstance().switchToMainMenu();
            }
        });
        backToMainMenu.setLayoutX(20);


        pane.getChildren().addAll(bg,backToMainMenu,backToGame,settingButton);

        Scene scene = new Scene(pane,450,250);
        stage.setScene(scene);
        backButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    Sounds.getInstance().clickButtonSound();
                    stage.setScene(scene);
                    stage.show();
                }
            }
        });
        stage.show();
    }
}
