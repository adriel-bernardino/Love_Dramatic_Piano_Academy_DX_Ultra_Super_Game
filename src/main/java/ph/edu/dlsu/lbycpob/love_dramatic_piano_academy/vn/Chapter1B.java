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
        //understand: tells the chapter which line of the story to begin from
        this.currentLineIndex = startingLine;

        //understand: remembers where the player can safely save
        this.lastCheckpointLine = startingLine;

        //understand: allows the route to be started normally
        this.hasReachedEnd = false;
        dialogueManager.build(
                //understand: refers to the current Chapter1B object & sendto skipToNextEvent method to build()
                //decision: use method reference because no extra arguments  needed
                this::skipToNextEvent,

                //understand: opens the save menu using Route B
                //decision: lambda used because save method needs specific arguments
                () -> sceneManager.switchToSaveMenu(1, 'B', lastCheckpointLine),

                //understand: sends the player back to the main menu
                //understand: create function w/ no parameters that switches to main menu when called
                () -> sceneManager.switchToMainMenu(),

                //understand: moves to the next line of the script
                this::advanceScript
        );
        //understand: prevents the first scene from appearing instantly
        isTransitioning = true;
        dialogueManager.hide();

        performFadeTransition(
                null,
                //understand: runs code after fade transition finishes
                () -> {
                    isTransitioning = false;

                    //understand: starts processing the first story line
                    processCurrentLine();
                }
        );
    }

    //understand: skips to the next rhythm section or the end of the route
    private void skipToNextEvent() {
        //understand: prevents skipping during transitions or after the route ends
        if (isTransitioning || hasReachedEnd) return;

        //understand: search only lines after the current line
        for (int i = currentLineIndex + 1; i < script.size(); i++) {

            String line = script.get(i);

            //understand: stop when the next major event is found
            if (line.contains("rhythm_start") || line.contains("route_end")) {
                currentLineIndex = i;
                processCurrentLine();
                return;
            }
        }
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
