package view;

import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Random;

public class BotController {
    private Bot bot;
    private ArrayList<Bot> botList= new ArrayList<>();
    private int botNumber;
    private Random rand = new Random();
    private int random;
    private boolean isBotRunning = false;
    private final int MAX_BOT_NUMBER = 10;
    public void createBot(Pane gameRoot){
        if (botList.size() < MAX_BOT_NUMBER) {
            random = rand.nextInt(1000);
            if (random == 0) {
                bot = new Bot("BotSprite1.png", 0, 0);
                bot.setTranslateY(0);
                bot.setTranslateX(1500);
                botList.add(bot);
                gameRoot.getChildren().add(bot);

            } else if (random == 1) {
                bot = new Bot("BotSprite1.png", 0, 0);
                bot.setTranslateY(300);
                bot.setTranslateX(1500);
                botList.add(bot);
                gameRoot.getChildren().add(bot);
            } else if (random == 3) {
                bot = new Bot("BotSprite1.png", 0, 0);
                bot.setTranslateY(500);
                bot.setTranslateX(1500);
                botList.add(bot);
                gameRoot.getChildren().add(bot);
            }
        }
    }
    public void botMove(Player player) {
        botNumber = botList.size();
        for (int i = 0; i < botNumber; i++) {
            bot = botList.get(i);
            if (Math.abs(player.getTranslateY() - bot.getTranslateY()) <= 30 && (player.getTranslateX() - bot.getTranslateX() < 0)) {
                bot.moveX(false);
                isBotRunning = true;
                bot.spriteAnimation.setAnimation(0);
                bot.spriteAnimation.play();
            }

            if (Math.abs(player.getTranslateY() - bot.getTranslateY()) <= 30 && (player.getTranslateX() - bot.getTranslateX() > 0)) {
                bot.moveX(true);
                isBotRunning = true;
                bot.spriteAnimation.setAnimation(1);
                bot.spriteAnimation.play();
            }
            if (Math.abs(player.getTranslateY() - bot.getTranslateY()) > 30) {
                bot.moveX(false);
                isBotRunning = true;
                bot.spriteAnimation.setAnimation(0);
                bot.spriteAnimation.play();
            }
            if (bot.botVelocity.getY() < 6) {
                bot.botVelocity = bot.botVelocity.add(0, 1);
            }
            bot.moveY((int) bot.botVelocity.getY());
        }

    }
    public ArrayList<Bot> getBotList(){
        return botList;
    }
}
