package view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import model.Sounds;

import java.util.ArrayList;


public class Player extends Pane {
    public static final int PLAYER_SIZE = 40;
    public static final int IMPACT_RADIUS = 100;
    public static final int PLAYER_DAMAGE = 10;
    String urlImg;
    Image playerImg;
    ImageView imageView;
    Rectangle rightImpactZone;
    Rectangle leftImpactZone;
    public Point2D playerVelocity = new Point2D(0, 0);
    public SpriteAnimation spriteAnimation;
    private SpriteAnimation impactZoneAnimation;
    private Pane paneForImpactZoneAnimation;
    private boolean canJump = true;
    private boolean looksToRight = true;
    private int PlayerSpeed = 5;
    private int JumpHeight = -23;
    private int HP = 100;

    public Player(String urlImg, int x, int y) {
        this.playerImg = new Image(urlImg);
        this.urlImg = urlImg;
        imageView = new ImageView(playerImg);
        imageView.setFitHeight(PLAYER_SIZE);
        imageView.setFitWidth(PLAYER_SIZE);
        imageView.setViewport(new Rectangle2D(x, y, PLAYER_SIZE, PLAYER_SIZE));

        initImpactZone(x,y);


        spriteAnimation = new SpriteAnimation(this.imageView, Duration.millis(5000), PLAYER_SIZE, PLAYER_SIZE,
                5,4,10,10);
        getChildren().add(imageView);
    }
    private void initImpactZone(int x, int y){

        rightImpactZone = new Rectangle(x + PLAYER_SIZE / 2, y, IMPACT_RADIUS, PLAYER_SIZE);
        leftImpactZone = new Rectangle(x - IMPACT_RADIUS + PLAYER_SIZE / 2, y, IMPACT_RADIUS, PLAYER_SIZE);

        Image impactZoneImg = new Image("damage.png");
        ImageView impactZoneImageView = new ImageView(impactZoneImg);
        impactZoneImageView.setFitHeight(PLAYER_SIZE);
        impactZoneImageView.setFitWidth(IMPACT_RADIUS);
        impactZoneImageView.setViewport(new Rectangle2D(x, y, PLAYER_SIZE, PLAYER_SIZE));

       // impactZoneAnimation = new SpriteAnimation(impactZoneImageView,new Duration(200),IMPACT_RADIUS*3,PLAYER_SIZE*2,2,3);
        paneForImpactZoneAnimation = new Pane();
        paneForImpactZoneAnimation.getChildren().add(impactZoneImageView);
        paneForImpactZoneAnimation.setVisible(false);
    }

    public void updateImpactZone() {
        rightImpactZone.setX(this.getTranslateX() + PLAYER_SIZE / 2);
        rightImpactZone.setY(this.getTranslateY());
        rightImpactZone.setVisible(false);

        leftImpactZone.setX(this.getTranslateX() - IMPACT_RADIUS + PLAYER_SIZE / 2);
        leftImpactZone.setY(this.getTranslateY());
        leftImpactZone.setVisible(false);
    }

    public void kick(ArrayList<Bot> botArrayList) {
        Rectangle currentImpactZone = looksToRight ? rightImpactZone : leftImpactZone;
        for (Bot bot : botArrayList) {
            if (currentImpactZone.getBoundsInParent().intersects(bot.getBoundsInParent())) {
                bot.setHP(bot.getHP() - PLAYER_DAMAGE);
            }
        }
    }

    public Pane getImpactZone() {
        return paneForImpactZoneAnimation;
    }


    public void setHP(int HP) {
        this.HP = HP;
    }

    public int getHP() {
        return HP;
    }

    public void moveX(boolean movingRight) {
        this.looksToRight = movingRight;
        for (int i = 0; i < Math.abs(PlayerSpeed); i++) {
          /*  for (Node platform : platforms){
                if(player.getBoundsInParent().intersects(platform.getBoundsInParent())){
                    if(movingRight){
                        if (player.getTranslateX() + 40 == platform.getTranslateX()){
                            return;
                        }
                    }else {
                        if (player.getTranslateX() == platform.getTranslateX() + 60) {
                            return;
                        }
                    }
                }
            }*/
            this.setTranslateX(this.getTranslateX() + (movingRight ? 1 : -1));
            updateImpactZone();
        }
    }

    public void moveY(int value) {
        boolean movingDown = value > 0;
        for (int i = 0; i < Math.abs(value); i++) {
            for (Node platform : GameField.platforms) {
                if (this.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingDown) {
                        if (this.getTranslateY() + PLAYER_SIZE == platform.getTranslateY()) {
                            canJump = true;
                            return;
                        }
                    } else {
                        if (this.getTranslateY() == platform.getTranslateY() + Platform.BLOCK_SIZE) {
                            return;
                        }
                    }
                }
            }
            this.setTranslateY(this.getTranslateY() + (movingDown ? 1 : -1));
            updateImpactZone();
        }
    }

    public void jumpPlayer() {
        if (canJump) {
            Sounds.getInstance().jump();
            playerVelocity = playerVelocity.add(0, JumpHeight);
            canJump = false;
        }
    }
}
