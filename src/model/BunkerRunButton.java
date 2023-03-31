package model;


import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;


public class BunkerRunButton extends Button {

    private String BUTTON_PRESSED_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/resources/menu_button_pressed.jpg');";
    private String BUTTON_FREE_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/resources/menu_button.jpg');";

    // в параметр конструктора необхідно ввести 1 (якщо це кнопка старту), 2 (якщо це кнопка виходу) або будь-яку інше ціле число (якщо це звичайна кнопка)

    public BunkerRunButton(String text, int v) {
        if (v == 1) {
            BUTTON_PRESSED_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/resources/menu_start_button_pressed.jpg');";
            BUTTON_FREE_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/resources/menu_start_button.jpg');";
        } else if (v == 2) {
            BUTTON_PRESSED_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/resources/menu_exit_button_pressed.jpg');";
            BUTTON_FREE_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/resources/menu_exit_button.jpg');";
        }
        setText(text);
        setButtonFont(v);
        setPrefWidth(410);
        setPrefHeight(65);
        setStyle(BUTTON_FREE_STYLE);
        initializeButtonListeners();
    }

    private void setButtonFont(int v) {
        if (v == 1) {
            setTextFill(Color.rgb(25, 136, 59));
        } else if (v == 2) {
            setTextFill(Color.rgb(185, 15, 18));
        } else {
            setTextFill(Color.rgb(70, 102, 149));
        }
        setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 25));
    }

    private void setButtonPressedStyle() {
        setStyle(BUTTON_PRESSED_STYLE);
        setPrefHeight(65);
        setLayoutY(getLayoutY() + 4);
    }

    private void setButtonReleasedStyle() {
        setStyle(BUTTON_FREE_STYLE);
        setPrefHeight(65);
        setLayoutY(getLayoutY() - 4);

    }

    private void initializeButtonListeners() {

        setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    setButtonPressedStyle();
                }

            }
        });

        setOnMouseReleased(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    setButtonReleasedStyle();
                }

            }
        });

        setOnMouseEntered(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                setEffect(new Glow());

            }
        });

        setOnMouseExited(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                setEffect(null);

            }
        });

    }

}
