package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.rhythm;

import javafx.scene.input.KeyCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyboardMapping {
    private final Map<KeyCode, List<String>> keyMap = new HashMap<>();

    public KeyboardMapping() {
        keyMap.put(KeyCode.A, List.of("B#3", "C4"));
        keyMap.put(KeyCode.S, List.of("D4"));
        keyMap.put(KeyCode.D, List.of("E4"));
        keyMap.put(KeyCode.F, List.of("E5"));
        keyMap.put(KeyCode.G, List.of("G4"));
        keyMap.put(KeyCode.H, List.of("A3", "A4"));
        keyMap.put(KeyCode.J, List.of("B3", "B4"));
        keyMap.put(KeyCode.K, List.of("C5"));
        keyMap.put(KeyCode.L, List.of("D5"));
        keyMap.put(KeyCode.SEMICOLON, List.of("F5"));
        keyMap.put(KeyCode.W, List.of("C#4", "Db4"));
        keyMap.put(KeyCode.E, List.of("D#4", "Eb4"));
        keyMap.put(KeyCode.T, List.of("F#3", "F#4", "Gb3", "Gb4"));
        keyMap.put(KeyCode.Y, List.of("G#3", "G#4", "Ab3", "Ab4"));
        keyMap.put(KeyCode.U, List.of("A#4", "Bb4"));
        keyMap.put(KeyCode.O, List.of("C#5", "Db5"));
        keyMap.put(KeyCode.P, List.of("D#5", "Eb5"));
    }

    public boolean matches(KeyCode key, String targetNote) {
        List<String> notes = keyMap.get(key);
        return notes != null && targetNote != null && notes.contains(targetNote.trim());
    }
}