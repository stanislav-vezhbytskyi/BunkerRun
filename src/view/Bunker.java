package view;

import javafx.scene.shape.Rectangle;

public class Bunker {
    private double bunkerHP;
    private double maxHP;
    private Rectangle lineHP;
    private Rectangle strokeLineHP;
    private Rectangle bunkerArea;
    private int width;

    public Bunker(int bunkerHP, int x, int y, int width, int height) {
        this.width = width;
        this.bunkerHP = bunkerHP;
        this.maxHP = bunkerHP;
        this.lineHP = new Rectangle(x, y, width, height);
        strokeLineHP = new Rectangle(x, y, width, height);
        bunkerArea = new Rectangle(0,0,250,ViewManager.HEIGHT);
    }

    public void setBunkerHP(double newHP) {
        this.bunkerHP = newHP;
    }

    public double getBunkerHP() {
        return bunkerHP;
    }
    public Rectangle getBunkerArea(){
        return bunkerArea;
    }
    public Rectangle getLineHP() {
        return this.lineHP;
    }
    public Rectangle getStrokeLineHP(){
        return strokeLineHP;
    }
    public void updateLineHP(){
        if(getBunkerHP()>0){
             lineHP.setWidth(width*bunkerHP/maxHP);
        }
    }
}
