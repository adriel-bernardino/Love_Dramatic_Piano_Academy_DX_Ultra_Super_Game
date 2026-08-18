package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core;

import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.shared.SoundEffect;

//understand: overlay for adjusting audio volume. reusable from any screen
public class SettingsPanel {

    private Rectangle dimOverlay;
    private VBox uiBox;

    public void show(Runnable onClose) {
        //understand: dims whatever is currently on screen behind the panel
        dimOverlay = new Rectangle(FXGL.getAppWidth(), FXGL.getAppHeight(), Color.color(0, 0, 0, 0.6));
        FXGL.addUINode(dimOverlay);

        Text title = new Text("Settings");

        Text musicLabel = new Text("Music Volume");
        Slider musicSlider = new Slider(0, 1, GlobalAudioManager.getInstance().getMusicVolume());
        musicSlider.valueProperty().addListener((obs, oldV, newV) ->
                GlobalAudioManager.getInstance().setMusicVolume(newV.doubleValue()));

        Text sfxLabel = new Text("SFX Volume");
        Slider sfxSlider = new Slider(0, 1, GlobalAudioManager.getInstance().getSfxVolume());
        sfxSlider.valueProperty().addListener((obs, oldV, newV) ->
                GlobalAudioManager.getInstance().setSfxVolume(newV.doubleValue()));

        Button closeBtn = FXGL.getUIFactoryService().newButton("Close");
        closeBtn.setOnAction(e -> {
            GlobalAudioManager.getInstance().playSoundEffect(SoundEffect.BUTTON_CLICK);
            hide();
            if (onClose != null) onClose.run();
        });

        uiBox = new VBox(15, title, musicLabel, musicSlider, sfxLabel, sfxSlider, closeBtn);
        uiBox.setAlignment(Pos.CENTER);
        uiBox.setTranslateX(FXGL.getAppWidth() / 2.0 - 150);
        uiBox.setTranslateY(FXGL.getAppHeight() / 2.0 - 120);

        FXGL.addUINode(uiBox);
    }

    public void hide() {
        if (dimOverlay != null) FXGL.removeUINode(dimOverlay);
        if (uiBox != null) FXGL.removeUINode(uiBox);
        dimOverlay = null;
        uiBox = null;
    }
}