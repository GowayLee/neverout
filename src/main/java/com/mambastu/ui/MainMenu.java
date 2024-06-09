package com.mambastu.ui;

import com.mambastu.controller.level.context.enums.GameMode;
import com.mambastu.listener.MainMenuListener;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class MainMenu {
    private final StackPane root;
    private final MainMenuListener listener;

    private final Pane menuPane;

    public MainMenu(StackPane root, MainMenuListener listener) {
        this.root = root;
        this.listener = listener;
        this.menuPane = new Pane();
    }

    public void show() {
        root.getChildren().remove(menuPane);
        root.getChildren().add(menuPane); // 确保菜单在游戏场景之上(StackPane的栈顶)
    }

    public void hide() {
        root.getChildren().remove(menuPane);
    }

    public void init() { // 初始化
        buildLayout();
    }

    private void buildLayout() {
        // 创建开始按钮
        Button startBtn = new Button("Start");
        startBtn.setLayoutX(root.getWidth() / 2);
        startBtn.setLayoutY(root.getHeight() / 2);
        startBtn.setOnAction(e -> {
            listener.startGame();
        });
        menuPane.getChildren().add(startBtn);

        Button testBtn = new Button("GameMode");
        testBtn.setLayoutX(root.getWidth() / 2 + 50);
        testBtn.setLayoutY(root.getHeight() / 2 + 50);
        testBtn.setOnAction(e -> {
            listener.selectGameMode(GameMode.NORMAL);
        });
        menuPane.getChildren().add(testBtn);
    }

}
