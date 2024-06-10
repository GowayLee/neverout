package com.mambastu;

import com.mambastu.material.resource.ImgCache;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

import com.mambastu.controller.input.InputManager;
import com.mambastu.controller.level.LevelController;
import com.mambastu.material.resource.ResourceManager;

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
        
        ResourceManager.getInstance().loadResources(); // 初始化资源管理器，载入JSON
        InputManager.init(scene); // 初始化输入管理器
        LevelController controller = new LevelController(root);
        controller.showMainMenu();

        stage.setTitle("Never Out");
        stage.setScene(scene);
        stage.getIcons().add(ImgCache.getImage("/static/image/player1.png"));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    
}