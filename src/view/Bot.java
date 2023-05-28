package view;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import model.Sounds;

import java.awt.*;

public class Bot extends Pane{
    public static final int BOT_SIZE = 60;
    public static final int BOT_DAMAGE = 1;
    public static final int BOT_IMPACT_RADIUS = 100;
    String urlImg = new String();
    Image botImg;
    ImageView imageView;
    private boolean looksToRight = false;
    public Point2D botVelocity = new Point2D(0,0);
    public SpriteAnimation spriteAnimation;
    public boolean isBotRunning = true;
    private Pane paneForImpactZoneAnimation;
    private int botSpeed = 1;
    private final int defaultHP = 20;
    private int HP = 20;
    private Rectangle HPLine = new Rectangle(this.getTranslateX(), this.getTranslateY() -10, BOT_SIZE*defaultHP/HP, 5);
    private int viewingDistance = 400;
    public int kickDelay = 0;
    Rectangle rightImpactZone;
    Rectangle leftImpactZone;
    public void setHP(int HP){
        this.HP = HP;
    }
    public int getHP(){
        return HP;
    }

    public int getViewingDistance(){
        return viewingDistance;
    }


    public Bot(String urlImg, int x, int y){

        HPLine.setFill(Color.RED);
        this.getChildren().add(HPLine);

        botImg = new Image(urlImg);
        this.urlImg = urlImg;
        imageView = new ImageView(botImg);
        imageView.setFitHeight(BOT_SIZE);
        imageView.setFitWidth(BOT_SIZE);
        imageView.setViewport(new Rectangle2D(x,y,BOT_SIZE,BOT_SIZE));

        initImpactZone(x,y);

        spriteAnimation = new SpriteAnimation(this.imageView, Duration.millis(900),BOT_SIZE,BOT_SIZE,4,4,10,10);

        getChildren().add(imageView);
    }
    public void moveX(boolean movingRight){
        looksToRight = movingRight;
        for (int i=0; i < Math.abs(botSpeed);i++){
          this.setTranslateX(this.getTranslateX() + (movingRight ? 1 : -1));
        }
        updateImpactZone();
    }

    public void moveY(int value){
        boolean movingDown = value > 0;
        for (int i=0; i < Math.abs(value);i++){
            for (Node platform : GameField.platforms){
                if(this.getBoundsInParent().intersects(platform.getBoundsInParent())){
                    if(movingDown){
                        if (this.getTranslateY() + BOT_SIZE == platform.getTranslateY()){
                            //canJump = true;
                            return;
                        }
                    }else {
                        if (this.getTranslateY() == platform.getTranslateY() + Platform.BLOCK_SIZE) {
                            return;
                        }
                    }
                }
            }
            this.setTranslateY(this.getTranslateY() + (movingDown ? 1 : -1));
        }
        updateImpactZone();
    }
    public void updateHPLine(){
        HPLine.setWidth((int)BOT_SIZE*HP/defaultHP);
    }

    private void initImpactZone(int x, int y){

        rightImpactZone = new Rectangle(x + BOT_SIZE / 2, y, BOT_IMPACT_RADIUS, BOT_SIZE);
        leftImpactZone = new Rectangle(x - BOT_IMPACT_RADIUS + BOT_SIZE / 2, y, BOT_IMPACT_RADIUS, BOT_SIZE);

        Image impactZoneImg = new Image("botDamage.png");
        ImageView impactZoneImageView = new ImageView(impactZoneImg);
        impactZoneImageView.setFitHeight(BOT_SIZE);
        impactZoneImageView.setFitWidth(BOT_IMPACT_RADIUS);
        impactZoneImageView.setViewport(new Rectangle2D(x, y, BOT_SIZE, BOT_SIZE));

        // impactZoneAnimation = new SpriteAnimation(impactZoneImageView,new Duration(200),IMPACT_RADIUS*3,PLAYER_SIZE*2,2,3);
        paneForImpactZoneAnimation = new Pane();
        paneForImpactZoneAnimation.getChildren().add(impactZoneImageView);
        paneForImpactZoneAnimation.setVisible(false);
    }

    public void updateImpactZone() {
        rightImpactZone.setX(this.getTranslateX() + BOT_SIZE / 2);
        rightImpactZone.setY(this.getTranslateY());
        rightImpactZone.setVisible(false);

        leftImpactZone.setX(this.getTranslateX() - BOT_IMPACT_RADIUS + BOT_SIZE / 2);
        leftImpactZone.setY(this.getTranslateY());
        leftImpactZone.setVisible(false);
    }

    public void kick(Player player) {
        Rectangle currentImpactZone = looksToRight ? rightImpactZone : leftImpactZone;
            if (currentImpactZone.getBoundsInParent().intersects(player.getBoundsInParent())) {
                player.setHP(player.getHP() - BOT_DAMAGE);
                System.out.println(player.getHP());
            }
    }
}
