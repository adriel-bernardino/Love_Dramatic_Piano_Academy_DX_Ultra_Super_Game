package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.shared;

//understand: immutable value object tracking saved audio volume preferences
public record SettingsData(double musicVolume, double sfxVolume) {}