package com.mambastu.ui;

import com.mambastu.controller.level.context.dto.record.LevelRecord;
import com.mambastu.listener.PauseMenuListener;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PauseMenu {
    private final PauseMenuListener listener;

    private final StackPane root;
    private LevelRecord record;

    private final Pane menuPane;

    public PauseMenu(StackPane root, PauseMenuListener listener) {
        this.root = root;
        this.listener = listener;
        this.menuPane = new Pane();
    }

    public void init(LevelRecord record) { // 初始化
        this.record = record;
        buildLayout();
        menuPane.setStyle("-fx-background-color: black;");
        menuPane.setOpacity(0.7); // 设置不透明度
    }

    public void show() {
        root.getChildren().remove(menuPane);
        root.getChildren().add(menuPane); // 确保菜单在游戏场景之上(StackPane的栈顶)
    }

    public void hide() {
        root.getChildren().remove(menuPane);
    }

    private void buildLayout() {
        // 创建显示暂停文字的Text节点
        Text pauseText = new Text("Pause!");
        pauseText.setFill(Color.WHITE); // 设置文本颜色为白色
        pauseText.setFont(new Font("Segoe Script", 300));
        pauseText.setLayoutX((root.getWidth() - pauseText.getLayoutBounds().getWidth()) / 2); // 居中
        pauseText.setLayoutY(root.getHeight() / 2); // 居中
        
        Text levelNumText = new Text("Current Level: " + String.valueOf(record.getLevelNum()));
        levelNumText.setFill(Color.WHITE); // 设置文本颜色为白色
        levelNumText.setFont(new Font("Segoe Script", 50)); // 设置字体大小和样式
        levelNumText.setLayoutX(50);
        levelNumText.setLayoutY(100);
        menuPane.getChildren().addAll(pauseText, levelNumText);
    }
}
