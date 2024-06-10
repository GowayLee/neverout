package com.mambastu.material.resource;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;

public class ImgCache {
    private static final Map<String, Image> imageCache = new HashMap<>();

    public static Image getImage(String path) {
        if (imageCache.containsKey(path)) {
            return imageCache.get(path);
        } else {
            Image image = new Image(path);
            imageCache.put(path, image);
            return image;
        }
    }

    public static void clearCache() {
        imageCache.clear();
    }
}
