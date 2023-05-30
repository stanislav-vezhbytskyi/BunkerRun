package view;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import model.Skin;
import model.SkinService;
import model.Sounds;

import java.util.ArrayList;

import static view.ViewManager.HEIGHT;
import static view.ViewManager.WIDTH;

public class Store {
    private AnchorPane storePane;
    private Scene storeScene;
    private GridPane mainPane;
    private Label coinsLabel;
    public Store() {
        storePane = new AnchorPane();
        storeScene = new Scene(storePane, WIDTH, HEIGHT);
        storeScene.getStylesheets().add(getClass().getResource("/store_styles.css").toExternalForm());
        setBackground();
        addBackButton();
        addCoinsLabel();
        initCoinsLabel();
        initGridPane();
    }
    private void addCoinsLabel() {
        coinsLabel = new Label();
        coinsLabel.setLayoutX(580);
        coinsLabel.setLayoutY(618);
        coinsLabel.getStyleClass().add("coins-label");
        storePane.getChildren().add(coinsLabel);
    }
    private void initCoinsLabel() {
        coinsLabel.setText(String.valueOf(ViewManager.getCoins().getTotalCoins()));
    }
    private void setBackground() {
        Image backgroundImage = new Image("resources/store.png", WIDTH, HEIGHT, false, true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        storePane.setBackground(new Background(background));
    }
    private void initGridPane() {
        mainPane = new GridPane();
        mainPane.setLayoutX(0);
        mainPane.setLayoutY(156);
        mainPane.setPrefWidth(1200);
        mainPane.setPrefHeight(440);
        mainPane.getStyleClass().add("grid-pane");
        mainPane.getColumnConstraints().add(new ColumnConstraints(60));
        mainPane.getColumnConstraints().add(new ColumnConstraints(320));
        mainPane.getColumnConstraints().add(new ColumnConstraints(60));
        mainPane.getColumnConstraints().add(new ColumnConstraints(320));
        mainPane.getColumnConstraints().add(new ColumnConstraints(60));
        mainPane.getColumnConstraints().add(new ColumnConstraints(320));
        mainPane.getColumnConstraints().add(new ColumnConstraints(60));

        mainPane.getRowConstraints().add(new RowConstraints(30));
        mainPane.getRowConstraints().add(new RowConstraints(175));
        mainPane.getRowConstraints().add(new RowConstraints(30));
        mainPane.getRowConstraints().add(new RowConstraints(175));
        mainPane.getRowConstraints().add(new RowConstraints(30));

        ArrayList<Skin> skins = SkinService.getSkins();
        int column = 1, row = 1;
        for(Skin skin: skins) {
            Pane skinPane = new Pane();
            skinPane.getStyleClass().add("skin-pane");
            skinPane.setMaxWidth(320);
            skinPane.setMaxHeight(170);

            Label skinNameLabel = new Label(skin.getTitle());
            skinNameLabel.setLayoutX(140);
            skinNameLabel.setLayoutY(30);
            skinNameLabel.getStyleClass().add("main-label");
            skinPane.getChildren().add(skinNameLabel);

            ImageView image = new ImageView(skin.getImage());
            image.setFitWidth(100);
            image.setFitHeight(140);
            image.setLayoutX(20);
            image.setLayoutY(15);
            skinPane.getChildren().add(image);

            Button button = new Button();
            button.setLayoutX(190);
            button.setLayoutY(120);
            button.setPrefWidth(110);
            button.getStyleClass().add("button");
            if(!skin.getIsBought()){
                Label priceLabel = new Label("Ціна:  " + skin.getPrice());
                priceLabel.setLayoutX(140);
                priceLabel.setLayoutY(70);
                priceLabel.getStyleClass().add("price-label");
                skinPane.getChildren().add(priceLabel);

                ImageView coinImage = new ImageView("Coin.png");
                coinImage.setFitWidth(28);
                coinImage.setFitHeight(28);
                coinImage.setLayoutX(230);
                coinImage.setLayoutY(65);
                skinPane.getChildren().add(coinImage);

                button.setText("КУПИТИ");
                button.getStyleClass().add("buy");
                button.setOnMouseReleased(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if(event.getButton().equals(MouseButton.PRIMARY)) {
                            buySkin(skin);
                        }
                    }
                });
            } else if(skin.getIsBought() && !skin.getIsPicked()){
                button.setText("ОБРАТИ");
                button.getStyleClass().add("pick");
                button.setOnMouseReleased(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if(event.getButton().equals(MouseButton.PRIMARY)) {
                            pickSkin(skin);
                        }
                    }
                });
            } else {
                button.setText("ОБРАНИЙ");
                button.getStyleClass().add("picked");
            }
            skinPane.getChildren().add(button);
            mainPane.add(skinPane, column, row);
            column += 2;
            if(column == 7) {
                column = 1;
                row += 2;
            }
        }
        storePane.getChildren().add(mainPane);
    }
    private void buySkin(Skin skin) {
        if(ViewManager.getCoins().deductCoinsForBuyingSkin(skin.getPrice())) {
            SkinService.buySkin(skin.getId());
            SkinService.pickSkin(skin.getId());
            initGridPane();
            initCoinsLabel();
        } else
            StoreMessage.showMessage();
    }
    private void pickSkin(Skin skin) {
        SkinService.pickSkin(skin.getId());
        initGridPane();
    }
    private void addBackButton() {
        Button backButton = new Button();
        backButton.setLayoutX(34);
        backButton.setLayoutY(34);
        backButton.setPrefWidth(70);
        backButton.setPrefHeight(70);
        backButton.getStyleClass().add("back-button");
        backButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    Sounds.getInstance().clickButtonSound();
                    ViewManager.getInstance().switchToMainMenu();
                }
            }
        });
        storePane.getChildren().add(backButton);
    }
    public void openStore() {
        ViewManager.getInstance().setMode(Mode.STORE);
        ViewManager.getInstance().setMainScene(storeScene);
    }
}