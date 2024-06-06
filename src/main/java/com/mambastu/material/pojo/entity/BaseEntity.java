package com.mambastu.material.pojo.entity;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseEntity {
    protected ImageView imageView; // 实体都能够被显示
    protected double x;
    protected double y;

    public void setImageSize(double width, double height) {
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
    }

    public void putOnPane (Pane root) { // 放置实体
        root.getChildren().add(imageView);
    }

    public void removeFromPane (Pane root) { // 移除实体
        root.getChildren().remove(imageView);
    }
    
    // TODO: 简易圆形Bounds内部类
}
