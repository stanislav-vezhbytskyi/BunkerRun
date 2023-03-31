package view;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.BunkerRunButton;

public class ViewManager {
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 675;
    private AnchorPane mainPane;
    private Scene mainScene;
    private Stage mainStage;

    public ViewManager() {
        mainPane = new AnchorPane();
        mainScene = new Scene(mainPane, WIDTH, HEIGHT);
        mainStage = new Stage();
        mainStage.setScene(mainScene);
        mainStage.setResizable(false);
        mainStage.setTitle("Bunker Run");
        createBackground();
        createButtons();
    }

    public Stage getMainStage() {
        return mainStage;
    }

    private void createBackground() {
        Image backgroundImage = new Image("resources/Menu.jpg", 1200, 675, false, true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        mainPane.setBackground(new Background(background));
    }

    private void createButtons() {
        BunkerRunButton startButton = new BunkerRunButton("ПОЧАТИ ГРУ", 1);
        startButton.setLayoutX(395);
        startButton.setLayoutY(200);
        mainPane.getChildren().add(startButton);

        BunkerRunButton storeButton = new BunkerRunButton("МАГАЗИН", 0);
        storeButton.setLayoutX(395);
        storeButton.setLayoutY(295);
        mainPane.getChildren().add(storeButton);

        BunkerRunButton settingsButton = new BunkerRunButton("НАЛАШТУВАННЯ", 0);
        settingsButton.setLayoutX(395);
        settingsButton.setLayoutY(370);
        mainPane.getChildren().add(settingsButton);

        BunkerRunButton aboutButton = new BunkerRunButton("ЗВ'ЯЗОК", 0);
        aboutButton.setLayoutX(395);
        aboutButton.setLayoutY(445);
        mainPane.getChildren().add(aboutButton);

        BunkerRunButton exitButton = new BunkerRunButton("ВИЙТИ", 2);
        exitButton.setLayoutX(395);
        exitButton.setLayoutY(520);
        mainPane.getChildren().add(exitButton);
    }
}
