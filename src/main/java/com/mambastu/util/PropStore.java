package com.mambastu.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.mambastu.material.pojo.prop.BaseProp;

public class PropStore {
    private static final PropStore INSTANCE = new PropStore();

    private JSONObject propResources; // 存放资源JSON对象
    private List<BaseProp> propList; // 存放所有道具

    private PropStore() {
        this.propList = new ArrayList<>();
    }

    public static PropStore getInstance() { return INSTANCE; }

    public BaseProp getRandProp() {
        int size = propList.size();
        int randIndex = (int) (Math.random() * size); // 随机索引值
        return propList.get(randIndex); // 返回随机道具对象
    }

    public void loadResources() { // 加载资源文件
        String JsonUrl = "PropConfig.json";
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(JsonUrl)) {
            if (inputStream != null) { 
                String jsonText = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                propResources = new JSONObject(new JSONTokener(jsonText));
                loadProps();
            } else {
                System.out.println("Unable to load " + JsonUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadProps() {
        propList = new ArrayList<>();
        JSONArray propArray = propResources.getJSONArray("Props");
        for (int i = 0; i < propArray.length(); i++) {
            JSONObject propJsonObj = propArray.getJSONObject(i);
            String name = propJsonObj.getString("name");
            String description = propJsonObj.getString("description");
            Integer price = Integer.parseInt(propJsonObj.getString("price"));
            Double buffValue = Double.parseDouble(propJsonObj.getString("buffValue"));
            BaseProp prop = getPropObj(name, description, price, buffValue);
            if (prop != null) {
                propList.add(prop);
            }
        }
    }

    private BaseProp getPropObj(String propName, String description, Integer price, Double buffValue) {
        String className = "com.mambastu.material.pojo.prop." + propName;
        try {
            BaseProp prop = (BaseProp) Class.forName(className).getDeclaredConstructor().newInstance();
            prop.loadValues(description, buffValue, price);
            return prop;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
