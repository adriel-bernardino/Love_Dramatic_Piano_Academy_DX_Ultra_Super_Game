package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.shared.SaveData;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.shared.SoundEffect;

public class MainMenuState {
    private Text titleText;
    private VBox menuBox;

    // Understand: Holds a reference to the controller interface
    private final CoreSceneManager sceneController;

    // Understand: Instance of our new SaveManager to handle file operations
    private final SaveManager saveManager;

    // Understand: Requires the controller to be passed in when created
    public MainMenuState(CoreSceneManager sceneController) {
        this.sceneController = sceneController;
        this.saveManager = new SaveManager();
    }

    public void start() {
        // Understand: Create Background using BackgroundManager entity world spawner
        FXGL.spawn("background", new SpawnData(0, 0).put("imageName", "title_screen.png"));

        // Understand: Use FXGL's text factory, but UNBIND the font to prevent the JavaFX crash
        titleText = FXGL.getUIFactoryService().newText("Love Dramatic Piano Academy", 100);
        titleText.fontProperty().unbind();
        titleText.setFont(Font.font("Georgia", 36));
        titleText.setFill(Color.BLANCHEDALMOND);

        double titleX = 336;
        double titleY = 285;

        // Understand: Apply the translated coordinates
        titleText.setTranslateX(titleX);
        titleText.setTranslateY(titleY);

        // Understand: Create Buttons via FXGL UIFactoryService to keep the lightning hover effects
        Button newGameBtn = FXGL.getUIFactoryService().newButton("New Game");
        Button loadGameBtn = FXGL.getUIFactoryService().newButton("Load Game");
        Button quitBtn = FXGL.getUIFactoryService().newButton("Quit");

        String buttonStyle =
                "-fx-background-color: white;" +
                "-fx-text-fill: black;" +
                "-fx-font-size: 18px;" +
                "-fx-font-family: 'Georgia';" +
                "-fx-background-radius: 5px;" +
                "-fx-padding: 8px 20px;";

        newGameBtn.setStyle(buttonStyle);
        loadGameBtn.setStyle(buttonStyle);
        quitBtn.setStyle(buttonStyle);

        // Understand: Set Button Actions
        newGameBtn.setOnAction(e -> {
            GlobalAudioManager.getInstance()
                    .playSoundEffect(SoundEffect.BUTTON_CLICK);//understand: play sfx

            System.out.println("Starting New Game...");

            // Understand: Creates a new immutable record with default starting parameters
            SaveData defaultData = new SaveData(0, "None", "0", 1);
            saveManager.saveGame(defaultData);

            // Understand: Transitions to the visual novel state using the CoreSceneManager
            sceneController.switchToVisualNovel();
        });

        loadGameBtn.setOnAction(e -> {
            GlobalAudioManager.getInstance()
                    .playSoundEffect(SoundEffect.BUTTON_CLICK);//understand: play sfx
            System.out.println("Opening Load Menu...");
            // Understand: Properly routes to the dedicated Load Menu State and STOPS.
            // The LoadMenuState itself will handle the actual loading logic now.
            sceneController.switchToLoadMenu();
        });

        quitBtn.setOnAction(e -> {
            GlobalAudioManager.getInstance()
                    .playSoundEffect(SoundEffect.BUTTON_CLICK);//understand: play sfx
            // Understand: Safely tells FXGL to close the application
            FXGL.getGameController().exit();
        });

        // Understand: Layout the Buttons in a Vertical Box, ensuring inner items are centered
        menuBox = new VBox(15, newGameBtn, loadGameBtn, quitBtn);
        menuBox.setAlignment(Pos.CENTER);

        // Understand: Position the VBox horizontally centered, and a little below the vertical center
        menuBox.setTranslateX(446);
        menuBox.setTranslateY(560);

        // Understand: Add UI elements to Scene
        FXGL.addUINode(titleText);
        FXGL.addUINode(menuBox);
    }

    public void cleanup() {
        // Understand: Removes title and buttons when transitioning out
        if (titleText != null) {
            FXGL.removeUINode(titleText);
        }
        if (menuBox != null) {
            FXGL.removeUINode(menuBox);
        }
    }
}