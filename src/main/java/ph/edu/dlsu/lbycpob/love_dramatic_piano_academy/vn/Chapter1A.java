package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.vn;

import com.almasb.fxgl.entity.SpawnData;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core.CoreSceneManager;

import java.util.ArrayList;
import java.util.List;

public class Chapter1A extends AbstractChapter {

    private final DialogueManager dialogueManager = new DialogueManager();
    private CharacterSprite sprite1Overlay;
    private CharacterSprite sprite2Overlay;
    private boolean isTransitioning = false;

    // Understand: New flag to prevent auto-advancing past the end screen
    private boolean hasReachedEnd = false;

    public Chapter1A(CoreSceneManager sceneManager) {
        super(sceneManager);
        // Understand: Loads the parsed script to read the asset file instead of hardcoded strings
        loadScript("Chapter1A");
    }

    @Override
    public void start(int startingLine) {
        this.currentLineIndex = startingLine;
        this.lastCheckpointLine = startingLine;
        this.hasReachedEnd = false;

        dialogueManager.build(
                this::skipToNextEvent,
                () -> sceneManager.switchToSaveMenu(1, 'A', lastCheckpointLine),
                // Understand: Changed from .exit() to return to the Main Menu
                () -> sceneManager.switchToMainMenu(),
                this::advanceScript
        );

        // Understand: Chapter spacing transition upon entrance to prevent the "cardboard cut out" feel
        isTransitioning = true;
        dialogueManager.hide();
        performFadeTransition(
                null,
                () -> {
                    isTransitioning = false;
                    processCurrentLine();
                }
        );
    }

    // Understand: Skips to the NEXT rhythm game or the end of the route, whichever comes first
    private void skipToNextEvent() {
        // Understand: Block skipping if we hit the end of the chapter
        if (isTransitioning || hasReachedEnd) return;

        // Start searching from the next line onwards
        for (int i = currentLineIndex + 1; i < script.size(); i++) {
            String line = script.get(i);
            if (line.contains("rhythm_start") || line.contains("route_end")) {
                currentLineIndex = i;
                processCurrentLine();
                return;
            }
        }
    }

    @Override
    protected void advanceScript() {
        // Understand: Block the Fast Forward feature from advancing into a gray screen
        if (isTransitioning || hasReachedEnd) return;
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

        if (isBlackScreen(line)) {
            isTransitioning = true;
            dialogueManager.hide();

            // Understand: Temporarily conceal sprites while the screen goes black
            if (sprite1Overlay != null) sprite1Overlay.getEntity().setVisible(false);
            if (sprite2Overlay != null) sprite2Overlay.getEntity().setVisible(false);

            performFadeTransition(
                    () -> {
                        checkAndSetBackground(line);
                        checkAndPlayAudio(line);
                    },
                    () -> {
                        isTransitioning = false;
                        advanceScript();
                    }
            );
            return;
        }

        checkAndSetBackground(line);
        checkAndPlayAudio(line);
        processSprites(line);
        dialogueManager.show();

        String eventType = extractTag(line, "EVENT_TYPE");
        if ("rhythm_start".equals(eventType)) {
            cleanupUiOnly();

            // Extract the dialogue lines specifically attached to the rhythm block
            List<String> rhythmDialogue = new ArrayList<>();
            String[] parts = line.split("\\| ");
            for (int i = 1; i < parts.length; i++) {
                rhythmDialogue.add(parts[i].trim());
            }

            // Fallback just in case
            if (rhythmDialogue.isEmpty()) {
                rhythmDialogue.add("System: Rhythm sequence initiating...");
            }

            String audioTrack = extractTag(line, "AUDIO");
            if (audioTrack != null) {
                audioTrack = audioTrack.replace("music/", "");
            } else {
                audioTrack = "becauseIntro.MP3";
            }

            // Sends the extracted dialogue, the next scene index, and the track name to the Rhythm Game
            sceneManager.switchToRhythmGame(rhythmDialogue, currentLineIndex + 1, audioTrack);
            return;

        } else if ("route_end".equals(eventType)) {
            isTransitioning = true;
            // Understand: Lock advancement right here to prevent reading the next blackScreen tag
            hasReachedEnd = true;
            dialogueManager.hide();

            performFadeTransition(
                    () -> {
                        if (sprite1Overlay != null) { sprite1Overlay.destroy(); sprite1Overlay = null; }
                        if (sprite2Overlay != null) { sprite2Overlay.destroy(); sprite2Overlay = null; }

                        com.almasb.fxgl.dsl.FXGL.spawn("background", new SpawnData(0, 0).put("imageName", "VNbgs/dialogueBG.png"));
                    },
                    () -> {
                        isTransitioning = false;

                        // Show the dialogue box and let the player use its built-in Save/Load/Quit buttons
                        dialogueManager.show();
                        dialogueManager.setLine("System", "This is the end of the currently available chapters. Thank you for playing! Please use the buttons below to Save or Quit.");
                    }
            );
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

    // Understand: Here's how to customize sprites
    private void processSprites(String line) {
        String sprite1Path = extractTag(line, "SPRITE_1");
        if (sprite1Path != null) {
            sprite1Path = sprite1Path.replace("textures/", "");
            if (sprite1Overlay == null) {
                // Understand: negative places them higher, kinda weird but o
                sprite1Overlay = new CharacterSprite("Sprite1", sprite1Path, 200, -350);

                // Understand: Pre-scale the sprite using the updated method before popping it in
                sprite1Overlay.setScale(0.75, 0.75);
                sprite1Overlay.popIn(500.0);
            } else {
                sprite1Overlay.setSprite(sprite1Path);
                sprite1Overlay.getEntity().setVisible(true);
            }
        } else if (sprite1Overlay != null) {
            sprite1Overlay.destroy();
            sprite1Overlay = null;
        }

        String sprite2Path = extractTag(line, "SPRITE_2");
        if (sprite2Path != null) {
            sprite2Path = sprite2Path.replace("textures/", "");
            if (sprite2Overlay == null) {
                sprite2Overlay = new CharacterSprite("Sprite2", sprite2Path, 800, -350);

                // Understand: Pre-scale the sprite using the updated method before popping it in
                sprite2Overlay.setScale(0.75, 0.75);
                sprite2Overlay.popIn(500.0);
            } else {
                sprite2Overlay.setSprite(sprite2Path);
                sprite2Overlay.getEntity().setVisible(true);
            }
        } else if (sprite2Overlay != null) {
            sprite2Overlay.destroy();
            sprite2Overlay = null;
        }
    }

    @Override
    public void cleanup() {
        cleanupUiOnly();
    }
}