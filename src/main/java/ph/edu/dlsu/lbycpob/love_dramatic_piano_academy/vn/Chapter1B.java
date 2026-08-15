package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.vn;

import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core.CoreSceneManager;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core.CoreSceneManager;
import com.almasb.fxgl.entity.SpawnData;
import java.util.ArrayList;
import java.util.List;

public class Chapter1B extends AbstractChapter{

    private final DialogueManager dialogueManager = new DialogueManager();//understand: manages & displays the dialogue
    private CharacterSprite sprite1Overlay;//understand: stores 1st char sprite shown on screen
    private CharacterSprite sprite2Overlay;//understand: stores 2nd char sprite shown on screen
    private boolean isTransitioning = false;//understand: stop player from skipping while the scene is changing
    private boolean hasReachedEnd = false; //understand: so does not go past end

    //understand: which story file to load
    public Chapter1B(CoreSceneManager sceneManager) {
        super(sceneManager);
        loadScript("Chapter1B");
    }


    @Override
    public void start(int startingLine) {

    }

    @Override
    protected void advanceScript() {

    }

    @Override
    protected void processCurrentLine() {

    }

    @Override
    public void cleanup() {

    }
}
