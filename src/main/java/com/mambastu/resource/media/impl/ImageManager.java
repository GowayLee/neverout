package com.mambastu.resource.media.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.mambastu.resource.media.MediaResourceManager;

import javafx.scene.image.Image;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageManager implements MediaResourceManager {
    private static final Logger logger = LogManager.getLogger(AudioManager.class);

    private static ImageManager INSTANCE = new ImageManager();

    private JSONObject imgResources;
    private final Map<String, Image> imageCache;

    public static ImageManager getInstance() {
        return INSTANCE;
    }

    private ImageManager() {
        this.imageCache = new HashMap<>();
    }

    /**
     * Load local image resource list from a static JSON file.
     * 
     */
    @Override
    public void loadResources() {
        String JsonUrl = "ImgResources.json";
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(JsonUrl)) {
            if (inputStream == null) {
                logger.error("Resource not found: " + JsonUrl);
                throw new FileNotFoundException("Resource not found: " + JsonUrl);
            }
            imgResources = new JSONObject(new JSONTokener(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Import all parent nodes from JSON and parse URL string
     * caseType 为想要的图片key，path 从左到右为：父节点1，父节点2，父节点3...
     *
     * @param caseType
     * @param paths
     * @return
     */
    private String parseURL(String caseType, String... paths) {
        JSONObject currentNode = imgResources;
        for (String path : paths) {
            currentNode = currentNode.getJSONObject(path);
            if (currentNode == null) {
                return null;
            }
        }
        return currentNode.optString(caseType, null);
    }

    /**
     * Get image from cache or load it from file. If image is not found, return
     * null.
     * 
     * @param caseType
     * @param caseType
     * @param paths
     * @return
     */
    public Image getImg(String caseType, String... paths) {
        String path = parseURL(caseType, paths);
        if (!imageCache.containsKey(path)) {
            Image image = new Image(path);
            imageCache.put(path, image);
            logger.info("Load image from: " + path);
            return image;
        }
        return imageCache.get(path);
    }

    @Override
    public void clearCache() {
        imageCache.clear();
    }
}
