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

    // Constructor for the Bot class
    public Bot(String urlImgSkin, String urlImgDamageArea, int x, int y, int DAMAGE,int SIZE) {
        // Call the constructor of the Player class
        super(urlImgSkin, urlImgDamageArea, x, y,SIZE,DAMAGE,1,1,20,800, 4);
        // Create an HPLine rectangle to represent the bot's HP
        HPLine = new Rectangle(this.getTranslateX(), this.getTranslateY() -10, SIZE*defaultHP/HP, 5);
        HPLine.setFill(Color.RED);
        getChildren().add(this.HPLine);
    }
    // Update the HPLine to reflect the current HP of the bot
    public void updateHPLine(){
        HPLine.setWidth((int)SIZE*HP/defaultHP);
    }
    // Perform an attack on the player and bunker
    public void performAttack(Player player,Bunker bunker){
        if(canAttack()){
            attack(player,bunker);
        }
    }

    // Execute the attack animation on the player and bunker
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
            // Reduce the player's HP if they are within the impact zone
            if (currentImpactZone.getBoundsInParent().intersects(player.getBoundsInParent())) {
                player.setHP(player.getHP() - DAMAGE);
            }
            // Reduce the bunker's HP if it is within the impact zone
            if(currentImpactZone.getBoundsInParent().intersects(bunker.getBunkerArea().getBoundsInParent())){
                bunker.setBunkerHP(bunker.getBunkerHP() - DAMAGE);
            }
            paneForImpactZoneAnimation.setVisible(false);
        });
        delay.play();

    }
}
