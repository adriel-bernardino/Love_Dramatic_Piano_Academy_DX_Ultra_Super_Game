package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.shared;

//decision: enum so all sfx is defined & can be easily changed
public enum SoundEffect {

    //understand: sound used for regular button interactions
    BUTTON_CLICK("click.wav"),

    //understand: sound used when making an important story choice
    CHOICE_SELECT("choice_select.wav");

    //understand: stores the actual filename of the sound file
    private final String fileName;

    SoundEffect(String fileName) {
        this.fileName = fileName;
    }

    //understand: gives the audio manager the filename it needs to load
    public String getFileName() {
        return fileName;
    }
}

