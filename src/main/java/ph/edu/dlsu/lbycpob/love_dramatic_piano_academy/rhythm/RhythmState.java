package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.rhythm;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.time.TimerAction;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core.CoreSceneManager;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core.GlobalAudioManager;

import java.util.ArrayList;
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

    private final List<NoteComponent> noteComponents = new ArrayList<>();
    private  List<BeatMapParser.NoteData> beatmapNotes;
    private AnimationTimer noteMovement;
    private long startTime;
    private int nextBeatmapIndex = 0;

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
        beatmapNotes = beatMapParser.getNoteData();
        nextBeatmapIndex = 0;
        audioManager.playMusic(songTrack);
        startTime  = System.nanoTime();
        noteMovement = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double currentTime = getCurrentTime();
                if (nextBeatmapIndex<beatmapNotes.size()){
                    BeatMapParser.NoteData data = beatmapNotes.get(nextBeatmapIndex);
                    double travelTime = NoteComponent.defaultTravelTime;
                    double spawnTime = data.getTimestamp() - travelTime;
                if (currentTime >= spawnTime) {
                    for (String note:data.getNotes()){
                        NoteComponent noteComponent = new NoteComponent(data.getTimestamp(),note);
                        noteComponents.add(noteComponent);
                        FXGL.addUINode(noteComponent.getVisualNote());
                    }
                    nextBeatmapIndex++;
                }
            }
                for (NoteComponent note: noteComponents){
                    double travelTime = NoteComponent.defaultTravelTime;
                    double spawnTime = note.getTimestamp() - travelTime;
                    double progress = (currentTime-spawnTime)/travelTime;
                    progress = Math.max(0,Math.min(1,progress));
                    double startY = NoteComponent.spawnY;
                    double targetY = NoteComponent.targetY - NoteComponent.noteHeight/2;
                    double currentY = startY + (targetY-startY)*progress;
                    note.setY(currentY);
                }
        }
        };
        noteMovement.start();
    }

    private double getSongProgress(){
        double songDuration = beatmapNotes.get(beatmapNotes.size() - 1).getTimestamp();
        if (songDuration<=0){
            return 0;
        }
        double progress = getCurrentTime()/songDuration;
        return Math.max(0, Math.min(1, progress));
    }
    private double getTravelTime() {
        double progress = getSongProgress();
        return NoteComponent.defaultTravelTime
                + (NoteComponent.minTravelTime
                - NoteComponent.defaultTravelTime) * progress;
    }
    private double getCurrentTime(){
        return (System.nanoTime()-startTime)/1_000_000_000.0;
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