package com.mambastu.material.resource;

import org.json.JSONObject;
import org.json.JSONTokener;

import javafx.scene.image.Image;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Author:JngyEn
 * Description:
 * DateTime: 2024/6/8下午7:36
 **/
public class ResourceManager {
    private static ResourceManager INSTANCE = new ResourceManager();

    private JSONObject imgResources ;

    private ResourceManager() {}

    public static ResourceManager getInstance() {
        return INSTANCE;
    }

    public void loadResources() { // TODO: 加载音频资源
        String JsonUrl = "ImgResources.json";
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(JsonUrl)) {
            if (inputStream == null) {
                throw new FileNotFoundException("Resource not found: " + JsonUrl);
            }
            imgResources = new JSONObject(new JSONTokener(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Image getImg(String caseType, String... paths) {
        return ImgCache.getImage(parseURL(caseType, paths));
    }

    /**
     * 传入 JSON 中的所有父节点，从而查询到url
     *
     * @param caseType
     * @param paths
     * @return
     */
    private String parseURL(String caseType, String... paths) {
        JSONObject currentNode = imgResources;
        for (String path : paths) {
            currentNode = currentNode.getJSONObject(path);
            if (currentNode == null) {;
                return null;
            }
        }
        return currentNode.optString(caseType, null);
    }
}
