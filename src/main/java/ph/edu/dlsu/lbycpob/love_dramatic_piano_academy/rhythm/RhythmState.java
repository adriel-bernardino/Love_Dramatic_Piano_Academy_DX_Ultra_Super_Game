package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.rhythm;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.time.TimerAction;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core.CoreSceneManager;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core.GlobalAudioManager;

import java.util.List;

public class RhythmState {

    private final CoreSceneManager sceneManager;
    private final GlobalAudioManager audioManager;

    private Text subText;
    private Button skipBtn;
    private TimerAction autoReturnTimer;
    private int storedResumeLine;

    private PianoKeyOverlay pianoKeyOverlay;
    private KeyboardMapping keyboardMapping;
    private BeatMapParser beatMapParser;


    public RhythmState(CoreSceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.audioManager = GlobalAudioManager.getInstance();
    }

    public void start(List<String> dialogueLines, int resumeLineIndex, String songTrack) {
        FXGL.spawn("background", new SpawnData(0, 0).put("imageName", "Rhythmbgs/rhythmSolo.png"));
        this.storedResumeLine = resumeLineIndex;

        pianoKeyOverlay = new PianoKeyOverlay();
        keyboardMapping = new KeyboardMapping();

        beatMapParser = new BeatMapParser();
        beatMapParser.loadBeatMap("beatmap_part2");
        List<BeatMapParser.NoteData> notes = beatMapParser.getNoteData();

        audioManager.playMusic(songTrack);

        String contextText = dialogueLines.isEmpty() ? "" : dialogueLines.get(0);
        subText = FXGL.getUIFactoryService().newText(contextText, 24);
        subText.setFill(Color.LIGHTGRAY);
        subText.setTranslateX(100);
        subText.setTranslateY(FXGL.getAppHeight() - 300);

        // Position the skip button at the top right
        skipBtn = FXGL.getUIFactoryService().newButton("Skip Song");
        skipBtn.setOnAction(e -> finishDummyRhythm());
        skipBtn.setTextFill(Color.LIGHTGRAY);
        skipBtn.setStyle("-fx-background-color: rgb(104 100 100);");
        skipBtn.setTranslateX(FXGL.getAppWidth() / 2);
        skipBtn.setTranslateY(50);

        FXGL.addUINode(subText);
        FXGL.addUINode(skipBtn);

        autoReturnTimer = FXGL.getGameTimer().runOnceAfter(this::finishDummyRhythm, Duration.seconds(10));
    }

    private void finishDummyRhythm() {
        if (autoReturnTimer != null) {
            autoReturnTimer.expire();
            autoReturnTimer = null;
        }
        sceneManager.switchToVisualNovelAtLine(storedResumeLine);
    }

    public void cleanup() {
        if (subText != null) {
            FXGL.removeUINode(subText);
            subText = null;
        }
        if (skipBtn != null) {
            FXGL.removeUINode(skipBtn);
            skipBtn = null;
        }
        if (autoReturnTimer != null) {
            autoReturnTimer.expire();
            autoReturnTimer = null;
        }
        if (pianoKeyOverlay != null) {
            pianoKeyOverlay.cleanup();
            pianoKeyOverlay = null;
        }
    }
}