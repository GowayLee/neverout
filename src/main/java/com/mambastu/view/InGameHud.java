package com.mambastu.view;

import com.mambastu.controller.context.dto.Context;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class InGameHud {
    private final StackPane root;
    private final Context ctx;

    private final SimpleIntegerProperty remainDuration;
    private final SimpleIntegerProperty HP;
    private final SimpleIntegerProperty killCount;
    private final SimpleDoubleProperty totalFireCD;
    private final SimpleDoubleProperty totalSkillCD;

    private final Timeline fireCDTimer;
    private final SimpleDoubleProperty fireCDprogress;
    private final Arc fireCDarc;

    private final Timeline skillCDTimer;
    private final SimpleDoubleProperty skillCDprogress;
    private Arc skillCDarc;
    private Circle skillCDDot;

    private final Pane menuPane;

    public InGameHud(StackPane root, Context ctx) {
        this.root = root;
        this.ctx = ctx;
        this.menuPane = new Pane();
        this.HP = new SimpleIntegerProperty(100); // 假设初始生命值为100
        this.remainDuration = new SimpleIntegerProperty(0);
        this.killCount = new SimpleIntegerProperty(0); // 初始击杀数为0
        this.totalFireCD = new SimpleDoubleProperty(1700); // 初始武器冷却时间为1700毫秒
        this.fireCDprogress = new SimpleDoubleProperty(1.0);
        this.fireCDTimer = new Timeline();
        this.fireCDTimer.setCycleCount(1);
        this.totalSkillCD = new SimpleDoubleProperty(5.0); // 初始技能冷却时间为5.0秒
        this.skillCDprogress = new SimpleDoubleProperty(1.0);
        this.skillCDTimer = new Timeline();
        this.skillCDTimer.setCycleCount(1);
        this.fireCDarc = new Arc(50, 120, 15, 15, 45, 0);
        this.skillCDarc = new Arc(50, 120, 25, 25, 225, 0);
        this.skillCDDot = new Circle(120, 120 , 15, Color.LIME);
    }

    public void init() { // 初始化
        bindProperties();
        buildLayout();
    }

    public void update() {
        bindProperties();
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
        totalFireCD.bind(ctx.getLevelConfig().getPlayer().getWeapon().getCoolTime()); // 绑定武器冷却时间属性
        setupFireCDTimerKeyFrames();
        totalFireCD.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                fireCDTimer.stop();
                setupFireCDTimerKeyFrames();
            }
        });

        ctx.getLevelConfig().getPlayer().getWeapon().getCoolTimer().statusProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Animation.Status.RUNNING) {
                fireCDTimer.play();
            }
        }); // 当武器冷却计时器开始时，启动冷却时间计时器
        totalSkillCD.bind(ctx.getLevelConfig().getPlayer().getSkillCD());
        setupSkillCDTimerKeyFrames();
        totalSkillCD.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                skillCDTimer.stop();
                setupSkillCDTimerKeyFrames();
            }
        });

        ctx.getLevelConfig().getPlayer().getSkillCDTimer().statusProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Animation.Status.RUNNING) {
                skillCDTimer.play();
            }
        });
    }

    private void setupFireCDTimerKeyFrames() { // 计算CD条的补间动画关键帧
        fireCDTimer.getKeyFrames().clear();
        int steps = (int) totalFireCD.get() / 16; // 在60fps的情况下确保每一帧都有关键帧
        for (int i = 0; i <= steps; i++) {
            double progress = (double) i / steps;
            Duration stepDuration = Duration.millis(totalFireCD.get()).multiply(progress);
            fireCDTimer.getKeyFrames().add(new KeyFrame(stepDuration, e -> fireCDprogress.set(progress)));
        }
        fireCDTimer.playFromStart();
    }

    private void setupSkillCDTimerKeyFrames() {
        skillCDTimer.getKeyFrames().clear();
        int steps = (int) totalSkillCD.get() * 1000 / 16; // 在60fps的情况下确保每一帧都有关键帧
        for (int i = 0; i <= steps; i++) {
            double progress = (double) i / steps;
            Duration stepDuration = Duration.millis(totalSkillCD.get() * 1000 * progress > 1 ? totalSkillCD.get() * 1000 * progress : 1.0);
            skillCDTimer.getKeyFrames().add(new KeyFrame(stepDuration, e -> skillCDprogress.set(progress)));
        }
        skillCDTimer.playFromStart();
    }

    private void buildLayout() {
        menuPane.getChildren().clear();
        buildCDLayout();

        Label HPLabel = new Label();
        HPLabel.textProperty().bind(HP.asString("HP: %d"));
        HPLabel.setStyle("-fx-text-fill: red;"); // 设置文本颜色为红色
        HPLabel.setFont(new Font("Segoe Script", 40)); // 设置字体大小和样式
        HPLabel.setLayoutX(20);
        HPLabel.setLayoutY(10);

        Label countdownLabel = new Label();
        countdownLabel.textProperty().bind(remainDuration.asString("%d"));
        countdownLabel.setStyle("-fx-text-fill: black; -fx-font-weight: bold;"); // 设置文本颜色为红色
        countdownLabel.setFont(new Font("Segoe Script", 50)); // 设置字体大小和样式
        countdownLabel.layoutXProperty().bind(Bindings.createDoubleBinding( // 居中显示倒计时文本
                () -> (menuPane.getWidth() - countdownLabel.getWidth()) / 2,
                menuPane.widthProperty(),
                countdownLabel.widthProperty()));
        countdownLabel.setLayoutY(10);

        Label killCountLabel = new Label();
        killCountLabel.textProperty().bind(killCount.asString("Kill: %d "));
        killCountLabel.setStyle("-fx-text-fill: red;");
        killCountLabel.setFont(new Font("Segoe Script", 40));
        killCountLabel.layoutXProperty().bind(Bindings.createDoubleBinding(
                () -> (menuPane.getWidth() - 20 - killCountLabel.getWidth()),
                menuPane.widthProperty(),
                killCountLabel.widthProperty()));
        killCountLabel.setLayoutY(10);

        menuPane.getChildren().addAll(HPLabel, countdownLabel, killCountLabel, fireCDarc, skillCDarc, skillCDDot);
    }

    private void buildCDLayout() {
        fireCDarc.setType(ArcType.OPEN);
        fireCDarc.setFill(Color.TRANSPARENT);
        fireCDarc.setStroke(Color.RED);
        fireCDarc.setStrokeWidth(3);
        fireCDarc.lengthProperty().bind(fireCDprogress.multiply(360));

        skillCDarc.setType(ArcType.OPEN);
        skillCDarc.setFill(Color.TRANSPARENT);
        skillCDarc.setStroke(Color.BLACK);
        skillCDarc.setStrokeWidth(3);
        skillCDarc.lengthProperty().bind(skillCDprogress.multiply(360));

        skillCDDot.visibleProperty().bind(skillCDprogress.isEqualTo(1)); // 设置技能冷却点可见性，当技能冷却完成时显示
    }
}
