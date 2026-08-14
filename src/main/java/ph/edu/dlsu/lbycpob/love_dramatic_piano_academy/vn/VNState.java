package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.vn;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core.SaveManager;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.shared.SaveData;

import java.util.Arrays;
import java.util.List;

public class VNState {

    // Understand: Dummy script array using the exact text from Chapter 0
    private final List<String> chapter0Script = Arrays.asList(
            "In a dark moonlit music room, Kepler is waiting for his friend Roni.",
            "While waiting, he sifts through the various sheet music on top of the piano until one in particular catches his eye.",
            "It was the sheet music for Because by The Beatles."
    );

    private int currentLineIndex = 0;

    private Text dialogueText;
    private Button nextBtn;
    private Button saveQuitBtn;
    private VBox uiBox;

    private final SaveManager saveManager;

    public VNState() {
        this.saveManager = new SaveManager();
    }

    // Understand: Accepts the starting line so the Load Game feature can resume exactly where it left off
    public void start(int startingLine) {
        this.currentLineIndex = startingLine;

        // Understand: Prevents out-of-bounds if the save file has a completed chapter index
        if (currentLineIndex >= chapter0Script.size()) {
            currentLineIndex = chapter0Script.size() - 1;
        }

        // Understand: Spawn the placeholder background defined in Chapter 0 script
        FXGL.spawn("background", new SpawnData(0, 0).put("imageName", "VNbgs/placeholder.png"));

        // Understand: Initialize Dialogue Text
        dialogueText = FXGL.getUIFactoryService().newText(chapter0Script.get(currentLineIndex), 36);
        dialogueText.setFill(Color.WHITE);
        dialogueText.setWrappingWidth(1000);

        // Understand: Initialize UI Buttons
        nextBtn = FXGL.getUIFactoryService().newButton("Next Line");
        saveQuitBtn = FXGL.getUIFactoryService().newButton("Save & Quit");

        // Understand: Progress the dialogue array
        nextBtn.setOnAction(e -> {
            if (currentLineIndex < chapter0Script.size() - 1) {
                currentLineIndex++;
                dialogueText.setText(chapter0Script.get(currentLineIndex));
            } else {
                dialogueText.setText("[ End of Chapter 0 Demo ]");
                nextBtn.setDisable(true);
            }
        });

        // Understand: Package the current state into SaveData and write it using SaveManager
        saveQuitBtn.setOnAction(e -> {
            // Hardcoding Chapter 0, Route A, and Slot 1 for this test environment
            SaveData currentProgress = new SaveData(0, "A", String.valueOf(currentLineIndex), 1);
            saveManager.saveGame(currentProgress);

            System.out.println("Progress saved! Exiting game.");
            FXGL.getGameController().exit();
        });

        uiBox = new VBox(20, dialogueText, nextBtn, saveQuitBtn);
        uiBox.setTranslateX((FXGL.getAppWidth() / 2.0) - 500);
        uiBox.setTranslateY((FXGL.getAppHeight() / 2.0));

        FXGL.addUINode(uiBox);
    }

    public void cleanup() {
        if (uiBox != null) {
            FXGL.removeUINode(uiBox);
        }
    }
}