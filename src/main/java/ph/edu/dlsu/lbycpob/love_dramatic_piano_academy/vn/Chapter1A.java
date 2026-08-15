package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.vn;

import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core.CoreSceneManager;

import java.util.ArrayList;
import java.util.List;

public class Chapter1A extends AbstractChapter {

    private final DialogueManager dialogueManager = new DialogueManager();
    private CharacterSprite sprite1Overlay;
    private CharacterSprite sprite2Overlay;
    private boolean isTransitioning = false;

    public Chapter1A(CoreSceneManager sceneManager) {
        super(sceneManager);
        // Understand: Loads the parsed script to read the asset file instead of hardcoded strings
        loadScript("Chapter1A");
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

    @Override
    protected void advanceScript() {
        if (isTransitioning) return;
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
            List<String> rhythmDialogue = new ArrayList<>();
            rhythmDialogue.add("Rhythm sequence initiating...");

            String audioTrack = extractTag(line, "AUDIO");
            if (audioTrack != null) audioTrack = audioTrack.replace("music/", "");
            else audioTrack = "becauseIntro.MP3";

            sceneManager.switchToRhythmGame(rhythmDialogue, currentLineIndex + 1, audioTrack);
            return;
        } else if ("route_end".equals(eventType)) {
            isTransitioning = true;
            dialogueManager.hide();

            // Understand: Graceful exit spacing transitioning back to main menu
            performFadeTransition(
                    this::cleanupUiOnly,
                    sceneManager::switchToMainMenu
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

    private void processSprites(String line) {
        String sprite1Path = extractTag(line, "SPRITE_1");
        if (sprite1Path != null) {
            sprite1Path = sprite1Path.replace("textures/", "");
            if (sprite1Overlay == null) {
                // Understand: Instantiated at (950, 100) and scaled to 2.2x to match the concept UI reference
                sprite1Overlay = new CharacterSprite("Sprite1", sprite1Path, 950, 100);
                sprite1Overlay.setScale(2.2, 2.2);

                // Understand this is to add animation
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
                // Understand: Also scale the second sprite
                sprite2Overlay = new CharacterSprite("Sprite2", sprite2Path, 300, 100);
                sprite2Overlay.setScale(2.2, 2.2);
                sprite2Overlay.popIn(500.0);
            } else {
                sprite2Overlay.setSprite(sprite2Path);
                sprite2Overlay.getEntity().setVisible(true);
            }
        } else if (sprite2Overlay != null) {
            // <- the missing branch from before, now fixed
            sprite2Overlay.destroy();
            sprite2Overlay = null;
        }
    }

    @Override
    public void cleanup() {
        cleanupUiOnly();
    }
}