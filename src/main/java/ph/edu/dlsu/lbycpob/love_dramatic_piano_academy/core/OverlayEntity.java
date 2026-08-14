package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

public abstract class OverlayEntity {

    protected Entity entity;
    protected Texture currentTexture;
    protected String imagePath;

    public OverlayEntity(String initialImagePath, double x, double y) {
        this.imagePath = initialImagePath;
        this.currentTexture = FXGL.texture(initialImagePath);

        this.entity = FXGL.entityBuilder()
                .at(x, y)
                .view(currentTexture)
                .zIndex(10) // Rendered on top of backgrounds
                .buildAndAttach();
    }

 
}