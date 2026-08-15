package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.dev;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;

import java.io.File;

public class DeveloperCoordinateTool extends GameApplication {

    private ImageView imageView;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1920);
        settings.setHeight(1080);
        settings.setTitle("Developer Coordinate Tool");
        // Disable the main menu for this standalone tool
        settings.setMainMenuEnabled(false);
    }

    @Override
    protected void initInput() {
        // Track mouse clicks globally
        FXGL.getInput().addAction(new UserAction("Click Coordinate") {
            @Override
            protected void onActionBegin() {
                double x = FXGL.getInput().getMouseXUI();
                double y = FXGL.getInput().getMouseYUI();

                // Ignore clicks if they fall on the UI buttons at the top left (Load/Quit)
                if (x < 350 && y < 60) {
                    return;
                }

                // Use FXGL's built-in dialog service for the prompt
                FXGL.getDialogService().showInputBox(
                        "Coordinate Clicked: X=" + x + " | Y=" + y + "\nEnter a label for this coordinate (or hit cancel):",
                        input -> input.matches(".*"), // Allow any string
                        name -> {
                            System.out.println("Coordinate Saved -> [" + name + "] X: " + x + " | Y: " + y);
                        }
                );
            }
        }, MouseButton.PRIMARY);
    }

    @Override
    protected void initUI() {
        // ImageView to display the loaded asset
        imageView = new ImageView();
        FXGL.addUINode(imageView);

        // Load Asset Button
        Button loadBtn = FXGL.getUIFactoryService().newButton("Load Asset");
        loadBtn.setTranslateX(10);
        loadBtn.setTranslateY(10);
        loadBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Image Asset");

            // Default directly to your project's texture directory
            File defaultDir = new File("src/main/resources/assets/textures");
            if (defaultDir.exists()) {
                fileChooser.setInitialDirectory(defaultDir);
            }

            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );

            // Using null as the owner window works perfectly in FXGL
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                imageView.setImage(new Image(selectedFile.toURI().toString()));
            }
        });

        // Quit Button
        Button quitBtn = FXGL.getUIFactoryService().newButton("Quit");
        quitBtn.setTranslateX(200); // Placed to the right of the load button
        quitBtn.setTranslateY(10);
        quitBtn.setOnAction(e -> FXGL.getGameController().exit());

        FXGL.addUINode(loadBtn);
        FXGL.addUINode(quitBtn);
    }

    public static void main(String[] args) {
        launch(args);
    }
}