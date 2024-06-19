package com.mambastu.material.pojo.entity.monster;

import java.util.List;
import java.util.Random;

import com.mambastu.factories.MonsterFactory;
import com.mambastu.material.pojo.entity.bullet.BaseBullet;
import com.mambastu.material.resource.ResourceManager;
import com.mambastu.util.AudioManager;
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
    private final static double INIT_SPEED = 3.0;
    private final static double SKILL_CD_1 = 5;
    private final static double SKILL_CD_2 = 1;

    private final static int MAX_DASH_TIMES = 3; // 最大冲刺次数

    private final static double MAX_INVISIBLE_TIME = 4; // 最大隐身时间（秒）

    private final static double MAX_CURTAIN_TIME = 6; // 最大帘幕时间（秒）
    private final static double CURTAIN_SPEED = 20;

    private final Image omenImage;
    private final Image bornImage;
    private final Image dieImage;
    private final Image warnImage;
    private final Image dashImage;
    private final Image redEyeImage;
    private final Image curtainImage;

    private final ImageView redEyeView;

    private final PauseTransition invisibleTimer;
    private final PauseTransition skillCooldownTimer;

    private int curDashCount;
    private final Timeline accelerateTimer;
    private final Timeline decelerateTimer;
    private final PauseTransition dashWaitTimer;

    private final FadeTransition fadeToInvisible;

    private final Timeline curtainTimer;
    private final ImageView curtainImageView;
    private final FadeTransition curtainFadeIn;
    private final FadeTransition curtainFadeOut;
    private boolean isCurtainReachTarget;
    private double randomTargetX;
    private double randomTargetY;

    private boolean isLowHP;

    private enum LordState {
        NORMAL, DASH, INVISIBLE, CURTAIN, WARNING
    }

    private LordState lordState;

    private final ColorAdjust warningColorAdjust;

    public HellLordMonster() {
        super();

        this.omenImage = ResourceManager.getInstance().getImg("omenImage", "Monster", "HellLord");
        this.bornImage = ResourceManager.getInstance().getImg("bornImage", "Monster", "HellLord");
        this.dieImage = ResourceManager.getInstance().getImg("dieImage", "Monster", "HellLord");
        this.warnImage = ResourceManager.getInstance().getImg("warnImage", "Monster", "HellLord");
        this.dashImage = ResourceManager.getInstance().getImg("dashImage", "Monster", "HellLord");
        this.redEyeImage = ResourceManager.getInstance().getImg("redEyeImage", "Monster", "HellLord");
        this.curtainImage = ResourceManager.getInstance().getImg("curtainImage", "Monster", "HellLord");
        setImageSize(150, 150);

        this.redEyeView = new ImageView(redEyeImage);

        this.damage = 5;
        this.warningColorAdjust = new ColorAdjust();
        warningColorAdjust.setHue(-0.1);
        this.initTimer.setDuration(Duration.seconds(1));

        this.accelerateTimer = new Timeline();
        this.decelerateTimer = new Timeline();
        this.dashWaitTimer = new PauseTransition();

        this.fadeToInvisible = new FadeTransition();

        this.curtainTimer = new Timeline();
        this.curtainImageView = new ImageView(curtainImage);
        this.curtainFadeIn = new FadeTransition();
        this.curtainFadeOut = new FadeTransition();

        this.invisibleTimer = new PauseTransition();
        this.skillCooldownTimer = new PauseTransition();

        this.isLowHP = false;

        initSkillFX();
    }

    private void initSkillFX() {
        skillCooldownTimer.setDuration(Duration.seconds(SKILL_CD_1));
        skillCooldownTimer.setOnFinished(event -> {
            triggerRandomSkill();
        });

        // 技能一：冲刺
        dashWaitTimer.setDuration(Duration.seconds(1.0));
        dashWaitTimer.setOnFinished(event -> {
            lordState = LordState.DASH;
            showingImage.set(dashImage);
            if (curDashCount > 0) {
                accelerateTimer.playFromStart();
            } else {
                resetToNormalState();
            }
        });
        accelerateTimer.getKeyFrames().add(new KeyFrame(Duration.millis(50), event -> speed += 1.0));
        accelerateTimer.setCycleCount(8);
        decelerateTimer.getKeyFrames().add(new KeyFrame(Duration.millis(50), event -> speed -= 1.0));
        decelerateTimer.setCycleCount(8); // 15 to 0 in 0.5 seconds
        accelerateTimer.setOnFinished(event -> decelerateTimer.play());
        decelerateTimer.setOnFinished(event -> {
            curDashCount--;
            dashWaitTimer.playFromStart();
        });

        // 技能二：隐身
        invisibleTimer.setDuration(Duration.seconds(MAX_INVISIBLE_TIME));
        invisibleTimer.setOnFinished(event -> {
            resetToNormalState();
            showingImageView.setOpacity(1);
        });

        fadeToInvisible.setDuration(Duration.seconds(1));
        fadeToInvisible.setNode(showingImageView);
        fadeToInvisible.setFromValue(1.0); // 初始透明度
        fadeToInvisible.setToValue(0.0); // 目标透明度
        fadeToInvisible.setOnFinished(event -> {
            lordState = LordState.INVISIBLE;
        });

        // 技能三：天幕
        redEyeView.setFitWidth(showingImageView.getFitWidth());
        redEyeView.setFitHeight(showingImageView.getFitHeight());
        redEyeView.xProperty().bind(showingImageView.xProperty());
        redEyeView.yProperty().bind(showingImageView.yProperty());

        curtainImageView.setFitWidth(GlobalVar.getGamePane().getWidth());
        curtainImageView.setFitHeight(GlobalVar.getGamePane().getHeight());

        curtainFadeIn.setDuration(Duration.seconds(0.6));
        curtainFadeIn.setNode(curtainImageView);
        curtainFadeIn.setFromValue(0);
        curtainFadeIn.setToValue(1);
        curtainFadeIn.setOnFinished(event -> {
            lordState = LordState.CURTAIN;
        });

        curtainFadeOut.setDuration(Duration.seconds(1));
        curtainFadeOut.setNode(curtainImageView);
        curtainFadeOut.setFromValue(1);
        curtainFadeOut.setToValue(0);
        curtainFadeOut.setOnFinished(event -> {
            GlobalVar.getGamePane().getChildren().remove(curtainImageView);
        });

        curtainTimer.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, event -> {
            curtainFadeIn.playFromStart();
        }), new KeyFrame(Duration.millis(900), event -> {
            GlobalVar.getGamePane().getChildren().add(redEyeView);
        }), new KeyFrame(Duration.millis(MAX_CURTAIN_TIME * 1000 - 500), event -> {
            GlobalVar.getGamePane().getChildren().remove(redEyeView);
        }), new KeyFrame(Duration.seconds(MAX_CURTAIN_TIME), event -> {
            curtainFadeOut.playFromStart(); // 开始透明消失动画
            resetToNormalState();
        }));
        curtainTimer.setCycleCount(1);
    }

    @Override
    public void init() {
        HP.set(10000);
        speed = 3;
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
        if (lordState == LordState.WARNING)
            return;
        if (lordState != LordState.CURTAIN) {
            double dx = targetX - x.get();
            double dy = targetY - y.get();
            double distance = BetterMath.sqrt(dx * dx + dy * dy);
            if (distance > 0) {
                x.set(x.get() + speed * dx / distance);
                y.set(y.get() + speed * dy / distance);
            }
            showingImageView.setX(x.get());
            showingImageView.setY(y.get());
        } else {
            moveRandomly(); // 幕布状态下随机移动
        }
        trappedInStage();
    }

    // 预警 随机释放技能
    private void triggerRandomSkill() {
        lordState = LordState.WARNING;
        showingImage.set(warnImage);
        if (state == State.NORMAL) {
            Random random = new Random();
            switch (random.nextInt(3)) {
                case 0:
                    startDashSkill();
                    break;
                case 1:
                    startInvisibleSkill();
                    break;
                case 2:
                    AudioManager.getInstance().playAudio("SoundEffects", "HellLordMonsterCurtain", "displayAudio");
                    startCurtainSkill();
                    break;
            }
        }
    }

    // 冲刺技能
    private void startDashSkill() {
        curDashCount = MAX_DASH_TIMES;
        dashWaitTimer.playFromStart();
    }

    // 隐身技能
    private void startInvisibleSkill() {
        invisibleTimer.playFromStart();
        // 使 showingImageView 逐渐变透明
        showingImageView.setOpacity(1);// 本体隐身，等待虚影变成透明
        fadeToInvisible.playFromStart();
    }

    // 召唤幕布 运行随机冲刺
    private void startCurtainSkill() {
        isCurtainReachTarget = true;

        curtainImageView.setOpacity(0.0);
        GlobalVar.getGamePane().getChildren().add(curtainImageView);
        curtainTimer.playFromStart();
    }

    // 随机移动
    private void moveRandomly() {
        if (isCurtainReachTarget) {
            isCurtainReachTarget = false;
            Random random = new Random();
            randomTargetX = 100 + random.nextDouble() * (GlobalVar.getGamePane().getWidth() - 200); // 避免移动到屏幕边缘
            randomTargetY = 100 + random.nextDouble() * (GlobalVar.getGamePane().getHeight() - 200);
        }

        double dx = randomTargetX - x.get();
        double dy = randomTargetY - y.get();
        double distance = BetterMath.sqrt(dx * dx + dy * dy);

        if (distance > 20) {
            x.set(x.get() + CURTAIN_SPEED * dx / distance);
            y.set(y.get() + CURTAIN_SPEED * dy / distance);
        } else {
            isCurtainReachTarget = true; // 防止无限循环移动到同一个位置
        }
        showingImageView.setX(x.get());
        showingImageView.setY(y.get());
    }

    // 回归原始状态
    private void resetToNormalState() {
        speed = INIT_SPEED;
        lordState = LordState.NORMAL;
        showingImage.set(bornImage);
        skillCooldownTimer.playFromStart();
    }

    @Override
    public void getHurt(BaseBullet bullet) {
        if (state == State.NORMAL && !inBulletQueue.contains(bullet)) { // 如果怪物处于正常状态，并且子弹不在无敌子弹队列中，则造成伤害
            HP.set(HP.get() - bullet.releaseDamage());
            if (HP.get() < 1000 && !isLowHP) { // 血量低于1000进入2阶段
                isLowHP = true;
                skillCooldownTimer.setDuration(Duration.seconds(SKILL_CD_2));
            }
            if (HP.get() > 0) { // 如果怪物血量大于0，则播放受伤动画，并进入无敌帧状态
                showingImageView.setEffect(colorAdjust);
                hurtFXTimer.play();
                inBulletQueue.add(bullet); // 将子弹加入无敌子弹队列中，防止连击
                runQueueTimer(); // 尝试开始无敌子弹队列计时器
            }
        }
    }

    @Override
    public Integer releaseDamage() {
        return (state == State.NORMAL) ? damage : 0;
    }

    @Override
    public void die() {
        state = State.DIE;
        showingImage.set(dieImage);
        showingImageView.setOpacity(1.0); // 确保透明度为1，以显示死亡动画
        curtainFadeOut.play(); // 死亡后立刻移除可能存在的天幕
        PauseTransition rmTimer = new PauseTransition(Duration.seconds(3));
        rmTimer.setOnFinished(e -> {
            removeFromPane(GlobalVar.getGamePane());
            MonsterFactory.getInstance().delete(this);
        });
        rmTimer.play();
    }
}
