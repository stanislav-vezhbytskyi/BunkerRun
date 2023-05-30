package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.BackgroundMusic;
import model.BunkerRunButton;


public class PauseMenu {
    public static void openPauseMenu(){
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);

        Rectangle bg = new Rectangle(0,0,450,250);

        bg.setFill(Color.color(48/255.0, 78/255.0, 78/255.0));
        Pane pane = new Pane();

        BunkerRunButton backToGame = new BunkerRunButton("Повернутись до гри",1);
        backToGame.setLayoutY(30);
        backToGame.setLayoutX(20);
        backToGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });

        BunkerRunButton settingButton = new BunkerRunButton("Налаштування",3);
        settingButton.setLayoutY(100);
        settingButton.setLayoutX(20);

        BunkerRunButton backToMainMenu = new BunkerRunButton("Вихід",2);
        backToMainMenu.setLayoutY(170);
        backToMainMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.close();
                BackgroundMusic.getInstance().stop();
                ViewManager.getInstance().switchToMainMenu();
            }
        });
        backToMainMenu.setLayoutX(20);


        pane.getChildren().addAll(bg,backToMainMenu,backToGame,settingButton);

        Scene scene = new Scene(pane,450,250);
        stage.setScene(scene);
        stage.show();
    }
}
