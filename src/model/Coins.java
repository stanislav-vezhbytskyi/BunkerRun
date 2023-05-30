package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Coins {
    private final int coinsForKillingBot = 1;
    private int coinsForGame;
    private int totalCoins;
    public Coins() {
        totalCoins = getCoinsFromFile();
        coinsForGame = 0;
    }
    private int getCoinsFromFile() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("Main.properties"));
            return Integer.parseInt(properties.getProperty("coins"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public int getTotalCoins() {
        return totalCoins;
    }
    public int getCoinsForGame() { // ?
        return coinsForGame;
    }
    public void addCoinsForKillingBot() {
        coinsForGame += coinsForKillingBot;
    }
    public boolean deductCoinsForBuyingSkin(int skinPrice) {
        if(totalCoins >= skinPrice) {
            totalCoins -= skinPrice;
            saveCoinsToFile();
            return true;
        }
        return false;
    }
    private void saveCoinsToFile() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("Main.properties"));
            properties.setProperty("coins", String.valueOf(totalCoins));
            try (FileOutputStream fileOutputStream = new FileOutputStream("Main.properties")) {
                properties.store(fileOutputStream, "coins");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveCoinsForVictory() {
        totalCoins += coinsForGame;
        coinsForGame = 0;
        saveCoinsToFile();
    }
    public void resetCoinsForGame() {
        coinsForGame = 0;
    }
}
