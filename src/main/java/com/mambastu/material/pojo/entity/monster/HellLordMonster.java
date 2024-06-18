package com.mambastu.material.pojo.entity.monster;

import java.util.List;
import java.util.Random;

import com.mambastu.factories.MonsterFactory;
import com.mambastu.material.resource.ResourceManager;
import com.mambastu.util.BetterMath;
import com.mambastu.util.GlobalVar;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class HellLordMonster extends BaseMonster {
    private final Image omenImage;
    private final Image bornImage;
    private final Image dieImage;
    private final Image curtainImage;

    int moveCount = 0;//skill运行计数器
    double randomX;
    double randomY;

    private final Random random;
    private final PauseTransition skillTimer;
    private final PauseTransition skillCooldownTimer;
    private final Timeline dashTimer;
    private final Timeline moveTimeline;

    private boolean isLowHP;

    private enum LordState {NORMAL, DASH, INVISIBLE, CURTAIN, WARNING}
    private LordState lordState;

    ColorAdjust warningColorAdjust = new ColorAdjust();

    public HellLordMonster() {
        super();
        warningColorAdjust.setHue(-0.1);
        this.omenImage = ResourceManager.getInstance().getImg("omenImage", "Monster", "HellLord");
        this.bornImage = ResourceManager.getInstance().getImg("bornImage", "Monster", "HellLord");
        this.dieImage = ResourceManager.getInstance().getImg("dieImage", "Monster", "HellLord");
        this.curtainImage = ResourceManager.getInstance().getImg("curtainImage", "Monster", "HellLord");
        this.random = new Random();
        setImageSize(150, 150);
        this.damage = 15;
        this.initTimer.setDuration(Duration.seconds(1));
        moveTimeline = new Timeline();
        dashTimer = new Timeline(
                new KeyFrame(Duration.seconds(1.0), event -> {
                    randomX = random.nextDouble();
                    randomY = random.nextDouble();
                }));
        dashTimer.setCycleCount(Timeline.INDEFINITE);
        skillTimer = new PauseTransition();
        this.skillCooldownTimer = new PauseTransition(Duration.seconds(5));
        this.skillCooldownTimer.setOnFinished(event -> {
            triggerRandomSkill();
            lordState=LordState.WARNING;
        });
        isLowHP = false;
    }

    @Override
    public void init() {
        HP.set(3000);
        speed=3;
        inBulletQueue.clear();
        showingImage.set(omenImage);
        showingImageView.imageProperty().bind(showingImage);
        state = State.OMEN;
        lordState = LordState.NORMAL;
        skillCooldownTimer.playFromStart();
    }


    @Override
    public void omen(List<BaseMonster> monsterList) {
        initTimer.setOnFinished(event -> {
            state = State.NORMAL;
            showingImage.set(bornImage);
            monsterList.add(this);
        });
        initTimer.playFromStart();
    }


    @Override
    public void move(double targetX, double targetY) {
        if (HP.get() < 1000 && !isLowHP) { // 血量低于1000进入2阶段
            isLowHP = true;
            this.skillCooldownTimer.setDuration(Duration.seconds(1));
        }

        if(lordState != LordState.CURTAIN) {
            double dx = targetX - x.get();
            double dy = targetY - y.get();
            double distance = BetterMath.sqrt(dx * dx + dy * dy);
            if (distance > 0) {
                x.set(x.get() + speed * dx / distance);
                y.set(y.get() + speed * dy / distance);
            }
            showingImageView.setX(x.get());
            showingImageView.setY(y.get());
        }else {
            dashTimer.playFromStart();
            x.set(x.get() + speed * randomX);
            y.set(y.get() + speed * randomY);
            showingImageView.setX(x.get());
            showingImageView.setY(y.get());
            System.out.println("Dashing");
        }
        trappedInStage();
    }

    @Override
    public Integer releaseDamage() {
        return (state==State.NORMAL) ? damage : 0;
    }

    @Override
    public void die() {
        state = State.DIE;
        showingImage.set(dieImage);
        PauseTransition rmTimer = new PauseTransition(Duration.seconds(3));
        rmTimer.setOnFinished(e -> {
            removeFromPane(GlobalVar.getGamePane());
            MonsterFactory.getInstance().delete(this);
        });
        rmTimer.play();
    }

    //预警 随机释放技能
    private void triggerRandomSkill() {
        if (state != State.DIE) {
            speed=0;

                switch (random.nextInt(2)) {
                    case 0:
                        startDashSkill();
                        break;
                    case 1:
                        startInvisibleSkill();
                        break;
                    case 2:
                        startCurtainSkill();
                        break;
                }
        }
    }

    //冲刺技能计时器
    private void startDashSkill() {
        System.out.println("111");
        lordState = LordState.DASH;
        skillTimer.setDuration(Duration.seconds(5));
        skillTimer.setOnFinished(event -> resetToNormalState());
        skillTimer.play();
        dashRepeatedly(3);
    }

    //冲刺逻辑，此方法仅用于速度变化，本身的行为逻辑依然是move
    private void dashRepeatedly(int repetitions) {
        if(repetitions > 0) {
            Timeline accelerate = new Timeline(new KeyFrame(Duration.millis(50), event -> speed += 1.5));
            accelerate.setCycleCount(10);

            Timeline decelerate = new Timeline(new KeyFrame(Duration.millis(50), event -> speed -= 1.5));
            decelerate.setCycleCount(10); // 15 to 0 in 0.5 seconds

            PauseTransition wait = new PauseTransition(Duration.seconds(0.5));

            accelerate.setOnFinished(event -> decelerate.play());
            decelerate.setOnFinished(event -> {
                wait.play();
                wait.setOnFinished(e -> dashRepeatedly(repetitions - 1));
            });

            accelerate.play();
        }
    }

    //该方法使怪物变成透明
    private void startInvisibleSkill() {
        lordState = LordState.INVISIBLE;
        skillTimer.setDuration(Duration.seconds(4));
        skillTimer.setOnFinished(event -> {
            resetToNormalState();
            showingImageView.setOpacity(1);
        });
        skillTimer.play();

        // 使 showingImageView 逐渐变透明
        showingImageView.setOpacity(1);//本体隐身，等待虚影变成透明
        FadeTransition fadeToInvisible = new FadeTransition(Duration.seconds(1), showingImageView);
        fadeToInvisible.setFromValue(1.0);  // 初始透明度
        fadeToInvisible.setToValue(0.0);    // 目标透明度
        fadeToInvisible.setOnFinished(event -> {
            speed = 5;//变透明后开始移动
        });
        fadeToInvisible.play();
    }

    // 召唤幕布 运行随机冲刺
    private void startCurtainSkill() {
        lordState = LordState.CURTAIN;
        skillTimer.setDuration(Duration.seconds(10));
        skillTimer.play();

        ImageView curtainView = new ImageView(curtainImage);
        curtainView.setFitWidth(GlobalVar.getGamePane().getWidth());
        curtainView.setFitHeight(GlobalVar.getGamePane().getHeight());
        GlobalVar.getGamePane().getChildren().add(curtainView);
        curtainView.toFront();//用于确保其始终处于最顶层

//        PauseTransition moveCycle = new PauseTransition(Duration.seconds(1));
//        moveCycle.setOnFinished(event -> {
//            moveRandomly();
//            moveCount++;
//            if (moveCount < 10) { // Adjusted to repeat 10 times
//                moveCycle.play();
//            }
//        });
//        moveCycle.play();

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), curtainView);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(event -> GlobalVar.getGamePane().getChildren().remove(curtainView));

        skillTimer.setOnFinished(event -> {
            resetToNormalState();
            dashTimer.stop();
            fadeOut.play();
        });
    }

//    // 随机移动
//    private void moveRandomly() {
//        double targetX = random.nextDouble() * GlobalVar.getGamePane().getWidth();
//        double targetY = random.nextDouble() * GlobalVar.getGamePane().getHeight();
//
//        double dx = targetX - x.get();
//        double dy = targetY - y.get();
//        double distance = BetterMath.sqrt(dx * dx + dy * dy);
//
//        if (distance > 0) {
//            double stepX = 10 * dx / distance;
//            double stepY = 10 * dy / distance;
//
//            if (moveTimeline != null && moveTimeline.getStatus() == Timeline.Status.RUNNING) {
//                moveTimeline.stop();
//            }
//
//            moveTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> {
//                x.set(x.get() + stepX);
//                y.set(y.get() + stepY);
//                showingImageView.setX(x.get());
//                showingImageView.setY(y.get());
//            }));
//            moveTimeline.setCycleCount(10); // 100ms间隔，共10次，持续1秒
//            moveTimeline.play();
//        }
//    }

    //回归原始状态
    private void resetToNormalState() {
        speed = 3;
        moveCount =0;
        lordState = LordState.NORMAL;
        skillCooldownTimer.playFromStart();
    }
}
