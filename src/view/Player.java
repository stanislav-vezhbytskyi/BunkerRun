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
    public final int SIZE;
    public static final int IMPACT_RADIUS = 100;
    public static int DAMAGE;
    private final double DELAY_BETWEEN_ATTACKS;
    private final int SPEED;
    private final int JUMP_HEIGHT = -23;
    private ImageView characterSkinImageView;
    Rectangle rightImpactZone;
    Rectangle leftImpactZone;
    public Point2D velocity = new Point2D(0, 0);
    public SpriteAnimation spriteAnimation;
    public SpriteAnimation impactZoneAnimation;
    Pane paneForImpactZoneAnimation;
    private boolean canJump = true;
    boolean looksToRight = true;
    private long timeLastAttack = 0;
    private int currentHP = 100;
    private double strafeAmount = 100;
    public double getStrafeAmount(){
        return  strafeAmount;
    }
    public void setStrafeAmount(double amount){
        this.strafeAmount = amount;
    }


    public Player(String urlImgSkin,String urlImgDamageArea, int x, int y, int SIZE, int DAMAGE,int SPEED, double DELAY_BETWEEN_ATTACKS) {
        this.SIZE = SIZE;
        this.DAMAGE = DAMAGE;
        this.SPEED = SPEED;
        this.DELAY_BETWEEN_ATTACKS = DELAY_BETWEEN_ATTACKS;
        Image image = new Image(urlImgSkin);
        characterSkinImageView = new ImageView(image);
        characterSkinImageView.setFitHeight(this.SIZE);
        characterSkinImageView.setFitWidth(this.SIZE);
        characterSkinImageView.setViewport(new Rectangle2D(x, y, SIZE, SIZE));

        initImpactZone(urlImgDamageArea,x, y);


        spriteAnimation = new SpriteAnimation(this.characterSkinImageView, Duration.millis(500), SIZE, SIZE,
                5, 4, 10, 10);
        getChildren().add(characterSkinImageView);
    }

    private void initImpactZone(String urlImgDamageArea,int x, int y) {

        rightImpactZone = new Rectangle(x + SIZE / 2, y, IMPACT_RADIUS, SIZE);
        leftImpactZone = new Rectangle(x - IMPACT_RADIUS + SIZE / 2, y, IMPACT_RADIUS, SIZE);

        Image impactZoneImg = new Image(urlImgDamageArea);
        ImageView impactZoneImageView = new ImageView(impactZoneImg);
        impactZoneImageView.setFitHeight(SIZE);
        impactZoneImageView.setFitWidth(IMPACT_RADIUS);
        impactZoneImageView.setViewport(new Rectangle2D(x, y, IMPACT_RADIUS * 3, SIZE * 2));

        impactZoneAnimation = new SpriteAnimation(impactZoneImageView, new Duration(200), IMPACT_RADIUS, SIZE, 2, 3, 10, 10);

        paneForImpactZoneAnimation = new Pane(impactZoneImageView);

    }

    public void updateImpactZone() {
        rightImpactZone.setX(this.getTranslateX() + SIZE / 2);
        rightImpactZone.setY(this.getTranslateY());

        leftImpactZone.setX(this.getTranslateX() - IMPACT_RADIUS + SIZE / 2);
        leftImpactZone.setY(this.getTranslateY());

        paneForImpactZoneAnimation.setVisible(false);


    }

    boolean canAttack() {
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

    void attack(ArrayList<Bot> botArrayList) {
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
                    bot.setHP(bot.getHP() - DAMAGE);
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
        this.currentHP = HP;
    }

    public int getHP() {
        return currentHP;
    }

    public void moveX(boolean movingRight) {
        this.looksToRight = movingRight;
        for (int i = 0; i < Math.abs(SPEED); i++) {
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
                        if (this.getTranslateY() + SIZE == platform.getTranslateY()) {
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

    public void jump() {
        if (canJump) {
            Sounds.getInstance().jump();
            velocity = velocity.add(0, JUMP_HEIGHT);
            canJump = false;
        }
    }
    public void strafe(boolean movingRight) {
        this.looksToRight = movingRight;
        for (int i = 0; i < Math.abs(SPEED*1.3); i++) {
            this.setTranslateX(this.getTranslateX() + (movingRight ? 1 : -1));
            updateImpactZone();
        }
    }
    public void updateStrafe(){
        if(strafeAmount<100){
            strafeAmount+=0.03;
        }
    }
}