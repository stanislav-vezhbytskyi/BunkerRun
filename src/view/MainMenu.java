package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import model.BunkerRunButton;
import model.BackgroundMusic;

import static view.ViewManager.HEIGHT;
import static view.ViewManager.WIDTH;

public class MainMenu {
    private Pane menuPane;
    private Scene menuScene;
    private StackPane contentPane;

    public MainMenu() {
        // Start the background music
        BackgroundMusic.getInstance().startSong("src/resources/song1.mp3");
        BackgroundMusic.getInstance().play();

        menuPane = new Pane();

        // Create and style the label
        Label label = new Label("Project 2023");
        label.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 14));
        label.setTextFill(Color.WHITE);
        label.setPrefWidth(1200);
        label.setLayoutY(650);
        label.setAlignment(Pos.CENTER);

       Media media1 = new Media(MainMenu.class.getResource("/resources/background.mp4").toExternalForm());
        MediaPlayer mediaPlayer1 = new MediaPlayer(media1);

        MediaView mediaView1 = new MediaView(mediaPlayer1);
        mediaView1.setFitWidth(WIDTH);
        mediaView1.setFitHeight(HEIGHT);
        menuPane.getChildren().add(mediaView1);

        mediaPlayer1.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                Media media2 = new Media(MainMenu.class.getResource("/resources/movingBackground.mp4").toExternalForm());

                // Create and configure the media player for the moving background video
                MediaPlayer mediaPlayer2 = new MediaPlayer(media2);
                mediaPlayer2.setCycleCount(MediaPlayer.INDEFINITE);

                // Create and style the second label
                Label label2 = new Label("Project 2023");
                label2.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 14));
                label2.setTextFill(Color.WHITE);
                label2.setPrefWidth(1200);
                label2.setLayoutY(650);
                label2.setAlignment(Pos.CENTER);

                MediaView mediaView2 = new MediaView(mediaPlayer2);
                mediaView2.setFitWidth(WIDTH);
                mediaView2.setFitHeight(HEIGHT);

                // Add the second video and label to the menu pane
                menuPane.getChildren().addAll(mediaView2);
                menuPane.getChildren().add(label2);
                createButtons();

                mediaPlayer2.play();
            }
        });

        mediaPlayer1.play();

        createButtons();
        menuPane.getChildren().addAll(label);

        menuScene = new Scene(menuPane, WIDTH, HEIGHT);
        createButtons();

    }

    // Create the background image for the menu pane
    private void createBackground() {
        Image backgroundImage = new Image("resources/Menu.jpg", 1200, 675, false, true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        menuPane.setBackground(new Background(background));
    }

    // Create buttons and add them to the menu pane
    private void createButtons() {
        BunkerRunButton startButton = new BunkerRunButton("ПОЧАТИ ГРУ", 1);
        startButton.setLayoutX(395);
        startButton.setLayoutY(200);
        menuPane.getChildren().add(startButton);

        startButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    startButton.setButtonReleasedStyle();
                    BackgroundMusic.getInstance().stop();
                    ViewManager.getInstance().getGameManager().startGame();
                }

            }
        });

        // Create and configure the store button
        BunkerRunButton storeButton = new BunkerRunButton("МАГАЗИН", 0);
        storeButton.setLayoutX(395);
        storeButton.setLayoutY(295);
        menuPane.getChildren().add(storeButton);

        storeButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton().equals(MouseButton.PRIMARY)) {
                    storeButton.setButtonReleasedStyle();
                    ViewManager.getInstance().getStoreManager().openStore();
                }
            }
        });

        // Create and configure the settings button
        BunkerRunButton settingsButton = new BunkerRunButton("НАЛАШТУВАННЯ", 0);
        settingsButton.setLayoutX(395);
        settingsButton.setLayoutY(370);
        menuPane.getChildren().add(settingsButton);
        settingsButton.setOnMouseReleased(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    settingsButton.setButtonReleasedStyle();
                    ViewManager.getInstance().goToSettings();
                }

            }
        });

        // Create and configure the contacts button
        BunkerRunButton aboutButton = new BunkerRunButton("ЗВ'ЯЗОК", 0);
        aboutButton.setLayoutX(395);
        aboutButton.setLayoutY(445);
        menuPane.getChildren().add(aboutButton);

        aboutButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                aboutButton.setButtonReleasedStyle();
                ViewManager.getInstance().goToContacts();
            }
        });

        // Create and configure the exit button
        BunkerRunButton exitButton = new BunkerRunButton("ВИЙТИ", 2);
        exitButton.setLayoutX(395);
        exitButton.setLayoutY(520);
        menuPane.getChildren().add(exitButton);

        exitButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    exitButton.setButtonReleasedStyle();
                    System.exit(0);
                }

            }
        });
    }

    public Scene getScene() {
        return menuScene;
    }
}
