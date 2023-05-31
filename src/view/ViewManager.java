package view;

import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.BackgroundMusic;
import model.Sounds;
import model.Coins;

public class ViewManager {
    private static ViewManager instance;

    // Singleton pattern to ensure only one instance of ViewManager exists
    public static ViewManager getInstance() {
        if (instance == null) {
            instance = new ViewManager();
        }
        return instance;
    }

    private static Coins coins;

    // Get the Coins instance, creating it if it doesn't exist
    public static Coins getCoins() {
        if (coins == null)
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
    private Settings settings = new Settings();
    private Contacts contacts = new Contacts();
    private Mode mode;

    // ViewManager constructor initializes mainStage and sets the mainScene to the mainMenu scene
    public ViewManager() {
        mainPane = new AnchorPane();
        mainScene = mainMenu.getScene();
        mode = Mode.MAINMENU;
        mainStage = new Stage();
        mainStage.setScene(mainScene);
        mainStage.setResizable(false);
        mainStage.setTitle("Bunker Run");
    }

    // Get the GameManager instance, creating it if it doesn't exist
    public GameField getGameManager() {
        if (gameManager == null) {
            gameManager = new GameField();
        }
        return gameManager;
    }

    // Get the Store instance, creating it if it doesn't exist
    public Store getStoreManager() {
        storeManager = new Store();
        return storeManager;
    }

    // Set the current mode
    public void setMode(Mode aMode) {
        mode = aMode;
    }

    // Get the current mode
    public Mode getMode() {
        return mode;
    }

    // Set the mainScene of the mainStage
    public void setMainScene(Scene aScene) {
        mainStage.setScene(aScene);
    }

    // Switch to the main menu scene, stopping sounds and game if in game mode
    public void switchToMainMenu() {
        Sounds.getInstance().stopSounds();

        if (mode == Mode.GAME) {
            getGameManager().stopGame();
            gameManager = null;
        }
        if (mode != Mode.STORE && mode != Mode.SETTINGS && mode != Mode.CONTACTS) {
            BackgroundMusic.getInstance().startSong("src/resources/song1.mp3");
        }
        mainStage.setScene(mainMenu.getScene());
    }

    // Switch to the settings scene
    public void goToSettings() {
        mode = Mode.SETTINGS;
        settings.updateVolume();
        mainStage.setScene(settings.getSettingsScene());
    }

    // Switch to the contacts scene
    public void goToContacts() {
        mode = Mode.CONTACTS;
        mainStage.setScene(contacts.getScene());
    }

    // Get the mainStage
    public Stage getMainStage() {
        return mainStage;
    }
}
