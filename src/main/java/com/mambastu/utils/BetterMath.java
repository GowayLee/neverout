package com.mambastu.utils;

public class BetterMath {
    public static double sqrt(double value) { // 使用牛顿法，提高误差容许值以降低计算平方根的复杂度
        if (value < 0) {
            throw new IllegalArgumentException("Negative value");
        }
        if (value == 0) {
            return 0;
        }
        double guess = value;
        double epsilon = 1e-2; // 0.01

        while (Math.abs(guess * guess - value) > epsilon) {
            guess = (guess + value / guess) / 2.0;
        }
        return guess;
    }

    public static double toDegree(double value) { // 将弧度转换为角度，预计算
        return value * 565.49; // 将弧度转换为角度
    }
}
