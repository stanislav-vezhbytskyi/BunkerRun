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


public class Player extends Pane {
    public static final int PLAYER_SIZE = 40;
    public static final int IMPACT_RADIUS = 100;
    String urlImg;
    Image playerImg;
    ImageView imageView;
    Rectangle rightImpactZone;
    Rectangle leftImpactZone;
    public Point2D playerVelocity = new Point2D(0,0);
    public SpriteAnimation spriteAnimation;
    private boolean canJump = true;
    private boolean looksToRight = true;
    private int PlayerSpeed = 5;
    private int JumpHeight = -23;
    private int HP = 100;
    public Player(String urlImg, int x, int y){
        this.playerImg = new Image(urlImg);
        this.urlImg = urlImg;
        imageView = new ImageView(playerImg);
        imageView.setFitHeight(PLAYER_SIZE);
        imageView.setFitWidth(PLAYER_SIZE);
        imageView.setViewport(new Rectangle2D(x,y,PLAYER_SIZE,PLAYER_SIZE));

        rightImpactZone = new Rectangle(x+PLAYER_SIZE/2,y,IMPACT_RADIUS,PLAYER_SIZE);
        rightImpactZone.setVisible(false);
        leftImpactZone = new Rectangle(x-IMPACT_RADIUS+PLAYER_SIZE/2,y,IMPACT_RADIUS,PLAYER_SIZE);
        leftImpactZone.setVisible(false);

        spriteAnimation = new SpriteAnimation(this.imageView, Duration.millis(200),PLAYER_SIZE,PLAYER_SIZE);

        getChildren().add(imageView);
    }
    public void updateImpactZone(){
        rightImpactZone.setX(this.getTranslateX()+PLAYER_SIZE/2);
        rightImpactZone.setY(this.getTranslateY());
        rightImpactZone.setVisible(false);

        leftImpactZone.setX(this.getTranslateX()-IMPACT_RADIUS+PLAYER_SIZE/2);
        leftImpactZone.setY(this.getTranslateY());
        leftImpactZone.setVisible(false);
    }
    public void kick(){
        if(looksToRight){
            rightImpactZone.setVisible(true);
            rightImpactZone.setFill(Color.RED);
        }
        else {
            leftImpactZone.setVisible(true);
            leftImpactZone.setFill(Color.RED);
        }
    }
    public Rectangle getRightImpactZone(){
        return rightImpactZone;
    }
    public Rectangle getLeftImpactZone(){
        return leftImpactZone;
    }
    public void setHP(int HP){
        this.HP = HP;
    }
    public int getHP(){
        return HP;
    }
    public void moveX(boolean movingRight){
        this.looksToRight = movingRight;
        for (int i=0; i < Math.abs(PlayerSpeed);i++){
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
    public void moveY(int value){
        boolean movingDown = value > 0;
        for (int i=0; i < Math.abs(value);i++){
            for (Node platform : GameField.platforms){
                if(this.getBoundsInParent().intersects(platform.getBoundsInParent())){
                    if(movingDown){
                        if (this.getTranslateY() + PLAYER_SIZE == platform.getTranslateY()){
                            canJump = true;
                            return;
                        }
                    }else {
                        if (this.getTranslateY() == platform.getTranslateY() + GameField.BLOCK_SIZE) {
                            return;
                        }
                    }
                }
            }
            this.setTranslateY(this.getTranslateY() + (movingDown ? 1 : -1));
            updateImpactZone();
        }
    }
    public void jumpPlayer(){
        if(canJump){
            Sounds.getInstance().jump();
            playerVelocity = playerVelocity.add(0, JumpHeight);
            canJump = false;
        }
    }
}
