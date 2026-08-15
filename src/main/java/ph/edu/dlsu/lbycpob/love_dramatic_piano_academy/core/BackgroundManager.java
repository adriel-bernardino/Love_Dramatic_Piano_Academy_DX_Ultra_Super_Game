package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.texture.Texture;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

// Understand: creates background entities and provides overlay control methods without breaking FXGL factory spawning
public class BackgroundManager implements EntityFactory {

    private Entity currentEntity;
    private Texture currentTexture;
    private String imagePath;

    @Spawns("background")
    public Entity newEntity(SpawnData data) {
        // Understand: Reads "imageName" from SpawnData if present, otherwise default to "VNbgs/mainMenu.png"
        this.imagePath = data.hasKey("imageName")
                ? data.get("imageName")
                : "VNbgs/mainMenu.png";

        this.currentTexture = FXGL.texture(imagePath);

        // Understand: creates the entity, puts it at the very back using z index
        this.currentEntity = FXGL.entityBuilder(data)
                .view(currentTexture)
                .zIndex(-100)
                .build();

        return this.currentEntity;
    }

    // Understand: Swaps the texture dynamically while retaining entity properties
    public void setSprite(String newImagePath) {
        if (this.currentEntity == null || this.imagePath.equalsIgnoreCase(newImagePath)) return;

        this.imagePath = newImagePath;
        Texture newTexture = FXGL.texture(newImagePath);
        this.currentEntity.getViewComponent().clearChildren();
        this.currentEntity.getViewComponent().addChild(newTexture);
        this.currentTexture = newTexture;
    }

    public void setPosition(double x, double y) {
        if (currentEntity != null) {
            currentEntity.setPosition(x, y);
        }
    }

    public void setScale(double scaleX, double scaleY) {
        if (currentEntity != null) {
            currentEntity.setScaleX(scaleX);
            currentEntity.setScaleY(scaleY);
        }
    }

    // Understand: Fade-In animation effect
    public void fadeIn(double durationMs) {
        if (currentEntity == null) return;
        currentEntity.getViewComponent().getParent().setOpacity(0.0);
        FadeTransition ft = new FadeTransition(Duration.millis(durationMs), currentEntity.getViewComponent().getParent());
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    // Understand: Fade-Out animation effect
    public void fadeOut(double durationMs, Runnable onFinished) {
        if (currentEntity == null) return;
        FadeTransition ft = new FadeTransition(Duration.millis(durationMs), currentEntity.getViewComponent().getParent());
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setOnFinished(e -> {
            if (onFinished != null) onFinished.run();
        });
        ft.play();
    }

    // Understand: Pop-In visual effect
    public void popIn(double durationMs) {
        if (currentEntity == null) return;
        currentEntity.setScaleX(0.2);
        currentEntity.setScaleY(0.2);
        ScaleTransition st = new ScaleTransition(Duration.millis(durationMs), currentEntity.getViewComponent().getParent());
        st.setFromX(0.2);
        st.setFromY(0.2);
        st.setToX(1.0);
        st.setToY(1.0);
        st.play();
    }

    public void destroy() {
        if (currentEntity != null) {
            currentEntity.removeFromWorld();
            currentEntity = null;
        }
    }

    public Entity getEntity() {
        return currentEntity;
    }
}