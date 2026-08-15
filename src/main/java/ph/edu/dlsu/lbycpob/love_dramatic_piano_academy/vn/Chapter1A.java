package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.vn;

import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core.CoreSceneManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Chapter1A extends AbstractChapter {

    private final DialogueManager dialogueManager = new DialogueManager();
    private CharacterSprite sprite1Overlay;
    private CharacterSprite sprite2Overlay;

    public Chapter1A(CoreSceneManager sceneManager) {
        super(sceneManager);
        this.script = Arrays.asList(
                "[001] [CHECKPOINT] [EVENT_TYPE: rhythm_start] [BG: textures/Rhythmbgs/rhythmSolopng.png] [AUDIO: music/becauseIntro.MP3] | Narration: Kepler places the sheet music on the rack...",
                "[002] [CHECKPOINT] [EVENT_TYPE: post_rhythm] [BG: textures/VNbgs/dialogueBG.png] [SPRITE_1: textures/Sprites/Psalm/psalm1.png] [AUDIO: music/mainTheme.mp3] | Narration: A shadow shifts against the window frame...",
                "[004] [BG: textures/VNbgs/dialogueBG.png] [SPRITE_1: textures/Sprites/Psalm/psalm1.png] | Psalm: Why did you stop?",
                "[021] [CHECKPOINT] [EVENT_TYPE: route_end] [BG: textures/VNbgs/dialogueBG.png] [SPRITE_1: textures/Sprites/Psalm/psalm1.png] [SPRITE_2: textures/Sprites/Roni/roni1.png] | Roni: Sorry I'm late."
        );
    }

    @Override
    public void start(int startingLine) {
        this.currentLineIndex = startingLine;
        this.lastCheckpointLine = startingLine;

        dialogueManager.build(
                () -> { currentLineIndex = script.size() - 1; processCurrentLine(); },
                () -> sceneManager.switchToSaveMenu(1, 'A', lastCheckpointLine),
                () -> com.almasb.fxgl.dsl.FXGL.getGameController().exit(),
                this::advanceScript
        );

        processCurrentLine();
    }

    @Override
    protected void advanceScript() {
        if (currentLineIndex < script.size() - 1) {
            currentLineIndex++;
            processCurrentLine();
        }
    }

    @Override
    protected void processCurrentLine() {
        String line = script.get(currentLineIndex);

        if (line.contains("[CHECKPOINT]")) {
            lastCheckpointLine = currentLineIndex;
        }

        checkAndSetBackground(line);
        checkAndPlayAudio(line);
        processSprites(line);

        String eventType = extractTag(line, "EVENT_TYPE");
        if ("rhythm_start".equals(eventType)) {
            cleanupUiOnly();
            List<String> rhythmDialogue = new ArrayList<>();
            rhythmDialogue.add("Rhythm sequence initiating...");
            String audioTrack = extractTag(line, "AUDIO").replace("music/", "");
            sceneManager.switchToRhythmGame(rhythmDialogue, currentLineIndex + 1, audioTrack);
            return;
        } else if ("route_end".equals(eventType)) {
            cleanupUiOnly();
            sceneManager.switchToMainMenu();
            return;
        }

        String[] parts = line.split("\\| ");
        String dialogue = parts.length > 1 ? parts[1] : "";
        String speaker = dialogue.contains(":") ? dialogue.split(":", 2)[0] : "Narration";
        dialogueManager.setLine(speaker, dialogue);
    }

    // Understand: fixes the earlier double-cleanup bug — this only tears down UI/sprites,
    // it does NOT call itself again; CoreSceneManager's cleanupCurrentState() handles the rest
    private void cleanupUiOnly() {
        dialogueManager.cleanup();
        if (sprite1Overlay != null) { sprite1Overlay.destroy(); sprite1Overlay = null; }
        if (sprite2Overlay != null) { sprite2Overlay.destroy(); sprite2Overlay = null; }
    }

    private void processSprites(String line) {
        String sprite1Path = extractTag(line, "SPRITE_1");
        if (sprite1Path != null) {
            sprite1Path = sprite1Path.replace("textures/", "");
            if (sprite1Overlay == null) sprite1Overlay = new CharacterSprite("Sprite1", sprite1Path, 1200, 200);
            else sprite1Overlay.setSprite(sprite1Path);

            // Understand this is to resize the sprites
            sprite1Overlay.setScale(0.25, 0.25);
            sprite1Overlay.popIn(1.0);

        } else if (sprite1Overlay != null) {
            sprite1Overlay.destroy();
            sprite1Overlay = null;
        }

        String sprite2Path = extractTag(line, "SPRITE_2");
        if (sprite2Path != null) {
            sprite2Path = sprite2Path.replace("textures/", "");
            if (sprite2Overlay == null) sprite2Overlay = new CharacterSprite("Sprite2", sprite2Path, 400, 200);
            else sprite2Overlay.setSprite(sprite2Path);
        } else if (sprite2Overlay != null) {   // <- the missing branch from before, now fixed
            sprite2Overlay.destroy();
            sprite2Overlay = null;
        }
    }

    @Override
    public void cleanup() {
        cleanupUiOnly();
    }
}