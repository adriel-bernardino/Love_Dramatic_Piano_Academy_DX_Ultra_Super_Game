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

    //understand: reads sprite tags from script line and updates the character sprites on screen
    private void processSprites(String line) {

        //understand: looks for a SPRITE_1 tag and gets the image path stored in it
        String sprite1Path = extractTag(line, "SPRITE_1");

        if (sprite1Path != null) {

            //understand: remove "textures/" cuz CharacterSprite uses a path relative to the textures folder
            sprite1Path = sprite1Path.replace("textures/", "");

            if (sprite1Overlay == null) {

                //understand: create first character sprite if one does not exist yet
                sprite1Overlay = new CharacterSprite(
                        "Sprite1",
                        sprite1Path,
                        200,
                        -350
                );

                //understand: scale sprite to make it fit the visual novel screen
                sprite1Overlay.setScale(0.75, 0.75);
                sprite1Overlay.popIn(500.0);

            } else {

                //decision: reuse existing sprite object instead of creating new one every time the char's expression changes
                sprite1Overlay.setSprite(sprite1Path);

                //understand: make sure existing sprite is visible
                sprite1Overlay.getEntity().setVisible(true);
            }

        } else if (sprite1Overlay != null) {

            //understand: if the current line has no SPRITE_1 tag, remove the previous sprite
            sprite1Overlay.destroy();

            //understand: set the reference to null because sprite no longer exists
            sprite1Overlay = null;
        }


        //understand: looks for a SPRITE_2 tag and gets its image path
        String sprite2Path = extractTag(line, "SPRITE_2");

        if (sprite2Path != null) {

            //understand: remove "textures/" because CharacterSprite uses a textures-relative path
            sprite2Path = sprite2Path.replace("textures/", "");

            if (sprite2Overlay == null) {

                //understand: create the second character sprite if does not exist yet
                sprite2Overlay = new CharacterSprite(
                        "Sprite2",
                        sprite2Path,
                        800,
                        -350
                );

                //understand: scale the sprite to fit the screen
                sprite2Overlay.setScale(0.75, 0.75);
                sprite2Overlay.popIn(500.0);

            } else {
                sprite2Overlay.setSprite(sprite2Path);
                sprite2Overlay.getEntity().setVisible(true);
            }

        } else if (sprite2Overlay != null) {
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

        //understand: checks ifline contains a background tag and updates it
        checkAndSetBackground(line);

        //understand: checks whether line contains an audio tag and plays it
        checkAndPlayAudio(line);

        //understand: checks line for character sprite tags and updates the sprites
        processSprites(line);

        //understand: makes the dialogue UI visible for normal dialogue
        dialogueManager.show();

        //understand: checks whether line contains a special event such as rhythm_start
        String eventType = extractTag(line, "EVENT_TYPE");

        if ("rhythm_start".equals(eventType)) {

            //decision: remove the VN UI before switching to the rhythm game so two game states do not display on top of each other
            cleanupUiOnly();

            //understand: creates a list to hold dialogue that will appear during rhythm mode
            List<String> rhythmDialogue = new ArrayList<>();
            String[] parts = line.split("\\| ");

            //decision: starts at index 1 because index 0 contains the line/event information
            for (int i = 1; i < parts.length; i++) {
                rhythmDialogue.add(parts[i].trim());
            }

            //understand: gives rhythm mode default dialogue if the script provides none
            if (rhythmDialogue.isEmpty()) {
                rhythmDialogue.add("System: Rhythm sequence initiating...");
            }

            //understand: gets audio file specified by the current script line
            String audioTrack = extractTag(line, "AUDIO");

            if (audioTrack != null) {
                //understand: remove "music/" because RhythmState only needs the filename
                audioTrack = audioTrack.replace("music/", "");

            } else {
                //understand: uses a fallback song if no audio file was specified
                audioTrack = "becauseIntro.MP3";
            }
            sceneManager.switchToRhythmGame(
                    rhythmDialogue,

                    //understand: tells rhythm mode which VN line to return to afterward
                    currentLineIndex + 1,

                    //understand: tells rhythm mode which song to play
                    audioTrack
            );

            //decision: stop processing this VN line because control has been handed to rhythm game
            return;

        } else if ("route_end".equals(eventType)) {

            //understand: marks that a scene transition is currently happening
            isTransitioning = true;

            //decision: prevent chapter from processing any lines after the route_end event
            hasReachedEnd = true;

            //understand: hides dialogue before the ending transition
            dialogueManager.hide();

            performFadeTransition(
                    () -> {

                        //understand: remove the first character from the scene
                        if (sprite1Overlay != null) {
                            sprite1Overlay.destroy();
                            sprite1Overlay = null;
                        }

                        //understand: remove the second character from the scene
                        if (sprite2Overlay != null) {
                            sprite2Overlay.destroy();
                            sprite2Overlay = null;
                        }

                        //decision: replace the previous scene with a simple background
                        //for the route-ending message
                        com.almasb.fxgl.dsl.FXGL.spawn(
                                "background",
                                new SpawnData(0, 0)
                                        .put("imageName", "VNbgs/dialogueBG.png")
                        );
                    },
                    () -> {

                        //understand: the fade transition has finished
                        isTransitioning = false;

                        //understand: show the dialogue UI again for the ending message
                        dialogueManager.show();

                        //understand: display a fixed message indicating that the route has ended
                        dialogueManager.setLine(
                                "System",
                                "This is the end of the currently available chapters. Thank you for playing! Please use the buttons below to Save or Quit."
                        );
                    }
            );

            //decision: stop processing because route_end is the final state of this chapter
            return;
        }
        String[] parts = line.split("\\| ");

        //understand: gets the dialogue portion after the "|" separator
        String dialogue = parts.length > 1 ? parts[1] : "";

        //understand: checks whether the dialogue contains a speaker name
        String speaker = dialogue.contains(":")
                //understand: splits only at the first ":" so the rest remains part of the dialogue
                ? dialogue.split(":", 2)[0]

                //understand: uses Narration when no speaker name is provided
                : "Narration";

        //understand: sends the speaker and dialogue text to the dialogue UI
        dialogueManager.setLine(speaker, dialogue);
    }


    @Override
    public void cleanup() {
        //understand: remove all VN UI and sprites when leaving Route B
        cleanupUiOnly();
    }
}
