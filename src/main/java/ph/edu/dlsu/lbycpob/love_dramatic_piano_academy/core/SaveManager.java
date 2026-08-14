package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core;

import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.shared.SaveData;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

// Understand: Encapsulates the reading and writing of multiple SaveData files using robust exception handling
public class SaveManager {

    private static final String SAVE_DIRECTORY = "saves/";

    public SaveManager() {
        // Understand: Ensure the saves directory exists upon initialization
        try {
            Files.createDirectories(Paths.get(SAVE_DIRECTORY));
        } catch (IOException e) {
            System.err.println("Warning: Could not create save directory. " + e.getMessage());
        }
    }

    // Understand: Saves the provided SaveData object to a file. The file name is dynamically generated based on the saveSlotId.
    public void saveGame(SaveData data) {
        // Understand: Uses the saveSlotId from the SaveData record to support multiple save files
        String fileName = SAVE_DIRECTORY + "saveSlot_" + data.saveSlotId() + ".dat";

        // Understand: try-with-resources automatically closes the streams, even if an exception occurs
        try (FileOutputStream fileOut = new FileOutputStream(fileName);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            out.writeObject(data);
            System.out.println("Successfully saved game to slot: " + data.saveSlotId());

        } catch (IOException e) {
            System.err.println("Failed to write save data to slot " + data.saveSlotId() + ": " + e.getMessage());
        }
    }


    // Understand: Loads the SaveData object from the specified slot ID. Returns null if the file doesn't exist or if an error occurs.
    public SaveData loadGame(int saveSlotId) {
        String fileName = SAVE_DIRECTORY + "saveSlot_" + saveSlotId + ".dat";
        File file = new File(fileName);

        // Understand: Defensive check to fail fast if the file doesn't exist
        if (!file.exists()) {
            System.out.println("Save file for slot " + saveSlotId + " does not exist.");
            return null;
        }

        try (FileInputStream fileIn = new FileInputStream(fileName);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            // Understand: Deserializes the object and casts it back to the SaveData record
            SaveData loadedData = (SaveData) in.readObject();
            System.out.println("Successfully loaded game from slot: " + saveSlotId);
            return loadedData;

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to read save data from slot " + saveSlotId + ": " + e.getMessage());
            return null;
        }
    }

    // Understand: Optional utility to delete a specific save slot if the user chooses to overwrite/clear it.
    public boolean deleteSave(int saveSlotId) {
        String fileName = SAVE_DIRECTORY + "saveSlot_" + saveSlotId + ".dat";
        File file = new File(fileName);

        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
}