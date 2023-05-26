package view;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import model.Sounds;
public class Bot extends Pane{
    public static final int BOT_SIZE = 40;
    String urlImg = new String();
    Image botImg;
    ImageView imageView;
    public Point2D botVelocity = new Point2D(0,0);
    public SpriteAnimation spriteAnimation;
    private int botSpeed = 1;
    private int HP = 20;
    public void setHP(int HP){
        this.HP = HP;
    }
    public int getHP(){
        return HP;
    }

    public Bot(String urlImg, int x, int y){
        botImg = new Image(urlImg);
        this.urlImg = urlImg;
        imageView = new ImageView(botImg);
        imageView.setFitHeight(BOT_SIZE);
        imageView.setFitWidth(BOT_SIZE);
        imageView.setViewport(new Rectangle2D(x,y,BOT_SIZE,BOT_SIZE));

        spriteAnimation = new SpriteAnimation(this.imageView, Duration.millis(200),BOT_SIZE,BOT_SIZE);

        getChildren().add(imageView);
    }
    public void moveX(boolean movingRight){
        for (int i=0; i < Math.abs(botSpeed);i++){
          this.setTranslateX(this.getTranslateX() + (movingRight ? 1 : -1));
        }
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
                        if (this.getTranslateY() == platform.getTranslateY() + GameField.BLOCK_SIZE) {
                            return;
                        }
                    }
                }
            }
            this.setTranslateY(this.getTranslateY() + (movingDown ? 1 : -1));
        }
    }
}
