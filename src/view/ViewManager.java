package view;

import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ViewManager {
    private static ViewManager instance;

    public static ViewManager getInstance() {
        if (instance == null) {
            instance = new ViewManager();
        }
        return instance;
    }

    public static final int WIDTH = 1200;
    public static final int HEIGHT = 675;
    private AnchorPane mainPane;
    private Scene mainScene;
    private Stage mainStage;
    private MainMenu mainMenu = new MainMenu();
    private GameField gameManager = new GameField();

    public ViewManager() {
        mainPane = new AnchorPane();
        mainScene = mainMenu.getScene();
        mainStage = new Stage();
        mainStage.setScene(mainScene);
        mainStage.setResizable(false);
        mainStage.setTitle("Bunker Run");
    }

    public GameField getGameManager() {
        return  gameManager;
    }

    public void setMainScene(Scene aScene) {
        mainStage.setScene(aScene);
    }

    public void switchToMainMenu() {
        mainStage.setScene(mainMenu.getScene());
    }

    public Stage getMainStage() {
        return mainStage;
    }
}
