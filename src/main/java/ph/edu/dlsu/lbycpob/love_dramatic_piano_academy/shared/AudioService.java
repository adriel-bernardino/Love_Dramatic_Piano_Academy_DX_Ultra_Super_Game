package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.shared;

// Understand: Dependency Inversion for audio manipulation across states[cite: 1]
public interface AudioService {
    void playMusic(String trackName);
    void setPlaybackSpeed(double speed); // Accommodates 1x, 2x, 4x
    void stopMusic();
    void playSoundEffect(SoundEffect soundEffect);//understand: allows any game state to request a sound effect

    //understand: controls volume of background music, 0.0 to 1.0
    void setMusicVolume(double volume);
    double getMusicVolume();

    //understand: controls volume of sound effects, 0.0 to 1.0
    void setSfxVolume(double volume);
    double getSfxVolume();
}