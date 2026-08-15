package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.vn;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import javafx.animation.FadeTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core.CoreSceneManager;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core.GlobalAudioManager;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core.SaveManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Understand: Base structure enforcing linear chapter execution so you never have to revisit old chapters once finished
public abstract class AbstractChapter {

    protected final CoreSceneManager sceneManager;
    protected final GlobalAudioManager audioManager;
    protected final SaveManager saveManager;

    protected List<String> script;
    protected int currentLineIndex = 0;

    // Understand: Tracks the last safe line to save the game (checkpoint system)
    protected int lastCheckpointLine = 0;

    public AbstractChapter(CoreSceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.audioManager = GlobalAudioManager.getInstance();
        this.saveManager = new SaveManager();
    }

    public abstract void start(int startingLine);
    protected abstract void advanceScript();
    protected abstract void processCurrentLine();
    public abstract void cleanup();

    // Understand: Dynamically loads the script text files into the standard list format to eliminate hardcoding
    protected void loadScript(String filename) {
        script = new ArrayList<>();
        try {
            InputStream is = getClass().getResourceAsStream("/assets/text/" + filename);
            if (is == null) {
                System.err.println("Script missing at: /assets/text/" + filename);
                return;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            boolean inEvents = false;
            boolean inChoices = false;

            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();

                // Understand: Parse sections based on header tags
                if (trimmed.startsWith("EVENTS:")) { inEvents = true; continue; }
                if (trimmed.startsWith("CHOICES:")) { inEvents = false; inChoices = true; continue; }
                if (trimmed.startsWith("SCENE:") || trimmed.startsWith("CHARACTERS:") || trimmed.startsWith("ID ")) continue;

                if (inEvents && !trimmed.isEmpty()) {
                    if (trimmed.startsWith("[")) {
                        script.add(trimmed);
                    } else {
                        // Understand: It's a dialogue line; append it to the preceding tag line separated by |
                        int lastIdx = script.size() - 1;
                        script.set(lastIdx, script.get(lastIdx) + " | " + trimmed);
                    }
                } else if (inChoices && !trimmed.isEmpty()) {
                    script.add(trimmed); // Understand: Store choice prompts sequentially
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to read script: " + filename);
            e.printStackTrace();
        }
    }

    // Understand: A unified fade transition to handle "Black Screen" events and chapter spacing without breaking BackgroundManager
    protected void performFadeTransition(Runnable onMiddle, Runnable onFinished) {
        Rectangle fadeRect = new Rectangle(FXGL.getAppWidth(), FXGL.getAppHeight(), Color.BLACK);
        fadeRect.setOpacity(0);
        FXGL.addUINode(fadeRect);

        FadeTransition ftIn = new FadeTransition(Duration.seconds(1.2), fadeRect);
        ftIn.setFromValue(0.0);
        ftIn.setToValue(1.0);

        FadeTransition ftOut = new FadeTransition(Duration.seconds(1.2), fadeRect);
        ftOut.setFromValue(1.0);
        ftOut.setToValue(0.0);

        ftIn.setOnFinished(e -> {
            if (onMiddle != null) onMiddle.run();
            // Understand: Pause in total blackness for a moment before fading back in
            FXGL.getGameTimer().runOnceAfter(ftOut::play, Duration.seconds(1.0));
        });

        ftOut.setOnFinished(e -> {
            FXGL.removeUINode(fadeRect);
            if (onFinished != null) onFinished.run();
        });

        ftIn.play();
    }

    // Understand: Detects if a black screen background is requested
    protected boolean isBlackScreen(String line) {
        String bgPath = extractTag(line, "BG");
        return bgPath != null && bgPath.toLowerCase().contains("blackscreen");
    }

    // Understand: Helper method to extract data from your bracket tags like [BG: path.png]
    protected String extractTag(String line, String tagName) {
        Pattern pattern = Pattern.compile("\\[" + tagName + ":\\s*(.*?)\\]");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    // Understand: Automatically updates the background if a [BG: ...] tag is present
    protected void checkAndSetBackground(String line) {
        String bgPath = extractTag(line, "BG");
        if (bgPath != null) {
            // Remove 'textures/' from the path as FXGL assumes the textures directory
            bgPath = bgPath.replace("textures/", "");
            FXGL.spawn("background", new SpawnData(0, 0).put("imageName", bgPath));
        }
    }

    // Understand: Automatically plays audio if an [AUDIO: ...] tag is present
    protected void checkAndPlayAudio(String line) {
        String audioPath = extractTag(line, "AUDIO");
        if (audioPath != null) {
            // Remove 'music/' from the path as GlobalAudioManager assumes the music directory
            audioPath = audioPath.replace("music/", "");
            audioManager.playMusic(audioPath);
        }
    }
}