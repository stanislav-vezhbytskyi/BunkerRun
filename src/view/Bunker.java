package view;

import javafx.scene.shape.Rectangle;

public class Bunker {
    private int bunkerHP;
    private Rectangle lineHP;
    private Rectangle bunkerArea;

    public Bunker(int bunkerHP, int x, int y, int width, int height) {
        this.bunkerHP = bunkerHP;
        this.lineHP = new Rectangle(x, y, width, height);
        bunkerArea = new Rectangle(0,0,350,ViewManager.HEIGHT);
    }

    public void setBunkerHP(int newHP) {
        this.bunkerHP = newHP;
    }

    public int getBunkerHP() {
        return bunkerHP;
    }
    public Rectangle getBunkerArea(){
        return bunkerArea;
    }
    public Rectangle getLineHP() {
        return this.lineHP;
    }
    public void updateLineHP(){
        if(getBunkerHP()>0){
             lineHP.setWidth(getBunkerHP()*0.5);
        }
    }
}
