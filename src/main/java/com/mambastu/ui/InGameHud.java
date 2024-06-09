package com.mambastu.ui;


import com.mambastu.controller.level.context.dto.Context;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class InGameHud {
    private final StackPane root;
    private final Context ctx;

    private final SimpleIntegerProperty HP;

    private final Pane menuPane;

    public InGameHud(StackPane root, Context ctx) {
        this.root = root;
        this.ctx = ctx;
        this.menuPane = new Pane();
        this.HP = new SimpleIntegerProperty(100); // 假设初始生命值为100
    }

    public void init() { // 初始化
        bindProperties();
        buildLayout();
    }

    public void show() {
        root.getChildren().remove(menuPane);
        root.getChildren().add(menuPane); // 确保菜单在游戏场景之上(StackPane的栈顶)
    }

    public void hide() {
        root.getChildren().remove(menuPane);
    }

    private void bindProperties() { // 绑定属性，例如生命值、分数等(可以省略)
        HP.bind(ctx.getLevelRecord().getPlayer().getHP());
    }

    private void buildLayout() { // TODO: 构建布局，包括显示分数、生命值、游戏时间等游戏信息。
        // Text HPText = new Text("HP: " + String.valueOf(HP.get()));
        Text HPText = new Text();
        HPText.textProperty().bind(HP.asString("HP: %d"));
        HPText.setFill(Color.RED); // 设置文本颜色为白色
        HPText.setFont(new Font("Segoe Script", 40)); // 设置字体大小和样式
        HPText.setLayoutX(50);
        HPText.setLayoutY(100);

        menuPane.getChildren().add(HPText);
    }
}
