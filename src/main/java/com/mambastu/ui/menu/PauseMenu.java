package com.mambastu.ui.menu;

import com.mambastu.listener.PauseMenuListener;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class PauseMenu {
    private final Scene scene;
    private final PauseMenuListener listener;

    public PauseMenu(Scene scene, PauseMenuListener listener) {
        this.scene = scene;
        this.listener = listener;
    }

    public void show() {
        buildLayout();
    }

    private void buildLayout() { // 构建暂停菜单的外观
        // 创建遮罩层
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);"); // 半透明黑色
        overlay.setVisible(false); // 初始时隐藏遮罩层
        overlay.prefWidthProperty().bind(scene.widthProperty()); // 遮罩层铺满整个场景宽度
        overlay.prefHeightProperty().bind(scene.heightProperty()); // 遮罩层铺满整个场景高度
        
    }
}
