package com.mambastu.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mambastu.controller.context.ContextManager;
import com.mambastu.controller.context.dto.Context;
import com.mambastu.controller.listener.EngineLayerListener;
import com.mambastu.controller.listener.GameOverMenuListener;
import com.mambastu.controller.listener.LevelMenuListener;
import com.mambastu.controller.listener.MainMenuListener;
import com.mambastu.controller.listener.PauseMenuListener;
import com.mambastu.core.GameEngine;
import com.mambastu.resource.media.impl.AudioManager;
import com.mambastu.view.GameOverMenu;
import com.mambastu.view.InGameHud;
import com.mambastu.view.LevelMenu;
import com.mambastu.view.MainMenu;
import com.mambastu.view.PauseMenu;

import javafx.scene.layout.StackPane;

public class LevelController {
    private static final Logger logger = LogManager.getLogger(LevelController.class); // 初始化日志记录器，用于记录程序运行过程中的日志信息

    private GameEngine gameEngine;
    private final Context ctx; // 上下文, 包含了游戏的各种参数，穿透关卡管理层、引擎层、逻辑层，可在UI层调用以获取需要显示的信息
    private final StackPane root;

    private final ContextManager ctxManager; // 上下文管理器，用于在过关后进入下一关前更新上下文

    private MainMenu mainMenu;
    private final PauseMenu pauseMenu;
    private final LevelMenu levelMenu;
    private GameOverMenu gameOverMenu;
    private final InGameHud inGameHud;

    private final EngineLayerHandler engineLayerListener;
    private final MainMenuHandler mainMenuListener;
    private final PauseMenuHandler pauseMenuListener;
    private final LevelMenuHandler levelMenuListener;
    private final GameOverMenuHandler gameOverMenuListener;

    // ================================= Init Section =================================
    public LevelController(StackPane root) {
        this.root = root;
        this.ctx = new Context(); // 初始化上下文，可在UI层调用以获取需要显示的信息

        this.ctxManager = new ContextManager(ctx); // 初始化上下文管理器，用于管理上下文，可在UI层调用以获取需要显示的信息

        this.engineLayerListener = new EngineLayerHandler(); // 初始化引擎层监听器，用于监听引擎层的运行状态，触发相应的事件，如游戏暂停、游戏结束等
        this.mainMenuListener = new MainMenuHandler();
        this.pauseMenuListener = new PauseMenuHandler();
        this.levelMenuListener = new LevelMenuHandler();
        this.gameOverMenuListener = new GameOverMenuHandler();

        this.pauseMenu = new PauseMenu(root, ctx, pauseMenuListener);
        this.levelMenu = new LevelMenu(root, ctx, levelMenuListener);
        this.inGameHud = new InGameHud(root, ctx); // 初始化游戏内HUD
    }

    private void initDynamicResource() { // 初始化动态资源ctx, 动态菜单
        ctxManager.init(); // 初始化上下文管理器
        initDynamicMenu();
    }

    private void updateDynamicMenu() { // 更新动态资源ctx, 动态菜单，如游戏内HUD、暂停菜单等，这些菜单需要根据关卡配置信息来显示不同的内容。
        pauseMenu.update();
        inGameHud.update();
        levelMenu.update();
    }

    private void initDynamicMenu() { // 初始化动态菜单，如游戏内HUD、暂停菜单等，这些菜单需要根据关卡配置信息来显示不同的内容。
        pauseMenu.init();
        inGameHud.init();
        levelMenu.init();
    }

    // ================================= Operation Section =================================
    public void showMainMenu() { // 显示主菜单逻辑
        mainMenu = new MainMenu(root, ctx, mainMenuListener);
        mainMenu.init();
        mainMenu.show();
    }

    private void showGameOverMenu() {
        gameOverMenu = new GameOverMenu(root, ctx, gameOverMenuListener);
        gameOverMenu.init();
        gameOverMenu.show();
    }

    private void startFirstLevel() { // 初始化引擎层, 开始关卡逻辑
        AudioManager.getInstance().loopAudio("BackgroundMusic", "BattleTheme2","displayAudio");
        AudioManager.getInstance().setVolume("BackgroundMusic", "BattleTheme2","displayAudio",0.3f);
        AudioManager.getInstance().playAudio("BackgroundMusic", "ReadyFight","displayAudio");
        AudioManager.getInstance().setVolume("BackgroundMusic", "ReadyFight","displayAudio",0.3f);

        initDynamicResource();
        gameEngine = new GameEngine(ctx, root, engineLayerListener);
        gameEngine.start();
        gameEngine.showGamePane();
        inGameHud.show();
    }

    private void startNextLevel() { // 生成新的LevelConfig与LevelRecord, 初始化引擎层,
        // 开始关卡逻辑，与startFirstLevel类似，但需要从上下文中获取关卡信息，并更新上下文中的关卡记录信息。
        AudioManager.getInstance().setVolume("BackgroundMusic", "BattleTheme2","displayAudio",0.3f);
        ctxManager.updateCtx();
        updateDynamicMenu();
        gameEngine = new GameEngine(ctx, root, engineLayerListener);
        gameEngine.start();
        gameEngine.showGamePane();
        inGameHud.show();
    }

    // ================================= Listeners and Handlers Section =================================
    private class EngineLayerHandler implements EngineLayerListener {
        @Override
        public void pauseGame() {
            pauseMenu.show();
            logger.info("LevelManager: Game is paused.");
        }

        @Override
        public void resumeGame() {
            pauseMenu.hide();
            logger.info("LevelManager: Game is resumed.");
        }

        @Override
        public void stopGame(boolean isPassLevel) {
            gameEngine.hideGamePane(); // 隐藏游戏界面，显示游戏结束菜单。
            inGameHud.hide();

            if (isPassLevel) { // 如果通过关卡，则显示下一关卡菜单，否则显示游戏结束菜单。
                AudioManager.getInstance().setVolume("BackgroundMusic", "BattleTheme2","displayAudio",0.04f);
                ctxManager.updateCoin(); // 更新玩家当前关卡获得的硬币数。
                levelMenu.update();
                levelMenu.show();
                logger.error("LevelManager: Level is past.");
            } else {
                AudioManager.getInstance().stopAudio("BackgroundMusic", "BattleTheme2","displayAudio");
                AudioManager.getInstance().playAudio("BackgroundMusic", "GameOver","displayAudio");
                ctxManager.recordBeforeOver();
                showGameOverMenu();
                logger.error("LevelManager: Game is over.");
            }
        }
    }

    private class MainMenuHandler implements MainMenuListener {
        @Override
        public void startGame() {
            startFirstLevel();
            mainMenu.hide();
        }
    }

    private class PauseMenuHandler implements PauseMenuListener {
        @Override
        public void resumeGame() {
            pauseMenu.hide();
            gameEngine.resumeThisEngine();
        }
    }

    private class LevelMenuHandler implements LevelMenuListener { // 关卡菜单监听器，用于监听关卡选择事件，开始相应的关卡。
        @Override
        public void startLevel() {
            startNextLevel();
            levelMenu.hide();
        }
    }

    private class GameOverMenuHandler implements GameOverMenuListener {
        @Override
        public void backMainMenu() {
            gameOverMenu.hide();
            showMainMenu();
        }

        @Override
        public void restartGame() {
            gameOverMenu.hide();
            startFirstLevel();
        }
    }
}
