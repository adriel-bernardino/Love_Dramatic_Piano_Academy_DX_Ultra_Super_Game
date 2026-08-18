package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.shared.AudioService;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.shared.SoundEffect;
import javafx.scene.media.AudioClip;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.shared.SettingsData;

import java.net.URL;

public class GlobalAudioManager implements AudioService {

    private static GlobalAudioManager instance;

    private MediaPlayer mediaPlayer;
    private String currentTrackName = "";
    private double currentSpeed = 1.0;
    private boolean isPaused = false;
    private double musicVolume = 0.5;
    private double sfxVolume = 0.5;
    private final SettingsManager settingsManager = new SettingsManager(); //understand: so settings save

    //understand: stores already loaded sound effects so they can play immediately
    private final Map<SoundEffect, AudioClip> soundEffects =
            new EnumMap<>(SoundEffect.class);

    private GlobalAudioManager() {
        //understand: load saved volume preferences before caching sound effects,
        //so clips get the correct volume applied on first load
        SettingsData saved = settingsManager.loadSettings();
        this.musicVolume = saved.musicVolume();
        this.sfxVolume = saved.sfxVolume();

        //understand: preload all sound effects once when the audio manager is created
        loadSoundEffects();
    }

    public static synchronized GlobalAudioManager getInstance() {
        if (instance == null) {
            instance = new GlobalAudioManager();
        }
        return instance;
    }

    private void loadSoundEffects() {

        //understand: go through every sound effect defined in the enum
        for (SoundEffect effect : SoundEffect.values()) {

            //understand: build the path to the sound file
            String path = "/assets/sfx/" + effect.getFileName();

            //understand: find the file inside the resources folder
            URL resource = getClass().getResource(path);

            if (resource == null) {
                System.err.println("SFX Error: Sound effect not found at " + path);
                continue;
            }

            //decision: AudioClip for short sounds because it can play w/o creating new MediaPlayer every time
            AudioClip clip = new AudioClip(resource.toExternalForm());
            clip.setVolume(sfxVolume);//understand: apply current sfx volume to newly loaded clip

            //understand: store the loaded clip so it can be reused immediately
            soundEffects.put(effect, clip);
        }
    }

    @Override
    public void playMusic(String trackName) {
        // Understand: If the requested track is already playing, maintain continuous playback across state transitions
        if (currentTrackName.equalsIgnoreCase(trackName) && mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            System.out.println("Audio Continuity: Keeping track playing -> " + trackName);
            return;
        }

        stopMusic();

        try {
            // Understand: Load track from resources/assets/music/
            String path = "/assets/music/" + trackName;
            URL resource = getClass().getResource(path);

            if (resource == null) {
                System.err.println("Audio Error: Track not found at " + path);
                return;
            }

            Media media = new Media(resource.toExternalForm());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setRate(currentSpeed);
            mediaPlayer.setVolume(musicVolume);//understand: apply current music volume to new track
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();

            currentTrackName = trackName;
            isPaused = false;
            System.out.println("Now Playing: " + trackName + " at " + currentSpeed + "x speed.");

        } catch (Exception e) {
            System.err.println("Audio Error: Failed to play track " + trackName + " - " + e.getMessage());
        }
    }

    public void playMainMenuTheme() {
        playMusic("Naalala Ka.mp3");
    }

    public void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
            isPaused = true;
            System.out.println("Audio Paused.");
        }
    }

    public void resumeMusic() {
        if (mediaPlayer != null && isPaused) {
            mediaPlayer.play();
            isPaused = false;
            System.out.println("Audio Resumed.");
        }
    }

    @Override
    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
            currentTrackName = "";
            isPaused = false;
            System.out.println("Audio Stopped.");
        }
    }

    @Override
    public void playSoundEffect(SoundEffect soundEffect) {

        //understand: retrieve the sound effect that was already loaded
        AudioClip clip = soundEffects.get(soundEffect);

        if (clip != null) {
            clip.play();//understand: play the preloaded sound immediately

        } else {
            System.err.println(
                    "SFX Error: No loaded sound effect for " + soundEffect
            );
        }
    }

    @Override
    public void setPlaybackSpeed(double speed) {
        this.currentSpeed = speed;
        if (mediaPlayer != null) {
            mediaPlayer.setRate(speed);
            System.out.println("Playback speed set to: " + speed + "x");
        }
    }

    @Override
    public void setMusicVolume(double volume) {
        this.musicVolume = volume;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    @Override
    public double getMusicVolume() {
        return musicVolume;
    }

    @Override
    public void setSfxVolume(double volume) {
        this.sfxVolume = volume;
        //understand: applies to every alrdy loaded clip immediately
        for (AudioClip clip : soundEffects.values()) {
            clip.setVolume(volume);
        }
    }

    @Override
    public double getSfxVolume() {
        return sfxVolume;
    }

    public void saveVolumeSettings() {
        settingsManager.saveSettings(musicVolume, sfxVolume);
    }

    // Understand: Crucial for Rhythm Game note-fall sync and timing verification
    public double getCurrentTimestampSeconds() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentTime().toSeconds();
        }
        return 0.0;
    }

    public void seekTo(double seconds) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.seconds(seconds));
        }
    }

    public boolean isPaused() {
        return isPaused;
    }

    public String getCurrentTrackName() {
        return currentTrackName;
    }
}