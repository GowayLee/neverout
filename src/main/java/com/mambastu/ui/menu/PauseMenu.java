package com.mambastu.ui.menu;

import com.mambastu.listener.PauseMenuListener;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PauseMenu {
    private final StackPane root;
    private final PauseMenuListener listener;

    private final Pane menuPane;

    public PauseMenu(StackPane root, PauseMenuListener listener) {
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
        menuPane.setStyle("-fx-background-color: black;");
        menuPane.setOpacity(0.7); // 设置不透明度
    }

    private void buildLayout() {
        // 创建显示暂停文字的Text节点
        Text pauseText = new Text("Pause!");
        pauseText.setFill(Color.WHITE);
        pauseText.setFont(new Font("Segoe Script", 300));
        pauseText.setX(root.getWidth() / 2 - pauseText.getLayoutBounds().getWidth() / 2); // 居中
        pauseText.setY(root.getHeight() / 2); // 居中
        

        menuPane.getChildren().add(pauseText);
    }
}
