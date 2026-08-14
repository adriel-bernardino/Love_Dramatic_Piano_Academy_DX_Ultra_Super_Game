package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MainMenuState {
    private Text titleText;
    private VBox menuBox;

    // Understand: Holds a reference to the controller interface
    private final CoreSceneManager sceneController;

    // Understand: NEW CONSTRUCTOR - Requires the controller to be passed in when created
    public MainMenuState(CoreSceneManager sceneController) {
        this.sceneController = sceneController;
    }

    public void start() {
        // Understand: Create Background using BackgroundManager entity world spawner
        FXGL.spawn("background", new SpawnData(0, 0).put("imageName", "mainMenu.png"));

        // Understand: Setup Title via FXGL UIFactoryService
        titleText = FXGL.getUIFactoryService().newText("OOP RHYTHM NOVEL", 40);
        titleText.setTranslateX(200);
        titleText.setTranslateY(200);

        // Understand: Create Buttons via FXGL UIFactoryService
        Button newGameBtn = FXGL.getUIFactoryService().newButton("New Game");
        Button loadGameBtn = FXGL.getUIFactoryService().newButton("Load Game");
        Button quitBtn = FXGL.getUIFactoryService().newButton("Quit");

        // Understand: Set Button Actions
        newGameBtn.setOnAction(e -> {
            System.out.println("Starting New Game...");
            cleanup(); // Remove the menu UI
            // TODO: Add logic here to start your game level/scene
        });

        loadGameBtn.setOnAction(e -> {
            System.out.println("Loading Game...");
            // TODO: Add logic here to load saved game data
        });

        quitBtn.setOnAction(e -> {
            // Understand: Safely tells FXGL to close the application
            FXGL.getGameController().exit();
        });

        // Understand: Layout the Buttons in a Vertical Box
        menuBox = new VBox(15, newGameBtn, loadGameBtn, quitBtn);
        menuBox.setTranslateX(280);
        menuBox.setTranslateY(260);

        // Understand: Add UI elements to Scene (Background is handled via World Entity z-index)
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