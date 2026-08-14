package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.shared.AudioService;

import java.net.URL;

public class GlobalAudioManager implements AudioService {

    private static GlobalAudioManager instance;

    private MediaPlayer mediaPlayer;
    private String currentTrackName = "";
    private double currentSpeed = 1.0;
    private boolean isPaused = false;

    private GlobalAudioManager() {}

    public static synchronized GlobalAudioManager getInstance() {
        if (instance == null) {
            instance = new GlobalAudioManager();
        }
        return instance;
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
    public void setPlaybackSpeed(double speed) {
        this.currentSpeed = speed;
        if (mediaPlayer != null) {
            mediaPlayer.setRate(speed);
            System.out.println("Playback speed set to: " + speed + "x");
        }
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