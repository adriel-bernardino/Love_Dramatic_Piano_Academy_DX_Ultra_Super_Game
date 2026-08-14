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
        FXGL.spawn("background", new SpawnData(0, 0).put("imageName", "mainMenu.png"));

        // Understand: Use FXGL's text factory, but UNBIND the font to prevent the JavaFX crash
        titleText = FXGL.getUIFactoryService().newText("Love Dramatic Piano Academy", 100);
        titleText.fontProperty().unbind();
        titleText.setFont(Font.font("Impact", 100));
        titleText.setFill(Color.BLANCHEDALMOND);

        // Understand: Calculate horizontal center, but move the vertical position higher on the screen
        double titleCenterX = (FXGL.getAppWidth() - titleText.getLayoutBounds().getWidth()) / 2;
        double titleHigherY = (FXGL.getAppHeight() / 2.0) - 220; // Offset by 50 pixels upwards

        // Understand: Apply the translated coordinates
        titleText.setTranslateX(titleCenterX);
        titleText.setTranslateY(titleHigherY);

        // Understand: Create Buttons via FXGL UIFactoryService to keep the lightning hover effects
        Button newGameBtn = FXGL.getUIFactoryService().newButton("New Game");
        Button loadGameBtn = FXGL.getUIFactoryService().newButton("Load Game");
        Button quitBtn = FXGL.getUIFactoryService().newButton("Quit");

        // Understand: Use ColorAdjust to make them darker WITHOUT breaking the FXGL CSS animations
        ColorAdjust darkenEffect = new ColorAdjust();
        darkenEffect.setBrightness(-0.6); // Adjust from 0.0 to -1.0 to make it darker

        newGameBtn.setEffect(darkenEffect);
        loadGameBtn.setEffect(darkenEffect);
        quitBtn.setEffect(darkenEffect);

        // Understand: Set Button Actions
        newGameBtn.setOnAction(e -> {
            System.out.println("Starting New Game...");

            // Understand: Creates a new immutable record with default starting parameters
            SaveData defaultData = new SaveData(0, "None", "0", 1);
            saveManager.saveGame(defaultData);

            // Understand: Transitions to the visual novel state using the CoreSceneManager
            sceneController.switchToVisualNovel();
        });

        loadGameBtn.setOnAction(e -> {
            System.out.println("Loading Game...");

            // Understand: Loads slot 1 by default for this integration.
            // In a fully built load menu, you would list available slots here.
            int slotToLoad = 1;
            SaveData loadedData = saveManager.loadGame(slotToLoad);

            if (loadedData != null) {
                System.out.println("Game loaded successfully. Chapter: " + loadedData.chapter() + " Route: " + loadedData.route());

                // Understand: Transitions to the visual novel state using the loaded data context
                sceneController.switchToVisualNovel();
            } else {
                System.out.println("No save data found in slot " + slotToLoad + ". Please start a New Game.");
            }
        });

        quitBtn.setOnAction(e -> {
            // Understand: Safely tells FXGL to close the application
            FXGL.getGameController().exit();
        });

        // Understand: Layout the Buttons in a Vertical Box, ensuring inner items are centered
        menuBox = new VBox(15, newGameBtn, loadGameBtn, quitBtn);
        menuBox.setAlignment(Pos.CENTER);

        // Understand: Position the VBox horizontally centered, and a little below the vertical center
        menuBox.setTranslateX((FXGL.getAppWidth() / 2.0) - 100); // 100 is approx half the button width
        menuBox.setTranslateY((FXGL.getAppHeight() / 2.0) + 200); // 50 pixels below exact center

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