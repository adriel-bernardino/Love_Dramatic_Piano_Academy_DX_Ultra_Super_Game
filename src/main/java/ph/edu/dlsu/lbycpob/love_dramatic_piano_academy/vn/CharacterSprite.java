package ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.vn;

import ph.edu.dlsu.lbycpob.love_dramatic_piano_academy.core.OverlayEntity;

public class CharacterSprite extends OverlayEntity {

    private final String characterName;

    public CharacterSprite(String characterName, String initialImagePath, double x, double y) {
        super(initialImagePath, x, y);
        this.characterName = characterName;
    }

    public String getCharacterName() {
        return characterName;
    }
}