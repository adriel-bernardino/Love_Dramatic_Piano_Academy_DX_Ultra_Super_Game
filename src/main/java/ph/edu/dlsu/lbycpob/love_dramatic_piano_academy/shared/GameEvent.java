package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.shared;

// Understand: Behavioral Contract defining global game events
public interface GameEvent {
    void onSongFinished();
    void onChapterEnded();
}