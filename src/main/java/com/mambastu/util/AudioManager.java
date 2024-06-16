package com.mambastu.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.json.JSONObject;
import org.json.JSONTokener;

public class AudioManager {
    private static final AudioManager INSTANCE = new AudioManager();
    private Map<String, Map<String, Map<String, String>>> musicResources;
    private Map<String, Clip> musicCache;

    private AudioManager() {
        this.musicResources = new HashMap<>();
        this.musicCache = new HashMap<>();
    }

    public static AudioManager getInstance() {
        return INSTANCE;
    }

    public void loadResources() {
        String jsonUrl = "AudioResources.json";
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(jsonUrl)) {
            if (inputStream != null) {
                String jsonText = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                JSONObject jsonObject = new JSONObject(new JSONTokener(jsonText));
                parseJSON(jsonObject);
                System.out.println("Music resources loaded successfully.");
            } else {
                System.out.println("Unable to load " + jsonUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseJSON(JSONObject jsonObject) {
        for (String category : jsonObject.keySet()) {
            JSONObject categoryObj = jsonObject.getJSONObject(category);
            Map<String, Map<String, String>> categoryMap = new HashMap<>();
            for (String name : categoryObj.keySet()) {
                JSONObject nameObj = categoryObj.getJSONObject(name);
                Map<String, String> nameMap = new HashMap<>();
                for (String key : nameObj.keySet()) {
                    nameMap.put(key, nameObj.getString(key));
                }
                categoryMap.put(name, nameMap);
            }
            musicResources.put(category, categoryMap);
        }
    }

    public String getMusicPath(String category, String name, String key) {
        if (musicResources.containsKey(category)) {
            Map<String, Map<String, String>> categoryMap = musicResources.get(category);
            if (categoryMap.containsKey(name)) {
                Map<String, String> nameMap = categoryMap.get(name);
                if (nameMap.containsKey(key)) {
                    return nameMap.get(key);
                }
            }
        }
        System.out.println("Music not found for key: " + key + ", category: " + category + ", name: " + name);
        return null;
    }

    public void loadMusic(String category, String name, String key) {
        String path = getMusicPath(category, name, key);
        if (path != null && !musicCache.containsKey(path)) {
            try {
                URL url = getClass().getResource(path);
                if (url != null) {
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    musicCache.put(path, clip);
                    System.out.println("Loaded music: " + path);
                } else {
                    System.out.println("Unable to load music from " + path);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void playMusic(String category, String name, String key) {
        String path = getMusicPath(category, name, key);
        if (path != null && musicCache.containsKey(path)) {
            Clip clip = musicCache.get(path);
            if (clip != null) {
                clip.start();
                System.out.println("Playing music: " + path);
            }
        } else {
            System.out.println("Music not found in cache: " + path);
        }
    }

    public void loopMusic(String category, String name, String key) {
        String path = getMusicPath(category, name, key);
        if (path != null && musicCache.containsKey(path)) {
            Clip clip = musicCache.get(path);
            if (clip != null) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                System.out.println("Looping music: " + path);
            }
        } else {
            System.out.println("Music not found in cache: " + path);
        }
    }

    public void stopMusic(String category, String name, String key) {
        String path = getMusicPath(category, name, key);
        if (path != null && musicCache.containsKey(path)) {
            Clip clip = musicCache.get(path);
            if (clip != null) {
                clip.stop();
                System.out.println("Stopped music: " + path);
            }
        } else {
            System.out.println("Music not found in cache: " + path);
        }
    }

    public void clearCache() {
        for (Clip clip : musicCache.values()) {
            clip.close();
        }
        musicCache.clear();
        System.out.println("Music cache cleared.");
    }
}
