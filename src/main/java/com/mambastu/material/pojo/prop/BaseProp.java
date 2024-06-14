package com.mambastu.material.pojo.prop;

import com.mambastu.material.pojo.entity.player.BasePlayer;

import javafx.scene.image.Image;
import lombok.Getter;

@Getter
public abstract class BaseProp {
    protected Image displayImage; // 道具显示图片
    protected double buffValue; // 属性加成值，例如攻击力、防御力等
    protected int price; // 价格

    public void loadValues(double buffValue, int price) {
        this.price = price;
        this.buffValue = buffValue;
    }

    abstract public void updateValue(BasePlayer player);

}
