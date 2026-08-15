package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.vn;

import com.almasb.fxgl.dsl.FXGL;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core.CoreSceneManager;

import java.util.Arrays;

public class ChapterIntro extends AbstractChapter {

    private final DialogueManager dialogueManager = new DialogueManager();
    private final ChoicePrompt choicePrompt = new ChoicePrompt();

    public ChapterIntro(CoreSceneManager sceneManager) {
        super(sceneManager);
        this.script = Arrays.asList(
                "[001] [CHECKPOINT] [BG: textures/VNbgs/playerSoloWide.png] [AUDIO: music/mainTheme.mp3] | Narration: The third-floor practice room is dim, illuminated only by a pale patch of moonlight on the wooden floorboards.",
                "[002] [BG: textures/VNbgs/playerSoloWide.png] | Narration: Kepler sits idle on the bench, resting his hands on the frame of the upright piano.",
                "[003] [BG: textures/VNbgs/playerSoloWide.png] | Narration: He flips through a scattered pile of sheet music, stopping when a worn score catches his eye: Because by The Beatles.",
                "[CHOICE_PROMPT]"
        );
    }

    @Override
    public void start(int startingLine) {
        this.currentLineIndex = startingLine;
        this.lastCheckpointLine = startingLine;

        dialogueManager.build(
                this::skipToChoice,
                () -> sceneManager.switchToSaveMenu(0, 'A', lastCheckpointLine),
                () -> FXGL.getGameController().exit(),
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

    // Understand: jumps straight to the choice prompt line, used by Skip
    private void skipToChoice() {
        currentLineIndex = script.size() - 1;
        processCurrentLine();
    }

    @Override
    protected void processCurrentLine() {
        String line = script.get(currentLineIndex);

        if (line.contains("[CHECKPOINT]")) {
            lastCheckpointLine = currentLineIndex;
        }

        if (line.equals("[CHOICE_PROMPT]")) {
            dialogueManager.hide();
            var opts = ChoicePrompt.options();
            opts.put("Option 1: Play 'Because'", () -> {
                choicePrompt.cleanup();
                sceneManager.switchToVisualNovelFull(1, 'A', 0);
            });
            opts.put("Option 2: Keep Waiting Silently", () -> {
                choicePrompt.cleanup();
                sceneManager.switchToVisualNovelFull(1, 'B', 0);
            });
            choicePrompt.show(opts);
            return;
        }

        checkAndSetBackground(line);
        checkAndPlayAudio(line);

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