package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.rhythm;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.time.TimerAction;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core.CoreSceneManager;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core.GlobalAudioManager;

import java.util.List;

// Understand: A lightweight dummy rhythm state that plays the requested track and seamlessly returns to the VN
public class RhythmState {

    private final CoreSceneManager sceneManager;
    private final GlobalAudioManager audioManager;

    private VBox uiBox;
    private TimerAction autoReturnTimer;
    private int storedResumeLine;

    public RhythmState(CoreSceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.audioManager = GlobalAudioManager.getInstance();
    }

    public void start(List<String> dialogueLines, int resumeLineIndex, String songTrack) {
        FXGL.spawn("background", new SpawnData(0, 0).put("imageName", "Rhythmbgs/rhythmSolo.png"));
        this.storedResumeLine = resumeLineIndex;



        // Understand: Play the dynamic track passed by the chapter logic (e.g., becauseIntro.MP3 or becauseWithRoni.MP3)
        audioManager.playMusic(songTrack);



        String contextText = dialogueLines.isEmpty() ? "" : dialogueLines.get(0);
        Text subText = FXGL.getUIFactoryService().newText(contextText, 24);
        subText.setFill(Color.LIGHTGRAY);

        Button skipBtn = FXGL.getUIFactoryService().newButton("Skip Song");
        skipBtn.setOnAction(e -> finishDummyRhythm());
        skipBtn.setTextFill(Color.LIGHTGRAY);
        skipBtn.setStyle("-fx-background-color: rgb(104 100 100);");

        uiBox = new VBox(20, subText, skipBtn);
        uiBox.setTranslateX(FXGL.getAppWidth() / 2.0 - 250);
        uiBox.setTranslateY(FXGL.getAppHeight() / 2.0 - 100);

        FXGL.addUINode(uiBox);

        // Understand: Simulates the song finishing automatically by using an FXGL timer.
        // Set to 10 seconds for the dummy test, but you can adjust this.
        autoReturnTimer = FXGL.getGameTimer().runOnceAfter(this::finishDummyRhythm, Duration.seconds(10));
    }

    private void finishDummyRhythm() {
        // Understand: Clean up the timer immediately if the player manually clicked skip
        if (autoReturnTimer != null) {
            autoReturnTimer.expire();
            autoReturnTimer = null;
        }

        // Understand: Call back to the CoreSceneManager to resume the VN state at the stored checkpoint line
        sceneManager.switchToVisualNovelAtLine(storedResumeLine);
    }

    public void cleanup() {
        if (uiBox != null) {
            FXGL.removeUINode(uiBox);
            uiBox = null;
        }
        if (autoReturnTimer != null) {
            autoReturnTimer.expire();
            autoReturnTimer = null;
        }
    }
}