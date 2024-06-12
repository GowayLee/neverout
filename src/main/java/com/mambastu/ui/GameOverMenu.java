package com.mambastu.ui;

import com.mambastu.controller.level.context.dto.Context;
import com.mambastu.listener.GameOverMenuListener;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameOverMenu {
    private final GameOverMenuListener listener;

    private final StackPane root;
    private final Context ctx;

    private final SimpleIntegerProperty killCount;

    private final Pane menuPane;

    public GameOverMenu(StackPane root, Context ctx, GameOverMenuListener listener) {
        this.listener = listener;
        this.root = root;
        this.killCount = new SimpleIntegerProperty();
        this.ctx = ctx;
        this.menuPane = new Pane();
    }

    public void init() { // 初始化
        bindProperties();
        buildLayout();
        menuPane.setStyle("-fx-background-color: black;");
        menuPane.setOpacity(0.90);
    }

    public void show() {
        root.getChildren().remove(menuPane);
        root.getChildren().add(menuPane); // 确保菜单在游戏场景之上(StackPane的栈顶)
    }

    public void hide() {
        root.getChildren().remove(menuPane);
    }

    private void bindProperties() { // 绑定属性，例如生命值、分数等
        killCount.bind(ctx.getLevelRecord().getKillCount());
    }

    private void buildLayout() { // TODO: 构建布局，包括显示分数、生命值、游戏时间等游戏信息。
        Text uDieText = new Text("You Die!");
        uDieText.setFill(Color.WHITE); // 设置文本颜色为白色
        uDieText.setFont(new Font("Segoe Script", 320));
        uDieText.setLayoutX((root.getWidth() - uDieText.getLayoutBounds().getWidth()) / 2); // 居中
        uDieText.setLayoutY(root.getHeight() / 2); // 居中

        Button restartBtn = new Button("Restart!");
        restartBtn.setLayoutX(root.getWidth() / 2 - 100);
        restartBtn.setLayoutY(root.getHeight() / 2 + 100);
        restartBtn.setOnAction(e -> {
            listener.restartGame();
        });

        Button backMainMenuBtn = new Button("Back to MainMenu");
        backMainMenuBtn.setLayoutX(root.getWidth() / 2 + 100);
        backMainMenuBtn.setLayoutY(root.getHeight() / 2 + 100);
        backMainMenuBtn.setOnAction(e -> {
            listener.backMainMenu();
        });

        menuPane.getChildren().addAll(uDieText, restartBtn, backMainMenuBtn);
    }
}
