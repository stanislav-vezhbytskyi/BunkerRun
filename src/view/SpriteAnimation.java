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
        imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
    }

    // Method to set the current offset Y value based on the animation number
    public void setAnimation(int numbAnimation) {
        this.currentOffsetY = offsetY + numbAnimation * (height + offsetY);
    }

    // Method that is called during the animation to update the frame
    protected void interpolate(double k) {
        int index = Math.min((int) Math.floor(k * count), count - 1);
        int x = (index % columns) * (width + offsetX) + offsetX;
        int y = currentOffsetY;
        imageView.setViewport(new Rectangle2D(x, y, width, height));
    }
}
