package com.mambastu.material.pojo.Interface;

import com.mambastu.controller.input.comp.GameInput;
import com.mambastu.material.pojo.enums.CollisionState;

import java.util.Set;

public interface Movable {
    void crossedBoundary();
    void savePreviousFrame();
}
