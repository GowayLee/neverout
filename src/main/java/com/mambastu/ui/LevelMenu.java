package com.mambastu.ui;

import com.mambastu.controller.level.context.dto.Context;
import com.mambastu.listener.LevelMenuListener;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class LevelMenu {
    private final LevelMenuListener listener;

    private final StackPane root;
    private final Context ctx;

    private final SimpleIntegerProperty killCount;

    private final Pane menuPane;

    public LevelMenu(StackPane root, Context ctx, LevelMenuListener listener) {
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
        menuPane.setOpacity(0.85);
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

    private void buildLayout() { // TODO: 构建关卡菜单的外观
        Text passText = new Text("Level Clear!");
        passText.setFill(Color.WHITE); // 设置文本颜色为白色
        passText.setFont(new Font("Segoe Script", 220));
        passText.setLayoutX((root.getWidth() - passText.getLayoutBounds().getWidth()) / 2); // 居中
        passText.setLayoutY(root.getHeight() / 2); // 居中

        Button nextLevelBtn = new Button("Next Level!");
        nextLevelBtn.setLayoutX(root.getWidth() / 2);
        nextLevelBtn.setLayoutY(root.getHeight() / 2 + 100);
        nextLevelBtn.setOnAction(e -> {
            listener.startLevel();
        });
    
        Label killCountLabel = new Label();
        killCountLabel.textProperty().bind(killCount.asString("Kill: %d "));
        killCountLabel.setStyle("-fx-text-fill: red;"); // 设置文本颜色为红色
        killCountLabel.setFont(new Font("Segoe Script", 50)); // 设置字体大小和样式
        killCountLabel.setLayoutX(500);
        killCountLabel.setLayoutY(100);

        menuPane.getChildren().addAll(passText, nextLevelBtn, killCountLabel);
    }
}
