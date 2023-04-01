package view;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import model.BunkerRunButton;

import static view.ViewManager.HEIGHT;
import static view.ViewManager.WIDTH;

public class GameField {
    private AnchorPane gamePane;
    private Scene gameScene;

    public GameField() {
        initGame();
    }

    private void initGame() {
        gamePane = new AnchorPane();
        gameScene = new Scene(gamePane, WIDTH, HEIGHT);
        Image backgroundImage = new Image("resources/blackBackground.jpg", 1200, 675, false, true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        gamePane.setBackground(new Background(background));
    }

    public void startGame() {
        ViewManager.getInstance().setMainScene(gameScene);
    }
}
