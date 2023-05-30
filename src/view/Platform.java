package view;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;

public class Platform {
    public static final int BLOCK_SIZE = 25;
    public static ArrayList<Node> generateAllBlocks(Pane gameRoot){
        ArrayList<Node> listBlock = new ArrayList<>();
        for (int i = 0; i < LevelData.LEVEL1.length; i++) {
            String line = LevelData.LEVEL1[i];
            for (int j = 0; j < line.length(); j++) {
                switch (line.charAt(j)) {
                    case '0':
                        break;
                    case '1':
                        Node standardPlatform = createEntity(j * BLOCK_SIZE, i * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, new Image("Artboard 1.png"));
                        gameRoot.getChildren().add(standardPlatform);
                        listBlock.add(standardPlatform);
                        break;
                    case '2':
                        Node jumpPlatform = createEntity(j * BLOCK_SIZE, i * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, new Image("Artboard 2.png"));
                        gameRoot.getChildren().add(jumpPlatform);
                        listBlock.add(jumpPlatform);
                        break;
                    case '3':
                        Node invisibleBridge = createEntity(j * BLOCK_SIZE, i * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, new Image("img_1.png"));
                        gameRoot.getChildren().add(invisibleBridge);
                        break;
                }
            }
        }
        return listBlock;
    }
    private static Node createEntity(int x, int y, int w, int h, Image image) {
        Pane entity = new Pane();
        entity.setTranslateX(x);
        entity.setTranslateY(y);
        entity.setPrefWidth(w);
        entity.setPrefHeight(h);
        entity.getChildren().add(new ImageView(image));
        return entity;
    }
}
