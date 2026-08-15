package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.texture.Texture;

// Understand: creates different background entities depending on the context
public class BackgroundManager implements EntityFactory {

    @Spawns("background")
    public Entity newEntity(SpawnData data) {
        // Understand: Reads "imageName" from SpawnData if present, otherwise default to "VNbgs/placeholder.png"
        String imageName = data.hasKey("imageName")
                ? data.get("imageName")
                : "VNbgs/mainMenu.png";

        // Understand: Sets background to the custom one defined in imageName
        Texture bg = FXGL.texture(imageName);

        // Understand: creates the entity, puts it at the very back using z index
        return FXGL.entityBuilder(data)
                .view(bg)
                .zIndex(-100)
                .build();
    }
}