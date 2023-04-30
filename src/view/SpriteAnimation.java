package view;


import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class SpriteAnimation extends Transition {
       private final ImageView imageView;
       private final int count = 2;
       private final int columns = 3;
       private int offsetX;
       private  int offsetY;
       private final int width;
       private final int height;

       public SpriteAnimation(
               ImageView imageView,
               Duration duration,
               int width, int height) {
           this.imageView = imageView;
       /*    this.count = count;
           this.columns = columns;*/
           this.offsetX = offsetX;
           this.offsetY = offsetY;
           this.width = width;
           this.height = height;
           setCycleDuration(duration);
       }
       public void setAnimation(int x){
           setOffsetY(40*x);
       }
       private void setOffsetX(int offsetX){
           this.offsetX = offsetX;
       }
       private void setOffsetY(int offsetY){
           this.offsetY = offsetY;
       }
       protected void interpolate(double k) {
           final int index = Math.min((int) Math.floor(k * count), count - 1);
           final int x = (index % columns) * width + offsetX;
           final int y = (index / columns) * height + offsetY;
           imageView.setViewport(new Rectangle2D(x, y, width, height));
       }
   }
