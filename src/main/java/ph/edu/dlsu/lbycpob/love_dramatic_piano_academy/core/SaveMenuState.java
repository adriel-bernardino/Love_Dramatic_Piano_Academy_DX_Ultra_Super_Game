package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.shared.SaveData;

// Understand: Dedicated state for saving to multiple slots using checkpoint data
public class SaveMenuState {

    private final CoreSceneManager sceneManager;
    private final SaveManager saveManager;
    private VBox uiBox;

    private int pendingChapterId;
    private char pendingRoute;
    private int pendingLine;

    public SaveMenuState(CoreSceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.saveManager = new SaveManager();
    }

    public void start(int chapterId, char route, int checkpointLine) {
        this.pendingChapterId = chapterId;
        this.pendingRoute = route;
        this.pendingLine = checkpointLine;

        // Understand: Spawns the dedicated main menu background
        FXGL.spawn("background", new SpawnData(0, 0).put("imageName", "mainMenu.png"));

        uiBox = new VBox(15);
        uiBox.setTranslateX(FXGL.getAppWidth() / 2.0 - 100);
        uiBox.setTranslateY(FXGL.getAppHeight() / 2.0 - 100);

        for (int i = 1; i <= 3; i++) {
            final int slot = i;
            Button slotBtn = FXGL.getUIFactoryService().newButton("Save to Slot " + slot);
            slotBtn.setOnAction(e -> {
                SaveData data = new SaveData(pendingChapterId, String.valueOf(pendingRoute), String.valueOf(pendingLine), slot);
                saveManager.saveGame(data);

                // Understand: Route back to the VN state using the stored checkpoint variables
                sceneManager.resumeVisualNovel(pendingChapterId, pendingRoute, pendingLine);
            });
            uiBox.getChildren().add(slotBtn);
        }

        Button cancelBtn = FXGL.getUIFactoryService().newButton("Cancel");
        cancelBtn.setOnAction(e -> sceneManager.resumeVisualNovel(pendingChapterId, pendingRoute, pendingLine));
        uiBox.getChildren().add(cancelBtn);

        FXGL.addUINode(uiBox);
    }

    public void cleanup() {
        if (uiBox != null) FXGL.removeUINode(uiBox);
        // Understand: The background entity is handled by CoreSceneManager's getEntitiesCopy() wipe
    }
}