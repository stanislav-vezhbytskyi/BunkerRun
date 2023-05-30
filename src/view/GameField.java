package view;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.BackgroundMusic;
import model.Sounds;
import model.SkinService;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static view.PauseMenu.openPauseMenu;
import static view.Platform.BLOCK_SIZE;
import static view.ViewManager.HEIGHT;
import static view.ViewManager.WIDTH;

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
    private   AnimationTimer timer;

    private boolean isGameOnPause = false;
    public void setGameOnPause(boolean isGameOnPause) {
        this.isGameOnPause = isGameOnPause;
    }
    public GameField() {
        initGame();
    }

    private void initGame() {
        ViewManager.getInstance().setMode(Mode.GAME);
        BackgroundMusic.getInstance().startSong("src/resources/songForFighting.mp3");

        gameScene = new Scene(appRoot,WIDTH,HEIGHT);

        ImageView backgroundIV = new ImageView(new Image("Background+bunker.png"));


        levelWidth = LevelData.LEVEL1[0].length() * BLOCK_SIZE;
        platforms = Platform.generateAllBlocks(gameRoot);


        player = new Player("5.png", "Damage-pers.png",0, 0,40,
                10,5,0.15,100,400,4);
        /*!!!!!!!!!*/String imageUrl = SkinService.getPickedSkinSprite();
        //player = new Player(imageUrl,0,0);
//        player = new Player("PlayerSprite1.png",0,0);
        player.setTranslateY(0);
        player.setTranslateX(0);
        player.translateXProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();
            if (offset > 640 && offset < levelWidth - 640) {
                gameRoot.setLayoutX(-(offset - 640));
                backgroundIV.setLayoutX(-(offset-640));
            }
        });

        bunker = new Bunker(500,10,10,250,20);
        bunker.getStrokeLineHP().setStrokeWidth(1);
        bunker.getStrokeLineHP().setStroke(Color.BLACK);
        bunker.getStrokeLineHP().setFill(Color.TRANSPARENT);
        bunker.getLineHP().setFill(Color.BLUE);


        playerHealthLine = new Rectangle(bunker.getLineHP().getX()+bunker.getLineHP().getWidth(), 10, 2 * player.getHP(), 20);
        playerHealthLine.setFill(Color.RED);

        playerHealthLineStroke = new Rectangle(bunker.getLineHP().getX()+bunker.getLineHP().getWidth(), 10, 2 * player.getHP(), 20);
        playerHealthLineStroke.setStroke(Color.BLACK);
        playerHealthLineStroke.setStrokeWidth(1);
        playerHealthLineStroke.setFill(Color.TRANSPARENT);


        /*!!!!!!!!!*/Rectangle HealthLine = new Rectangle(10, 10, 2 * player.getHP(), 20);
        HealthLine.setFill(Color.RED);


        strafeAmountLine = new Rectangle(10, bunker.getLineHP().getY()+bunker.getLineHP().getHeight(),
                2 * player.getStrafeAmount(), 10);
        strafeAmountLine.setFill(Color.YELLOW);
        strafeAmountLineStroke = new Rectangle(10, bunker.getLineHP().getY()+bunker.getLineHP().getHeight(),
                3 * player.getStrafeAmount(), 10);
        strafeAmountLine.setFill(Color.YELLOW);
        strafeAmountLineStroke.setStrokeWidth(1);
        strafeAmountLineStroke.setStroke(Color.BLACK);
        strafeAmountLineStroke.setFill(Color.TRANSPARENT);


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

        uiRoot.getChildren().add(pauseButton);
        uiRoot.getChildren().addAll(bunker.getLineHP(),bunker.getStrokeLineHP());
        uiRoot.getChildren().addAll(playerHealthLine,playerHealthLineStroke);
        uiRoot.getChildren().addAll(strafeAmountLine,strafeAmountLineStroke);

        gameRoot.getChildren().addAll(player,player.getImpactZone());
        appRoot.getChildren().addAll(backgroundIV,gameRoot, uiRoot);

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

    private void update() {

        botController.updateBot(gameRoot,player,bunker);
        updatePlayerHealthLine();
        updateStrafeAmountLine();
        player.updateStrafe();
        bunker.updateLineHP();
        if(botController.botsAreOver()){
            endGame(true);
        }
        if(bunker.getBunkerHP()<=0||player.getHP()<=0){
            endGame(false);
        }



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


        if(isPressed(KeyCode.K)) {
            player.performAttack(botController.getBotList());
        }
        if(isPressed(KeyCode.O)) {
            System.out.println("X: " + player.getTranslateX() + "; Y: " + player.getTranslateY() + "; DAMAGE: " + player.DAMAGE + ".");
        }
    }
    public void updatePlayerHealthLine(){
        playerHealthLine.setWidth(2 * player.getHP());
    }
    public void updateStrafeAmountLine(){
        strafeAmountLine.setWidth(3 * player.getStrafeAmount());
    }

    private boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

    public void startGame() {
        ViewManager.getInstance().setMainScene(gameScene);
    }

    public void stopGame() {
        timer.stop();
    }
    public void endGame(boolean isWin){
        Label text = new Label( isWin ? "win" : "you are lose");
        text.setFont(Font.font(150));
        text.setTextFill(isWin?Color.YELLOW:Color.RED);
        text.setAlignment(Pos.CENTER);
        text.setPrefSize(1200,675);

        appRoot.getChildren().add(text);

        Sounds.getInstance().stopRunning();

        timer.stop();

        PauseTransition delay = new PauseTransition(Duration.seconds(4));
        delay.setOnFinished(event -> {
            BackgroundMusic.getInstance().stop();
            ViewManager.getInstance().switchToMainMenu();
        });
        delay.play();
    }
}
