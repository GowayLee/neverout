package com.mambastu;

import com.mambastu.material.resource.ImgCache;
import com.mambastu.util.AudioManager;
import javafx.application.Application;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

import com.mambastu.controller.input.InputManager;
import com.mambastu.controller.level.LevelController;
import com.mambastu.material.resource.ResourceManager;
import com.mambastu.util.PropStore;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {

        StackPane root = new StackPane();
        scene = new Scene(root, 1920, 1080);
        // 绑定StackPane的尺寸到场景的尺寸
        root.prefWidthProperty().bind(scene.widthProperty());
        root.prefHeightProperty().bind(scene.heightProperty());

        ResourceManager.getInstance().loadResources(); // 初始化资源管理器，载入JSON
        PropStore.getInstance().loadResources(); // 初始化道具存储器，载入JSON
        audioManagerLoad();//初始化音频播放器
        InputManager.init(scene); // 初始化输入管理器

        Image cursorImage = ResourceManager.getInstance().getImg("cursorImage", "System", "MainMenu"); // 载入光标图片，并设置为鼠标光标
        ImageCursor customCursor = new ImageCursor(cursorImage, 4, 8);
        scene.setCursor(customCursor);

        LevelController controller = new LevelController(root);
        controller.showMainMenu();

        stage.setScene(scene);
        stage.setTitle("Never Out");
        stage.getIcons().add(ImgCache.getImage("/static/image/char/LaughPlayer.png"));
        stage.show();
    }

    private void audioManagerLoad() {
        AudioManager.getInstance().loadResources();
        AudioManager.getInstance().loadAudio("BackgroundMusic", "BattleTheme1","displayAudio");
        AudioManager.getInstance().loadAudio("BackgroundMusic", "BattleTheme2","displayAudio");
        AudioManager.getInstance().loadAudio("BackgroundMusic", "BattleTheme3","displayAudio");
        AudioManager.getInstance().loadAudio("BackgroundMusic", "PassLevel","displayAudio");
        AudioManager.getInstance().loadAudio("BackgroundMusic", "ReadyFight","displayAudio");
        AudioManager.getInstance().loadAudio("BackgroundMusic", "GameOver","displayAudio");

        AudioManager.getInstance().loadAudio("SoundEffects", "FireNormal","displayAudio");
        AudioManager.getInstance().loadAudio("SoundEffects", "GunReloading","displayAudio");
        AudioManager.getInstance().loadAudio("SoundEffects", "HitMonster","displayAudio");
        AudioManager.getInstance().loadAudio("SoundEffects", "SkillJokerDown","displayAudio");
        AudioManager.getInstance().loadAudio("SoundEffects", "SkillJokerUp","displayAudio");
        AudioManager.getInstance().loadAudio("SoundEffects", "SkillJokerUp","displayAudio");
        AudioManager.getInstance().loadAudio("SoundEffects", "SkillLaugh","displayAudio");
    }

    public static void main(String[] args) {
        launch();
    }
}


