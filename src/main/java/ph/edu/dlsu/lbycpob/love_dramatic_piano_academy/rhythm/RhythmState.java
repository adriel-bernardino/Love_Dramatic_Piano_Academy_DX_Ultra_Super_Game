package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.rhythm;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.time.TimerAction;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core.CoreSceneManager;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core.GlobalAudioManager;

import java.util.ArrayList;
import java.util.Iterator;
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

    private static final int MAX_LIVES = 10;
    private int lives = MAX_LIVES;

    public RhythmState(CoreSceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.audioManager = GlobalAudioManager.getInstance();
    }

    public void start(List<String> dialogueLines, int resumeLineIndex, String songTrack) {
        FXGL.spawn("background", new SpawnData(0, 0).put("imageName", "Rhythmbgs/rhythmSolo.png"));
        this.storedResumeLine = resumeLineIndex;
        lives = MAX_LIVES;
        pianoKeyOverlay = new PianoKeyOverlay();
        keyboardMapping = new KeyboardMapping();
        beatMapParser = new BeatMapParser();
        beatMapParser.loadBeatMap("beatmap_part2");
        beatmapNotes = beatMapParser.getNoteData();
        if (beatmapNotes.isEmpty()) {
            System.err.println("Beatmap contains no notes.");
            finishDummyRhythm();
            return;
        }
        nextBeatmapIndex = 0;
        audioManager.playMusic(songTrack);
        startTime  = System.nanoTime();
        noteMovement = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double currentTime = getCurrentTime();
                double finalTimestamp = beatmapNotes.get(beatmapNotes.size() - 1).getTimestamp();

                if (currentTime >= finalTimestamp + 1.0) {
                    finishDummyRhythm();
                    return;
                }
                KeyCode key = pianoKeyOverlay.consumeKeyPress();
                if (key!=null){
                    checkKeyPress(key);
                }
                if (nextBeatmapIndex<beatmapNotes.size()) {
                    BeatMapParser.NoteData data = beatmapNotes.get(nextBeatmapIndex);
                    double travelTime = getTravelTime();
                    double spawnTime = data.getTimestamp() - travelTime;
                    if (currentTime >= spawnTime) {
                        List<String> notes = data.getNotes();
                        for (int i = 0; i < notes.size(); i++) {
                            String note = notes.get(i);
                            double noteTimestamp = data.getTimestamp();
                            if (data.getType() == BeatMapParser.NoteType.OCTAVE || data.getType() == BeatMapParser.NoteType.ARPEGGIO) {
                                noteTimestamp += i * NoteComponent.specialNoteInterval;}
                                NoteComponent noteComponent = new NoteComponent(noteTimestamp, note, travelTime);noteComponents.add(noteComponent);
                                FXGL.addUINode(noteComponent.getVisualNote());}nextBeatmapIndex++;
                    }
                }
                Iterator<NoteComponent> iterator = noteComponents.iterator();
                while (iterator.hasNext()) {
                    NoteComponent note = iterator.next();
                    double travelTime = note.getTravelTime();
                    double spawnTime = note.getTimestamp() - travelTime;
                    double progress = (currentTime - spawnTime) / travelTime;
                    double startY = NoteComponent.spawnY;
                    double targetY = NoteComponent.targetY - NoteComponent.noteHeight / 2;
                    double currentY = startY + (targetY - startY) * progress;
                    note.setY(currentY);
                    double noteCenterY = note.getVisualNote().getY() + NoteComponent.noteHeight / 2;
                    if (noteCenterY > NoteComponent.missY) {
                        System.out.println("MISS: " + note.getNote());
                        showFeedback("MISS!", Color.RED);
                        loseLife();
                        FXGL.removeUINode(note.getVisualNote());
                        iterator.remove();
                        if (lives <= 0) {
                            finishDummyRhythm();
                            return;
                        }
                    }
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
        return NoteComponent.defaultTravelTime + (NoteComponent.minTravelTime - NoteComponent.defaultTravelTime) * progress;
    }
    private double getCurrentTime(){
        return (System.nanoTime()-startTime)/1_000_000_000.0;
    }
    private void clearNotes() {
        for (NoteComponent note : noteComponents) {
            FXGL.removeUINode(note.getVisualNote());
        }
        noteComponents.clear();
    }
    private void finishDummyRhythm() {
        if (noteMovement != null) {
            noteMovement.stop();
            noteMovement = null;
        }
        if (autoReturnTimer != null) {
            autoReturnTimer.expire();
            autoReturnTimer = null;
        }
        clearNotes();
        if (pianoKeyOverlay != null) {
            pianoKeyOverlay.cleanup();pianoKeyOverlay = null;
        }
        sceneManager.switchToVisualNovelAtLine(storedResumeLine);
    }
    private void loseLife() {
        lives--;
        System.out.println("LIFE LOST | Remaining: " + lives);
        if (lives <= 0) {
            System.out.println("GAME OVER");
        }
    }

    private void checkKeyPress(KeyCode key) {
        Iterator<NoteComponent> iterator = noteComponents.iterator();
        while (iterator.hasNext()) {NoteComponent note = iterator.next();double noteCenterY = note.getVisualNote().getY() + NoteComponent.noteHeight / 2;
            boolean hittable = Math.abs(noteCenterY - NoteComponent.targetY) <= NoteComponent.hitTolerance;
            if (hittable && keyboardMapping.matches(key, note.getNote())) {
                System.out.println("HIT: " + note.getNote());
                showFeedback("HIT!", Color.LIMEGREEN);
                FXGL.removeUINode(note.getVisualNote());iterator.remove();
                return;
            }
        }
    }

    public void cleanup() {
        if (noteMovement != null) {
            noteMovement.stop();
            noteMovement = null;
        }
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
        clearNotes();
        if (pianoKeyOverlay != null) {
            pianoKeyOverlay.cleanup();
            pianoKeyOverlay = null;
        }
    }
    private void showFeedback(String message, Color color) {
        Text feedback = FXGL.getUIFactoryService().newText(message, 32);
        feedback.setFill(color);
        feedback.setTranslateX(FXGL.getAppWidth() * 0.45);
        feedback.setTranslateY(FXGL.getAppHeight() * 0.60);
        FXGL.addUINode(feedback);
        FXGL.getGameTimer().runOnceAfter(
                () -> FXGL.removeUINode(feedback),
                Duration.seconds(0.5)
        );
    }
}