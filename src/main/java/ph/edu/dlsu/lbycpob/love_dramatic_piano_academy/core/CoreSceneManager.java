package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core;

import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.rhythm.RhythmState;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.shared.SceneController;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.vn.VNState;

import java.util.List;

public class CoreSceneManager implements SceneController {

    private final MainMenuState mainMenu;
    private final VNState vnState;
    private final RhythmState rhythmState;

    private LoveDramaticApp.GameState currentState = LoveDramaticApp.GameState.MAIN_MENU;

    public CoreSceneManager() {
        this.mainMenu = new MainMenuState(this);
        this.vnState = new VNState(this);
        this.rhythmState = new RhythmState(this);
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
        }
    }

    public void switchToMainMenu() {
        cleanupCurrentState();
        currentState = LoveDramaticApp.GameState.MAIN_MENU;
        GlobalAudioManager.getInstance().playMainMenuTheme();
        mainMenu.start();
    }

    @Override
    public void switchToVisualNovel() {
        switchToVisualNovelAtLine(0);
    }

    public void switchToVisualNovelAtLine(int lineIndex) {
        cleanupCurrentState();
        currentState = LoveDramaticApp.GameState.VISUAL_NOVEL;
        vnState.start(lineIndex);
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
}