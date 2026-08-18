package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.vn;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.time.TimerAction;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core.GlobalAudioManager;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.shared.SoundEffect;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core.SettingsPanel;

// Understand: Single reusable owner of dialogue UI and speaker tag management
public class DialogueManager {

    private static final double BOX_MARGIN_X = 60, BOX_WIDTH = 1800, BOX_HEIGHT = 260, BOX_BOTTOM_MARGIN = 40, TEXT_PADDING = 40;
    private static final double NORMAL_SPEED_SEC = 0.025, FAST_SPEED_SEC = 0.005;

    private StackPane dialogueBox;
    private VBox speakerTag;
    private Text speakerText, bodyText;
    private HBox buttonBar;

    // Understand: Top-right visual controls for quick manual navigation and automated fast forwarding
    private Button nextBtn, fastForwardBtn, settingsBtn;
    private boolean isFastForwarding = false;

    private String targetText = "";
    private int charIndex = 0;
    private TimerAction typewriterTimer;
    private Runnable onSkip, onSave, onQuit, onAdvance;

    public DialogueManager() {}

    public void build(Runnable onSkip, Runnable onSave, Runnable onQuit, Runnable onAdvance) {
        this.onSkip = onSkip; this.onSave = onSave; this.onQuit = onQuit; this.onAdvance = onAdvance;
        buildSpeakerTag(); buildDialogueBox(); buildButtonBar();
        FXGL.addUINode(speakerTag); FXGL.addUINode(dialogueBox); FXGL.addUINode(buttonBar);
    }

    private void buildSpeakerTag() {
        speakerText = new Text("");
        speakerText.setFill(Color.WHITE);
        speakerText.setFont(Font.font("Palatino", javafx.scene.text.FontWeight.BOLD, 22));

        speakerTag = new VBox(speakerText);
        speakerTag.setAlignment(Pos.CENTER_LEFT);
        speakerTag.setPadding(new Insets(10, 24, 10, 24));
        speakerTag.setStyle("-fx-background-color: rgba(60,60,60,0.85); -fx-background-radius: 6 6 0 0;");
        speakerTag.setTranslateX(BOX_MARGIN_X + 20);
        speakerTag.setTranslateY(FXGL.getAppHeight() - BOX_HEIGHT - BOX_BOTTOM_MARGIN - 48);
    }

    private void buildDialogueBox() {
        bodyText = new Text("");
        bodyText.setFill(Color.BLACK);
        bodyText.setFont(Font.font("Palatino", 26));
        bodyText.setWrappingWidth(BOX_WIDTH - TEXT_PADDING * 2);

        StackPane.setAlignment(bodyText, Pos.TOP_LEFT);
        StackPane.setMargin(bodyText, new Insets(TEXT_PADDING + 10, TEXT_PADDING, TEXT_PADDING, TEXT_PADDING));

        // Understand: Uses standard JavaFX buttons to allow full custom inline styling without FXGL property bindings
        nextBtn = new Button("Next ►");
        fastForwardBtn = new Button("Fast Forward ►►");

        applyTopButtonStyle(nextBtn, false);
        applyTopButtonStyle(fastForwardBtn, false);

        nextBtn.setOnAction(e -> {
            e.consume();
            GlobalAudioManager.getInstance()
                    .playSoundEffect(SoundEffect.BUTTON_CLICK);//understand: play sfx
            handleAdvance();
        });
        fastForwardBtn.setOnAction(e -> {
            e.consume();
            GlobalAudioManager.getInstance()
                    .playSoundEffect(SoundEffect.BUTTON_CLICK);//understand: play sfx

            isFastForwarding = !isFastForwarding;
            applyTopButtonStyle(fastForwardBtn, isFastForwarding);
            if (isFastForwarding) {
                if (charIndex < targetText.length()) startTypewriter();
                else if (onAdvance != null) onAdvance.run();
            }
        });

        HBox topRightControls = new HBox(8, nextBtn, fastForwardBtn);
        topRightControls.setAlignment(Pos.TOP_RIGHT);
        StackPane.setAlignment(topRightControls, Pos.TOP_RIGHT);
        StackPane.setMargin(topRightControls, new Insets(12, 20, 0, 0));

        dialogueBox = new StackPane(bodyText, topRightControls);
        dialogueBox.setPrefSize(BOX_WIDTH, BOX_HEIGHT);
        dialogueBox.setStyle("-fx-background-color: rgba(235,235,235,0.92); -fx-background-radius: 12;");
        dialogueBox.setTranslateX(BOX_MARGIN_X);
        dialogueBox.setTranslateY(FXGL.getAppHeight() - BOX_HEIGHT - BOX_BOTTOM_MARGIN);
        dialogueBox.setOnMouseClicked(e -> {
            //understand: play sfx
            GlobalAudioManager.getInstance()
                    .playSoundEffect(SoundEffect.BUTTON_CLICK);

            handleAdvance();
        });
    }

    private void applyTopButtonStyle(Button btn, boolean active) {
        String bg = active ? "rgba(200, 200, 200, 0.95)" : "rgba(235, 235, 235, 0.92)";
        btn.setStyle("-fx-background-color: " + bg + "; -fx-text-fill: #000000; -fx-font-family: 'Palatino'; -fx-font-size: 13px; -fx-border-color: rgba(160, 160, 160, 0.5); -fx-border-radius: 4; -fx-background-radius: 4; -fx-padding: 4 10 4 10;");
    }

    private void buildButtonBar() {
        Button skipBtn = FXGL.getUIFactoryService().newButton("Skip");
        Button saveBtn = FXGL.getUIFactoryService().newButton("Save");
        Button quitBtn = FXGL.getUIFactoryService().newButton("Quit");

        ColorAdjust darken = new ColorAdjust(); darken.setBrightness(-0.4);
        skipBtn.setEffect(darken); saveBtn.setEffect(darken); quitBtn.setEffect(darken);

        skipBtn.setOnAction(e -> {
            GlobalAudioManager.getInstance()
                    .playSoundEffect(SoundEffect.BUTTON_CLICK);//understand: play sfx
            if (onSkip != null) onSkip.run();
        });
        saveBtn.setOnAction(e -> {
            GlobalAudioManager.getInstance()
                    .playSoundEffect(SoundEffect.BUTTON_CLICK);//understand: play sfx
            if (onSave != null) onSave.run();
        });
        quitBtn.setOnAction(e -> {
            GlobalAudioManager.getInstance()
                    .playSoundEffect(SoundEffect.BUTTON_CLICK);//understand: play sfx
            if (onQuit != null) onQuit.run();
        });

        settingsBtn = FXGL.getUIFactoryService().newButton("Settings");
        settingsBtn.setEffect(darken);
        settingsBtn.setOnAction(e -> {
            GlobalAudioManager.getInstance()
                    .playSoundEffect(SoundEffect.BUTTON_CLICK);//understand: play sfx
            new SettingsPanel().show(null);
        });

        buttonBar = new HBox(10, skipBtn, new Text("|"), saveBtn, new Text("|"), quitBtn, new Text("|"), settingsBtn);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.setPrefWidth(BOX_WIDTH);
        buttonBar.setTranslateX(BOX_MARGIN_X);
        buttonBar.setTranslateY(FXGL.getAppHeight() - 85);
    }

    public void setLine(String speaker, String dialogue) {
        stopTypewriter();
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

        this.targetText = dialogue == null ? "" : dialogue;
        this.charIndex = 0;
        this.bodyText.setText("");
        if (!targetText.isEmpty()) startTypewriter();
    }

    // Understand: Controls typewriter interval speed and automatically triggers scene advancement when fast forwarding
    private void startTypewriter() {
        stopTypewriter();
        double interval = isFastForwarding ? FAST_SPEED_SEC : NORMAL_SPEED_SEC;
        typewriterTimer = FXGL.getGameTimer().runAtInterval(() -> {
            if (charIndex < targetText.length()) {
                charIndex++;
                bodyText.setText(targetText.substring(0, charIndex));
            } else {
                stopTypewriter();
                if (isFastForwarding && onAdvance != null) {
                    FXGL.getGameTimer().runOnceAfter(onAdvance, Duration.seconds(0.15));
                }
            }
        }, Duration.seconds(interval));
    }

    private void handleAdvance() {
        if (charIndex < targetText.length()) finishTyping();
        else if (onAdvance != null) onAdvance.run();
    }

    private void finishTyping() {
        stopTypewriter();
        charIndex = targetText.length();
        bodyText.setText(targetText);
    }

    private void stopTypewriter() {
        if (typewriterTimer != null) { typewriterTimer.expire(); typewriterTimer = null; }
    }

    public void hide() { stopTypewriter(); dialogueBox.setVisible(false); speakerTag.setVisible(false); buttonBar.setVisible(false); }
    public void show() { dialogueBox.setVisible(true); buttonBar.setVisible(true); }

    public void cleanup() {
        stopTypewriter();
        if (dialogueBox != null) FXGL.removeUINode(dialogueBox);
        if (speakerTag != null) FXGL.removeUINode(speakerTag);
        if (buttonBar != null) FXGL.removeUINode(buttonBar);
        dialogueBox = null; speakerTag = null; buttonBar = null;
    }
}