package com.mambastu.resource.input;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mambastu.controller.listener.InputListener;
import com.mambastu.enums.GameInput;
import com.mambastu.resource.ResourceManager;

public class InputManager implements ResourceManager {
    private static final Logger logger = LogManager.getLogger(InputManager.class);

    private static InputManager INSTANCE = new InputManager();

    private InputListener listener;

    /*
     * 使用键盘输入判定系统：
     * 1、对于操纵角色的连贯性输入信号，存入activeInputs集合中
     * 2、对于暂停游戏等非连贯性输入信号，使用侦听器模式来避免频繁地在游戏引擎逻辑中进行判定
     */
    @Getter
    private final Set<GameInput> activeInputs;

    private InputManager() {
        activeInputs = new HashSet<>();
    }

    public static InputManager getInstance() {
        if (INSTANCE == null) { // 未经初始化即调用
            logger.error("InputManeger is being called without initialization.");
            throw new IllegalStateException("InputManeger is not initialized. Call initialize() first.");
        }
        return INSTANCE;
    }

    public void addListener(InputListener listener) {
        this.listener = listener; // 添加侦听器
    }

    public void init(Scene scene) {
        handleKeyboardInput(scene);
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
                    activeInputs.add(GameInput.SKILL);
                    break;
                case K:
                    activeInputs.add(GameInput.FIRE);
                    break;
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
                    activeInputs.remove(GameInput.SKILL);
                    break;
                case K:
                    activeInputs.remove(GameInput.FIRE);
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
