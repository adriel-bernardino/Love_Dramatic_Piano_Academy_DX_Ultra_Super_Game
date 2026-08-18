package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.rhythm;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteComponent {
    private final double timestamp;
    private final String note;
    private final Rectangle visualNote;
    private final int lane;
    private final double travelTime;

    public static final double noteWidth = FXGL.getAppWidth() * 0.0104;
    public static final double noteHeight = FXGL.getAppHeight() * 0.0370;
    public static final double spawnY = FXGL.getAppHeight() * 0.01;
    public static final double targetY = FXGL.getAppHeight() * 0.650;
    public static final double missY = FXGL.getAppHeight() * 0.720;
    public static final double hitTolerance = 60;
    public static final double defaultTravelTime = 2.0;
    public static final double minTravelTime = 2.0;
    public static final double specialNoteInterval = 0.4;

    NoteComponent(double timestamp, String note, double travelTime) {
        this.timestamp = timestamp;
        this.note = note;
        this.lane = getLaneForNote(note);
        this.travelTime = travelTime;

        visualNote = new Rectangle(noteWidth, noteHeight);
        visualNote.setX(getlaneX(this.lane));
        visualNote.setY(spawnY);
    }

    private static final Map<List<String>, Integer> noteLaneMapping = new HashMap<>();
    static {
        noteLaneMapping.put(List.of("B#3", "C4"), 0);
        noteLaneMapping.put(List.of("C#4", "Db4"), 1);
        noteLaneMapping.put(List.of("D4"), 2);
        noteLaneMapping.put(List.of("D#4", "Eb4"), 3);
        noteLaneMapping.put(List.of("E4"), 4);
        noteLaneMapping.put(List.of("E5"), 5);
        noteLaneMapping.put(List.of("F#3", "F#4", "Gb3", "Gb4"), 6);
        noteLaneMapping.put(List.of("G4"), 7);
        noteLaneMapping.put(List.of("G#3", "G#4", "Ab3", "Ab4"), 8);
        noteLaneMapping.put(List.of("A3", "A4"), 9);
        noteLaneMapping.put(List.of("A#4", "Bb4"), 10);
        noteLaneMapping.put(List.of("B3", "B4"), 11);
        noteLaneMapping.put(List.of("C5"), 12);
        noteLaneMapping.put(List.of("C#5", "Db5"), 13);
        noteLaneMapping.put(List.of("D5"), 14);
        noteLaneMapping.put(List.of("D#5", "Eb5"), 15);
        noteLaneMapping.put(List.of("F5"), 16);
    }

    public double getTravelTime() {
        return travelTime;
    }

    private static int getLaneForNote(String note) {
        if (note == null) {
            return 0;
        }
        String cleanNote = note.trim();

        for (Map.Entry<List<String>, Integer> entry : noteLaneMapping.entrySet()) {
            if (entry.getKey().contains(cleanNote)) {
                return entry.getValue();
            }
        }
        return 0;
    }

    private static final double[] LANE_X = {
            0.526,  // A
            0.546,  // W
            0.566,  // S
            0.586,  // E
            0.605,  // D
            0.638,  // F
            0.658,  // T
            0.676,  // G
            0.694,  // Y
            0.7125, // H
            0.731,  // U
            0.751,  // J
            0.786,  // K
            0.806,  // O
            0.8245, // L
            0.843,  // P
            0.863   // ;
    };

    private double getlaneX(int lane) {
        if (lane < 0 || lane >= LANE_X.length) {
            return FXGL.getAppWidth() * LANE_X[0];
        }
        return FXGL.getAppWidth() * LANE_X[lane];
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setY(double y) {
        visualNote.setY(y);
    }

    public String getNote() {
        return note;
    }

    public Rectangle getVisualNote() {
        return visualNote;
    }

    public int getLane() {
        return lane;
    }
}