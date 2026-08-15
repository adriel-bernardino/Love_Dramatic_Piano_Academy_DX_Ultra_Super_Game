package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.vn;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core.CoreSceneManager;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core.GlobalAudioManager;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core.SaveManager;

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