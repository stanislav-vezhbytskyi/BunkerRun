package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class StoreMessage {
    public static void showMessage(){
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);

        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: #304e4e");
        Label messageLabel = new Label("Недостатньо монет");
        messageLabel.setLayoutX(25);
        messageLabel.setLayoutY(35);
        messageLabel.setPrefWidth(250);
        messageLabel.getStyleClass().add("main-label");

        Button okButton = new Button("OK");
        okButton.setLayoutX(100);
        okButton.setLayoutY(90);
        okButton.setPrefWidth(100);
        okButton.getStyleClass().addAll("button", "ok-button");
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.close();
            }
        });
        pane.getChildren().addAll(messageLabel, okButton);

        Scene scene = new Scene(pane,300,145);
        scene.getStylesheets().add(StoreMessage.class.getResource("/store_styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
