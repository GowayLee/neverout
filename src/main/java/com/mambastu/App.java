package com.mambastu;

import com.mambastu.material.resource.ImgCache;
import com.mambastu.util.AudioManager;
import com.mambastu.util.GlobalVar;

import javafx.application.Application;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {

        StackPane root = new StackPane();
        scene = new Scene(root, 1920, 1080);
        // 绑定StackPane的尺寸到场景的尺寸
        root.prefWidthProperty().bind(scene.widthProperty());
        root.prefHeightProperty().bind(scene.heightProperty());
        GlobalVar.getScreenWidthProperty().bind(scene.widthProperty());
        GlobalVar.getScreenHeightProperty().bind(scene.heightProperty());

        ResourceManager.getInstance().loadResources(); // 初始化图片资源管理器，载入JSON
        AudioManager.getInstance().loadResources(); // 初始化音频资源管理器，载入JSON
        PropStore.getInstance().loadResources(); // 初始化道具存储器，载入JSON
        InputManager.init(scene); // 初始化输入管理器

        Image cursorImage = ResourceManager.getInstance().getImg("cursorImage", "System", "MainMenu"); // 载入光标图片，并设置为鼠标光标
        ImageCursor customCursor = new ImageCursor(cursorImage, 4, 8);
        scene.setCursor(customCursor);

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


