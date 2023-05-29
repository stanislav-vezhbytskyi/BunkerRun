package view;

import javafx.animation.AnimationTimer;
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
import model.BackgroundMusic;
import model.Sounds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static view.PauseMenu.openPauseMenu;
import static view.Platform.BLOCK_SIZE;
import static view.ViewManager.HEIGHT;
import static view.ViewManager.WIDTH;

public class GameField {
    private Scene gameScene;
    private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
    public static ArrayList<Node> platforms = new ArrayList<>();
    private Pane appRoot = new Pane();
    private Pane gameRoot = new Pane();
    private Pane uiRoot = new Pane();
    private Player player;
    private Rectangle playerHealthLine;
    private Bunker bunker;
    private Rectangle PlayerHealthLine;
    private Rectangle strafeAmountLine;
    private int levelWidth;
    private boolean isPlayerRunning = false;
    private BotController botController = new BotController();
    private int kickDelay = 0;


    public GameField() {
        initGame();
    }

    private void initGame() {
        BackgroundMusic.getInstance().startSong("src/music/songForFighting.mp3");


        gameScene = new Scene(appRoot,WIDTH,HEIGHT);

        ImageView backgroundIV = new ImageView(new Image("Background+bunker.png"));





        levelWidth = LevelData.LEVEL1[0].length() * BLOCK_SIZE;
        platforms = Platform.generateAllBlocks(gameRoot);


        player = new Player("5.png", 0, 0);
        player.setTranslateY(0);
        player.setTranslateX(0);
        player.translateXProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();
            if (offset > 640 && offset < levelWidth - 640) {
                gameRoot.setLayoutX(-(offset - 640));
                backgroundIV.setLayoutX(-(offset-640));
            }
        });

        bunker = new Bunker(500,10,10,200,20);
        bunker.getLineHP().setStrokeWidth(1);
        bunker.getLineHP().setStroke(Color.BLACK);
        bunker.getLineHP().setFill(Color.BLUE);


        playerHealthLine = new Rectangle(bunker.getLineHP().getX()+bunker.getLineHP().getWidth(), 10, 2 * player.getHP(), 20);
        playerHealthLine.setFill(Color.RED);
        playerHealthLine.setStrokeWidth(1);
        playerHealthLine.setStroke(Color.BLACK);

        strafeAmountLine = new Rectangle(600, 10, 2 * player.getStrafeAmount(), 20);
        strafeAmountLine.setFill(Color.YELLOW);

        Image image = new Image("pauseIcon.png");
        ImageView imageView = new ImageView(image);

        Button pauseButton = new Button("", imageView);
        pauseButton.setMaxWidth(30);
        pauseButton.setMaxHeight(30);
        pauseButton.setTranslateX(1160);
        pauseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                openPauseMenu();
            }
        });

        //спроба виклика менюхи з ESCAPE
      /*  gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.ESCAPE) {
                    openPauseMenu();
                }
            }
        });*/

        uiRoot.getChildren().add(pauseButton);
        uiRoot.getChildren().add(bunker.getLineHP());
        uiRoot.getChildren().add(playerHealthLine);
        uiRoot.getChildren().add(strafeAmountLine);

        gameRoot.getChildren().addAll(player,player.getImpactZone());
        appRoot.getChildren().addAll(backgroundIV,gameRoot, uiRoot);

        gameScene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        gameScene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();
    }

    private void update() {

        botController.updateBot(gameRoot,player,bunker);
        updatePlayerHealthLine();
        updateStrafeAmountLine();
        bunker.updateLineHP();


        if (isPressed(KeyCode.W) && player.getTranslateY() >= 5) {
            player.jumpPlayer();
            player.spriteAnimation.setAnimation(2);
            player.spriteAnimation.play();

        }

        if (isPressed(KeyCode.D) && isPressed(KeyCode.SHIFT) && player.getTranslateX() + 40 <= levelWidth - 5 && player.getStrafeAmount() > 0) {
            player.strafe(true);
            /*if (kickDelay <= 0) {
                player.kick(botController.getBotList());
                kickDelay = 7;
            }
            else {
                kickDelay-=1;
            }*/
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
            /*if (kickDelay <= 0) {
                player.kick(botController.getBotList());
                kickDelay = 7;
            }
            else {
                kickDelay-=1;
            }*/
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
        if (player.playerVelocity.getY() < 6) {
            player.playerVelocity = player.playerVelocity.add(0, 1);
        }
        player.moveY((int) player.playerVelocity.getY());


        if(isPressed(KeyCode.K)) {
            player.performAttack(botController.getBotList());
        }
        if(isPressed(KeyCode.O)) {
            System.out.println("X: " + player.getTranslateX() + "; Y: " + player.getTranslateY() + ".");
        }
    }
    public void updatePlayerHealthLine(){
        playerHealthLine.setWidth(2 * player.getHP());
        playerHealthLine.setX(bunker.getLineHP().getX()+bunker.getLineHP().getWidth());
    }
    public void updateStrafeAmountLine(){
        strafeAmountLine.setWidth(2 * player.getStrafeAmount());
    }
    private boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

    public void startGame() {
        ViewManager.getInstance().setMainScene(gameScene);
    }
}
