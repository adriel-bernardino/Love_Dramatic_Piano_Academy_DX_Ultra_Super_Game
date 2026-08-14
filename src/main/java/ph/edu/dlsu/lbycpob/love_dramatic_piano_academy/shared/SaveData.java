package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.shared;

import java.util.List;
import java.io.Serializable;

// Understand: immutable Value Object tracking progress
public record SaveData(
        int chapter,
        String route,
        String line,
        int saveSlotId
) implements Serializable {}