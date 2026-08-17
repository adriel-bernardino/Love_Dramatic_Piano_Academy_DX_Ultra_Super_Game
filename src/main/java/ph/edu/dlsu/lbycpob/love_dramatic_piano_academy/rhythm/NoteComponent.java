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
