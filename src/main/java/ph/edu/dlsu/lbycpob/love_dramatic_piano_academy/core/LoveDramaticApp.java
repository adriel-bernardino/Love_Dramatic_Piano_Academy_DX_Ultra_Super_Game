package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;

public class LoveDramaticApp extends GameApplication {

    // Understand: Keep the enum here so it can be referenced globally if needed
    public enum GameState { MAIN_MENU, VISUAL_NOVEL, RHYTHM_GAME, SAVE_MENU, LOAD_MENU }

    // Understand: various variables that will be needed throughout the game
    private int storyChapter = 0;
    private char route = 'A';
    private boolean isPaused = false;

    // Understand: The centralized scene manager replaces individual state variables
    private CoreSceneManager sceneManager;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1920);
        settings.setHeight(1080);
        settings.setTitle("Love Dramatic Piano Academy");
    }

    @Override
    protected void initGame() {
        // Understand: Register entity factories centrally
        FXGL.getGameWorld().addEntityFactory(new BackgroundManager());

        // Understand: Initialize the manager, which in turn initializes the states
        sceneManager = new CoreSceneManager();

        // Understand: Boot the game into the main menu
        sceneManager.startInitialState();
    }

    public static void main(String[] args) {
        launch(args);
    }
}