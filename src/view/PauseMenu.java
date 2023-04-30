package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static view.ViewManager.getInstance;

public class PauseMenu {
    public static void openPauseMenu(){
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        Pane pane = new Pane();

        Button backToMainMenu = new Button("Exit");
        backToMainMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.close();
                ViewManager.getInstance().switchToMainMenu();
            }
        });
        pane.getChildren().add(backToMainMenu);

        Scene scene = new Scene(pane,400,400);
        stage.setScene(scene);
        stage.show();
    }
}
