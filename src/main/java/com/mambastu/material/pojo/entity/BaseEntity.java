package com.mambastu.material.pojo.entity;

import com.mambastu.material.pojo.bound.Bound;
import com.mambastu.material.pojo.bound.ScreenBound;
import com.mambastu.material.pojo.enums.CollisionState;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseEntity {
    protected boolean isOnStage; // 是否在舞台上，Otherwise就在对象池中
    protected final ImageView showingImageView = new ImageView(); // 正在被展示的图片节点
    protected final SimpleObjectProperty<Image> showingImage = new SimpleObjectProperty<>(); // 正在被绑定展示的图片
    protected SimpleDoubleProperty x = new SimpleDoubleProperty();
    protected SimpleDoubleProperty y = new SimpleDoubleProperty();
    protected double prevX; // 用于碰撞检测的变量，记录上一次的位置
    protected double prevY;
    protected Bound bound; // 碰撞箱

    abstract public void init();

    public void setImageSize(double width, double height) {
        showingImageView.setFitWidth(width);
        showingImageView.setFitHeight(height);
        bound.reArrangeBoundSize(width, height);
    }

    public void putOnPane(Pane root) { // 放置实体
        root.getChildren().add(showingImageView);
    }

    public void removeFromPane(Pane root) { // 移除实体
        root.getChildren().remove(showingImageView);
    }

    public void trappedInStage() { // 防止实体超出舞台范围，并触发碰撞检测
        CollisionState collisionState = ScreenBound.collisionState(this.getBound());
        if (collisionState == CollisionState.HORIZONTAL)
            x.set(prevX);
        if (collisionState == CollisionState.VERTICAL)
            y.set(prevY);
        if (collisionState == CollisionState.BOTH) {
            x.set(prevX);
            y.set(prevY);
        }
        savePreviousFrame();
    }

    public void savePreviousFrame() { // 保存上一次的位置，用于碰撞检测
        prevX = x.get();
        prevY = y.get();
    }
}