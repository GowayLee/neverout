package com.mambastu.ui;

import com.mambastu.controller.level.context.dto.Context;
import com.mambastu.controller.level.context.enums.GameMode;
import com.mambastu.listener.MainMenuListener;
import com.mambastu.material.pojo.entity.player.PlayerTypes;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class MainMenu {
    private final MainMenuListener listener;

    private final StackPane root;
    private final Context ctx;

    private final SimpleObjectProperty<GameMode> gameMode; // 游戏模式
    private final SimpleObjectProperty<PlayerTypes> playerType; // 玩家类型

    private final Pane menuPane;

    public MainMenu(StackPane root, Context ctx,  MainMenuListener listener) {
        this.root = root;
        this.ctx = ctx;
        this.listener = listener;
        this.gameMode = new SimpleObjectProperty<>(GameMode.NORMAL);
        this.playerType = new SimpleObjectProperty<>(PlayerTypes.LaughPlayer);
        this.menuPane = new Pane();
    }

    public void show() {
        root.getChildren().remove(menuPane);
        root.getChildren().add(menuPane); // 确保菜单在游戏场景之上(StackPane的栈顶)
    }

    public void hide() {
        root.getChildren().remove(menuPane);
    }

    
    /** 
     * @param bindProperties(
     */
    public void init() { // 初始化
        bindProperties();
        buildLayout();
    }

    private void bindProperties() {
        ctx.getGameMode().bind(gameMode); // 将游戏模式绑定到上下文
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
            gameMode.set(GameMode.NORMAL);;
        });
        menuPane.getChildren().add(testBtn);
    }

}
