package com.mambastu.gameobjects.entity;

import com.mambastu.enums.gameobjects.CollisionState;
import com.mambastu.gameobjects.bound.Bound;
import com.mambastu.gameobjects.bound.ScreenBound;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;

public abstract class BaseEntity {
    @Getter @Setter
    protected boolean isOnStage; // 是否在舞台上，Otherwise就在对象池中
    protected final ImageView showingImageView = new ImageView(); // 正在被展示的图片节点
    protected final SimpleObjectProperty<Image> showingImage = new SimpleObjectProperty<>(); // 正在被绑定展示的图片
    @Getter
    protected SimpleDoubleProperty x = new SimpleDoubleProperty();
    @Getter
    protected SimpleDoubleProperty y = new SimpleDoubleProperty();
    protected double prevX; // 用于碰撞检测的变量，记录上一次的位置
    protected double prevY;
    @Getter
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

    /**
     * Avoid enetity move to the outside of the stage
     * 
     * @return boolean 
     */
    public boolean trappedInStage() {
        CollisionState collisionState = ScreenBound.collisionState(bound);
        switch (collisionState) {
            case HORIZONTAL:
                x.set(prevX);
                break;
            case VERTICAL:
                y.set(prevY);
                break;
            case BOTH:
                x.set(prevX);
                y.set(prevY);
                break;
            case FALSE:
                savePreviousFrame();
                return false;
            default:
                return false;
        }
        savePreviousFrame();
        return true;
    }

    public void savePreviousFrame() { // 保存上一次的位置，用于碰撞检测
        prevX = x.get();
        prevY = y.get();
    }
}