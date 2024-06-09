package com.mambastu.material.resource;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Author:JngyEn
 * Description:
 * DateTime: 2024/6/8下午7:36
 **/
public class ResourceManager { // TODO: 实现缓存
    private static ResourceManager resourceManager;
    private JSONObject Resources ;

    private ResourceManager() {}

    public static ResourceManager getResourceManager() {
        if ( resourceManager == null) {
            resourceManager = new ResourceManager();
        }
        return resourceManager;
    }


    public void loadResources() {
        String JsonUrl = "MonsterResources.json";
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(JsonUrl)) {
            if (inputStream == null) {
                throw new FileNotFoundException("Resource not found: " + JsonUrl);
            }
            Resources = new JSONObject(new JSONTokener(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 传入 JSON 中的所有父节点，从而查询到url
     *
     * @param caseType
     * @param path
     * @return
     */
    public String getResourcesByTypeAndCase(String caseType, String... path) {
        JSONObject currentNode = Resources;
        for (String p : path) {
            currentNode = currentNode.getJSONObject(p);
            if (currentNode == null) {;
                return null;
            }
        }
        return currentNode.optString(caseType, null);
    }
}
