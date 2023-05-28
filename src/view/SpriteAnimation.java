package view;


import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class SpriteAnimation extends Transition {
    private final ImageView imageView;
    private final int count;
    private final int columns;
    private final int offsetX;
    private final int offsetY;
    private final int width;
    private final int height;
    private int currentOffsetY;

    public SpriteAnimation(ImageView imageView, Duration duration, int width, int height, int count, int columns,
                           int offsetX, int offsetY) {
        this.imageView = imageView;
        this.count = count;
        this.columns = columns;
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        setCycleDuration(duration);
        currentOffsetY = offsetY;
    }

    public void setAnimation(int numbAnimation) {
        this.currentOffsetY = numbAnimation*(offsetY+height);
    }
    protected void interpolate(double k) {
        int index = Math.min((int) Math.floor(k * count), count - 1);
        int x = (index % columns) * width + offsetX;
        int y = (index / columns) * height + currentOffsetY;
        System.out.println("x: " +x  + "y: "+y);
        imageView.setViewport(new Rectangle2D(x, y, width, height));
    }
}
