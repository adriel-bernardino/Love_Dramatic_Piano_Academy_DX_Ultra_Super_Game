package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.rhythm;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.shape.Rectangle;

public class NoteComponent {
    private double timestamp;
    private String note;
    private Rectangle VisualNote;
    int lane;

    public static final double noteWidth = FXGL.getAppWidth() * 0.0104;
    public static final double noteHeight = FXGL.getAppHeight() * 0.0370;
    public static final double spawnY = FXGL.getAppHeight()*0.01;
    NoteComponent(double timestamp, String note, int lane){
        this.timestamp = timestamp;
        this.note = note;
        this.lane = lane;

        VisualNote = new Rectangle(noteWidth,noteHeight);
        VisualNote.setY(spawnY);
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setY(double y){
        VisualNote.setY(y);
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
