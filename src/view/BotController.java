package view;

import javafx.scene.layout.Pane;
import model.Coins;

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
    private int numberOfBotsOnLevel;
    public BotController(int numberOfBotsOnLevel){
        this.numberOfBotsOnLevel = numberOfBotsOnLevel;
    }

    // Check if all bots on the level are defeated
    public boolean botsAreOver(){
        return numberOfBotsOnLevel<=0;
    }

    // Create a new bot and add it to the game pane
    private void createBot(Pane gameRoot) {
        if (botList.size() < MAX_BOT_NUMBER&&numberOfBotsOnLevel-botList.size()>0) {
            random = rand.nextInt(BOT_SPAWN_FREQUENCY);

            if (random == 0 || random == 1 || random == 2) {
                bot = new Bot("Sprite-bot-2-neon.png", "Damage-bot.png",0, 0,2,60);
                bot.setTranslateY(140 + 225 * random);
                bot.setTranslateX(1940);
                botList.add(bot);
                gameRoot.getChildren().addAll(bot,bot.getImpactZone());
            }
        }
    }

    // Move the bots towards the player or away from the player depending on their position
    private void botMove(Player player, Bunker bunker) {
        botNumber = botList.size();
        for (int i = 0; i < botNumber; i++) {
            bot = botList.get(i);
            double playerBotHeightDifference = Math.abs(player.getTranslateY() - bot.getTranslateY());
            double playerBotWidthDifference = Math.abs(player.getTranslateX() - bot.getTranslateX());

            if (bot.isBotRunning) {
                // If the player is within the bot's viewing distance, move towards the player
                if (playerBotHeightDifference <= 30 && playerBotWidthDifference <= bot.getViewingDistance()) {
                    boolean playerOnRight = player.getTranslateX() - bot.getTranslateX() > 0;
                    bot.moveX(playerOnRight);
                    bot.isBotRunning = true;
                    bot.spriteAnimation.setAnimation(playerOnRight ? 1 : 0);
                    bot.spriteAnimation.play();
                }

                // If the player is outside the viewing distance, move away from the player
                if (playerBotHeightDifference > 30 || playerBotWidthDifference > bot.getViewingDistance()) {
                    bot.moveX(false);
                    bot.isBotRunning = true;
                    bot.spriteAnimation.setAnimation(0);
                    bot.spriteAnimation.play();
                }

                // If the bot intersects with the bunker area, stop and perform an attack
                if(bot.getBoundsInParent().intersects(bunker.getBunkerArea().getBoundsInParent())){
                    bot.isBotRunning = false;
                    bot.performAttack(player,bunker);
                }
            }
            // If the player is within the bot's impact radius, stop moving
            if (playerBotHeightDifference <= 30 && ((isPlayerOnRight(player, bot) && player.getTranslateX() + player.SIZE / 2 - bot.getTranslateX() - bot.SIZE / 2 <= bot.BOT_IMPACT_RADIUS) || (!isPlayerOnRight(player, bot) && bot.getTranslateX() - player.getTranslateX() - player.SIZE / 2 + bot.SIZE / 2 <= bot.BOT_IMPACT_RADIUS))) {
                bot.isBotRunning = false;
            } else if((bot.getTranslateX()>bunker.getBunkerArea().getWidth()-bot.getWidth())){
                bot.isBotRunning = true;
            }
            // Apply gravity to the bot's vertical movement
            if (bot.velocity.getY() < 6) {
                bot.velocity = bot.velocity.add(0, 1);
            }
            //bot.moveY((int) bot.botVelocity.getY());
            // If the bot is not running, perform an attack
            if (!bot.isBotRunning) {
                bot.performAttack(player,bunker);

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


    // Check if any bots are defeated and update the game state accordingly
    private void checkBotsAlive(Pane gameRoot, Player player) {
        Iterator<Bot> iterator = botList.iterator();
        while (iterator.hasNext()) {
            Bot bot = iterator.next();
            if (bot.getHP() <= 0) {
                numberOfBotsOnLevel--;
                gameRoot.getChildren().remove(bot);
                ViewManager.getCoins().addCoinsForKillingBot();
                iterator.remove();
                //
                // Increase the player's strafe amount if it's below a certain threshold
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

        // Update the bots' movements, check for defeated bots, and update health bar lines
        botMove(player,bunker);
        checkBotsAlive(gameRoot, player);

        // Update the health bar lines for all bots
        updateHPLines();
    }

    public ArrayList<Bot> getBotList() {
        return botList;
    }
}
