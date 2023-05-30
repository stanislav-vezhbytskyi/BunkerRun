package application;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import view.ViewManager;

public class MainClass extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage = ViewManager.getInstance().getMainStage();
            primaryStage.getIcons().add(new Image("logo.jpg"));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
