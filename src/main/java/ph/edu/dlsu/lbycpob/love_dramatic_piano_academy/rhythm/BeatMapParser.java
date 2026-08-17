package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.rhythm;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class BeatMapParser {
    private List<NoteData> notes = new ArrayList<>();

    public enum NoteType{
        SINGLE,
        ARPEGGIO,
        CHORD,
        OCTAVE
    }
    public class NoteData {
        double timestamp;
        String notes;
        NoteType type;

        NoteData(double timestamp, String notes, NoteType type) {
            this.timestamp = timestamp;
            this.notes = notes;
            this.type = type;
        }

        public double getTimestamp() {
            return timestamp;
        }

        public String getNotes() {
            return notes;
        }
    }
    private NoteType detectNoteType(String note) {
        if (note.contains("Octave")) {
            return NoteType.OCTAVE;
        } else if (note.contains("Arpeggio")) {
            return NoteType.ARPEGGIO;
        } else if (note.contains("+")) {
            return NoteType.CHORD;
        }
        return NoteType.SINGLE;
    }
    public void loadBeatMap(String filename) {
        try {
            InputStream ibm = getClass().getResourceAsStream("/assets/Beatmap/" + filename);
            if (ibm == null) {
                System.err.println("Script missing at: /assets/Beatmap/" + filename);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(ibm));
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();

                String timestring = trimmed.substring(0, 8);
                String[] t_parts = timestring.split(":");
                double minutes = Double.parseDouble(t_parts[0]);
                double seconds = Double.parseDouble(t_parts[1]);
                double timestamp = minutes * 60 + seconds;

                String noteString = trimmed.substring(9);
                NoteType type = detectNoteType(noteString);
                NoteData noteData = new NoteData(timestamp, noteString,type);
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