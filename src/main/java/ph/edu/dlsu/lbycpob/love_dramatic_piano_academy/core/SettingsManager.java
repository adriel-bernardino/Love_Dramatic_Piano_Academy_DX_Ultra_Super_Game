package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core;

import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.shared.SettingsData;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

// Understand: Encapsulates saving/loading volume prefs
public class SettingsManager {

    private static final String SETTINGS_DIRECTORY = "saves/";
    private static final String SETTINGS_FILE = SETTINGS_DIRECTORY + "settings.txt";

    public SettingsManager() {
        try {
            Files.createDirectories(Paths.get(SETTINGS_DIRECTORY));
        } catch (IOException e) {
            System.err.println("Warning: Could not create settings directory. " + e.getMessage());
        }
    }

    public void saveSettings(double musicVolume, double sfxVolume) {
        Properties props = new Properties();
        props.setProperty("musicVolume", String.valueOf(musicVolume));
        props.setProperty("sfxVolume", String.valueOf(sfxVolume));

        try (FileWriter writer = new FileWriter(SETTINGS_FILE)) {
            props.store(writer, "Love Dramatic Piano Academy - Settings");
        } catch (IOException e) {
            System.err.println("Failed to write settings: " + e.getMessage());
        }
    }

    public SettingsData loadSettings() {
        File file = new File(SETTINGS_FILE);

        //understand: defaults used if no settings file exists yet
        if (!file.exists()) {
            return new SettingsData(0.5, 0.5);
        }

        Properties props = new Properties();
        try (FileReader reader = new FileReader(file)) {
            props.load(reader);
            double musicVolume = Double.parseDouble(props.getProperty("musicVolume", "0.5"));
            double sfxVolume = Double.parseDouble(props.getProperty("sfxVolume", "0.5"));
            return new SettingsData(musicVolume, sfxVolume);
        } catch (IOException | NumberFormatException e) {
            System.err.println("Failed to read settings, using defaults: " + e.getMessage());
            return new SettingsData(0.5, 0.5);
        }
    }
}