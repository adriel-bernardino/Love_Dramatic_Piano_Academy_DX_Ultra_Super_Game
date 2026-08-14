package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import javafx.scene.text.Text;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.rhythm.RhythmState;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.vn.VNState;

public class LoveDramaticApp extends GameApplication {

    // Understand: Sets up all
    public enum GameState { MAIN_MENU, VISUAL_NOVEL, RHYTHM_GAME }
    private GameState currentState = GameState.MAIN_MENU;

    // Understand: various variables that will be needed throughout the game
    private int storyChapter = 0;
    private char route = 'A';
    private boolean isPaused = false;

    private MainMenuState mainMenu;
    private VNState vnState;
    private RhythmState rhythmState;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1920);
        settings.setHeight(1080);
        settings.setTitle("Love Dramatic Piano Academy");

    }

    @Override
    protected void initGame() {
        mainMenu = new MainMenuState();
        vnState = new VNState();
        rhythmState = new RhythmState();




    }
}
