package com.mambastu;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

import com.mambastu.controller.level.LevelController;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        Pane root = new Pane();
        scene = new Scene(root, 800, 800);
        
        LevelController controller = new LevelController(scene);
        controller.showMainMenu();

        stage.setTitle("Never Out");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    
}