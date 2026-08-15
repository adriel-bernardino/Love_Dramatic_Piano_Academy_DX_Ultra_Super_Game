package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

public abstract class OverlayEntity {

    protected Entity entity;
    protected Texture currentTexture;
    protected String imagePath;

    // Understand: Store the target scale so animations know what size to return to instead of defaulting to 1.0
    protected double targetScaleX = 1.0;
    protected double targetScaleY = 1.0;

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

        // Understand: Re-apply targeted scaling onto newly swapped textures so they don't shrink
        newTexture.setScaleX(targetScaleX);
        newTexture.setScaleY(targetScaleY);

        this.entity.getViewComponent().clearChildren();
        this.entity.getViewComponent().addChild(newTexture);
        this.currentTexture = newTexture;
    }

    public void setPosition(double x, double y) {
        entity.setPosition(x, y);
    }

    // Understand: Sets the base scale and updates the texture directly
    public void setScale(double scaleX, double scaleY) {
        this.targetScaleX = scaleX;
        this.targetScaleY = scaleY;
        currentTexture.setScaleX(scaleX);
        currentTexture.setScaleY(scaleY);
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

    // Understand: Pop-In visual effect that now respects the target scale
    public void popIn(double durationMs) {
        currentTexture.setScaleX(0.2);
        currentTexture.setScaleY(0.2);

        ScaleTransition st = new ScaleTransition(Duration.millis(durationMs), currentTexture);
        st.setFromX(0.2);
        st.setFromY(0.2);
        st.setToX(targetScaleX);
        st.setToY(targetScaleY);
        st.play();
    }

    public void destroy() {
        entity.removeFromWorld();
    }

    public Entity getEntity() {
        return entity;
    }
}