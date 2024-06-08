package com.mambastu.infuse.input;

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Set;

import com.mambastu.infuse.input.comp.GameInput;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
public class InputManager { // TODO: 实现单例模式
    /*
     * 使用两套输入判定系统：
     * 1、对于操纵角色的连贯性输入信号，存入activeInputs集合中
     * 2、对于暂停游戏等非连贯性输入信号，使用侦听器模式来避免频繁地在游戏引擎逻辑中进行判定
     */
    private final PropertyChangeSupport support;
    private Set<GameInput> activeInputs;
    private boolean gamePause = false;
    private final ControllerManager controllers = new ControllerManager();;
    ControllerState currState;




    public InputManager(Scene scene) {
        this.support = new PropertyChangeSupport(this);
        activeInputs = new HashSet<>();
        initialize(scene);
    }

    public void addPropertyListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void initialize(Scene scene) {
        handleKeyboardInput(scene);
        controllers.initSDLGamepad();
        new Thread(this::updateJoystickInput).start();//手柄
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
            System.out.println(event.getCode());
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
                case P:
                    gamePause = gamePause ? false : true;
                    support.firePropertyChange("gamePause", !gamePause, gamePause);
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
                    activeInputs.remove(GameInput.MOVE_BOOST);
                    break;
                case K:
                    activeInputs.remove(GameInput.SKILL);
                    break;
                default:
                    break;
            }
        }
    }



    private void updateJoystickInput() {
        while (true) {
            currState = controllers.getState(0);
            if (currState.isConnected) {
                double leftX = currState.leftStickX;
                double leftY = currState.leftStickY;
                leftJoystickInput(leftX, leftY);
            }
        }
    }

    private void leftJoystickInput(double leftX, double leftY) {
        // 向上
        if (leftY > 0.5 || (leftY > 0.3 && Math.abs(leftX) < 0.3)) {
            activeInputs.add(GameInput.MOVE_UP);
        } else {
            activeInputs.remove(GameInput.MOVE_UP);
        }

        // 向下
        if (leftY < -0.5 || (leftY < -0.3 && Math.abs(leftX) < 0.3)) {
            activeInputs.add(GameInput.MOVE_DOWN);
        } else {
            activeInputs.remove(GameInput.MOVE_DOWN);
        }

        // 向左
        if (leftX < -0.5 || (leftX < -0.3 && Math.abs(leftY) < 0.3)) {
            activeInputs.add(GameInput.MOVE_LEFT);
        } else {
            activeInputs.remove(GameInput.MOVE_LEFT);
        }

        // 向右
        if (leftX > 0.5 || (leftX > 0.3 && Math.abs(leftY) < 0.3)) {
            activeInputs.add(GameInput.MOVE_RIGHT);
        } else {
            activeInputs.remove(GameInput.MOVE_RIGHT);
        }
    }
}
