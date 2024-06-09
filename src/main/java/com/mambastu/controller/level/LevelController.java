package com.mambastu.controller.level;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mambastu.controller.level.context.dto.Context;
import com.mambastu.controller.level.context.enums.GameMode;
import com.mambastu.controller.level.context.manager.ContextManager;
import com.mambastu.core.engine.GameEngine;
import com.mambastu.listener.EngineLayerListener;
import com.mambastu.listener.MainMenuListener;
import com.mambastu.listener.PauseMenuListener;
import com.mambastu.ui.GameOverMenu;
import com.mambastu.ui.InGameHud;
import com.mambastu.ui.LevelMenu;
import com.mambastu.ui.MainMenu;
import com.mambastu.ui.PauseMenu;

import javafx.scene.layout.StackPane;

public class LevelController {
    private static final Logger logger = LogManager.getLogger(LevelController.class); // 初始化日志记录器，用于记录程序运行过程中的日志信息

    private GameEngine gameEngine;
    private final Context ctx; // 上下文, 包含了游戏的各种参数，穿透关卡管理层、引擎层、逻辑层，可在UI层调用以获取需要显示的信息
    private final StackPane root;

    private final ContextManager ctxManager; // 上下文管理器，用于在过关后进入下一关前更新上下文

    private final MainMenu mainMenu;
    private final PauseMenu pauseMenu;
    private final LevelMenu levelMenu;
    private final GameOverMenu gameOverMenu;
    private final InGameHud inGameHud;

    private final EngineLayerHandler engineLayerListener;
    private final MainMenuHandler mainMenuListener;
    private final PauseMenuHandler pauseMenuListener;

    // ================================= Init Section =================================
    public LevelController(StackPane root) {
        this.root = root;
        this.ctx = new Context(); // 初始化上下文，可在UI层调用以获取需要显示的信息

        this.ctxManager = new ContextManager(ctx); // 初始化上下文管理器，用于管理上下文，可在UI层调用以获取需要显示的信息

        this.engineLayerListener = new EngineLayerHandler(); // 初始化引擎层监听器，用于监听引擎层的运行状态，触发相应的事件，如游戏暂停、游戏结束等
        this.mainMenuListener = new MainMenuHandler();
        this.pauseMenuListener = new PauseMenuHandler();

        this.mainMenu = new MainMenu(root, mainMenuListener); // 初始化主菜单，传入关卡配置信息，用于显示关卡选择
        this.pauseMenu = new PauseMenu(root, pauseMenuListener);
        this.levelMenu = new LevelMenu(root);
        this.gameOverMenu = new GameOverMenu(root); // 初始化游戏结束菜单，传入关卡配置信息，用于显示关卡选择
        this.inGameHud = new InGameHud(root); // 初始化游戏内HUD
    }

    public void init() {
        mainMenu.init();
        inGameHud.init(ctx);
    }

    private void initDynamicMenu() { // 初始化动态菜单，如游戏内HUD、暂停菜单等，这些菜单需要根据关卡配置信息来显示不同的内容。
        pauseMenu.init(ctx.getLevelRecord());
    }

    // ================================= Operation Section =================================
    public void showMainMenu() { // 显示主菜单逻辑
        mainMenu.show();
    }

    private void startFirstLevel() { // 初始化引擎层, 开始关卡逻辑
        ctxManager.init(); // 初始化上下文管理器
        initDynamicMenu();
        gameEngine = new GameEngine(ctx, root, engineLayerListener);
        gameEngine.start();
        inGameHud.show();
    }

    private void startNextLevel() { // 生成新的LevelConfig与LevelRecord, 初始化引擎层,
        // 开始关卡逻辑，与startFirstLevel类似，但需要从上下文中获取关卡信息，并更新上下文中的关卡记录信息。
        ctxManager.updateCtx();
        initDynamicMenu();
        gameEngine = new GameEngine(ctx, root, engineLayerListener);
        gameEngine.start();
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
        public void stopGame() {
            logger.error("LevelManager: Game is over.");
        }
    }

    private class MainMenuHandler implements MainMenuListener {
        @Override
        public void selectGameMode(GameMode gameMode) {
            ctx.setGameMode(gameMode);
        }

        @Override
        public void startGame() {
            mainMenu.hide();
            startFirstLevel();
        }
    }

    private class PauseMenuHandler implements PauseMenuListener {
        @Override
        public void resumeGame() {
            pauseMenu.hide();
            gameEngine.resumeThisEngine();
        }
    }
}
