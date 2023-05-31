package view;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import model.BackgroundMusic;
import model.Sounds;
import model.SkinService;
import java.util.ArrayList;
import java.util.HashMap;
import static view.PauseMenu.openPauseMenu;
import static view.ViewManager.*;

public class GameField {
    private Scene gameScene;
    public static final int BLOCK_SIZE = 25;
    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
    public static ArrayList<Node> platforms = new ArrayList<>();
    private Pane appRoot = new Pane();
    private Pane gameRoot = new Pane();
    private Pane uiRoot = new Pane();
    private Player player;
    private Rectangle playerHealthLine;
    private Rectangle playerHealthLineStroke;
    private Bunker bunker;
    private Rectangle strafeAmountLine;
    private Rectangle strafeAmountLineStroke;
    private int levelWidth;
    private boolean isPlayerRunning = false;
    private BotController botController = new BotController(30);
    private AnimationTimer timer;

    private boolean isGameOnPause = false;
    public void setGameOnPause(boolean isGameOnPause) {
        this.isGameOnPause = isGameOnPause;
    }
    public GameField() {
        initGame();
    }

    // Initialize the game by setting up the scene, background music, and creating game objects.
    private void initGame() {
        ViewManager.getInstance().setMode(Mode.GAME);
        BackgroundMusic.getInstance().startSong("src/resources/songForFighting.mp3");

        gameScene = new Scene(appRoot,WIDTH,HEIGHT);

        // Set up the game background image
        ImageView backgroundIV = new ImageView(new Image("Background+bunker.png"));


        // Calculate the level width based on the level data
        levelWidth = LevelData.LEVEL1[0].length() * BLOCK_SIZE;
        // Generate all the platforms/blocks for the game level
        platforms = Platform.generateAllBlocks(gameRoot);

        // Create the player object with the chosen skin and initial properties
        String urlSkin = SkinService.getPickedSkinSprite();
        player = new Player(urlSkin, "Damage-pers.png",0, 0,40,
                10,5,0.15,100,400,4);
        player.setTranslateY(0);
        player.setTranslateX(0);
        // Update the game root's position when the player moves
        player.translateXProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();
            if (offset > 640 && offset < levelWidth - 640) {
                gameRoot.setLayoutX(-(offset - 640));
                backgroundIV.setLayoutX(-(offset-640));
            }
        });

        // Create the bunker object with its dimensions and health properties
        bunker = new Bunker(500,10,10,250,20);
        bunker.getStrokeLineHP().setStrokeWidth(1);
        bunker.getStrokeLineHP().setStroke(Color.BLACK);
        bunker.getStrokeLineHP().setFill(Color.TRANSPARENT);
        bunker.getLineHP().setFill(Color.BLUE);


        // Create the player's health line and its stroke for display
        playerHealthLine = new Rectangle(bunker.getLineHP().getX()+bunker.getLineHP().getWidth(), 10, 2 * player.getHP(), 20);
        playerHealthLine.setFill(Color.RED);

        playerHealthLineStroke = new Rectangle(bunker.getLineHP().getX()+bunker.getLineHP().getWidth(), 10, 2 * player.getHP(), 20);
        playerHealthLineStroke.setStroke(Color.BLACK);
        playerHealthLineStroke.setStrokeWidth(1);
        playerHealthLineStroke.setFill(Color.TRANSPARENT);


        /*!!!!!!!!!*/Rectangle HealthLine = new Rectangle(10, 10, 2 * player.getHP(), 20);
        HealthLine.setFill(Color.RED);


        // Create the amount line for strafing (sideways movement)
        strafeAmountLine = new Rectangle(10, bunker.getLineHP().getY()+bunker.getLineHP().getHeight(),
                2 * player.getStrafeAmount(), 10);
        strafeAmountLine.setFill(Color.YELLOW);
        strafeAmountLineStroke = new Rectangle(10, bunker.getLineHP().getY()+bunker.getLineHP().getHeight(),
                3 * player.getStrafeAmount(), 10);
        strafeAmountLine.setFill(Color.YELLOW);
        strafeAmountLineStroke.setStrokeWidth(1);
        strafeAmountLineStroke.setStroke(Color.BLACK);
        strafeAmountLineStroke.setFill(Color.TRANSPARENT);


        // Create a button for pausing the game
        Image image = new Image("img_2.png");
        ImageView imageView = new ImageView(image);

        Button pauseButton = new Button("", imageView);
        pauseButton.setMaxWidth(30);
        pauseButton.setMaxHeight(30);
        pauseButton.setTranslateX(1160);
       // GameField gameField = this;
        pauseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Sounds.getInstance().clickButtonSound();
                openPauseMenu(ViewManager.getInstance().getGameManager());
                isGameOnPause = true;
            }
        });

        // Add UI elements to the UI root
        uiRoot.getChildren().add(pauseButton);
        uiRoot.getChildren().addAll(bunker.getLineHP(),bunker.getStrokeLineHP());
        uiRoot.getChildren().addAll(playerHealthLine,playerHealthLineStroke);
        uiRoot.getChildren().addAll(strafeAmountLine,strafeAmountLineStroke);

        // Add game objects to the game root
        gameRoot.getChildren().addAll(player,player.getImpactZone());
        appRoot.getChildren().addAll(backgroundIV,gameRoot, uiRoot);

        // Set up keyboard input handlers
        gameScene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        gameScene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!isGameOnPause){
                    update();
                }
            }
        };
        timer.start();
    }

    // Update the game state
    private void update() {

        // Update the bot controller and other game elements
        botController.updateBot(gameRoot,player,bunker);
        updatePlayerHealthLine();
        updateStrafeAmountLine();
        player.updateStrafe();
        bunker.updateLineHP();
        // Check for game over conditions
        if(botController.botsAreOver()){
            endGame(true);
        }
        if(bunker.getBunkerHP()<=0||player.getHP()<=0){
            endGame(false);
        }

        // Handle player movement and actions based on keyboard input
        if (isPressed(KeyCode.W) && player.getTranslateY() >= 5) {
            player.jump();
            player.spriteAnimation.setAnimation(2);
            player.spriteAnimation.play();

        }

        if (isPressed(KeyCode.D) && isPressed(KeyCode.SHIFT) && player.getTranslateX() + 40 <= levelWidth - 5 && player.getStrafeAmount() > 0) {
            player.strafe(true);

            player.setStrafeAmount(player.getStrafeAmount()-1);
            if (!isPlayerRunning) {
                Sounds.getInstance().startRunning();
                isPlayerRunning = true;
            }
            player.spriteAnimation.setAnimation(1);
            player.spriteAnimation.play();
        }
        if (isPressed(KeyCode.A) && isPressed(KeyCode.SHIFT) && player.getTranslateX() >= 5 && player.getStrafeAmount() > 0) {
            player.strafe(false);

            player.setStrafeAmount(player.getStrafeAmount()-1);
            if (!isPlayerRunning) {
                Sounds.getInstance().startRunning();
                isPlayerRunning = true;
            }
            player.spriteAnimation.setAnimation(1);
            player.spriteAnimation.play();
        }
        if (isPressed(KeyCode.A) && player.getTranslateX() >= 5) {
            player.moveX(false);
            if (!isPlayerRunning) {
                Sounds.getInstance().startRunning();
                isPlayerRunning = true;
            }
            player.spriteAnimation.setAnimation(0);
            player.spriteAnimation.play();
        }
        if (isPressed(KeyCode.D) && player.getTranslateX() + 40 <= levelWidth - 5) {
            player.moveX(true);
            if (!isPlayerRunning) {
                Sounds.getInstance().startRunning();
                isPlayerRunning = true;
            }
            player.spriteAnimation.setAnimation(1);
            player.spriteAnimation.play();
        }
        if (!isPressed(KeyCode.A) && !isPressed(KeyCode.D)) {
            Sounds.getInstance().stopRunning();
            isPlayerRunning = false;
        }
        if (player.velocity.getY() < 6) {
            player.velocity = player.velocity.add(0, 1);
        }
        player.moveY((int) player.velocity.getY());


        // Perform an attack if the attack key is pressed
        if(isPressed(KeyCode.K)) {
            player.performAttack(botController.getBotList());
        }
    }
    // Update the player's health line based on their current health value
    public void updatePlayerHealthLine(){
        playerHealthLine.setWidth(2 * player.getHP());
    }
    // Update the strafe amount line based on the player's strafe amount value
    public void updateStrafeAmountLine(){
        strafeAmountLine.setWidth(3 * player.getStrafeAmount());
    }

    private boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

    // Start the game by setting the main scene
    public void startGame() {
        ViewManager.getInstance().setMainScene(gameScene);
    }

    public void stopGame() {
        timer.stop();
    }
    // End the game and handle win/lose conditions
    public void endGame(boolean isWin){

        if (isWin) {
            ViewManager.getCoins().saveCoinsForVictory(bunker.getBunkerHP(), player.getHP());
        } else {
            getCoins().resetCoinsForGame();
        }

        // Display the end game image and play appropriate sounds
        Image endGameImage = new Image(isWin?"win.png":"lose.png");
        ImageView endGameIV = new ImageView(endGameImage);

        appRoot.getChildren().add(endGameIV);

        Sounds.getInstance().stopRunning();

        timer.stop();

        if (isWin) {
            Sounds.getInstance().win();
        } else {
            Sounds.getInstance().notWin();
        }

        // Wait for a delay and then switch back to the main menu
        PauseTransition delay = new PauseTransition(Duration.seconds(4));
        delay.setOnFinished(event -> {
            BackgroundMusic.getInstance().stop();
            ViewManager.getInstance().switchToMainMenu();
        });
        delay.play();
    }
}
