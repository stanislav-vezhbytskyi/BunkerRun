package view;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import model.BunkerRunButton;
import model.BackgroundMusic;

import static view.ViewManager.HEIGHT;
import static view.ViewManager.WIDTH;

public class MainMenu {
    //private BackgroundMusic mainTheme = new BackgroundMusic("src/music/song1.mp3");
    private AnchorPane menuPane;
    private Scene menuScene;

    public MainMenu() {
        BackgroundMusic.getInstance().startSong("src/music/song1.mp3");
        BackgroundMusic.getInstance().play();
        menuPane = new AnchorPane();
        menuScene = new Scene(menuPane, WIDTH, HEIGHT);
        createBackground();
        createButtons();
    }

    private void createBackground() {
        Image backgroundImage = new Image("resources/Menu.jpg", 1200, 675, false, true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        menuPane.setBackground(new Background(background));
    }

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

        BunkerRunButton storeButton = new BunkerRunButton("МАГАЗИН", 0);
        storeButton.setLayoutX(395);
        storeButton.setLayoutY(295);
        menuPane.getChildren().add(storeButton);

        storeButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton().equals(MouseButton.PRIMARY)) {
                    storeButton.setButtonReleasedStyle();
                    BackgroundMusic.getInstance().stop();
                    ViewManager.getInstance().getStoreManager().openStore();
                }
            }
        });

        BunkerRunButton settingsButton = new BunkerRunButton("НАЛАШТУВАННЯ", 0);
        settingsButton.setLayoutX(395);
        settingsButton.setLayoutY(370);
        menuPane.getChildren().add(settingsButton);

        BunkerRunButton aboutButton = new BunkerRunButton("ЗВ'ЯЗОК", 0);
        aboutButton.setLayoutX(395);
        aboutButton.setLayoutY(445);
        menuPane.getChildren().add(aboutButton);

        BunkerRunButton exitButton = new BunkerRunButton("ВИЙТИ", 2);
        exitButton.setLayoutX(395);
        exitButton.setLayoutY(520);
        menuPane.getChildren().add(exitButton);

        exitButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    System.exit(0);
                    exitButton.setButtonReleasedStyle();
                }

            }
        });
    }

    public Scene getScene() {
        return menuScene;
    }
}
