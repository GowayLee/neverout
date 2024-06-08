package com.mambastu.ui.menu;

import com.mambastu.controller.level.comp.GameMode;
import com.mambastu.listener.MainMenuListener;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class MainMenu {
    private final Scene scene;
    private final MainMenuListener listener;

    public MainMenu(Scene scene, MainMenuListener listener) {
        this.scene = scene;
        this.listener = listener;
    }

    public void show() {
        buildLayout();
    }

    private void buildLayout() { // 构建主菜单的外观
        Pane pane = new Pane();
        // 创建开始按钮
        Button startBtn = new Button("Start");
        startBtn.setLayoutX(scene.getWidth() / 2);
        startBtn.setLayoutY(scene.getHeight() / 2);
        startBtn.setOnAction(e -> {
            listener.startGame();
        });
        pane.getChildren().add(startBtn);

        Button testBtn = new Button("GameMode");
        testBtn.setLayoutX(scene.getWidth() / 2 + 50);
        testBtn.setLayoutY(scene.getHeight() / 2 + 50);
        testBtn.setOnAction(e -> {
            listener.selectGameMode(GameMode.NORMAL);
        });
        pane.getChildren().add(testBtn);
        scene.setRoot(pane);
    }

}
