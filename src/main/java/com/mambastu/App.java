package com.mambastu;

import com.mambastu.material.resource.ImgCache;
import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.io.IOException;
import java.net.URL;

import com.mambastu.controller.input.InputManager;
import com.mambastu.controller.level.LevelController;
import com.mambastu.material.resource.ResourceManager;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private Stage stage; // 新增的stage引用
    private WebView webView; // 新增的webView引用

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage; // 初始化stage引用

        // 添加WebView组件
        webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        // 加载HTML文件
        URL url = getClass().getResource("/mainMenu.html");
        if (url == null) {
            System.out.println("Unable to find HTML file");
            throw new IOException("Unable to find HTML file");
        }
        System.out.println("HTML file URL: " + url.toExternalForm());
        webEngine.load(url.toExternalForm());

        // 将Java方法暴露给JavaScript
        //TODO: 1. 背景设置 2. 标题字体设置 3. 移动尺寸后无法正常调用函数debug 4. 优化动画显示
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("app", new JavaApp()); // 暴露Java对象给JavaScript
                System.out.println("Java methods are now accessible from JavaScript.");
            } else if (newState == Worker.State.FAILED) {
                System.out.println("Failed to load HTML content");
            }
        });

        // 设置初始场景
        StackPane root = new StackPane();
        root.getChildren().add(webView); // 将WebView添加到根节点
        scene = new Scene(root, 1920, 1080);
        stage.setScene(scene);
        stage.setTitle("Never Out");
        stage.getIcons().add(ImgCache.getImage("/static/image/player1.png"));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public class JavaApp {
        public void startGame() {
            System.out.println("开始游戏方法已调用");

            // 重新设置游戏场景
            StackPane root = new StackPane();
            scene = new Scene(root, 1920, 1080);
            // 绑定StackPane的尺寸到场景的尺寸
            root.prefWidthProperty().bind(scene.widthProperty());
            root.prefHeightProperty().bind(scene.heightProperty());

            ResourceManager.getInstance().loadResources(); // 初始化资源管理器，载入JSON
            InputManager.init(scene); // 初始化输入管理器
            LevelController controller = new LevelController(root);
            controller.showMainMenu();

            stage.setScene(scene); // 更新stage的场景
        }

        public void selectMode() {
            System.out.println("选择游戏模式方法已调用");
        }

        public void selectPlayerType() {
            System.out.println("选择玩家对象类型方法已调用");
        }

        public void exitGame() {
            System.out.println("退出游戏方法已调用");
        }
    }
}
