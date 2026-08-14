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

    // Understand: Swaps the texture dynamically while retaining entity properties
    public void setSprite(String newImagePath) {
        if (this.imagePath.equalsIgnoreCase(newImagePath)) return;

        this.imagePath = newImagePath;
        Texture newTexture = FXGL.texture(newImagePath);
        this.entity.getViewComponent().clearChildren();
        this.entity.getViewComponent().addChild(newTexture);
        this.currentTexture = newTexture;
    }

    public void setPosition(double x, double y) {
        entity.setPosition(x, y);
    }

    public void setScale(double scaleX, double scaleY) {
        entity.setScaleX(scaleX);
        entity.setScaleY(scaleY);
    }

    // Understand: Fade-In animation effect
    public void fadeIn(double durationMs) {
        entity.getViewComponent().getParent().setOpacity(0.0);
        FadeTransition ft = new FadeTransition(Duration.millis(durationMs), entity.getViewComponent().getParent());
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    // Understand: Fade-Out animation effect
    public void fadeOut(double durationMs, Runnable onFinished) {
        FadeTransition ft = new FadeTransition(Duration.millis(durationMs), entity.getViewComponent().getParent());
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setOnFinished(e -> {
            if (onFinished != null) onFinished.run();
        });
        ft.play();
    }

    // Understand: Pop-In visual effect
    public void popIn(double durationMs) {
        entity.setScaleX(0.2);
        entity.setScaleY(0.2);
        ScaleTransition st = new ScaleTransition(Duration.millis(durationMs), entity.getViewComponent().getParent());
        st.setFromX(0.2);
        st.setFromY(0.2);
        st.setToX(1.0);
        st.setToY(1.0);
        st.play();
    }

    public void destroy() {
        entity.removeFromWorld();
    }

    public Entity getEntity() {
        return entity;
    }
}