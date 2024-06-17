package com.mambastu.ui;

import com.mambastu.controller.level.context.dto.Context;
import com.mambastu.controller.level.context.enums.GameMode;
import com.mambastu.listener.MainMenuListener;
import com.mambastu.material.pojo.entity.player.PlayerTypes;
import com.mambastu.material.resource.ResourceManager;
import com.mambastu.util.BetterMath;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class MainMenu {
    private final MainMenuListener listener;

    private final StackPane root;
    private final Context ctx;

    private final SimpleObjectProperty<GameMode> gameMode; // 游戏模式
    private final SimpleObjectProperty<PlayerTypes> playerType; // 玩家类型

    private final Pane menuPane;
    private final Pane titlePane;
    private final Group facePane;
    private final Group btnPanes;


    public MainMenu(StackPane root, Context ctx,  MainMenuListener listener) {
        this.root = root;
        this.ctx = ctx;
        this.listener = listener;
        this.gameMode = new SimpleObjectProperty<>(GameMode.NORMAL);
        this.playerType = new SimpleObjectProperty<>(PlayerTypes.JokerPlayer);
        this.menuPane = new Pane();
        this.facePane = new Group();
        this.btnPanes = new Group();
        this.titlePane= new Pane();
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
        buildTitle();
        menuPane.getChildren().addAll(facePane, btnPanes, titlePane);
    }

    private void buildTitle(){
        Text title = new Text();
        title.setText("JokerBro");
        title.styleProperty().bind(Bindings.concat(
                "-fx-font-family: 'Segoe Script'; ",
                "-fx-font-weight: bold; ",
                "-fx-font-size: ", root.widthProperty().multiply(0.1).asString(), "px;"
        ));
        titlePane.layoutXProperty().bind(root.widthProperty().multiply(0.25));
        titlePane.layoutYProperty().bind(root.heightProperty().multiply(0.2));
        titlePane.getChildren().add(title);
    }

    /**
     * 设置多个按钮
     */
    private void buildBtns(){
        //创建位于左侧的按钮
        buildBtn(ButtonType.START_GAME, 0.02,0.6);
        buildBtn(ButtonType.MODE_SELECT, 0.02,0.725);
        buildBtn(ButtonType.PLAYER_SELECT, 0.02,0.85);
        //创建位于有右侧的按钮
        buildBtn(ButtonType.PLAYER_SELECT, 0.7,0.6);
        buildBtn(ButtonType.PLAYER_SELECT, 0.7,0.85);
    }

    /**
     * 创造单个按钮
     *
     * @param buttonName
     * @param xRadio: 相对于root宽度的倍率
     * @param yRatio: 相对于root高度的倍率
     */
    private void buildBtn(ButtonType buttonName, Double xRadio, Double yRatio)  {
        Group btnPane = new Group();
        ImageView circleView = new ImageView(ResourceManager.getInstance().getImg("circleImage", "System", "MainMenu"));
        circleView.fitHeightProperty().bind(root.heightProperty().multiply(0.1));
        circleView.fitWidthProperty().bind(root.widthProperty().multiply(0.25));
        circleView.setLayoutX(0);
        circleView.setLayoutY(0);
        circleView.setVisible(false);

        Text startBtn = new Text(buttonName.name());
//        startBtn.setFont(Font.font("Segoe Script", FontWeight.BOLD, 80));
        startBtn.styleProperty().bind(Bindings.concat(
                "-fx-font-family: 'Segoe Script'; ",
                "-fx-font-weight: bold; ",
                "-fx-font-size: ", circleView.fitWidthProperty().multiply(0.08).asString(), "px;"
        ));
        startBtn.setFill(Color.BLACK);
        startBtn.setOpacity(0.8);

        // Group的大小取决于更大的circleView，所以要设置文字居中，也就是从左上角向下和向右大概四分之一circleView长宽
        startBtn.layoutXProperty().bind(circleView.fitWidthProperty().multiply(0.15));
        startBtn.layoutYProperty().bind(circleView.fitHeightProperty().multiply(0.65));
        startBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            circleView.setVisible(true); // 显示圆圈
        });

        startBtn.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            circleView.setVisible(false);
        });
        startBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            switch (buttonName) {
                case START_GAME: {
                    listener.startGame();
                    break;
                }
                case EXIT_GAME: {
                    break;
                }
                case DEVELOPERS:{
                    break;
                }

                case MODE_SELECT:{
                    break;
                }
                case PLAYER_SELECT:{
                    break;
                }
            }
        });
        btnPane.getChildren().addAll(circleView, startBtn);
        btnPane.layoutXProperty().bind(root.widthProperty().multiply(xRadio));
        btnPane.layoutYProperty().bind(root.heightProperty().multiply(yRatio));
        btnPanes.getChildren().add(btnPane);
    }

    private void buildFace() {
        Group leftEye = createEye();
        Group rightEye = createEye();
        ImageView face = new ImageView(ResourceManager.getInstance().getImg("bornImage", "Monster", "BossMonster"));
        face.fitWidthProperty().bind(root.widthProperty().multiply(0.3));
        face.fitHeightProperty().bind(root.heightProperty().multiply(0.3));
//        leftEye.setLayoutX(face.getFitWidth() * 0.300);
//        leftEye.setLayoutY(face.getFitHeight() * 0.45);
//        rightEye.setLayoutX(face.getFitWidth() * 0.700);
//        rightEye.setLayoutY(leftEye.getLayoutY());
        leftEye.layoutXProperty().bind(face.fitWidthProperty().multiply(0.3));
        leftEye.layoutYProperty().bind(face.fitHeightProperty().multiply(0.45));
        rightEye.layoutXProperty().bind(face.fitWidthProperty().multiply(0.7));
        rightEye.layoutYProperty().bind(face.fitHeightProperty().multiply(0.45));

        facePane.layoutXProperty()
            .bind(root.widthProperty().multiply(0.35));

        facePane.layoutYProperty()
            .bind(root.heightProperty().multiply(0.3));

        facePane.getChildren().addAll(face, leftEye, rightEye);

        root.setOnMouseMoved(event -> {
            updatePupilPosition(event.getX(), event.getY(), leftEye);
            updatePupilPosition(event.getX(), event.getY(), rightEye);
        });
    }

    private Group createEye() {
        // 创建眼睛背景
//        Circle eyeBackground = new Circle(30);
        Circle eyeBackground = new Circle();
        eyeBackground.radiusProperty().bind(root.widthProperty().multiply(0.3*0.15));
        eyeBackground.setFill(Color.WHITE);
        eyeBackground.setStroke(Color.WHITE);
        eyeBackground.setStrokeWidth(2);

        // 创建眼珠
//        Circle pupil = new Circle(13);
        Circle pupil = new Circle();
        pupil.radiusProperty().bind(root.widthProperty().multiply(0.3*0.15*0.3));
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
    private enum ButtonType {
        START_GAME,
        EXIT_GAME,
        DEVELOPERS,
        MODE_SELECT,
        PLAYER_SELECT;
    }
}
