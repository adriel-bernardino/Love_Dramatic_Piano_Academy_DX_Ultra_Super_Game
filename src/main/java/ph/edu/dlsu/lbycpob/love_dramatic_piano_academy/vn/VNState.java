package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.vn;

import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core.CoreSceneManager;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core.GlobalAudioManager;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.shared.SoundEffect;

// Understand: Acts purely as a bridge linking the Main Menu/Load Menu to the correct linear Chapter Class
public class VNState {

    private final CoreSceneManager sceneManager;
    private AbstractChapter currentChapterLogic;

    public VNState(CoreSceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    // Understand: Determines which linear chapter to inject and execute
    public void start(int chapterId, char route, int startingLine) {
        if (chapterId == 0) {
            currentChapterLogic = new ChapterIntro(sceneManager);
        } else if (chapterId == 1 && route == 'A') {
            currentChapterLogic = new Chapter1A(sceneManager);
        }
        else if (chapterId == 1 && route == 'B') {
            currentChapterLogic = new Chapter1B(sceneManager);
        } else {
            GlobalAudioManager.getInstance()
                    .playSoundEffect(SoundEffect.FAILED);//understand: play sfx
            System.err.println("Chapter/Route combination not found!");
            return;
        }

        currentChapterLogic.start(startingLine);
    }

    public void cleanup() {
        if (currentChapterLogic != null) {
            currentChapterLogic.cleanup();
            currentChapterLogic = null;
        }
    }
}