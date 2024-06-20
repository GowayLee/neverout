package com.mambastu.view.dynamic.impl;

import com.mambastu.controller.context.dto.Context;
import com.mambastu.controller.listener.PauseMenuListener;
import com.mambastu.view.dynamic.DynamicMenu;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PauseMenu implements DynamicMenu{
    private final PauseMenuListener listener;

    private final StackPane root;
    private final Context ctx;

    private final SimpleIntegerProperty levelNum;

    private final Pane menuPane;

    public PauseMenu(StackPane root, Context ctx, PauseMenuListener listener) {
        this.root = root;
        this.ctx = ctx;
        this.listener = listener;
        this.levelNum = new SimpleIntegerProperty(0);
        this.menuPane = new Pane();
    }

    
    /** 
     * @param show(
     */
    @Override
    public void init() { // 初始化
        bindProperties();
        buildLayout();
        menuPane.setStyle("-fx-background-color: black;");
        menuPane.setOpacity(0.7); // 设置不透明度
    }

    @Override
    public void update() {
        bindProperties();
    }

    @Override
    public void show() {
        root.getChildren().remove(menuPane);
        root.getChildren().add(menuPane); // 确保菜单在游戏场景之上(StackPane的栈顶)
    }

    @Override
    public void hide() {
        root.getChildren().remove(menuPane);
    }

    private void bindProperties() {
        levelNum.bind(ctx.getLevelRecord().getLevelNum());
    }

    private void buildLayout() {
        // 创建显示暂停文字的Text节点
        Text pauseText = new Text("Pause!");
        pauseText.setFill(Color.WHITE); // 设置文本颜色为白色
        pauseText.setFont(new Font("Segoe Script", 300));
        pauseText.setLayoutX((root.getWidth() - pauseText.getLayoutBounds().getWidth()) / 2); // 居中
        pauseText.setLayoutY(root.getHeight() / 2); // 居中
        
        Label levelNumLabel = new Label();
        levelNumLabel.textProperty().bind(levelNum.asString("Current Level: %d"));
        levelNumLabel.setStyle("-fx-text-fill: white;"); // 设置文本颜色为红色
        levelNumLabel.setFont(new Font("Segoe Script", 50)); // 设置字体大小和样式
        levelNumLabel.setLayoutX(50);
        levelNumLabel.setLayoutY(100);
        menuPane.getChildren().addAll(pauseText, levelNumLabel);
    }
}
