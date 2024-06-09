package com.mambastu.controller.input;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mambastu.controller.input.comp.GameInput;
import com.mambastu.listener.InputListener;

public class InputManager {
    private static final Logger logger = LogManager.getLogger(InputManager.class);

    private InputListener listener;

    private static InputManager INSTANCE;
    /*
     * 使用两套输入判定系统：
     * 1、对于操纵角色的连贯性输入信号，存入activeInputs集合中
     * 2、对于暂停游戏等非连贯性输入信号，使用侦听器模式来避免频繁地在游戏引擎逻辑中进行判定
     */
    private final Set<GameInput> activeInputs;

    private InputManager(Scene scene) {
        activeInputs = new HashSet<>();
        initialize(scene);
    }

    public static InputManager getInstance() {
        if (INSTANCE == null) { // 未经初始化即调用
            logger.error("InputManeger is being called without initialization.");
            throw new IllegalStateException("InputManeger is not initialized. Call initialize() first.");
        }
        return INSTANCE;
    }

    public static void init(Scene scene) {
        if (INSTANCE == null) { // 初始化
            INSTANCE = new InputManager(scene);
        } else { // 重复初始化
            logger.error("InputManager is already initialized.");
            throw new IllegalStateException("InputManager is already initialized.");
        }
    }

    public void addListener(InputListener listener) {
        this.listener = listener; // 添加侦听器
    }

    public void initialize(Scene scene) {
        handleKeyboardInput(scene);
        // 手柄的handle方法
    }
    
    public Set<GameInput> getActiveInputs() {
        return activeInputs;
    }

    private void handleKeyboardInput(Scene scene) { // 映射键盘输入
        scene.setOnKeyPressed(event -> {
            mapKeyToGameInput(event.getCode(), true);
        });

        scene.setOnKeyReleased(event -> {
            mapKeyToGameInput(event.getCode(), false);
        });
    }

    private void mapKeyToGameInput(KeyCode code, boolean isPressed) {
        if (isPressed) {
            switch (code) {
                case W:
                    activeInputs.add(GameInput.MOVE_UP);
                    break;
                case S:
                    activeInputs.add(GameInput.MOVE_DOWN);
                    break;
                case A:
                    activeInputs.add(GameInput.MOVE_LEFT);
                    break;
                case D:
                    activeInputs.add(GameInput.MOVE_RIGHT);
                    break;
                case SHIFT:
                    activeInputs.add(GameInput.MOVE_BOOST);
                    break;
                case K:
                    activeInputs.add(GameInput.SKILL);
                    break;
                // case P:
                //     listener.switchPausenResume();
                //     break;
                default:
                    break;
            }
        } else {
            switch (code) {
                case W:
                    activeInputs.remove(GameInput.MOVE_UP);
                    break;
                case S:
                    activeInputs.remove(GameInput.MOVE_DOWN);
                    break;
                case A:
                    activeInputs.remove(GameInput.MOVE_LEFT);
                    break;
                case D:
                    activeInputs.remove(GameInput.MOVE_RIGHT);
                    break;
                case SHIFT:
                    activeInputs.remove(GameInput.MOVE_BOOST);
                    break;
                case K:
                    activeInputs.remove(GameInput.SKILL);
                    break;
                case P:
                    listener.switchPausenResume();
                    break;
                default:
                    break;
            }
        }
    }
}
