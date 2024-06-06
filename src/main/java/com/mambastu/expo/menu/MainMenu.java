package com.mambastu.expo.menu;

import com.mambastu.infuse.level.LevelManager;
import com.mambastu.infuse.level.comp.GameMode;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class MainMenu {
    private LevelManager levelManager;
    private final Scene scene;

    public MainMenu(Scene scene) {
        this.scene = scene;
    }

    public void show() {
        init();
    }

    private void init() {
        levelManager = new LevelManager(scene);
        buildLayout();
    }

    private void buildLayout() { // 构建主菜单的外观
        Pane pane = new Pane();
        // 创建开始按钮
        Button startBtn = new Button("Start");
        startBtn.setLayoutX(scene.getWidth() / 2);
        startBtn.setLayoutY(scene.getHeight() / 2);
        startBtn.setOnAction(e -> {
            levelManager.startLevel();
        });
        pane.getChildren().add(startBtn);

        Button testBtn = new Button("GameMode");
        testBtn.setLayoutX(scene.getWidth() / 2 + 50);
        testBtn.setLayoutY(scene.getHeight() / 2 + 50);
        testBtn.setOnAction(e -> {
            levelManager.getConfig().setGameMode(GameMode.NORMAL);;
        });
        pane.getChildren().add(testBtn);
        scene.setRoot(pane);
    }

}
