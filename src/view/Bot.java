package view;

import javafx.animation.PauseTransition;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Bot extends Player{
    public static final int BOT_IMPACT_RADIUS = 100;
    public boolean isBotRunning = true;
    private final int defaultHP = 20;
    private int HP = 20;
    private Rectangle HPLine;
    private int viewingDistance = 400;
    public void setHP(int HP){
        this.HP = HP;
    }
    public int getHP(){
        return HP;
    }

    public int getViewingDistance(){
        return viewingDistance;
    }

    public Bot(String urlImgSkin, String urlImgDamageArea, int x, int y, int DAMAGE,int SIZE) {
        super(urlImgSkin, urlImgDamageArea, x, y,SIZE,DAMAGE,1,1,20,800, 4);
        HPLine = new Rectangle(this.getTranslateX(), this.getTranslateY() -10, SIZE*defaultHP/HP, 5);
        HPLine.setFill(Color.RED);
        getChildren().add(this.HPLine);
    }
    public void updateHPLine(){
        HPLine.setWidth((int)SIZE*HP/defaultHP);
    }
    public void performAttack(Player player,Bunker bunker){
        if(canAttack()){
            attack(player,bunker);
        }
    }

    public void attack(Player player,Bunker bunker) {
        this.spriteAnimation.setAnimation(looksToRight ? 3 : 2);
        this.spriteAnimation.play();

        Rectangle currentImpactZone = looksToRight ? this.rightImpactZone : this.leftImpactZone;

        impactZoneAnimation.setAnimation(looksToRight ? 0 : 1);

        paneForImpactZoneAnimation.setVisible(true);
        paneForImpactZoneAnimation.setTranslateX(currentImpactZone.getX());
        paneForImpactZoneAnimation.setTranslateY(currentImpactZone.getY());

        impactZoneAnimation.play();

        PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
        delay.setOnFinished(event -> {
            if (currentImpactZone.getBoundsInParent().intersects(player.getBoundsInParent())) {
                player.setHP(player.getHP() - DAMAGE);
            }
            if(currentImpactZone.getBoundsInParent().intersects(bunker.getBunkerArea().getBoundsInParent())){
                bunker.setBunkerHP(bunker.getBunkerHP() - DAMAGE);
                System.out.println(bunker.getBunkerHP());
            }
            paneForImpactZoneAnimation.setVisible(false);
        });
        delay.play();

    }
}
