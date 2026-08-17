package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.rhythm;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class NoteComponent {
    private double timestamp;
    private String note;
    private Rectangle VisualNote;
    int lane;
    NoteComponent(double timestamp, String note, int lane){
        this.timestamp = timestamp;
        this.note = note;
        this.lane = lane;

        VisualNote = new Rectangle();
    }

    public enum NoteType{
        SINGLE,
        CHORD,
        ARPEGGIO,
        OCTAVE
    }

    public NoteType detectNoteType(String note){
         if (note.contains("Arpeggio")){
             return NoteType.ARPEGGIO;
        } else if (note.contains("+")) {
             return NoteType.CHORD;
         } else if (note.contains("Octave")) {
             return NoteType.OCTAVE;
         }
         else return NoteType.SINGLE;
    }

    public double getTimestamp() {
        return timestamp;
    }
    public String getNote() {
        return note;
    }
    public Rectangle getVisualNote() {
        return VisualNote;
    }
    public int getLane() {
        return lane;
    }
}
