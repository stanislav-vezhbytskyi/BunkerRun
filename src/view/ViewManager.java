package view;

import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.BackgroundMusic;
import model.Coins;

public class ViewManager {
    private static ViewManager instance;

    public static ViewManager getInstance() {
        if (instance == null) {
            instance = new ViewManager();
        }
        return instance;
    }

    private static Coins coins;

    public static Coins getCoins() {
        if(coins == null)
            coins = new Coins();
        return coins;
    }

    public static final int WIDTH = 1200;
    public static final int HEIGHT = 675;
    private AnchorPane mainPane;
    private Scene mainScene;
    private Stage mainStage;
    private MainMenu mainMenu = new MainMenu();
    private GameField gameManager;
    private Store storeManager;
    private BackgroundMusic backgroundMusic;
//    private Mode mode;

    public ViewManager() {
        mainPane = new AnchorPane();
        mainScene = mainMenu.getScene();
        mainStage = new Stage();
        mainStage.setScene(mainScene);
        mainStage.setResizable(false);
        mainStage.setTitle("Bunker Run");
    }

    public GameField getGameManager() {
        gameManager = new GameField();
        return  gameManager;
    }
     public Store getStoreManager() {
        storeManager = new Store();
        return storeManager;
     }

//    public void setMode(Mode aMode) {
//        mode = aMode;
//    }
//
//    public Mode getMode() {return mode;}

    public void setMainScene(Scene aScene) {
        mainStage.setScene(aScene);
    }

    public void switchToMainMenu() {
        BackgroundMusic.getInstance().startSong("src/music/song1.mp3");
       // BackgroundMusic.getInstance().play();
        mainStage.setScene(mainMenu.getScene());
    }

    public Stage getMainStage() {
        return mainStage;
    }
}
