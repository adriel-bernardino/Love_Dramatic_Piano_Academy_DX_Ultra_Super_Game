package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.rhythm;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.time.TimerAction;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.effect.DropShadow;
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

    private final PianoKeyOverlay pianoKeyOverlay;
    private KeyboardMapping keyboardMapping;
    private BeatMapParser beatMapParser;

    private final List<NoteComponent> noteComponents = new ArrayList<>();
    private  List<BeatMapParser.NoteData> beatmapNotes;
    private AnimationTimer noteMovement;
    private long startTime;
    private int nextBeatmapIndex = 0;

    private static final int MAX_LIVES = 10;
    private int lives = MAX_LIVES;
    private Line targetLine;
    private final List<Rectangle> lifeIndicators = new ArrayList<>();
    private Text songTitle;
    private Text countdownText;

    public RhythmState(CoreSceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.audioManager = GlobalAudioManager.getInstance();
        pianoKeyOverlay = new PianoKeyOverlay();
    }
    public void start(List<String> dialogueLines, int resumeLineIndex, String songTrack) {
        FXGL.spawn("background", new SpawnData(0, 0).put("imageName", "Rhythmbgs/rhythmPsalm.png"));
        pianoKeyOverlay.activate();
        double targetY = NoteComponent.targetY;
        targetLine = new Line(FXGL.getAppWidth() * 0.515, targetY, FXGL.getAppWidth() * 0.925, targetY);
        targetLine.setStroke(Color.BLACK);
        targetLine.setStrokeWidth(3);
        FXGL.addUINode(targetLine);
        this.storedResumeLine = resumeLineIndex;
        lives = MAX_LIVES;
        clearLifeDisplay();
        createLifeDisplay();
        keyboardMapping = new KeyboardMapping();
        beatMapParser = new BeatMapParser();
        String songTitleText;
        if (songTrack.contains("Intro")) {
            beatMapParser.loadBeatMap("beatmap_part1");
            songTitleText = "Because by The Beatles";
        } else {
            beatMapParser.loadBeatMap("beatmap_part2");
            songTitleText = "Lich Mich Im Arsch";
        }
        beatmapNotes = beatMapParser.getNoteData();
        if (beatmapNotes.isEmpty()) {
            System.err.println("Beatmap contains no notes.");
            finishDummyRhythm();
            return;
        }
        showSongTitle(songTitleText);
        nextBeatmapIndex = 0;
        startCountdown(songTrack);
    }
    private void startCountdown(String songTrack) {
        FXGL.getGameTimer().runOnceAfter(() -> {
            countdownText = new Text("3");
            countdownText.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 64));
            countdownText.setFill(Color.WHITE);
            countdownText.setTranslateX(FXGL.getAppWidth() * 0.48);
            countdownText.setTranslateY(FXGL.getAppHeight() * 0.50);
            FXGL.addUINode(countdownText);
        }, Duration.seconds(2));
        FXGL.getGameTimer().runOnceAfter(() -> {
            if (countdownText != null) {
                countdownText.setText("2");
            }
        }, Duration.seconds(3));
        FXGL.getGameTimer().runOnceAfter(() -> {
            if (countdownText != null) {
                countdownText.setText("1");
            }
        }, Duration.seconds(4));
        FXGL.getGameTimer().runOnceAfter(() -> {
            if (countdownText != null) {
                FXGL.removeUINode(countdownText);
                countdownText = null;
            }
            if (songTitle != null) {
                FXGL.removeUINode(songTitle);
                songTitle = null;
            }
            startRhythmGame(songTrack);
        }, Duration.seconds(5));
    }
    private Rectangle songTitleBackground;
    private void showSongTitle(String title) {
        songTitleBackground = new Rectangle(FXGL.getAppWidth() * 0.60, FXGL.getAppHeight() * 0.14);
        songTitleBackground.setFill(Color.rgb(255, 255, 255, 0.40));
        songTitleBackground.setArcWidth(25);
        songTitleBackground.setArcHeight(25);
        songTitleBackground.setTranslateX((FXGL.getAppWidth() - songTitleBackground.getWidth()) / 2);
        songTitleBackground.setTranslateY(FXGL.getAppHeight()* 0.41);
        songTitle = new Text(title);
        songTitle.setFont(Font.font("Open Sans", FontWeight.BOLD, 48));
        songTitle.setFill(Color.BLACK);
        DropShadow shadow = new DropShadow();
        shadow.setRadius(10);
        shadow.setSpread(0.15);
        shadow.setColor(Color.rgb(0, 0, 0, 0.35));
        songTitle.setEffect(shadow);
        songTitle.setTranslateX((FXGL.getAppWidth() - songTitle.getLayoutBounds().getWidth()) / 2);
        songTitle.setTranslateY(FXGL.getAppHeight() * 0.50);
        FXGL.addUINode(songTitleBackground);
        FXGL.addUINode(songTitle);
        FXGL.getGameTimer().runOnceAfter(() -> {
            if (songTitle != null) {
                FXGL.removeUINode(songTitle);
                songTitle = null;
            }
            if (songTitleBackground != null) {
                FXGL.removeUINode(songTitleBackground);
                songTitleBackground = null;
            }
        }, Duration.seconds(2));
    }
    private void startRhythmGame(String songTrack) {
        audioManager.playMusic(songTrack);
        startTime = System.nanoTime();
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
                if (key != null) {
                    checkKeyPress(key);
                }
                if (nextBeatmapIndex < beatmapNotes.size()) {
                    BeatMapParser.NoteData data = beatmapNotes.get(nextBeatmapIndex);
                    double travelTime = getTravelTime();
                    double spawnTime = data.getTimestamp() - travelTime;
                    if (currentTime >= spawnTime) {
                        List<String> notes = data.getNotes();
                        for (int i = 0; i < notes.size(); i++) {
                            String note = notes.get(i);
                            double noteTimestamp = data.getTimestamp();
                            if (data.getType() == BeatMapParser.NoteType.OCTAVE || data.getType() == BeatMapParser.NoteType.ARPEGGIO) {
                                noteTimestamp += i * NoteComponent.specialNoteInterval;
                            }
                            NoteComponent noteComponent = new NoteComponent(noteTimestamp, note, travelTime);
                            noteComponents.add(noteComponent);
                            FXGL.addUINode(noteComponent.getVisualNote());
                        }
                        nextBeatmapIndex++;
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
                        showFeedback("MISS!", Color.RED
                        );
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

    private void createLifeDisplay() {
        for (int i = 0; i < MAX_LIVES; i++) {
            Rectangle life = new Rectangle(FXGL.getAppWidth() * 0.015, FXGL.getAppHeight() * 0.025);
            life.setFill(Color.RED);
            life.setTranslateX(FXGL.getAppWidth() * 0.02 + i * FXGL.getAppWidth() * 0.02);
            life.setTranslateY(FXGL.getAppHeight() * 0.03);
            lifeIndicators.add(life);
            FXGL.addUINode(life);
        }
    }

    private void updateLifeDisplay() {
        for (int i = 0; i < lifeIndicators.size(); i++) {

            if (i < lives) {
                lifeIndicators.get(i).setOpacity(1);
            } else {
                lifeIndicators.get(i).setOpacity(0.2);
            }
        }
    }

    private void clearLifeDisplay() {
        for (Rectangle life : lifeIndicators) {
            FXGL.removeUINode(life);
        }
        lifeIndicators.clear();
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
            pianoKeyOverlay.hide();
        }
        if (targetLine != null) {
            FXGL.removeUINode(targetLine);
            targetLine = null;
        }
        if (songTitle != null) {
            FXGL.removeUINode(songTitle);
            songTitle = null;
        }
        if (songTitleBackground != null) {
            FXGL.removeUINode(songTitleBackground);
            songTitleBackground = null;
        }
        clearLifeDisplay();
        sceneManager.switchToVisualNovelAtLine(storedResumeLine);
    }
    private void loseLife() {
        lives--;
        System.out.println("LIFE LOST | Remaining: " + lives);
        updateLifeDisplay();
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
            pianoKeyOverlay.hide();
        }
        if (targetLine != null) {
            FXGL.removeUINode(targetLine);
            targetLine = null;
        }
        if (songTitle != null) {
            FXGL.removeUINode(songTitle);
            songTitle = null;if (songTitleBackground != null) {
                FXGL.removeUINode(songTitleBackground);
                songTitleBackground = null;
            }

        }
        clearLifeDisplay();
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