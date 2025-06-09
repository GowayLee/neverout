package com.mambastu;

import com.mambastu.controller.LevelController;
import com.mambastu.controller.PropStoreController;
import com.mambastu.resource.input.InputManager;
import com.mambastu.resource.media.impl.AudioManager;
import com.mambastu.resource.media.impl.ImageManager;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

import com.mambastu.utils.GlobalVar;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage primaryStage) throws IOException {
        // 适应系统显示缩放比例
        System.setProperty("prism.allowhidpi", "True");
        
        StackPane root = new StackPane();
        scene = new Scene(root, 1920, 1080);
        // 绑定StackPane的尺寸到场景的尺寸
        root.prefWidthProperty().bind(scene.widthProperty());
        root.prefHeightProperty().bind(scene.heightProperty());
        GlobalVar.getScreenWidthProperty().bind(scene.widthProperty());
        GlobalVar.getScreenHeightProperty().bind(scene.heightProperty());

        // initialize resource managers with parsing JSON file
        ImageManager.getInstance().loadResources(); // 初始化图片资源管理器，载入JSON
        AudioManager.getInstance().loadResources(); // 初始化音频资源管理器，载入JSON
        PropStoreController.getInstance().loadResources(); // 初始化道具存储器，载入JSON
        InputManager.getInstance().init(scene); // 初始化输入管理器

        Image cursorImage = ImageManager.getInstance().getImg("cursorImage", "System", "MainMenu"); // Using comstimized image as the cursor
        ImageCursor customCursor = new ImageCursor(cursorImage, 4, 8);
        scene.setCursor(customCursor);

        LevelController controller = new LevelController(root);
        controller.showMainMenu();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Never Out");
        primaryStage.getIcons().add(ImageManager.getInstance().getImg("bornImage", "Player", "LaughPlayer"));
        primaryStage.show();
    }

    @Override
    public void stop() {
        // release memory
        ImageManager.getInstance().clearCache(); // 清理图片缓存，释放内存
        AudioManager.getInstance().clearCache(); // 清理音频缓存，释放内存
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch();
    }
}


