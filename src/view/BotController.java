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
    private final int MAX_BOT_NUMBER = 10;
    private final int BOT_SPAWN_FREQUENCY = 900;

    private void createBot(Pane gameRoot) {
        if (botList.size() < MAX_BOT_NUMBER) {
            random = rand.nextInt(BOT_SPAWN_FREQUENCY);

            if (random == 0 || random == 1 || random == 2) {
                bot = new Bot("Sprite-bot-2-neon.png", 0, 0);
                bot.setTranslateY(140 + 225 * random);
                bot.setTranslateX(1940);
                botList.add(bot);
                gameRoot.getChildren().add(bot);
            }
        }
    }

    private void botMove(Player player, Bunker bunker) {
        botNumber = botList.size();
        for (int i = 0; i < botNumber; i++) {
            bot = botList.get(i);
            double playerBotHeightDifference = Math.abs(player.getTranslateY() - bot.getTranslateY());
            double playerBotWidthDifference = Math.abs(player.getTranslateX() - bot.getTranslateX());

            if (bot.isBotRunning) {
                if (playerBotHeightDifference <= 30 && playerBotWidthDifference <= bot.getViewingDistance()) {
                    boolean playerOnRight = player.getTranslateX() - bot.getTranslateX() > 0;
                    bot.moveX(playerOnRight);
                    bot.isBotRunning = true;
                    bot.spriteAnimation.setAnimation(playerOnRight ? 1 : 0);
                    bot.spriteAnimation.play();
                }

                if (playerBotHeightDifference > 30 || playerBotWidthDifference > bot.getViewingDistance()) {
                    bot.moveX(false);
                    bot.isBotRunning = true;
                    bot.spriteAnimation.setAnimation(0);
                    bot.spriteAnimation.play();
                }

                if(bot.getBoundsInParent().intersects(bunker.getBunkerArea().getBoundsInParent())){
                    bot.isBotRunning = false;
                    bot.kick(player,bunker);
                }
            }
            if (playerBotHeightDifference <= 30 && ((isPlayerOnRight(player, bot) && player.getTranslateX() + player.PLAYER_SIZE / 2 - bot.getTranslateX() - bot.BOT_SIZE / 2 <= bot.BOT_IMPACT_RADIUS) || (!isPlayerOnRight(player, bot) && bot.getTranslateX() - player.getTranslateX() - player.PLAYER_SIZE / 2 + bot.BOT_SIZE / 2 <= bot.BOT_IMPACT_RADIUS))) {
                bot.isBotRunning = false;
            } else if((bot.getTranslateX()>bunker.getBunkerArea().getWidth()-bot.getWidth())){
                bot.isBotRunning = true;
            }
            if (bot.botVelocity.getY() < 6) {
                bot.botVelocity = bot.botVelocity.add(0, 1);
            }
            //bot.moveY((int) bot.botVelocity.getY());
            if (!bot.isBotRunning) {
                if (bot.kickDelay == 0) {
                    bot.kick(player,bunker);

                    bot.kickDelay = 20;
                } else {
                    bot.kickDelay--;
                }
            }
        }

    }

    private boolean isPlayerOnRight(Player player, Bot bot) {
        return player.getTranslateX() > bot.getTranslateX();
    }

    public void updateHPLines() {
        botNumber = botList.size();
        for (int i = 0; i < botNumber; i++) {
            bot = botList.get(i);
            bot.updateHPLine();
        }
    }


    private void checkBotsAlive(Pane gameRoot, Player player) {
        Iterator<Bot> iterator = botList.iterator();
        while (iterator.hasNext()) {
            Bot bot = iterator.next();
            if (bot.getHP() <= 0) {
                gameRoot.getChildren().remove(bot);
                iterator.remove();
                if (player.getStrafeAmount() >= 75)
                    player.setStrafeAmount(100);
                else {
                    player.setStrafeAmount(player.getStrafeAmount() + 25);
                }
            }
        }
    }

    public void updateBot(Pane gameRoot, Player player, Bunker bunker) {
        createBot(gameRoot);

        botMove(player,bunker);
        checkBotsAlive(gameRoot, player);

        updateHPLines();
    }

    public ArrayList<Bot> getBotList() {
        return botList;
    }
}
