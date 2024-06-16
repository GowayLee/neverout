package com.mambastu;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.beans.binding.Bindings;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import com.mambastu.controller.input.InputManager;
import com.mambastu.controller.level.LevelController;
import com.mambastu.material.resource.ResourceManager;
import com.mambastu.util.PropStore;

/**
 * JavaFX App
 */
public class Test extends Application {

    private static Scene scene;
    private  Stage stage; // 新增的stage引用
    private WebView webView; // 新增的webView引用

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("JavaFX Layout Example");

        // 创建GridPane布局
        GridPane gridPane = new GridPane();


        // 设置列约束
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();
        col1.setPercentWidth(33.33);
        col2.setPercentWidth(33.33);
        col3.setPercentWidth(33.33);
        gridPane.getColumnConstraints().addAll(col1, col2, col3);

        // 设置行约束
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        RowConstraints row3 = new RowConstraints();
        row1.setPercentHeight(33.33);
        row2.setPercentHeight(33.33);
        row3.setPercentHeight(33.33);
        gridPane.getRowConstraints().addAll(row1, row2, row3);

        // 添加毛玻璃效果
        Image bgImage = new Image("static/image/stone.png");
        ImageView imageView = new ImageView(bgImage);
        imageView.setEffect(new GaussianBlur(10));  // 模糊强度
        imageView.setPreserveRatio(false);
        imageView.fitWidthProperty().bind(gridPane.widthProperty());
        imageView.fitHeightProperty().bind(gridPane.heightProperty());
        GridPane.setConstraints(imageView, 0, 0, GridPane.REMAINING, GridPane.REMAINING);
        gridPane.getChildren().add(imageView);

        // 创建并设置标题
        Label title = new Label("JokerBro");
        title.styleProperty().bind(Bindings.concat("-fx-font-size: ", primaryStage.heightProperty().multiply(0.18), "px;"));
        GridPane.setConstraints(title, 0, 0, 3, 1, HPos.CENTER, VPos.CENTER);


        // 加载人物
        ImageView imageView2 = new ImageView(new Image("static/image/char/JokerPlayer.png"));  // 使用本地图片路径
        imageView2.setPreserveRatio(true);
        imageView2.fitWidthProperty().bind(gridPane.widthProperty().multiply(0.4));
        imageView2.fitHeightProperty().bind(gridPane.heightProperty().multiply(0.4));
        GridPane.setConstraints(imageView2, 1, 1, 1, 1, HPos.CENTER, VPos.CENTER);

        // 创建左眼和右眼
        Group leftEye = createEye();
        Group rightEye = createEye();

        // 将眼睛放置在人物图片内
        Pane leftEyePane = new Pane(leftEye);
        Pane rightEyePane = new Pane(rightEye);

        // 绑定左眼的位置
        leftEyePane.layoutXProperty().bind(imageView2.fitWidthProperty().multiply(0.25));
        leftEyePane.layoutYProperty().bind(imageView2.fitHeightProperty().multiply(0.35));

        // 绑定右眼的位置
        rightEyePane.layoutXProperty().bind(imageView2.fitWidthProperty().multiply(0.55));
        rightEyePane.layoutYProperty().bind(imageView2.fitHeightProperty().multiply(0.35));

        Pane imagePane = new Pane(imageView2, leftEyePane, rightEyePane);
        GridPane.setConstraints(imagePane, 1, 1, 1, 1, HPos.CENTER, VPos.CENTER);
        gridPane.getChildren().add(imagePane);

        // 监听鼠标移动事件，更新眼珠位置
        imagePane.setOnMouseMoved(event -> {
            double mouseX = event.getX();
            double mouseY = event.getY();

            updatePupilPosition(leftEye, mouseX, mouseY, leftEyePane);
            updatePupilPosition(rightEye, mouseX, mouseY, rightEyePane);
        });


        // 创建并设置按钮
        Button button1 = new Button("Start Game");
        Button button2 = new Button("Quit Game");
        Button button3 = new Button("Model Select");
        Button button4 = new Button("Character Select");
        // 动态调整按钮大小和文字大小
        button1.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.25));
        button1.prefHeightProperty().bind(primaryStage.heightProperty().multiply(0.1));
//        button1.styleProperty().bind(Bindings.concat("-fx-font-size: ", primaryStage.heightProperty().multiply(0.03).asString(), "px;"));
        button1.setOnAction(e -> startGame());
        button2.prefWidthProperty().bind(button1.prefWidthProperty());
        button2.prefHeightProperty().bind(button1.prefHeightProperty());
        button2.styleProperty().bind(button1.styleProperty());
        button3.prefWidthProperty().bind(button1.prefWidthProperty());
        button3.prefHeightProperty().bind(button1.prefHeightProperty());
        button3.styleProperty().bind(button1.styleProperty());
        button4.prefWidthProperty().bind(button1.prefWidthProperty());
        button4.prefHeightProperty().bind(button1.prefHeightProperty());
        button4.styleProperty().bind(button1.styleProperty());
        VBox leftButtons = new VBox(10, button1, button2);
        leftButtons.setAlignment(Pos.CENTER);
        VBox rightButtons = new VBox(10, button3, button4);
        rightButtons.setAlignment(Pos.CENTER);

        GridPane.setConstraints(leftButtons, 0, 2, 1, 1, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(rightButtons, 2, 2, 1, 1, HPos.CENTER, VPos.CENTER);

        // 将所有组件添加到GridPane
        gridPane.getChildren().addAll(title, leftButtons, rightButtons);

        // 创建场景并设置在舞台上
        Scene scene = new Scene(gridPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch();
    }

    public void startGame() {
        System.out.println("开始游戏方法已调用");

        // 重新设置游戏场景
        StackPane root = new StackPane();
        scene = new Scene(root, 1920, 1080);
        // 绑定StackPane的尺寸到场景的尺寸
        root.prefWidthProperty().bind(scene.widthProperty());
        root.prefHeightProperty().bind(scene.heightProperty());

        ResourceManager.getInstance().loadResources(); // 初始化资源管理器，载入JSON
        PropStore.getInstance().loadResources(); // 初始化道具存储器，载入JSON
        InputManager.init(scene); // 初始化输入管理器
        LevelController controller = new LevelController(root);
        controller.showMainMenu();

        stage.setScene(scene); // 更新stage的场景
    }

    // 创建眼睛背景和眼珠的方法
    private Group createEye() {
        // 创建眼睛背景
        Circle eyeBackground = new Circle(25);
        eyeBackground.setFill(Color.WHITE);
        eyeBackground.setStroke(Color.BLACK);
        eyeBackground.setStrokeWidth(2);

        // 创建眼珠
        Circle pupil = new Circle(10);
        pupil.setFill(Color.BLACK);

        // 将眼珠添加到眼睛背景中
        Group eye = new Group(eyeBackground, pupil);
        return eye;
    }

    // 更新眼珠位置的方法
    private void updatePupilPosition(Group eye, double mouseX, double mouseY, Pane eyePane) {
        Circle pupil = (Circle) eye.getChildren().get(1);
        Circle eyeBackground = (Circle) eye.getChildren().get(0);

        double eyeCenterX = eyePane.getLayoutX() + eyeBackground.getCenterX();
        double eyeCenterY = eyePane.getLayoutY() + eyeBackground.getCenterY();

        double deltaX = mouseX - eyeCenterX;
        double deltaY = mouseY - eyeCenterY;

        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        double maxDistance = eyeBackground.getRadius() - pupil.getRadius();

        if (distance > maxDistance) {
            deltaX = (deltaX / distance) * maxDistance;
            deltaY = (deltaY / distance) * maxDistance;
        }

        pupil.setTranslateX(deltaX);
        pupil.setTranslateY(deltaY);
    }

}
