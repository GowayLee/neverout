package com.mambastu.view;

import javafx.scene.Scene;

public abstract class BaseMenu {
    private final Scene scene;

    public BaseMenu(Scene scene) {
        this.scene = scene;
    }

    abstract void show();

    abstract void buildLayout();
}
