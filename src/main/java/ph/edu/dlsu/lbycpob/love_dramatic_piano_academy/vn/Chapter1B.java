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

    //understand: cleans VN UI and sprites w/o cleaning the whole game state
    private void cleanupUiOnly() {
        dialogueManager.cleanup();

        if (sprite1Overlay != null) {
            sprite1Overlay.destroy();
            sprite1Overlay = null;
        }

        if (sprite2Overlay != null) {
            sprite2Overlay.destroy();
            sprite2Overlay = null;
        }
    }

    @Override
    protected void advanceScript() {
        //understand: prevent advancing during transitions or after the route ends
        if (isTransitioning || hasReachedEnd) return;

        //understand: stops the index from going past the final script line
        if (currentLineIndex < script.size() - 1) {
            currentLineIndex++;

            //understand: process new line after advancing
            processCurrentLine();
        }
    }

    @Override
    protected void processCurrentLine() {
        //understand: gets current story line using the current index
        String line = script.get(currentLineIndex);

        //decision: checkpoints are stored in script so content and save points can be changed w/o changing the code
        if (line.contains("[CHECKPOINT]")) {
            //understand: remembers the current line as latest safe save point
            lastCheckpointLine = currentLineIndex;
        }

        //understand: checks whether this line represents a black-screen scene transition
        if (isBlackScreen(line)) {

            //understand: marks that a transition is happening
            isTransitioning = true;

            //decision: hide dialogue during transition so the old dialogue not visible while the scene changes
            dialogueManager.hide();

            //understand: hide the first character sprite if it currently exists
            if (sprite1Overlay != null) {
                sprite1Overlay.getEntity().setVisible(false);
            }
            //understand: hide the second character sprite if it currently exists
            if (sprite2Overlay != null) {
                sprite2Overlay.getEntity().setVisible(false);
            }

            performFadeTransition(
                    () -> {
                        //understand: changes background while the screen is hidden
                        checkAndSetBackground(line);

                        //understand: starts any audio specified by this script line
                        checkAndPlayAudio(line);
                    },
                    () -> {
                        //understand: marks transition as finished
                        isTransitioning = false;

                        //understand: moves to next line after the transition finishes
                        advanceScript();
                    }
            );
            //decision: stop here because the transition will handle advancing the script after fade finishes
            return;
        }
    }


    @Override
    public void cleanup() {

    }
}
