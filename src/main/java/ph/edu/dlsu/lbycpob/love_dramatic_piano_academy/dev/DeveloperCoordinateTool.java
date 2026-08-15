package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.dev;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Optional;

public class DeveloperCoordinateTool extends GameApplication {


    /// REMEMBER (0,0) in java is the top left corner of objects

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
        FXGL.getInput().addAction(new UserAction("Click Coordinate") {
            @Override
            protected void onActionBegin() {
                double x = FXGL.getInput().getMouseXUI();
                double y = FXGL.getInput().getMouseYUI();

                // Ignore clicks if they fall on the UI buttons at the top left (Load/Quit)
                if (x < 350 && y < 60) {
                    return;
                }

                // Use standard JavaFX TextInputDialog for proper cancel/close support
                TextInputDialog dialog = new TextInputDialog("");
                dialog.setTitle("Save Coordinate");
                dialog.setHeaderText("Coordinate Clicked: X=" + x + " | Y=" + y);
                dialog.setContentText("Enter a label for this coordinate:");

                // This blocks until the user clicks OK, Cancel, or the window's X button
                Optional<String> result = dialog.showAndWait();

                result.ifPresent(name -> {
                    if (!name.trim().isEmpty()) {
                        System.out.println("Coordinate Saved -> [" + name + "] X: " + x + " | Y: " + y);
                    }
                });
            }
        }, MouseButton.PRIMARY);
    }

    @Override
    protected void initUI() {
        // ImageView to display the loaded asset
        imageView = new ImageView();
        FXGL.addUINode(imageView);

        // Rainbow CSS string
        String rainbowStyle = "-fx-background-color: linear-gradient(to right, red, orange, yellow, green, blue, indigo, violet); " +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; " +
                "-fx-border-color: white; -fx-border-width: 2px; " +
                "-fx-border-radius: 5px; -fx-background-radius: 5px;";

        // Load Asset Button
        Button loadBtn = FXGL.getUIFactoryService().newButton("Load Asset");
        loadBtn.setStyle(rainbowStyle);
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

            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                imageView.setImage(new Image(selectedFile.toURI().toString()));
            }
        });

        // Quit Button
        Button quitBtn = FXGL.getUIFactoryService().newButton("Quit");
        quitBtn.setStyle(rainbowStyle);
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