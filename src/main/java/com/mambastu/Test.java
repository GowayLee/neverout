package com.mambastu;

import com.badlogic.gdx.Game;
import com.mambastu.material.resource.ImgCache;
import com.mambastu.ui.GameOverMenu;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

import com.mambastu.controller.input.InputManager;
import com.mambastu.controller.level.LevelController;
import com.mambastu.material.resource.ResourceManager;
import com.mambastu.util.PropStore;

/**
 * JavaFX App
 */
public class Test extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {

        StackPane root = new StackPane();
        scene = new Scene(root, 1920, 1080);
        // 绑定StackPane的尺寸到场景的尺寸
        root.prefWidthProperty().bind(scene.widthProperty());
        root.prefHeightProperty().bind(scene.heightProperty());

        ResourceManager.getInstance().loadResources(); // 初始化资源管理器，载入JSON
        PropStore.getInstance().loadResources(); // 初始化道具存储器，载入JSON
        InputManager.init(scene); // 初始化输入管理器
        LevelController controller = new LevelController(root);
        controller.showMainMenu();


        stage.setScene(scene);
        stage.setTitle("Never Out");
        stage.getIcons().add(ImgCache.getImage("/static/image/char/LaughPlayer.png"));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
