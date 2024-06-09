package com.mambastu;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

import com.mambastu.controller.input.InputManager;
import com.mambastu.controller.level.LevelController;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        StackPane root = new StackPane();
        scene = new Scene(root, 1920, 1080);
        // 绑定StackPane的尺寸到场景的尺寸
        root.prefWidthProperty().bind(scene.widthProperty());
        root.prefHeightProperty().bind(scene.heightProperty());
        
        InputManager.init(scene);
        LevelController controller = new LevelController(root);
        controller.init();
        controller.showMainMenu();

        stage.setTitle("Never Out");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    
}