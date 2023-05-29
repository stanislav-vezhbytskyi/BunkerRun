package view;

import javafx.animation.PauseTransition;
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
    public static final int PLAYER_DAMAGE = 3;
    private double DELAY_BETWEEN_ATTACKS = 0.5;
    private String urlImg;
    private Image playerImg;
    private ImageView imageView;
    private Rectangle rightImpactZone;
    private Rectangle leftImpactZone;
    public Point2D playerVelocity = new Point2D(0, 0);
    public SpriteAnimation spriteAnimation;
    public SpriteAnimation impactZoneAnimation;
    private Pane paneForImpactZoneAnimation;
    private boolean canJump = true;
    private boolean looksToRight = true;
    private long timeLastAttack = 0;
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

        initImpactZone(x, y);


        spriteAnimation = new SpriteAnimation(this.imageView, Duration.millis(500), PLAYER_SIZE, PLAYER_SIZE,
                5, 4, 10, 10);
        getChildren().add(imageView);
    }

    private void initImpactZone(int x, int y) {

        rightImpactZone = new Rectangle(x + PLAYER_SIZE / 2, y, IMPACT_RADIUS, PLAYER_SIZE);
        leftImpactZone = new Rectangle(x - IMPACT_RADIUS + PLAYER_SIZE / 2, y, IMPACT_RADIUS, PLAYER_SIZE);

        Image impactZoneImg = new Image("Damage-pers.png");
        ImageView impactZoneImageView = new ImageView(impactZoneImg);
        impactZoneImageView.setFitHeight(PLAYER_SIZE);
        impactZoneImageView.setFitWidth(IMPACT_RADIUS);
        impactZoneImageView.setViewport(new Rectangle2D(x, y, IMPACT_RADIUS * 3, PLAYER_SIZE * 2));

        impactZoneAnimation = new SpriteAnimation(impactZoneImageView, new Duration(200), IMPACT_RADIUS, PLAYER_SIZE, 2, 3, 10, 10);

        paneForImpactZoneAnimation = new Pane(impactZoneImageView);

    }

    public void updateImpactZone() {
        rightImpactZone.setX(this.getTranslateX() + PLAYER_SIZE / 2);
        rightImpactZone.setY(this.getTranslateY());

        leftImpactZone.setX(this.getTranslateX() - IMPACT_RADIUS + PLAYER_SIZE / 2);
        leftImpactZone.setY(this.getTranslateY());

        paneForImpactZoneAnimation.setVisible(false);


    }

    private boolean canAttack() {
        long currentTime = System.currentTimeMillis();
        boolean canAttack = (currentTime - timeLastAttack) / 1000 > DELAY_BETWEEN_ATTACKS;
        this.timeLastAttack = canAttack ? System.currentTimeMillis() : this.timeLastAttack;
        return canAttack;
    }

    public void performAttack(ArrayList<Bot> botArrayList){
        if(canAttack()){
            attack(botArrayList);
        }
    }

    private void attack(ArrayList<Bot> botArrayList) {
        this.spriteAnimation.setAnimation(looksToRight ? 3 : 4);
        this.spriteAnimation.play();
        Rectangle currentImpactZone = looksToRight ? rightImpactZone : leftImpactZone;

        impactZoneAnimation.setAnimation(looksToRight ? 1 : 0);

        paneForImpactZoneAnimation.setVisible(true);
        paneForImpactZoneAnimation.setTranslateX(currentImpactZone.getX());
        paneForImpactZoneAnimation.setTranslateY(currentImpactZone.getY());

        impactZoneAnimation.play();


        PauseTransition delay = new PauseTransition(Duration.seconds(0.25));
        delay.setOnFinished(event -> {
            for (Bot bot : botArrayList) {
                if (currentImpactZone.getBoundsInParent().intersects(bot.getBoundsInParent())) {
                    bot.setHP(bot.getHP() - PLAYER_DAMAGE);
                }
            }
            paneForImpactZoneAnimation.setVisible(false);
        });
        delay.play();

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
