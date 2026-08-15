package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.vn;

import com.almasb.fxgl.dsl.FXGL;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core.CoreSceneManager;

public class ChapterIntro extends AbstractChapter {

    private final DialogueManager dialogueManager = new DialogueManager();
    private final ChoicePrompt choicePrompt = new ChoicePrompt();
    private boolean isTransitioning = false;

    public ChapterIntro(CoreSceneManager sceneManager) {
        super(sceneManager);
        // Understand: Loads the parsed script to read the asset file instead of hardcoded strings
        loadScript("Chapter0");
    }

    @Override
    public void start(int startingLine) {
        this.currentLineIndex = startingLine;
        this.lastCheckpointLine = startingLine;

        dialogueManager.build(
                this::skipToChoice,
                () -> sceneManager.switchToSaveMenu(0, 'A', lastCheckpointLine),
                // Understand: Changed from .exit() to return to the Main Menu
                () -> sceneManager.switchToMainMenu(),
                this::advanceScript
        );

        processCurrentLine();
    }

    @Override
    protected void advanceScript() {
        // Understand: Prevent user skipping while screen is fading
        if (isTransitioning) return;
        if (currentLineIndex < script.size() - 1) {
            currentLineIndex++;
            processCurrentLine();
        }
    }

    // Understand: jumps straight to the choice prompt line, used by Skip
    private void skipToChoice() {
        if (isTransitioning) return;

        // Understand: Find the index of the first choice line dynamically
        for (int i = 0; i < script.size(); i++) {
            if (script.get(i).contains("Option 1")) {
                currentLineIndex = i;
                processCurrentLine();
                return;
            }
        }
    }

    @Override
    protected void processCurrentLine() {
        String line = script.get(currentLineIndex);

        if (line.contains("[CHECKPOINT]")) {
            lastCheckpointLine = currentLineIndex;
        }

        // Understand: Catch choices automatically from the parsed script lines
        if (line.contains("Option 1") || line.contains("Option 2")) {
            dialogueManager.hide();
            var opts = ChoicePrompt.options();
            opts.put("Play 'Because'", () -> {
                choicePrompt.cleanup();
                sceneManager.switchToVisualNovelFull(1, 'A', 0);
            });
            opts.put("Keep Waiting Silently", () -> {
                choicePrompt.cleanup();
                sceneManager.switchToVisualNovelFull(1, 'B', 0);
            });
            choicePrompt.show(opts);
            return;
        }

        // Understand: Trigger black screen fade based on the tag detection
        if (isBlackScreen(line)) {
            isTransitioning = true;
            dialogueManager.hide();
            performFadeTransition(
                    () -> {
                        checkAndSetBackground(line);
                        checkAndPlayAudio(line);
                    },
                    () -> {
                        isTransitioning = false;
                        advanceScript(); // Understand: Automatically advance past the black screen
                    }
            );
            return;
        }

        checkAndSetBackground(line);
        checkAndPlayAudio(line);
        dialogueManager.show();

        String[] parts = line.split("\\| ");
        String dialogue = parts.length > 1 ? parts[1] : "";
        dialogueManager.setLine("Narration", dialogue);
    }

    @Override
    public void cleanup() {
        dialogueManager.cleanup();
        choicePrompt.cleanup();
    }
}