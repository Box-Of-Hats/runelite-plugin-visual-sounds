package com.visualSounds;

import java.util.ArrayList;
import java.util.List;

public class GameSoundList {
    private List<GameSound> gameSoundList = new ArrayList<>();
    private int maxLength = 10;

    public GameSoundList() {
    }

    public void add(GameSound gameSound) {
        this.gameSoundList.add(0, gameSound);
        if (this.gameSoundList.stream().count() > maxLength) {
            this.gameSoundList = this.gameSoundList.subList(0, maxLength);
        }
    }

    public List<GameSound> getGameSoundList() {
        return this.gameSoundList;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
}


