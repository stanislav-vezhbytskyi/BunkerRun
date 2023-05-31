package view;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import model.Sounds;

import static view.ViewManager.HEIGHT;
import static view.ViewManager.WIDTH;
public class Contacts {
    private AnchorPane pane;
    private Scene scene;
    public Contacts() {
        pane = new AnchorPane();
        pane.setBackground(Background.fill(Color.color(48/255.0, 78/255.0, 78/255.0)));
        createInfo();
        createButton();
        scene = new Scene(pane, WIDTH, HEIGHT);
    }

    private void createInfo() {
        Label label = new Label("""
                Виконали:
                Будковська Олександра
                Вежбицький Станіслав (моно: 5375414135604923)
                Вінярська Заріна
                Дробишев Владислав
                Кучер Даніл
                """);
        label.setTextFill(Color.WHITESMOKE);
        label.setPrefWidth(WIDTH);
        label.setPrefHeight(HEIGHT);
        label.setAlignment(Pos.CENTER);
        label.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 32));
        pane.getChildren().add(label);
    }

    private void createButton() {
        Button backButton = new Button();
        backButton.setLayoutX(34);
        backButton.setLayoutY(34);
        backButton.setPrefWidth(70);
        backButton.setPrefHeight(70);
        backButton.setStyle("-fx-background-color: #00f9ff;\n" +
                "    -fx-background-radius: 50;\n" +
                "    -fx-padding: 5;\n" +
                "    -fx-background-image: url('arrow.png');\n" +
                "    -fx-background-size: 50 50;\n" +
                "    -fx-background-repeat: no-repeat;\n" +
                "    -fx-background-position: center;");
        backButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    Sounds.getInstance().clickButtonSound();
                    ViewManager.getInstance().switchToMainMenu();
                }
            }
        });
        backButton.setOnMouseEntered(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                DropShadow effect = new DropShadow(BlurType.GAUSSIAN, Color.rgb(255, 255, 255, 0.6), 20, 0.5, 0, 0);
                backButton.setEffect(effect);

            }
        });

        backButton.setOnMouseExited(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                backButton.setEffect(null);

            }
        });
        pane.getChildren().add(backButton);
    }

    public Scene getScene() {
        return scene;
    }
}
