package com.mambastu.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

public class AudioManager {
    private static final Logger logger = LogManager.getLogger(AudioManager.class);

    private static final AudioManager INSTANCE = new AudioManager();

    private static final int THREAD_POOL_SIZE = 5; // 线程池的大小
    private static final int CLIP_POOL_SIZE = 3; // Clip冗余池的大小
    private final ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    private final Map<String, Map<String, Map<String, String>>> audioResources;
    private final Map<String, Clip> audioCache;
    private final Map<String, List<Clip>> effectCache;

    private boolean readyForEnlarge = true; // 是否可以扩容(线程安全)
    private Map<String, Long> lastPlayTimes;

    public static AudioManager getInstance() {
        return INSTANCE;
    }

    private AudioManager() {
        this.audioResources = new HashMap<>();
        this.audioCache = new HashMap<>();
        this.effectCache = new HashMap<>();
    }

    public void loadResources() {
        String jsonUrl = "AudioResources.json";
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(jsonUrl)) {
            if (inputStream != null) {
                String jsonText = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                JSONObject jsonObject = new JSONObject(new JSONTokener(jsonText));
                parseJSON(jsonObject);
                logger.info("Audio resources loaded successfully");
            } else {
                logger.error("Unable to load " + jsonUrl);
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
                    // 音效素材储存在多冗余池中
                    if (category.equals("SoundEffects"))
                        loadSoundEffects(nameObj.getString(key));

                }
                categoryMap.put(name, nameMap);
            }
            audioResources.put(category, categoryMap);
        }
    }

    private String getAudioPath(String category, String name, String key) {
        if (audioResources.containsKey(category)) {
            Map<String, Map<String, String>> categoryMap = audioResources.get(category);
            if (categoryMap.containsKey(name)) {
                Map<String, String> nameMap = categoryMap.get(name);
                if (nameMap.containsKey(key)) {
                    return nameMap.get(key);
                }
            }
        }
        logger.error("In Method getAudioPath: Audio not found for key: " + key + ", category: " + category + ", name: "
                + name);
        return null;
    }

    private void loadSoundEffects(String path) {
        try {
            URL url = getClass().getResource(path);
            if (url != null) {
                List<Clip> clips = new ArrayList<>();
                for (int i = 0; i < CLIP_POOL_SIZE; i++) { // 创建冗余池
                    try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url)) {
                        Clip newClip = AudioSystem.getClip();
                        newClip.open(audioInputStream);
                        clips.add(newClip);
                    }
                }
                effectCache.put(path, clips);
                logger.info("Loaded sound effect: " + path);
            } else {
                logger.error("Unable to load audio from " + path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enlargeEffectCache(String path) { // 扩充音效冗余池
        List<Clip> clips = effectCache.get(path);
        try {
            URL url = getClass().getResource(path);
            if (url != null) {
                int size = clips.size();
                for (int i = 0; i < size; i++) { // 扩容1倍
                    try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url)) {
                        Clip newClip = AudioSystem.getClip();
                        newClip.open(audioInputStream);
                        clips.add(newClip);
                    }
                }
                logger.info("Enlarge the pool of sound effect: " + path);
            } else {
                logger.error("Unable to load audio from " + path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        readyForEnlarge = true;
    }

    private void loadAudio(String path) {
        try {
            URL url = getClass().getResource(path);
            if (url != null) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                audioCache.put(path, clip);
                logger.info("Loaded audio: " + path);
            } else {
                logger.error("Unable to load audio from " + path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Clip getClip(String category, String name, String key) {
        String path = getAudioPath(category, name, key);
        if (path == null)
            return null;
        if (category == "SoundEffects") { // 如果需要的是音效，则从音效冗余池缓存中获取
            if (!effectCache.containsKey(path)) { // 如果音效未在缓存中，则加载该音效
                loadSoundEffects(path);
            }
            List<Clip> clips = effectCache.get(path); // 获取音效冗余池
            int i = 0;
            for (Clip clip : clips) { // 查找一个未在使用的音效
                if (!clip.isRunning()) { // 如果音效未在播放，则返回该音效
                    return clip;
                }
                i++;
            }
            if ((double) i / clips.size() > 0.7 && readyForEnlarge) {
                readyForEnlarge = false;
                enlargeEffectCache(path);
            }
            return clips.get(0); // 如果没有未使用的音效，则返回第一个音效，并等待其他音效空闲
        } else {
            if (!audioCache.containsKey(path)) { // 如果音频未在缓存中，则加载该音频
                loadAudio(path);
            }
            return audioCache.get(path);
        }
    }

    /**
     * 检查指定音频是否正在播放
     * 
     * @param category
     * @param name
     * @param key
     * @return boolean
     */
    public boolean isAudioPlaying(String category, String name, String key) {
        Clip clip = getClip(category, name, key);
        return clip != null && clip.isRunning();
    }

    /**
     * 播放某一首音乐，如果音乐不存在，则不播放
     * 
     * @param category
     * @param name
     * @param key
     */
    public void playAudio(String category, String name, String key) {
        executor.submit(() -> {
            Clip clip = getClip(category, name, key);
            if (clip != null) {
                if (category == "SoundEffects") {
                    clip.setFramePosition(0); // 重置到起始位置
                    clip.start();
                } else {
                    if (clip.isRunning()) {
                        clip.stop();
                    }
                    clip.setFramePosition(0); // 重置到起始位置
                    clip.start();
                }
                // logger.info("Playing audio: /" + category + "/" + name + "/" + key);
            }
        });

    }

    /**
     * 指定冷却时间，播放指定音乐
     * 
     * @param category
     * @param name
     * @param key
     * @param cooldown
     */
    public void playAudio(String category, String name, String key, int cooldown) {
        String path = getAudioPath(category, name, key);
        if (path != null) {
            long currentTime = System.currentTimeMillis();
            long lastPlayTime = lastPlayTimes.getOrDefault(path, 0L);
            if (currentTime - lastPlayTime >= cooldown * 1000) {
                Clip clip = getClip(category, name, key);
                if (clip != null) {
                    clip.stop(); // 结束正在播放的
                    clip.setFramePosition(0);
                    clip.start();
                    lastPlayTimes.put(path, currentTime);
                    logger.info("Playing audio: " + path);
                }
            } else {
                logger.info("Audio is on cooldown: " + path);
            }
        } else {
            logger.error("In Method playAudio: Audio not found for key: " + key + ", category: " + category + ", name: "
                    + name);
        }
    }

    public void loopAudio(String category, String name, String key) {
        Clip clip = getClip(category, name, key);
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            logger.info("Looping audio: /" + category + "/" + name + "/" + key);
        }
    }

    public void stopAudio(String category, String name, String key) {
        Clip clip = getClip(category, name, key);
        if (clip != null) {
            clip.stop();
            logger.info("Stopped audio: /" + category + "/" + name + "/" + key);
        }
    }

    public void setVolume(String category, String name, String key, float volume) {
        Clip clip = getClip(category, name, key);
        if (clip != null) {
            if (volume < 0.0f || volume > 1.0f) {
                throw new IllegalArgumentException("Volume not valid: " + volume);
            }
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
            logger.info("Volume set to: " + volume + " For /" + category + "/" + name + "/" + key);
        }
    }

    public void clearCache() {
        for (Clip clip : audioCache.values()) {
            clip.close();
        }
        audioCache.clear();
        logger.info("Audio cache cleared");
    }
}
