package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.vn;

import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

// Understand: Single reusable owner of dialogue UI and speaker tag management
public class DialogueManager {

    private static final double BOX_MARGIN_X = 60, BOX_WIDTH = 1800, BOX_HEIGHT = 260, BOX_BOTTOM_MARGIN = 40, TEXT_PADDING = 40;

    private StackPane dialogueBox;
    private VBox speakerTag;
    private Text speakerText, bodyText;
    private HBox buttonBar;
    private Runnable onSkip, onSave, onQuit, onAdvance;

    public DialogueManager() {}

    // Understand: Initializes and attaches dialogue UI nodes to FXGL scene
    public void build(Runnable onSkip, Runnable onSave, Runnable onQuit, Runnable onAdvance) {
        this.onSkip = onSkip; this.onSave = onSave; this.onQuit = onQuit; this.onAdvance = onAdvance;
        buildSpeakerTag();
        buildDialogueBox();
        buildButtonBar();
        FXGL.addUINode(speakerTag); FXGL.addUINode(dialogueBox); FXGL.addUINode(buttonBar);
    }




    private void buildButtonBar() {
        Button skipBtn = FXGL.getUIFactoryService().newButton("Skip");
        Button saveBtn = FXGL.getUIFactoryService().newButton("Save");
        Button quitBtn = FXGL.getUIFactoryService().newButton("Quit");

        ColorAdjust darken = new ColorAdjust(); darken.setBrightness(-0.4);
        skipBtn.setEffect(darken); saveBtn.setEffect(darken); quitBtn.setEffect(darken);

        skipBtn.setOnAction(e -> { if (onSkip != null) onSkip.run(); });
        saveBtn.setOnAction(e -> { if (onSave != null) onSave.run(); });
        quitBtn.setOnAction(e -> { if (onQuit != null) onQuit.run(); });

        buttonBar = new HBox(10, skipBtn, new Text("|"), saveBtn, new Text("|"), quitBtn);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setPrefWidth(BOX_WIDTH);
        buttonBar.setTranslateX(BOX_MARGIN_X);
        buttonBar.setTranslateY(FXGL.getAppHeight() - 85);
    }

    // Understand: Cleans 'Speaker:' prefixes from dialogue strings to avoid rendering name twice
    public void setLine(String speaker, String dialogue) {
        if (dialogue != null) {
            if (speaker != null && !speaker.isEmpty() && dialogue.startsWith(speaker + ": ")) {
                dialogue = dialogue.substring(speaker.length() + 2).trim();
            } else if (dialogue.contains(": ")) {
                int colonIndex = dialogue.indexOf(": ");
                String prefix = dialogue.substring(0, colonIndex).trim();
                if (!prefix.contains(" ") || speaker == null || speaker.equals("Narration")) {
                    speaker = prefix;
                    dialogue = dialogue.substring(colonIndex + 2).trim();
                }
            }
        }
        speakerText.setText(speaker == null ? "" : speaker);
        speakerTag.setVisible(speaker != null && !speaker.isEmpty());
        bodyText.setText(dialogue == null ? "" : dialogue);
    }

    public void hide() { dialogueBox.setVisible(false); speakerTag.setVisible(false); buttonBar.setVisible(false); }
    public void show() { dialogueBox.setVisible(true); buttonBar.setVisible(true); }

    // Understand: Removes UI nodes on scene exit
    public void cleanup() {
        if (dialogueBox != null) FXGL.removeUINode(dialogueBox);
        if (speakerTag != null) FXGL.removeUINode(speakerTag);
        if (buttonBar != null) FXGL.removeUINode(buttonBar);
        dialogueBox = null; speakerTag = null; buttonBar = null;
    }
}