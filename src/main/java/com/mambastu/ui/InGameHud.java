package com.mambastu.ui;


import com.mambastu.controller.level.context.dto.Context;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

public class InGameHud {
    private final StackPane root;
    private final Context ctx;

    private final SimpleIntegerProperty remainDuration;
    private final SimpleIntegerProperty HP;
    private final SimpleIntegerProperty killCount;

    private final Pane menuPane;

    public InGameHud(StackPane root, Context ctx) {
        this.root = root;
        this.ctx = ctx;
        this.menuPane = new Pane();
        this.HP = new SimpleIntegerProperty(100); // 假设初始生命值为100
        this.remainDuration = new SimpleIntegerProperty(0);
        this.killCount = new SimpleIntegerProperty(0); // 初始击杀数为0
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

    private void bindProperties() { // 绑定属性，例如生命值、分数等
        remainDuration.bind(ctx.getLevelRecord().getRemainDuration());
        HP.bind(ctx.getLevelRecord().getPlayer().getHP());
        killCount.bind(ctx.getLevelRecord().getKillCount());
    }

    private void buildLayout() { // TODO: 构建布局，包括显示分数、生命值、游戏时间等游戏信息。
        Label HPLabel = new Label();
        HPLabel.textProperty().bind(HP.asString("HP: %d"));
        HPLabel.setStyle("-fx-text-fill: red;"); // 设置文本颜色为红色
        HPLabel.setFont(new Font("Segoe Script", 40)); // 设置字体大小和样式
        HPLabel.setLayoutX(50);
        HPLabel.setLayoutY(20);

        Label countdownLabel = new Label();
        countdownLabel.textProperty().bind(remainDuration.asString("Time Left: %d s"));
        countdownLabel.setStyle("-fx-text-fill: black;"); // 设置文本颜色为红色
        countdownLabel.setFont(new Font("Segoe Script", 30)); // 设置字体大小和样式
        countdownLabel.setLayoutX(500);
        countdownLabel.setLayoutY(25);

        Label killCountLabel = new Label();
        killCountLabel.textProperty().bind(killCount.asString("Kill: %d "));
        killCountLabel.setStyle("-fx-text-fill: red;"); // 设置文本颜色为红色
        killCountLabel.setFont(new Font("Segoe Script", 40)); // 设置字体大小和样式
        killCountLabel.setLayoutX(1000);
        killCountLabel.setLayoutY(20);

        menuPane.getChildren().addAll(HPLabel, countdownLabel, killCountLabel);
    }
}
