package com.mambastu.utils;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import lombok.Getter;

public class GlobalVar {
    private static final SimpleDoubleProperty screenWidth = new SimpleDoubleProperty();
    private static final SimpleDoubleProperty screenHeight = new SimpleDoubleProperty();
    // 获取游戏面板。
    private static Pane gamePane;
    private static Scene primeScene; // 场景。

    static {
        gamePane = new Pane(); // 防止NullPointerException
        screenHeight.set(1080.0);
        screenWidth.set(1920.0);
    }

    public static void setPrimeScene(Scene scene) { // 设置场景。
        primeScene = scene;
    }
    
    public static Scene getPrimeScene() { // 获取场景。
        return primeScene;
    }

    public static void setGamePane(Pane pane) { // 设置游戏面板。
        gamePane = pane;
    }

    public static double getScreenWidth() {
        return screenWidth.get();
    }

    public static SimpleDoubleProperty getScreenWidthProperty() { // 获取屏幕宽度属性。
        return screenWidth;
    }

    public static double getScreenHeight() { // 获取屏幕高度。
        return screenHeight.get();
    }

    public static SimpleDoubleProperty getScreenHeightProperty() { // 获取屏幕高度属性。
        return screenHeight;
    }
}
