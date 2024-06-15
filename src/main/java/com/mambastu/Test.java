package com.mambastu;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.mambastu.material.pojo.prop.BaseProp;
import com.mambastu.material.resource.ResourceManager;

public class Test {
    private List<BaseProp> propList; // 存放所有道具
    private JSONObject propResources; // 存放图片资源的JSON对象

    public static void main(String[] args) throws Exception {
        ResourceManager.getInstance().loadResources(); // 初始化资源管理器，载入JSON
        Test t = new Test();
        t.loadResources(); // 加载道具资源
    }

    public void loadResources() {
        String JsonUrl = "PropConfig.json";
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(JsonUrl)) {
            if (inputStream != null) { 
                propResources = new JSONObject(new JSONTokener(inputStream));
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
            Integer price = Integer.parseInt(propJsonObj.getString("price"));
            Double buffValue = Double.parseDouble(propJsonObj.getString("buffValue"));
            BaseProp prop = getPropObj(name, price, buffValue);
            if (prop != null) {
                System.out.println(prop.toString());
                propList.add(prop);
            }
        }
    }

    private BaseProp getPropObj(String propName, Integer price, Double buffValue) {
        String className = "com.mambastu.material.pojo.prop." + propName;
        try {
            BaseProp prop = (BaseProp) Class.forName(className).getDeclaredConstructor().newInstance();
            prop.loadValues(buffValue, price);
            return prop;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
