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

}