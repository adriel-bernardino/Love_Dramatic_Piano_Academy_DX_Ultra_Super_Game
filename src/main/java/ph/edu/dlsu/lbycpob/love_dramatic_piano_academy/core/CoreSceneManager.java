package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.rhythm.RhythmState;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.shared.SceneController;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.vn.VNState;

import java.util.List;

// Understand: Central state manager bridging menus, linear chapters, rhythm games, and saving/loading
public class CoreSceneManager implements SceneController {

    private final MainMenuState mainMenu;
    private final VNState vnState;
    private final RhythmState rhythmState;

    private final SaveMenuState saveMenuState;
    private final LoadMenuState loadMenuState;

    private LoveDramaticApp.GameState currentState = LoveDramaticApp.GameState.MAIN_MENU;

    private int activeChapter = 0;
    private char activeRoute = 'A';

    public CoreSceneManager() {
        this.mainMenu = new MainMenuState(this);
        this.vnState = new VNState(this);
        this.rhythmState = new RhythmState(this);
        this.saveMenuState = new SaveMenuState(this);
        this.loadMenuState = new LoadMenuState(this);
    }

    public void startInitialState() {
        GlobalAudioManager.getInstance().playMainMenuTheme();
        mainMenu.start();
    }

    private void cleanupCurrentState() {
        switch (currentState) {
            case MAIN_MENU:
                mainMenu.cleanup();
                break;
            case VISUAL_NOVEL:
                vnState.cleanup();
                break;
            case RHYTHM_GAME:
                rhythmState.cleanup();
                break;
            case SAVE_MENU:
                saveMenuState.cleanup();
                break;
            case LOAD_MENU:
                loadMenuState.cleanup();
                break;
        }

        // Understand: The nuclear option. Wipes EVERY entity (backgrounds, overlays, notes) from the game world.
        // Uses getEntitiesCopy() to prevent ConcurrentModificationException while destroying them.
        FXGL.getGameWorld().getEntitiesCopy().forEach(Entity::removeFromWorld);
    }

    public void switchToMainMenu() {
        cleanupCurrentState();
        currentState = LoveDramaticApp.GameState.MAIN_MENU;
        GlobalAudioManager.getInstance().playMainMenuTheme();
        mainMenu.start();
    }

    @Override
    public void switchToVisualNovel() {
        switchToVisualNovelFull(0, 'A', 0);
    }

    public void switchToVisualNovelFull(int chapter, char route, int lineIndex) {
        cleanupCurrentState();
        // Understand: Explicitly stop any menu music when entering the VN state
        GlobalAudioManager.getInstance().stopMusic();
        this.activeChapter = chapter;
        this.activeRoute = route;
        this.currentState = LoveDramaticApp.GameState.VISUAL_NOVEL;
        vnState.start(chapter, route, lineIndex);
    }

    public void switchToVisualNovelAtLine(int lineIndex) {
        cleanupCurrentState();
        // Understand: Explicitly stop any menu music when entering the VN state
        GlobalAudioManager.getInstance().stopMusic();
        this.currentState = LoveDramaticApp.GameState.VISUAL_NOVEL;
        vnState.start(activeChapter, activeRoute, lineIndex);
    }

    @Override
    public void switchToRhythmGame() {
        switchToRhythmGame(List.of("Playing piano..."), 0, "becauseIntro.MP3");
    }

    public void switchToRhythmGame(List<String> dialogueLines, int resumeLineIndex, String songTrack) {
        cleanupCurrentState();
        currentState = LoveDramaticApp.GameState.RHYTHM_GAME;
        rhythmState.start(dialogueLines, resumeLineIndex, songTrack);
    }

    public void switchToSaveMenu(int chapterId, char route, int checkpointLine) {
        cleanupCurrentState();
        // Understand: Play main menu theme instead of stopping audio; GlobalAudioManager ensures it won't restart if already playing
        GlobalAudioManager.getInstance().playMainMenuTheme();
        currentState = LoveDramaticApp.GameState.SAVE_MENU;
        saveMenuState.start(chapterId, route, checkpointLine);
    }

    public void switchToLoadMenu() {
        cleanupCurrentState();
        // Understand: Play main menu theme instead of stopping audio; GlobalAudioManager ensures it won't restart if already playing
        GlobalAudioManager.getInstance().playMainMenuTheme();
        currentState = LoveDramaticApp.GameState.LOAD_MENU;
        loadMenuState.start();
    }

    public void resumeVisualNovel(int chapterId, char route, int checkpointLine) {
        cleanupCurrentState();
        // Understand: Explicitly stop menu music when returning to the VN state from a menu
        GlobalAudioManager.getInstance().stopMusic();
        this.activeChapter = chapterId;
        this.activeRoute = route;
        this.currentState = LoveDramaticApp.GameState.VISUAL_NOVEL;
        vnState.start(chapterId, route, checkpointLine);
    }

    public int getActiveChapter() { return activeChapter; }
    public char getActiveRoute() { return activeRoute; }
}