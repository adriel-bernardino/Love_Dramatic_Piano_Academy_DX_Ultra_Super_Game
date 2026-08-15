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
        this.activeChapter = chapter;
        this.activeRoute = route;
        this.currentState = LoveDramaticApp.GameState.VISUAL_NOVEL;
        vnState.start(chapter, route, lineIndex);
    }

    public void switchToVisualNovelAtLine(int lineIndex) {
        cleanupCurrentState();
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

    // Understand: Now cleans up the VN state and stops music so the Save screen is totally isolated
    public void switchToSaveMenu(int chapterId, char route, int checkpointLine) {
        cleanupCurrentState();
        GlobalAudioManager.getInstance().stopMusic();
        currentState = LoveDramaticApp.GameState.SAVE_MENU;
        saveMenuState.start(chapterId, route, checkpointLine);
    }

    public void switchToLoadMenu() {
        cleanupCurrentState();
        GlobalAudioManager.getInstance().stopMusic();
        currentState = LoveDramaticApp.GameState.LOAD_MENU;
        loadMenuState.start();
    }

    // Understand: Because we destroyed the VN state to open the Save Menu cleanly, we must rebuild it from the checkpoint.
    public void resumeVisualNovel(int chapterId, char route, int checkpointLine) {
        cleanupCurrentState();
        this.activeChapter = chapterId;
        this.activeRoute = route;
        this.currentState = LoveDramaticApp.GameState.VISUAL_NOVEL;
        vnState.start(chapterId, route, checkpointLine);
    }

    public int getActiveChapter() { return activeChapter; }
    public char getActiveRoute() { return activeRoute; }
}