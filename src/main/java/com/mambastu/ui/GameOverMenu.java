package com.mambastu.ui;

import com.mambastu.controller.level.context.dto.Context;
import com.mambastu.listener.GameOverMenuListener;

import com.mambastu.material.resource.ResourceManager;
import com.mambastu.util.BetterMath;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameOverMenu {
    private final GameOverMenuListener listener;

    private final StackPane root;
    private final Context ctx;

    private final SimpleIntegerProperty totalDuration;
    private final SimpleIntegerProperty totalKillCount;
    private final SimpleIntegerProperty totalLevelNum;

    private final Pane menuPane;

    public GameOverMenu(StackPane root, Context ctx, GameOverMenuListener listener) {
        this.listener = listener;
        this.root = root;
        this.ctx = ctx;
        this.totalKillCount = new SimpleIntegerProperty();
        this.totalDuration = new SimpleIntegerProperty();
        this.totalLevelNum = new SimpleIntegerProperty();
        this.menuPane = new Pane();
    }


    public void init() { // 初始化
        bindProperties();
        buildLayout();
        menuPane.setStyle("-fx-background-color: white;");
        menuPane.setOpacity(0.8);
    }

    public void show() {
        root.getChildren().remove(menuPane);
        root.getChildren().add(menuPane); // 确保菜单在游戏场景之上(StackPane的栈顶)
    }

    public void hide() {
        root.getChildren().remove(menuPane);
    }

    private void bindProperties() { // 绑定属性
        totalDuration.bind(ctx.getGlobalRecord().getTotalDuration()); // 绑定总游戏时间属性到全局记录的属性上。
        totalKillCount.bind(ctx.getGlobalRecord().getTotalKillCount());
        totalLevelNum.bind(ctx.getGlobalRecord().getTotalLevelNum());
    }


    private void buildLayout(){

        // 设置标题
        Text title = new Text("GAME OVER");
        title.layoutXProperty().bind(menuPane.heightProperty().multiply(0.28));
        title.layoutYProperty().bind(menuPane.widthProperty().multiply(0.1));
        title.styleProperty().bind(Bindings.concat(
                "-fx-font-family: 'Segoe Script'; ",
                "-fx-font-weight: bold; ",
                "-fx-font-size: ", menuPane.widthProperty().multiply(0.10).asString(), "px;"
        ));

        //设置分割线
        Line line = new Line();
        line.startXProperty().bind(menuPane.widthProperty().multiply(0.5));
        line.endXProperty().bind(menuPane.widthProperty().multiply(0.5));
        line.startYProperty().bind(menuPane.heightProperty().multiply(0.25));
        line.endYProperty().bind(menuPane.heightProperty().multiply(0.95));

        //本轮成绩设置
        Pane scareBox = new Pane();
        scareBox.layoutXProperty().bind(menuPane.widthProperty().multiply(0.1));
        scareBox.layoutYProperty().bind(menuPane.heightProperty().multiply(0.25));
        scareBox.prefWidthProperty().bind(menuPane.widthProperty().multiply(0.35));
        scareBox.prefHeightProperty().bind(menuPane.heightProperty().multiply(0.7));
        scareBox.setStyle("-fx-border-color: black; -fx-border-width: 3; -fx-border-style: dashed");
        //设置具体成绩内容
        addScore(scareBox,ScoreType.SCORE_TITLE,0.1,0.1);
        addScore(scareBox,ScoreType.TOTAL_LEVEL,0.1,0.4);
        addScore(scareBox,ScoreType.TOTAL_KILL,0.1,0.6);
        addScore(scareBox,ScoreType.TOTAL_DURATION,0.1,0.8);

        //设置右侧内容
        Pane chooseBox = new Pane();
        chooseBox.layoutXProperty().bind(menuPane.widthProperty().multiply(0.55));
        chooseBox.layoutYProperty().bind(menuPane.heightProperty().multiply(0.25));
        chooseBox.prefWidthProperty().bind(menuPane.widthProperty().multiply(0.35));
        chooseBox.prefHeightProperty().bind(menuPane.heightProperty().multiply(0.7));
//        chooseBox.setStyle("-fx-border-color: black; -fx-border-width: 3; -fx-border-style: dashed");
        //设置语录
        Text quote = new Text("The Joker may have faltered this round, yet the dance of life twirls on.\nCast your next die !!");
        quote.wrappingWidthProperty().bind(chooseBox.widthProperty().multiply(0.9));
        quote.styleProperty().bind(Bindings.concat(
                "-fx-font-family: 'Segoe Script'; ",
                "-fx-font-weight: bold; ",
                "-fx-font-size: ", chooseBox.widthProperty().multiply(0.05).asString(), "px;"
        ));
        quote.layoutXProperty().bind(chooseBox.widthProperty().multiply(0.028));
        quote.layoutYProperty().bind(chooseBox.heightProperty().multiply(0.05));
        chooseBox.getChildren().add(quote);
        //设置按钮
        addButton(chooseBox, ButtonType.REPLAY, 0.02, 0.3);
        addButton(chooseBox, ButtonType.RETURN, 0.02, 0.6);
        //设置人物
        Pane facePane = new Pane();
        facePane.layoutXProperty().bind(menuPane.widthProperty().multiply(0.8));
        facePane.layoutYProperty().bind(menuPane.heightProperty().multiply(0.8));
        facePane.prefWidthProperty().bind(menuPane.widthProperty().multiply(0.2));
        facePane.prefHeightProperty().bind(menuPane.heightProperty().multiply(0.2));
//        facePane.setStyle("-fx-border-color: black; -fx-border-width: 3; -fx-border-style: dashed");
        buildFace(facePane);
        

        //设置画布
        menuPane.getChildren().addAll(title,line,scareBox,chooseBox,facePane);
    }

    private void addScore(Pane pane, ScoreType scoreType, Double xRatio, Double yRation){
        Text score = new Text();
        score.layoutXProperty().bind(pane.heightProperty().multiply(xRatio));
        score.layoutYProperty().bind(pane.widthProperty().multiply(yRation));
        score.styleProperty().bind(Bindings.concat(
                "-fx-font-family: 'Segoe Script'; ",
                "-fx-font-weight: bold; ",
                "-fx-font-size: ", pane.widthProperty().multiply(0.08).asString(), "px;"
        ));

        switch (scoreType) {
            case TOTAL_KILL: {
                score.textProperty().bind(totalKillCount.asString("Total Kill: %d "));
                score.setFill(Color.web("#FF709E"));
                break;
            }
            case TOTAL_DURATION: {
                score.textProperty().bind(totalDuration.asString("Total Duration: %d "));
                score.setFill(Color.web("#FF937A"));
                break;
            }
            case TOTAL_LEVEL: {
                score.textProperty().bind(totalLevelNum.asString("Total Level: %d "));
                score.setFill(Color.web("#5E66C2"));
                break;
            }
            case SCORE_TITLE: {
                score.setText("Stats All Folks !");
                score.setFill(Color.web("#5E66C2"));
                score.styleProperty().bind(Bindings.concat(
                        "-fx-font-family: 'Segoe Script'; ",
                        "-fx-font-weight: normal; ",
                        "-fx-font-size: ", pane.widthProperty().multiply(0.1).asString(), "px;"
                ));
                break;
            }
        }

        pane.getChildren().add(score);
    }

    private void addButton(Pane pane, ButtonType buttonType, Double xRatio, Double yRation){
        Group buttonBox = new Group();
        //设置背景图

        ImageView circleView = new ImageView(ResourceManager.getInstance().getImg("circleImage", "System", "MainMenu"));
        circleView.fitHeightProperty().bind(pane.heightProperty().multiply(0.3));
        circleView.fitWidthProperty().bind(pane.widthProperty().multiply(0.7));
        circleView.setLayoutX(0);
        circleView.setLayoutY(0);
        circleView.setVisible(false);
        //设置按钮
        Text button = new Text();
        button.styleProperty().bind(Bindings.concat(
                "-fx-font-family: 'Segoe Script'; ",
                "-fx-font-weight: normal; ",
                "-fx-font-size: ", circleView.fitWidthProperty().multiply(0.1).asString(), "px;"
        ));
        button.setFill(Color.web("#9F9DD3"));
        button.setOpacity(0.8);
        button.layoutXProperty().bind(circleView.fitWidthProperty().multiply(0.15));
        button.layoutYProperty().bind(circleView.fitHeightProperty().multiply(0.65));
        switch (buttonType) {
            case REPLAY : {
                button.setText("Hit Me Again!");
                break;
            }
            case RETURN:{
                button.setText("Escape to the Lobby!");
                break;
            }
        }
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            circleView.setVisible(true); // 显示圆圈
            button.setFill(Color.web("#FF709E"));
            button.styleProperty().bind(Bindings.concat(
                    "-fx-font-family: 'Segoe Script'; ",
                    "-fx-font-weight: bolder; ",
                    "-fx-font-size: ", circleView.fitWidthProperty().multiply(0.15).asString(), "px;"
            ));

        });
        button.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            circleView.setVisible(false);
            button.setFill(Color.web("#9F9DD3"));
            button.styleProperty().bind(Bindings.concat(
                    "-fx-font-family: 'Segoe Script'; ",
                    "-fx-font-weight: bold; ",
                    "-fx-font-size: ", circleView.fitWidthProperty().multiply(0.1).asString(), "px;"
            ));

        });
        button.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {

            switch (buttonType) {
                case REPLAY: {
                    listener.restartGame();
                    break;
                }
                case RETURN: {
                    listener.backMainMenu();
                    break;
                }

            }

        });
        buttonBox.getChildren().addAll(circleView, button);
        buttonBox.layoutXProperty().bind(pane.widthProperty().multiply(xRatio));
        buttonBox.layoutYProperty().bind(pane.heightProperty().multiply(yRation));
        pane.getChildren().add(buttonBox);
    }
    private void buildFace(Pane pane) {
        Group facePane = new Group();
        Group leftEye = createEye(pane);
        Group rightEye = createEye(pane);
        ImageView face = new ImageView(ResourceManager.getInstance().getImg("bornImage", "Player", "JokerPlayer"));
        face.fitWidthProperty().bind(pane.widthProperty().multiply(0.9));
        face.fitHeightProperty().bind(pane.heightProperty().multiply(0.9));
        face.setLayoutX(0);
        face.setLayoutY(0);
        leftEye.layoutXProperty().bind(face.fitWidthProperty().multiply(0.33));
        leftEye.layoutYProperty().bind(face.fitHeightProperty().multiply(0.42));
        rightEye.layoutXProperty().bind(face.fitWidthProperty().multiply(0.65));
        rightEye.layoutYProperty().bind(face.fitHeightProperty().multiply(0.42));
        facePane.setLayoutX(0);
        facePane.setLayoutY(0);


        facePane.getChildren().addAll(face, leftEye, rightEye);

        root.setOnMouseMoved(event -> {
            updatePupilPosition(pane, event.getX(), event.getY(), leftEye);
            updatePupilPosition(pane, event.getX(), event.getY(), rightEye);
        });
        pane.getChildren().add(facePane);

    }

    private Group createEye(Pane pane) {
        // 创建眼睛背景
        Circle eyeBackground = new Circle();
        eyeBackground.radiusProperty().bind(pane.widthProperty().multiply(0.3*0.25));
        eyeBackground.setFill(Color.WHITE);
        eyeBackground.setStroke(Color.WHITE);
        eyeBackground.setStrokeWidth(2);

        // 创建眼珠
        Circle pupil = new Circle();
        pupil.radiusProperty().bind(pane.widthProperty().multiply(0.3*0.20*0.45));
        pupil.setFill(Color.BLACK);

        // 将眼珠添加到眼睛背景中
        return new Group(eyeBackground, pupil);
    }

    // 更新眼珠位置的方法
    private void updatePupilPosition(Pane facePane, double mouseX, double mouseY, Group eye) {
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

    private enum ScoreType {
        SCORE_TITLE, TOTAL_KILL, TOTAL_DURATION, TOTAL_LEVEL;
    }
    private enum ButtonType {
        REPLAY, RETURN;
    }
}
