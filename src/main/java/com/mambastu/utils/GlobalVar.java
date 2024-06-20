package com.mambastu.utils;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;

public class GlobalVar {
    private static final SimpleDoubleProperty screenWidth = new SimpleDoubleProperty();
    private static final SimpleDoubleProperty screenHeight = new SimpleDoubleProperty();
    private static Pane gamePane;

    static {
        gamePane = new Pane(); // 防止NullPointerException
        screenHeight.set(1080.0);
        screenWidth.set(1920.0);
    }

    public static Pane getGamePane() { // 获取游戏面板。
        return gamePane;
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
