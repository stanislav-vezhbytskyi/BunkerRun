package view;

import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class BotController {
    private Bot bot;
    private ArrayList<Bot> botList = new ArrayList<>();
    private int botNumber;
    private Random rand = new Random();
    private int random;
    private boolean isBotRunning = false;
    private final int MAX_BOT_NUMBER = 10;
    private final int BOT_SPAWN_FREQUENCY = 1000;

    private void createBot(Pane gameRoot) {
        if (botList.size() < MAX_BOT_NUMBER) {
            random = rand.nextInt(BOT_SPAWN_FREQUENCY);

            if(random ==0||random == 1||random == 2){
                bot = new Bot("BotSprite1.png", 0, 0);
                bot.setTranslateY(150+200*random);
                bot.setTranslateX(1500);
                botList.add(bot);
                gameRoot.getChildren().add(bot);
            }
        }
    }

    private void botMove(Player player) {
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

    private void checkBotsAlive(Pane gameRoot) {
        Iterator<Bot> iterator = botList.iterator();
        while (iterator.hasNext()) {
            Bot bot = iterator.next();
            if (bot.getHP() <= 0) {
                gameRoot.getChildren().remove(bot);
                iterator.remove();
            }
        }
    }
    public void updateBot(Pane gameRoot,Player player){
        createBot(gameRoot);

        botMove(player);
        checkBotsAlive(gameRoot);
    }
    public ArrayList<Bot> getBotList() {
        return botList;
    }
}
