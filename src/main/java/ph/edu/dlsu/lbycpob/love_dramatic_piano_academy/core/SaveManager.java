package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core;

import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.shared.SaveData;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

// Understand: Encapsulates saving and loading using a human-readable text file format.
public class SaveManager {

    private static final String SAVE_DIRECTORY = "saves/";

    public SaveManager() {
        try {
            Files.createDirectories(Paths.get(SAVE_DIRECTORY));
        } catch (IOException e) {
            System.err.println("Warning: Could not create save directory. " + e.getMessage());
        }
    }

    public void saveGame(SaveData data) {
        String fileName = SAVE_DIRECTORY + "saveSlot_" + data.saveSlotId() + ".txt";
        Properties props = new Properties();

        // Understand: Storing the variables correlating to LoveDramaticApp's state
        props.setProperty("chapter", String.valueOf(data.chapter()));
        props.setProperty("route", data.route());
        props.setProperty("line", data.line());
        props.setProperty("saveSlotId", String.valueOf(data.saveSlotId()));

        try (FileWriter writer = new FileWriter(fileName)) {
            // Understand: Writes the properties to a text file with a timestamp comment
            props.store(writer, "Love Dramatic Piano Academy - Save Data");
            System.out.println("Successfully saved game to text file in slot: " + data.saveSlotId());
        } catch (IOException e) {
            System.err.println("Failed to write save data to slot " + data.saveSlotId() + ": " + e.getMessage());
        }
    }

    public SaveData loadGame(int saveSlotId) {
        String fileName = SAVE_DIRECTORY + "saveSlot_" + saveSlotId + ".txt";
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("Save file for slot " + saveSlotId + " does not exist.");
            return null;
        }

        Properties props = new Properties();
        try (FileReader reader = new FileReader(file)) {
            props.load(reader);

            // Understand: Extracting the string values and parsing them back into their correct types
            int chapter = Integer.parseInt(props.getProperty("chapter", "0"));
            String route = props.getProperty("route", "A");
            String line = props.getProperty("line", "0");
            int id = Integer.parseInt(props.getProperty("saveSlotId", String.valueOf(saveSlotId)));

            System.out.println("Successfully loaded game from text file slot: " + saveSlotId);
            return new SaveData(chapter, route, line, id);

        } catch (IOException | NumberFormatException e) {
            System.err.println("Failed to read save data from slot " + saveSlotId + ": " + e.getMessage());
            return null;
        }
    }
}