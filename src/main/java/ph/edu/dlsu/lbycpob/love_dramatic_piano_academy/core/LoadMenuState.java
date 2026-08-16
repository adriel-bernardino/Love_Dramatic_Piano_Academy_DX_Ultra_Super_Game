package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.shared.SaveData;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.shared.SoundEffect;

// Understand: Dedicated state for picking which save slot to load
public class LoadMenuState {

    private final CoreSceneManager sceneManager;
    private final SaveManager saveManager;
    private VBox uiBox;
    private Rectangle dimOverlay;

    public LoadMenuState(CoreSceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.saveManager = new SaveManager();
    }

    public void start() {
        // Understand: Spawns the dedicated main menu background
        FXGL.spawn("background", new SpawnData(0, 0).put("imageName", "mainMenu.png"));

        // Understand: Add a semi-transparent black overlay to dim the background
        dimOverlay = new Rectangle(FXGL.getAppWidth(), FXGL.getAppHeight(), Color.color(0, 0, 0, 0.6));
        FXGL.addUINode(dimOverlay);

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
                    GlobalAudioManager.getInstance()
                            .playSoundEffect(SoundEffect.LOAD_SUCCESS);//understand: play sfx
                }
            });
            uiBox.getChildren().add(slotBtn);
        }

        Button cancelBtn = FXGL.getUIFactoryService().newButton("Back to Menu");
        cancelBtn.setOnAction(e -> {

            //understand: plays sfx
            GlobalAudioManager.getInstance()
                    .playSoundEffect(SoundEffect.BUTTON_CLICK);

            sceneManager.switchToMainMenu();
        });
        uiBox.getChildren().add(cancelBtn);

        FXGL.addUINode(uiBox);
    }

    public void cleanup() {
        if (dimOverlay != null) FXGL.removeUINode(dimOverlay);
        if (uiBox != null) FXGL.removeUINode(uiBox);
    }
}