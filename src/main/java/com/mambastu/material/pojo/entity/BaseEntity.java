package com.mambastu.material.pojo.entity;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseEntity {
    protected ImageView showingImageView; // 正在被展示的图片
    protected SimpleDoubleProperty x = new SimpleDoubleProperty();
    protected SimpleDoubleProperty y = new SimpleDoubleProperty();
    protected double prevX; // 用于碰撞检测的变量，记录上一次的位置
    protected double prevY;

    abstract public void init(); // 更新实体的位置和状态等

    public void setImageSize(double width, double height) {
        showingImageView.setFitWidth(width);
        showingImageView.setFitHeight(height);
    }

    public void putOnPane (Pane root) { // 放置实体
        root.getChildren().add(showingImageView);
    }

    public void removeFromPane (Pane root) { // 移除实体
        root.getChildren().remove(showingImageView);
    }
    
    // TODO: 简易圆形Bounds内部类
}
