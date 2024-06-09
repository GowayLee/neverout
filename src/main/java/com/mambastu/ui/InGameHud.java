package com.mambastu.ui;


import com.mambastu.controller.level.context.dto.Context;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class InGameHud {
    private final StackPane root;

    private final Pane menuPane;

    private Context ctx;

    public InGameHud(StackPane root) {
        this.root = root;
        this.menuPane = new Pane();
    }

    public void init(Context ctx) { // 初始化
        this.ctx = ctx;
        buildLayout();
        menuPane.setStyle("-fx-background-color: red;");
        menuPane.setOpacity(0.2); // 设置不透明度
    }

    public void show() {
        root.getChildren().remove(menuPane);
        root.getChildren().add(menuPane); // 确保菜单在游戏场景之上(StackPane的栈顶)
    }

    public void hide() {
        root.getChildren().remove(menuPane);
    }

    private void buildLayout() { // TODO: 构建布局，包括显示分数、生命值、游戏时间等游戏信息。
        Rectangle rect = new Rectangle(200, 100, Color.BLUE); // 创建一个矩形，用于显示分数和生命值等游戏信息。
        rect.setTranslateX(10); // 设置矩形的位置，使其位于屏幕的左上角。
        rect.setTranslateY(10); // 设置矩形的位置，使其位于屏幕的左上角。
        menuPane.getChildren().add(rect); // 将矩形添加到菜单面板中。


        
    }
}
