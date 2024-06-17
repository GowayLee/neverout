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
import javax.sound.sampled.FloatControl;

import org.json.JSONObject;
import org.json.JSONTokener;

public class AudioManager {
    private static final AudioManager INSTANCE = new AudioManager();
    private Map<String, Map<String, Map<String, String>>> audioResources;
    private Map<String, Long> lastPlayTimes;
    private Map<String, Clip> audioCache;

    private AudioManager() {
        this.audioResources = new HashMap<>();
        this.audioCache = new HashMap<>();
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
                System.out.println("Audio resources loaded successfully.");
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
            audioResources.put(category, categoryMap);
        }
    }

    public String getAudioPath(String category, String name, String key) {
        if (audioResources.containsKey(category)) {
            Map<String, Map<String, String>> categoryMap = audioResources.get(category);
            if (categoryMap.containsKey(name)) {
                Map<String, String> nameMap = categoryMap.get(name);
                if (nameMap.containsKey(key)) {
                    return nameMap.get(key);
                }
            }
        }
        System.out.println("Audio not found for key: " + key + ", category: " + category + ", name: " + name);
        return null;
    }

    public void loadAudio(String category, String name, String key) {
        String path = getAudioPath(category, name, key);
        if (path != null && !audioCache.containsKey(path)) {
            try {
                URL url = getClass().getResource(path);
                if (url != null) {
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    audioCache.put(path, clip);
                    System.out.println("Loaded audio: " + path);
                } else {
                    System.out.println("Unable to load audio from " + path);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isAudioPlaying(String category, String name, String key) {
        String path = getAudioPath(category, name, key);
        if (path != null && audioCache.containsKey(path)) {
            Clip clip = audioCache.get(path);
            return clip != null && clip.isRunning();
        }
        return false;
    }

    public void playAudio(String category, String name, String key) {
        String path = getAudioPath(category, name, key);
        if (path != null && audioCache.containsKey(path)) {
            Clip clip = audioCache.get(path);
            if (clip != null) {
                clip.stop();
                clip.setFramePosition(0); // Reset the clip to the beginning
                clip.start();
                System.out.println("Playing audio: " + path);
            }
        } else {
            System.out.println("Audio not found in cache: " + path);
        }
    }

    public void playAudio(String category, String name, String key, int cooldown) {
        String path = getAudioPath(category, name, key);
        if (path != null) {
            long currentTime = System.currentTimeMillis();
            long lastPlayTime = lastPlayTimes.getOrDefault(path, 0L);
            if (currentTime - lastPlayTime >= cooldown * 1000) {
                if (audioCache.containsKey(path)) {
                    Clip clip = audioCache.get(path);
                    if (clip != null) {
                        clip.stop(); // Stop the clip if it is currently playing
                        clip.setFramePosition(0); // Reset the clip to the beginning
                        clip.start(); // Start playing the clip
                        lastPlayTimes.put(path, currentTime);
                        System.out.println("Playing audio: " + path);
                    }
                } else {
                    System.out.println("Audio not found in cache: " + path);
                }
            } else {
                System.out.println("Audio is on cooldown: " + path);
            }
        } else {
            System.out.println("Audio not found for key: " + key + ", category: " + category + ", name: " + name);
        }
    }

    public void loopAudio(String category, String name, String key) {
        String path = getAudioPath(category, name, key);
        if (path != null && audioCache.containsKey(path)) {
            Clip clip = audioCache.get(path);
            if (clip != null) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                System.out.println("Looping audio: " + path);
            }
        } else {
            System.out.println("Audio not found in cache: " + path);
        }
    }

    public void stopAudio(String category, String name, String key) {
        String path = getAudioPath(category, name, key);
        if (path != null && audioCache.containsKey(path)) {
            Clip clip = audioCache.get(path);
            if (clip != null) {
                clip.stop();
                System.out.println("Stopped audio: " + path);
            }
        } else {
            System.out.println("Audio not found in cache: " + path);
        }
    }

    public void clearCache() {
        for (Clip clip : audioCache.values()) {
            clip.close();
        }
        audioCache.clear();
        System.out.println("Audio cache cleared.");
    }

    public void setVolume(String category, String name, String key, float volume) {
        String path = getAudioPath(category, name, key);
        if (path != null && audioCache.containsKey(path)) {
            Clip clip = audioCache.get(path);
            if (clip != null) {
                if (volume < 0.0f || volume > 1.0f) {
                    throw new IllegalArgumentException("Volume not valid: " + volume);
                }
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
                gainControl.setValue(dB);
                System.out.println("Volume set to: " + volume);
            }
        } else {
            System.out.println("Audio not found in cache: " + path);
        }
    }
}
