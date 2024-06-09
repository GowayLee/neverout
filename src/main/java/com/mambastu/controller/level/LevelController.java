package com.mambastu.controller.level;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mambastu.controller.level.comp.GameMode;
import com.mambastu.controller.level.comp.config.GlobalConfig;
import com.mambastu.core.engine.GameEngine;
import com.mambastu.listener.EngineLayerListener;
import com.mambastu.listener.MainMenuListener;
import com.mambastu.listener.PauseMenuListener;
import com.mambastu.ui.menu.GameOverMenu;
import com.mambastu.ui.menu.LevelMenu;
import com.mambastu.ui.menu.MainMenu;
import com.mambastu.ui.menu.PauseMenu;

import javafx.scene.layout.StackPane;

public class LevelController {
    private static final Logger logger = LogManager.getLogger(LevelController.class); // 初始化日志记录器，用于记录程序运行过程中的日志信息

    private GameEngine gameEngine;
    private final GlobalConfig config; // 全局配置信息(需要改一个名称)，包含了游戏的各种参数，穿透关卡管理层、引擎层、逻辑层，可在UI层调用以获取需要显示的信息
    private final StackPane root;

    private final MainMenu mainMenu;
    private final PauseMenu pauseMenu;
    private final LevelMenu levelMenu;
    private final GameOverMenu gameOverMenu;

    private final EngineLayerHandler engineLayerListener;
    private final MainMenuHandler mainMenuListener;
    private final PauseMenuHandler pauseMenuListener;

    public LevelController (StackPane root) {
        this.root = root;
        this.config = new GlobalConfig(); // 初始化全局配置信息，包含游戏的各种参数，可在UI层调用以获取需要显示的信息
        this.engineLayerListener = new EngineLayerHandler(); // 初始化引擎层监听器，用于监听引擎层的运行状态，触发相应的事件，如游戏暂停、游戏结束等
        this.mainMenuListener = new MainMenuHandler();
        this.pauseMenuListener = new PauseMenuHandler();
        this.mainMenu = new MainMenu(root, mainMenuListener); // 初始化主菜单，传入关卡配置信息，用于显示关卡选择
        this.pauseMenu = new PauseMenu(root, pauseMenuListener);
        this.levelMenu = new LevelMenu(root);
        this.gameOverMenu = new GameOverMenu(root); // 初始化游戏结束菜单，传入关卡配置信息，用于显示关卡选择
    }

    public void init() {
        mainMenu.init();
        pauseMenu.init();
    }
   
    public void showMainMenu() { // 显示主菜单逻辑
        mainMenu.show();
    }

    public void startLevel() { // 初始化引擎层,开始关卡逻辑
        gameEngine = new GameEngine(config, root, engineLayerListener);
        // initEngineStateListener();
        gameEngine.start();
    }

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

    private class MainMenuHandler implements MainMenuListener{
        @Override
        public void selectGameMode(GameMode gameMode) {
            config.setGameMode(gameMode);
        }

        @Override
        public void startGame() {
            mainMenu.hide();
            startLevel();
        }
    }

    private class PauseMenuHandler implements PauseMenuListener{
        @Override
        public void resumeGame() {
            pauseMenu.hide();
            gameEngine.resumeThisEngine();
        }
    }
}   
