package com.mambastu.infuse.input;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Set;

import com.mambastu.infuse.pojo.enums.GameInput;
public class InputManager {
    private Set<GameInput> activeInputs;

    public InputManager(Scene scene) {
        activeInputs = new HashSet<>();
        initialize(scene);
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
                case P:
                    activeInputs.add(GameInput.GAME_PAUSE);
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
                case P:
                    activeInputs.remove(GameInput.GAME_PAUSE);
                    break;
                default:
                    break;
            }
        }
    }
}
