package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.rhythm;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class BeatMapParser {
    private final List<NoteData> notes = new ArrayList<>();
    private final Map<String, List<String>> octaveMapping = new HashMap<>();
    private final Map<String, List<String>> arpeggioMapping = new HashMap<>();

    public BeatMapParser() {
        octaveMapping.put("A", List.of("A3", "A4"));
        octaveMapping.put("G#", List.of("G#3", "G#4"));
        octaveMapping.put("F#", List.of("F#3", "F#4"));
        octaveMapping.put("E", List.of("E4", "E5"));
        octaveMapping.put("D", List.of("D4", "D5"));
        octaveMapping.put("C#", List.of("C#4", "C#5"));
        octaveMapping.put("B", List.of("B3", "B4"));
        octaveMapping.put("Bb", List.of("A#4", "Bb4"));

        arpeggioMapping.put("C Major", List.of("C4", "E4", "G4", "C5"));
        arpeggioMapping.put("C#m", List.of("C#4", "E4", "G#4", "C#5"));
    }

    public enum NoteType {
        SINGLE,
        ARPEGGIO,
        CHORD,
        OCTAVE
    }

    public class NoteData {
        private final double timestamp;
        private final List<String> notes;
        private final NoteType type;

        NoteData(double timestamp, List<String> notes, NoteType type) {
            this.timestamp = timestamp;
            this.notes = notes;
            this.type = type;
        }

        public NoteType getType() {
            return type;
        }

        public double getTimestamp() {
            return timestamp;
        }

        public List<String> getNotes() {
            return notes;
        }
    }

    private NoteType detectNoteType(String note) {
        if (note.contains("Arpeggio")) {
            return NoteType.ARPEGGIO;
        } else if (note.contains("(Octave)")) {
            return NoteType.OCTAVE;
        } else if (note.contains("+")) {
            return NoteType.CHORD;
        }
        return NoteType.SINGLE;
    }

    private String cleanChord(String note) {
        int descriptorStart = note.indexOf("(");
        if (descriptorStart != -1) {
            note = note.substring(0, descriptorStart);
        }
        return note.trim();
    }

    private List<String> extractNotes(String note, NoteType type) {
        switch (type) {
            case SINGLE:
                return List.of(note.trim());
            case CHORD:
                return extractChord(note);
            case OCTAVE:
                return extractOctave(note);
            case ARPEGGIO:
                return extractArpeggio(note);
            default:
                return List.of();
        }
    }

    public List<String> extractArpeggio(String note) {
        String cleanNote = note
                .replace("Arpeggio", "")
                .replace("(Higher Octave)", "")
                .replace("(Fade)", "")
                .trim();
        List<String> mapped = arpeggioMapping.get(cleanNote);
        return mapped != null ? mapped : List.of("C4");
    }

    private List<String> extractChord(String note) {
        String cleanNote = cleanChord(note);
        String[] parts = cleanNote.split("\\s*\\+\\s*");
        List<String> chordNotes = new ArrayList<>();
        for (String part : parts) {
            if (!part.trim().isEmpty()) {
                chordNotes.add(part.trim());
            }
        }
        return chordNotes;
    }

    public List<String> extractOctave(String note) {
        String cleanNote = note.replace("(Octave)", "").trim();
        List<String> mapped = octaveMapping.get(cleanNote);
        return mapped != null ? mapped : List.of(cleanNote);
    }

    public void loadBeatMap(String filename) {
        notes.clear();
        try {
            InputStream ibm = getClass().getResourceAsStream("/assets/Beatmap/" + filename + ".txt");
            if (ibm == null) {
                System.err.println("Script missing at: /assets/Beatmap/" + filename + ".txt");
                return;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(ibm));
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }

                if (trimmed.length() < 9) {
                    continue;
                }

                String timestring = trimmed.substring(0, 8);
                String[] t_parts = timestring.split(":");
                double minutes = Double.parseDouble(t_parts[0]);
                double seconds = Double.parseDouble(t_parts[1]);
                double timestamp = minutes * 60 + seconds;

                String noteString = trimmed.substring(9).trim();
                NoteType type = detectNoteType(noteString);
                List<String> extractedNotes = extractNotes(noteString, type);
                NoteData noteData = new NoteData(timestamp, extractedNotes, type);
                notes.add(noteData);
            }
        } catch (Exception e) {
            System.err.println("Failed to read script: " + filename);
            e.printStackTrace();
        }
    }

    public List<NoteData> getNoteData() {
        return notes;
    }
}