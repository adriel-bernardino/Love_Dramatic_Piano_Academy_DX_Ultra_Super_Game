package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.vn;

import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.LinkedHashMap;
import java.util.Map;

// Understand: Factory Pattern — builds an N-button center-screen overlay on demand.
// Chapters never build their own VBox of buttons anymore; they just call ChoicePrompt.show(...)
public class ChoicePrompt {

    private VBox box;

    // Understand: options is label -> action, preserving insertion order (min 2, but extendable to any count)
    public void show(Map<String, Runnable> options) {
        box = new VBox(18);
        box.setAlignment(Pos.CENTER);

        for (Map.Entry<String, Runnable> entry : options.entrySet()) {
            Button btn = FXGL.getUIFactoryService().newButton(entry.getKey());
            btn.setOnAction(e -> entry.getValue().run());
            box.getChildren().add(btn);
        }

        box.setTranslateX(FXGL.getAppWidth() / 2.0 - 160);
        box.setTranslateY(FXGL.getAppHeight() / 2.0 - (options.size() * 30));

        FXGL.addUINode(box);
    }

    public void cleanup() {
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