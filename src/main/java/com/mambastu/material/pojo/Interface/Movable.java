package com.mambastu.material.pojo.Interface;

import javafx.scene.layout.Pane;

public interface Movable {
    void crossedBoundary(Pane root);

    void savePreviousFrame();
}
