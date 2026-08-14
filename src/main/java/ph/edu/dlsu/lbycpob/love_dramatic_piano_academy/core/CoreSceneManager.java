package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core;

import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.rhythm.RhythmState;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.shared.SceneController;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.vn.VNState;

// Understand: A separate controller class dedicated to managing state transitions
public class CoreSceneManager implements SceneController {

    private final MainMenuState mainMenu;
    private final VNState vnState;
    private final RhythmState rhythmState;

    private LoveDramaticApp.GameState currentState = LoveDramaticApp.GameState.MAIN_MENU;

    public CoreSceneManager() {
        // Understand: Instantiate the states here and pass this controller to them
        this.mainMenu = new MainMenuState(this);
        this.vnState = new VNState();
        this.rhythmState = new RhythmState();
    }

    // Understand: Called by the main app to boot up the initial state
    public void startInitialState() {
        mainMenu.start();
    }

    // Understand: Cleans up the active state before moving to the next
    private void cleanupCurrentState() {
        switch (currentState) {
            case MAIN_MENU:
                mainMenu.cleanup();
                break;
            case VISUAL_NOVEL:
                // vnState.cleanup(); // Uncomment when cleanup is added to VNState
                break;
            case RHYTHM_GAME:
                // rhythmState.cleanup(); // Uncomment when cleanup is added to RhythmState
                break;
        }
    }

    @Override
    public void switchToVisualNovel() {
        cleanupCurrentState();
        currentState = LoveDramaticApp.GameState.VISUAL_NOVEL;
        System.out.println("Switching to Visual Novel State...");

        // Hardcoding to start at line 0 for the sake of the dummy test.
        vnState.start(0);
    }

    @Override
    public void switchToRhythmGame() {
        cleanupCurrentState();
        currentState = LoveDramaticApp.GameState.RHYTHM_GAME;
        System.out.println("Switching to Rhythm Game State...");
        // rhythmState.start(); // Uncomment when start is added to RhythmState
    }
}