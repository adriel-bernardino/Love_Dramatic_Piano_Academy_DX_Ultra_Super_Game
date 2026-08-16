package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.vn;

import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core.GlobalAudioManager;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.shared.SoundEffect;

import java.util.LinkedHashMap;
import java.util.Map;

// Understand: Factory Pattern — builds an N-button center-screen overlay on demand.
// Chapters never build their own VBox of buttons anymore; they just call ChoicePrompt.show(...)
public class ChoicePrompt {

    private VBox box;
    private Rectangle darkOverlay;

    // Understand: options is label -> action, preserving insertion order (min 2, but extendable to any count)
    public void show(Map<String, Runnable> options) {
        // Understand: Creates a full-screen semi-transparent darkening backdrop behind the choice prompts
        darkOverlay = new Rectangle(FXGL.getAppWidth(), FXGL.getAppHeight(), Color.rgb(0, 0, 0, 0.75));

        box = new VBox(18);
        box.setAlignment(Pos.CENTER);

        for (Map.Entry<String, Runnable> entry : options.entrySet()) {
            Button btn = FXGL.getUIFactoryService().newButton(entry.getKey());
            btn.setOnAction(e -> {

                //understand: plays sound when the player selects a story choice
                GlobalAudioManager.getInstance()
                        .playSoundEffect(SoundEffect.CHOICE_SELECT);

                //understand: runs action associated with the selected choice
                entry.getValue().run();
            });
            box.getChildren().add(btn);
        }

        box.setTranslateX(FXGL.getAppWidth() / 2.0 - 160);
        box.setTranslateY(FXGL.getAppHeight() / 2.0 - (options.size() * 30));

        // Understand: Add the dimming overlay to the UI first so it renders behind the button menu
        FXGL.addUINode(darkOverlay);
        FXGL.addUINode(box);
    }

    public void cleanup() {
        if (darkOverlay != null) {
            FXGL.removeUINode(darkOverlay);
            darkOverlay = null;
        }
        if (box != null) {
            FXGL.removeUINode(box);
            box = null;
        }
    }

    // Convenience builder so call sites read cleanly
    public static Map<String, Runnable> options() {
        return new LinkedHashMap<>();
    }
}