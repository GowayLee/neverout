package com.mambastu.ui;

import com.mambastu.controller.level.context.dto.Context;
import com.mambastu.controller.level.context.enums.GameMode;
import com.mambastu.listener.MainMenuListener;
import com.mambastu.material.pojo.entity.player.PlayerTypes;
import com.mambastu.material.resource.ResourceManager;
import com.mambastu.util.BetterMath;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MainMenu {
    private final MainMenuListener listener;

    private final StackPane root;
    private final Context ctx;

    private final SimpleObjectProperty<GameMode> gameMode; // 游戏模式
    private final SimpleObjectProperty<PlayerTypes> playerType; // 玩家类型

    private final Pane menuPane;

    private final Group facePane;
    private final Group btnPane;

    public MainMenu(StackPane root, Context ctx,  MainMenuListener listener) {
        this.root = root;
        this.ctx = ctx;
        this.listener = listener;
        this.gameMode = new SimpleObjectProperty<>(GameMode.NORMAL);
        this.playerType = new SimpleObjectProperty<>(PlayerTypes.JokerPlayer);
        this.menuPane = new Pane();
        this.facePane = new Group();
        this.btnPane = new Group();
    }

    public void show() {
        root.getChildren().remove(menuPane);
        root.getChildren().add(menuPane); // 确保菜单在游戏场景之上(StackPane的栈顶)
    }

    public void hide() {
        root.getChildren().remove(menuPane);
    }

    public void init() { // 初始化
        bindProperties();
        buildLayout();
    }

    private void bindProperties() {
        ctx.getGameMode().bind(gameMode); // 将游戏模式绑定到上下文
    }

    private void buildLayout() {
        buildFace();
        buildBtns();
        menuPane.getChildren().addAll(facePane, btnPane);
    }

    private void buildBtns() {
        ImageView circleView = new ImageView(ResourceManager.getInstance().getImg("circleImage", "System", "MainMenu"));
        circleView.setFitWidth(600); 
        circleView.setFitHeight(120);
        circleView.setLayoutX(0);
        circleView.setLayoutY(0);
        circleView.setVisible(false);

        Text startBtn = new Text("Start Game");
        startBtn.setFont(Font.font("Segoe Script", FontWeight.BOLD, 80));
        startBtn.setFill(Color.BLACK);
        startBtn.setOpacity(0.8);
        startBtn.setLayoutX(50);
        startBtn.setLayoutY(85);

        startBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            circleView.setVisible(true); // 显示圆圈
        });

        startBtn.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            circleView.setVisible(false);
        });

        startBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            listener.startGame();
        });

        btnPane.getChildren().addAll(circleView, startBtn);

        btnPane.setLayoutX(70);
        btnPane.layoutYProperty()
            .bind(root.heightProperty().subtract(btnPane.layoutBoundsProperty().get().getHeight()).multiply(0.68));
    }

    private void buildFace() {
        Group leftEye = createEye();
        Group rightEye = createEye();
        ImageView face = new ImageView(ResourceManager.getInstance().getImg("bornImage", "Monster", "BossMonster"));
        face.setFitWidth(380);
        face.setFitHeight(380);
        leftEye.setLayoutX(face.getFitWidth() * 0.300); 
        leftEye.setLayoutY(face.getFitHeight() * 0.45); 
        rightEye.setLayoutX(face.getFitWidth() * 0.700);
        rightEye.setLayoutY(leftEye.getLayoutY());

        facePane.layoutXProperty()
            .bind(root.widthProperty().subtract(face.layoutBoundsProperty().get().getWidth()).divide(2));

        facePane.layoutYProperty()
            .bind(root.heightProperty().subtract(face.layoutBoundsProperty().get().getHeight()).divide(3));

        facePane.getChildren().addAll(face, leftEye, rightEye);

        root.setOnMouseMoved(event -> {
            updatePupilPosition(event.getX(), event.getY(), leftEye);
            updatePupilPosition(event.getX(), event.getY(), rightEye);
        });
    }

    private Group createEye() {
        // 创建眼睛背景
        Circle eyeBackground = new Circle(30);
        eyeBackground.setFill(Color.WHITE);
        eyeBackground.setStroke(Color.WHITE);
        eyeBackground.setStrokeWidth(2);

        // 创建眼珠
        Circle pupil = new Circle(13);
        pupil.setFill(Color.BLACK);

        // 将眼珠添加到眼睛背景中
        Group eye = new Group(eyeBackground, pupil);
        return eye;
    }

    // 更新眼珠位置的方法
    private void updatePupilPosition(double mouseX, double mouseY, Group eye) {
        Circle pupil = (Circle) eye.getChildren().get(1);
        Circle eyeBackground = (Circle) eye.getChildren().get(0);

        double eyeCenterX = eye.getLayoutX() + eyeBackground.getCenterX() + facePane.getLayoutX();
        double eyeCenterY = eye.getLayoutY() + eyeBackground.getCenterY() + facePane.getLayoutY();

        double deltaX = mouseX - eyeCenterX;
        double deltaY = mouseY - eyeCenterY;
        double distance = BetterMath.sqrt(deltaX * deltaX + deltaY * deltaY);
        double maxDistance = eyeBackground.getRadius() - pupil.getRadius();

        if (distance > maxDistance) {
            deltaX = (deltaX / distance) * maxDistance;
            deltaY = (deltaY / distance) * maxDistance;
        }

        pupil.setTranslateX(deltaX);
        pupil.setTranslateY(deltaY);
    }
}
