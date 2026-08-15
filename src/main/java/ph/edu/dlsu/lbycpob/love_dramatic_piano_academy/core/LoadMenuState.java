package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.shared.SaveData;

// Understand: Dedicated state for picking which save slot to load
public class LoadMenuState {

    private final CoreSceneManager sceneManager;
    private final SaveManager saveManager;
    private VBox uiBox;

    public LoadMenuState(CoreSceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.saveManager = new SaveManager();
    }

    public void start() {
        // Understand: Spawns the dedicated main menu background
        FXGL.spawn("background", new SpawnData(0, 0).put("imageName", "mainMenu.png"));

        uiBox = new VBox(15);
        uiBox.setTranslateX(FXGL.getAppWidth() / 2.0 - 100);
        uiBox.setTranslateY(FXGL.getAppHeight() / 2.0 - 100);

        for (int i = 1; i <= 3; i++) {
            final int slot = i;
            Button slotBtn = FXGL.getUIFactoryService().newButton("Load Slot " + slot);
            slotBtn.setOnAction(e -> {
                SaveData data = saveManager.loadGame(slot);
                if (data != null) {
                    sceneManager.switchToVisualNovelFull(data.chapter(), data.route().charAt(0), Integer.parseInt(data.line()));
                }
            });
            uiBox.getChildren().add(slotBtn);
        }

        Button cancelBtn = FXGL.getUIFactoryService().newButton("Back to Menu");
        cancelBtn.setOnAction(e -> sceneManager.switchToMainMenu());
        uiBox.getChildren().add(cancelBtn);

        FXGL.addUINode(uiBox);
    }

    public void cleanup() {
        if (uiBox != null) FXGL.removeUINode(uiBox);
    }
}