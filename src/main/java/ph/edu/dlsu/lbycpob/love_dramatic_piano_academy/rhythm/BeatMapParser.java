package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.rhythm;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class BeatMapParser {
    private List<NoteData> notes = new ArrayList<>();

    public class NoteData {
        double timestamp;
        String notes;

        NoteData(double timestamp, String notes) {
            this.timestamp = timestamp;
            this.notes = notes;
        }

        public double getTimestamp() {
            return timestamp;
        }

        public String getNotes() {
            return notes;
        }
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
                NoteData noteData = new NoteData(timestamp, noteString);
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