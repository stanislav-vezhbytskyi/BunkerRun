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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;

import static view.PauseMenu.openPauseMenu;
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
    private int levelWidth;

    public GameField() {
        initGame();
    }

    private void initGame() {
      /*  gamePane = new AnchorPane();
        gameScene = new Scene(gamePane, WIDTH, HEIGHT);
        Image backgroundImage = new Image("resources/blackBackground.jpg", 1200, 675, false, true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        gamePane.setBackground(new Background(background));*/
        gameScene = new Scene(appRoot);


        Rectangle bg = new Rectangle(1200,675);
        bg.setFill(Color.gray(0.5));


        levelWidth = LevelData.LEVEL1[0].length() * BLOCK_SIZE;

        for (int i=0; i< LevelData.LEVEL1.length; i++){
            String line = LevelData.LEVEL1[i];
            for (int j=0; j <line.length();j++){
                switch (line.charAt(j)){
                    case '0':
                        break;
                    case '1':
                        Node platform = createEntity(j*BLOCK_SIZE, i *BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, Color.GREEN);
                        platforms.add(platform);
                        break;
                    case '2':
                        Node platform_y = createEntity(j*BLOCK_SIZE, i *BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, Color.YELLOW);
                        platforms.add(platform_y);
                        break;
                }
            }
        }

        player = new Player("PlayerSprite1.png",0,0);
        player.setTranslateY(0);
        player.setTranslateX(0);
        player.translateXProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();
            if (offset > 640 && offset < levelWidth-640){
                gameRoot.setLayoutX(-(offset-640));
            }
        });


        Rectangle HealthLine = new Rectangle(10,10,2* player.getHP(),20);
        HealthLine.setFill(Color.RED);



        Image image = new Image("pauseIcon.png");
        ImageView imageView = new ImageView(image);

        Button stopButton = new Button("",imageView);
        stopButton.setMaxWidth(30);
        stopButton.setMaxHeight(30);
        stopButton.setTranslateX(1160);
        stopButton.setOnAction(new EventHandler<ActionEvent>() {
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




        uiRoot.getChildren().add(stopButton);
        uiRoot.getChildren().add(HealthLine);
        gameRoot.getChildren().add(player);
        appRoot.getChildren().addAll(bg, gameRoot, uiRoot);


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
    private void update(){
        if (isPressed(KeyCode.W) && player.getTranslateY() >= 5){
            player.jumpPlayer();
            player.spriteAnimation.setAnimation(2);
            player.spriteAnimation.play();
        }
        if (isPressed(KeyCode.A) && player.getTranslateX() >=5){
            player.moveX(false);
            player.spriteAnimation.setAnimation(0);
            player.spriteAnimation.play();
        }
        if (isPressed(KeyCode.D) && player.getTranslateX() + 40 <=levelWidth-5){
            player.moveX(true);
            player.spriteAnimation.setAnimation(1);
            player.spriteAnimation.play();
        }
        if (player.playerVelocity.getY() < 6){
            player.playerVelocity = player.playerVelocity.add(0,1);
        }
        player.moveY((int)player.playerVelocity.getY());
    }
    private Node createEntity(int x, int y, int w, int h, Color color){
        Rectangle entity = new Rectangle(w, h);
        entity.setTranslateX(x);
        entity.setTranslateY(y);
        entity.setFill(color);
        gameRoot.getChildren().add(entity);
        return entity;

    }
    private boolean isPressed(KeyCode key){
        return keys.getOrDefault(key, false);
    }

    public void startGame() {
        ViewManager.getInstance().setMainScene(gameScene);
    }
}
