package com.mambastu.view.fixed.impl;

import com.mambastu.controller.context.dto.Context;
import com.mambastu.controller.listener.MainMenuListener;
import com.mambastu.enums.GameMode;
import com.mambastu.enums.gameobjects.PlayerTypes;
import com.mambastu.resource.media.impl.AudioManager;
import com.mambastu.resource.media.impl.ImageManager;
import com.mambastu.utils.BetterMath;
import com.mambastu.view.fixed.FixedMenu;

import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class MainMenu implements FixedMenu{
    private final MainMenuListener listener;

    private final StackPane root;
    private final Context ctx;

    private final SimpleObjectProperty<GameMode> gameMode; // 游戏模式
    private final SimpleObjectProperty<PlayerTypes> playerType; // 玩家类型

    private final Pane menuPane;
    private final Group facePane;
    private final Group startBtnPane;
    private final Group helpBtnPane;
    private final HBox charPane; // 角色面板，用于显示角色选择界面
    private final Pane charIntroPane; // 介绍面板，用于显示角色介绍界面
    private final HBox modePane; // 模式面板，用于显示游戏模式选择界面
    private final Pane modeIntroPane;

    private Node modeTemp =  new Pane();
    private Node charTemp = new Pane();
    private boolean isCharIntroPinned = false;
    private boolean isModeIntroPinned = false;
    private int currentIndex = 0;
    private ImageView imageView = new ImageView();
    private Image[] images = {
        ImageManager.getInstance().getImg("helpKeyBoardImage", "System", "MainMenu"),
        ImageManager.getInstance().getImg("helpGamePadImage", "System", "MainMenu")
    };
    HBox progressDots = new HBox(8);

    public MainMenu(StackPane root, Context ctx, MainMenuListener listener) {
        this.root = root;
        this.ctx = ctx;
        this.listener = listener;
        this.gameMode = new SimpleObjectProperty<>(GameMode.NORMAL);
        this.playerType = new SimpleObjectProperty<>(PlayerTypes.JokerPlayer);
        this.menuPane = new Pane();
        this.facePane = new Group();
        this.startBtnPane = new Group();
        this.helpBtnPane = new Group();
        this.charPane = new HBox();
        this.charIntroPane = new Pane();
        this.modePane = new HBox();
        this.modeIntroPane = new Pane();
    }

    @Override
    public void show() {
        root.getChildren().clear();
        root.getChildren().add(menuPane); // 确保菜单在游戏场景之上(StackPane的栈顶)
    }

    @Override
    public void hide() {
        root.getChildren().remove(menuPane);
    }

    @Override
    public void init() { // 初始化
        bindProperties();
        buildLayout();
        menuPane.setOpacity(1.0);
    }

    private void bindProperties() {
        ctx.getGameMode().bind(gameMode); // 将游戏模式绑定到上下文
        ctx.getPlayerType().bind(playerType); // 将玩家类型绑定到上下文
    }

    private void buildLayout() {
        menuPane.getChildren().clear();
        
        buildFaceLayout();
        buildStartBtnLayout();
        buildHelpBtnLayout();
        buildCharLayout();
        bulidGameModeLayout();

        menuPane.getChildren().addAll(facePane, startBtnPane, helpBtnPane, charPane, charIntroPane, modePane, modeIntroPane);
    }

    private void bulidGameModeLayout() {
        modeIntroPane.getChildren().clear();

        ImageView normalModeIntro = createModeIntro(
                ImageManager.getInstance().getImg("normalIntroImage", "System", "MainMenu"));
        ImageView normalModeView = createMode(
                ImageManager.getInstance().getImg("normalModeImage", "System", "MainMenu"),
                GameMode.NORMAL, normalModeIntro);

        ImageView challengeModeIntro = createModeIntro(
                ImageManager.getInstance().getImg("challengeIntroImage", "System", "MainMenu"));
        ImageView challengeModeView = createMode(
                ImageManager.getInstance().getImg("challengeModeImage", "System", "MainMenu"),
                GameMode.CHALLENGE, challengeModeIntro);

        modePane.getChildren().addAll(normalModeView, challengeModeView);
        modePane.setSpacing(70);
        modePane.setPadding(new Insets(50));
        modePane.setAlignment(Pos.CENTER);

        modePane.setLayoutX(70);
        modePane.layoutYProperty()
                .bind(root.heightProperty().subtract(modePane.layoutBoundsProperty().get().getHeight()).multiply(0.72));

        modeIntroPane.setPrefSize(620, 620);
        modeIntroPane.setLayoutX(55);
        modeIntroPane.setLayoutY(10);
    }

    private ImageView createMode(Image img, GameMode gameMode, ImageView introView) {
        ImageView modeView = new ImageView(img);
        modeView.setFitWidth(110);
        modeView.setFitHeight(110);

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLACK);
        shadow.setRadius(20);
        shadow.setOffsetX(10);
        shadow.setOffsetY(10);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.25), modeView);
        scaleTransition.setFromX(1);
        scaleTransition.setToX(-1);
        scaleTransition.setFromY(1);
        scaleTransition.setToY(1);
        scaleTransition.setCycleCount(2);
        scaleTransition.setAutoReverse(true);

        
        modeView.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            if (scaleTransition.getStatus() != Animation.Status.RUNNING) {
                modeView.setEffect(shadow);
                scaleImageView(modeView, 1.2);

                if (isModeIntroPinned) {
                    modeTemp = modeIntroPane.getChildren().get(0); // 获取当前角色介绍图像视图的引用，以便稍后重新添加它。
                    modeIntroPane.getChildren().clear(); // 清除介绍面板，以便添加新的角色介绍图像视图。
                    modeIntroPane.getChildren().add(introView); // 添加新的角色介绍图像视图。
                } else {
                    modeIntroPane.getChildren().clear();
                    modeIntroPane.getChildren().add(introView);
                }
            }
        });

        modeView.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            if (scaleTransition.getStatus() != Animation.Status.RUNNING) { // 确保动画没有在运行
                modeView.setEffect(null);
                scaleImageView(modeView, 1.0);

                if (isModeIntroPinned) {
                    modeIntroPane.getChildren().clear();
                    modeIntroPane.getChildren().add(modeTemp);
                } else {
                    modeIntroPane.getChildren().clear();
                }
            }
        });

        modeView.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            AudioManager.getInstance().playAudio("SoundEffects", "DingDong", "displayAudio");
            this.gameMode.setValue(gameMode);
            modeIntroPane.getChildren().clear();
            modeIntroPane.getChildren().add(introView);
            modeTemp = introView;
            isModeIntroPinned = true;
            scaleTransition.play();
        });

        return modeView;
    }

    private ImageView createModeIntro(Image img) {
        ImageView introView = new ImageView(img);
        introView.setFitWidth(620); // 设置角色图像的宽度和高度
        introView.setFitHeight(620); // 设置角色图像的宽度和高度
        return introView;
    }

    private void buildCharLayout() {
        charIntroPane.getChildren().clear();

        ImageView laughPlayerIntro = createCharIntro(
                ImageManager.getInstance().getImg("laughIntroImage", "System", "MainMenu")); // 创建角色介绍图像视图
        ImageView laughPlayerView = createChar(
                ImageManager.getInstance().getImg("laughPlayerImage", "System", "MainMenu"),
                PlayerTypes.LaughPlayer, laughPlayerIntro);

        ImageView jokerPlayerIntro = createCharIntro(
                ImageManager.getInstance().getImg("jokerIntroImage", "System", "MainMenu"));
        ImageView jokerPlayerView = createChar(
                ImageManager.getInstance().getImg("jokerPlayerImage", "System", "MainMenu"),
                PlayerTypes.JokerPlayer, jokerPlayerIntro);

        ImageView basakerPlayerIntro = createCharIntro(
                ImageManager.getInstance().getImg("basakerIntroImage", "System", "MainMenu"));
        ImageView basakerPlayerView = createChar(
                ImageManager.getInstance().getImg("basakerPlayerImage", "System", "MainMenu"),
                PlayerTypes.BasakerPlayer, basakerPlayerIntro);

        charPane.getChildren().addAll(laughPlayerView, jokerPlayerView, basakerPlayerView);
        charPane.setSpacing(70);
        charPane.setPadding(new Insets(50));
        charPane.setAlignment(Pos.CENTER);

        charPane.layoutXProperty()
                .bind(root.widthProperty().subtract(charPane.layoutBoundsProperty().get().getWidth()).multiply(0.66));
        charPane.layoutYProperty()
                .bind(root.heightProperty().subtract(charPane.layoutBoundsProperty().get().getHeight()).multiply(0.55));

        charIntroPane.setPrefSize(620, 620);
        charIntroPane.layoutXProperty()
                .bind(root.widthProperty().subtract(charIntroPane.layoutBoundsProperty().get().getWidth())
                        .multiply(0.65));
        charIntroPane.setLayoutY(10);
    }

    private ImageView createChar(Image img, PlayerTypes playerTypes, ImageView introView) {
        ImageView charView = new ImageView(img); // 创建角色图像视图
        charView.setFitWidth(110);
        charView.setFitHeight(110);

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLACK);
        shadow.setRadius(20);
        shadow.setOffsetX(10);
        shadow.setOffsetY(10);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.25), charView);
        scaleTransition.setFromX(1);
        scaleTransition.setToX(-1);
        scaleTransition.setFromY(1);
        scaleTransition.setToY(1);
        scaleTransition.setCycleCount(2);
        scaleTransition.setAutoReverse(true);

        charView.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> { // 鼠标进入事件处理程序，显示角色信息面板
            if (scaleTransition.getStatus() != Animation.Status.RUNNING) {
                charView.setEffect(shadow);
                scaleImageView(charView, 1.2);
                if (isCharIntroPinned) {
                    charTemp = charIntroPane.getChildren().get(0); // 获取当前角色介绍图像视图的引用，以便稍后重新添加它。
                    charIntroPane.getChildren().clear(); // 清除介绍面板，以便添加新的角色介绍图像视图。
                    charIntroPane.getChildren().add(introView); // 添加新的角色介绍图像视图。
                } else {
                   charIntroPane.getChildren().clear();
                   charIntroPane.getChildren().add(introView);
                }
            }
        });

        charView.addEventHandler(MouseEvent.MOUSE_EXITED, event -> { // 鼠标离开事件处理程序，隐藏角色信息面板
            if (scaleTransition.getStatus() != Animation.Status.RUNNING) {
                charView.setEffect(null);
                scaleImageView(charView, 1.0);

                if (isCharIntroPinned) {
                    charIntroPane.getChildren().clear();
                    charIntroPane.getChildren().add(charTemp);
                } else {
                    charIntroPane.getChildren().clear();
                }
            }
        });

        charView.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> { // 鼠标点击事件处理程序，选择角色并隐藏角色选择面板
            AudioManager.getInstance().playAudio("SoundEffects", "Ding", "displayAudio");
            this.playerType.setValue(playerTypes); // 设置玩家类型为所选角色类型
            charIntroPane.getChildren().clear();
            charIntroPane.getChildren().add(introView); // 添加角色介绍图像视图到介绍面板
            charTemp = introView;
            isCharIntroPinned = true; // 标记面板为固定状态，不再响应鼠标事件
            scaleTransition.play();
        });

        return charView;
    }

    private ImageView createCharIntro(Image img) {
        ImageView introView = new ImageView(img);
        introView.setFitWidth(620); // 设置角色图像的宽度和高度
        introView.setFitHeight(620); // 设置角色图像的宽度和高度
        return introView;
    }

    private void scaleImageView(ImageView imageView, double scale) {
        ScaleTransition st = new ScaleTransition(Duration.millis(150), imageView);
        st.setToX(scale);
        st.setToY(scale);
        st.play();
    }

    private void buildStartBtnLayout() {
        startBtnPane.getChildren().clear();

        ImageView circleView = new ImageView(ImageManager.getInstance().getImg("circleImage", "System", "MainMenu"));
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

        startBtnPane.getChildren().addAll(circleView, startBtn);

        startBtnPane.setLayoutX(70);
        startBtnPane.layoutYProperty()
                .bind(root.heightProperty().subtract(startBtnPane.layoutBoundsProperty().get().getHeight()).multiply(0.68));
    }

    private void buildHelpBtnLayout() {
        helpBtnPane.getChildren().clear();

        ImageView circleView = new ImageView(ImageManager.getInstance().getImg("circleImage", "System", "MainMenu"));
        circleView.setFitWidth(600);
        circleView.setFitHeight(120);
        circleView.setLayoutX(0);
        circleView.setLayoutY(0);
        circleView.setVisible(false);

        Text helpBtn = new Text("HELP");
        helpBtn.setFont(Font.font("Segoe Script", FontWeight.BOLD, 80));
        helpBtn.setFill(Color.BLACK);
        helpBtn.setOpacity(0.8);
        helpBtn.setLayoutX(135);
        helpBtn.setLayoutY(85);

        helpBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            circleView.setVisible(true); // 显示圆圈
        });

        helpBtn.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            circleView.setVisible(false);
        });

        helpBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            showHelpImageLayer();
        });

        helpBtnPane.getChildren().addAll(circleView, helpBtn);

        helpBtnPane.layoutXProperty()
                .bind(root.widthProperty().subtract(helpBtnPane.layoutBoundsProperty().get().getWidth()).multiply(0.78));
        helpBtnPane.layoutYProperty()
                .bind(root.heightProperty().subtract(helpBtnPane.layoutBoundsProperty().get().getHeight()).multiply(0.9));
    }

    private void showHelpImageLayer() {
        StackPane imageLayer = new StackPane();
        imageLayer.setStyle("-fx-background-color: rgba(255, 255, 255, 0.6);");
        imageLayer.setAlignment(Pos.CENTER);
        imageLayer.setOnMouseClicked(event -> root.getChildren().remove(imageLayer));

        imageView.setImage(images[currentIndex]);
        imageView.setOnMouseClicked(event -> {
            event.consume(); // 消耗事件以防止触发父层的关闭事件
            currentIndex = (currentIndex + 1) % images.length;
            imageView.setImage(images[currentIndex]);
            updateProgressDots(); // 更新进度点
        });
        imageView.setFitHeight(648);
        imageView.setFitWidth(1152);

        progressDots.setAlignment(Pos.CENTER);
        updateProgressDots();

        VBox vbox = new VBox(10, imageView, progressDots);
        vbox.setAlignment(Pos.CENTER);
        imageLayer.getChildren().add(vbox);

        root.getChildren().add(imageLayer);
    }

    private void updateProgressDots() {
        progressDots.getChildren().clear();
        for (int i = 0; i < images.length; i++) {
            Circle dot = new Circle(6);
            if (i == currentIndex) {
                dot.setFill(Color.ROYALBLUE);
            } else {
                dot.setFill(Color.GAINSBORO);
            }
            progressDots.getChildren().add(dot);
        }
    }

    private void buildFaceLayout() {
        facePane.getChildren().clear();

        Group leftEye = createEye();
        Group rightEye = createEye();
        ImageView face = new ImageView(ImageManager.getInstance().getImg("bornImage", "Monster", "BossMonster"));
        face.setFitWidth(550);
        face.setFitHeight(324);
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
        Circle eyeBackground = new Circle(86.4);
        eyeBackground.setRadius(86.4);
        eyeBackground.setFill(Color.WHITE);
        eyeBackground.setStroke(Color.WHITE);
        eyeBackground.setStrokeWidth(2);

        // 创建眼珠
        Circle pupil = new Circle(26.0);
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
