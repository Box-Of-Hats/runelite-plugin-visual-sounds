package com.visualsounds;

import java.util.ArrayList;
import java.util.List;

public class GameSoundList {
    private List<GameSound> gameSoundList = new ArrayList<>();
    private int maxLength = 10;
    /**
     * Should duplicate sounds be ignored?
     */
    private boolean dedupe = false;

    public GameSoundList(int maxLength, boolean dedupe) {
        this.maxLength = maxLength;
        this.dedupe = dedupe;
    }

    public void clear(){
        this.gameSoundList.clear();
    }

    public void add(GameSound gameSound) {
        if (dedupe) {
            boolean alreadyExists = this.gameSoundList.stream().filter(gs -> gs.soundId == gameSound.soundId).count() > 0;
            if (alreadyExists) {
                return;
            }

            this.gameSoundList.add(0, gameSound);
        } else {
            this.gameSoundList.add(0, gameSound);
        }

        if (this.gameSoundList.size() > maxLength) {
            this.gameSoundList = new ArrayList<>(gameSoundList.subList(0, maxLength));
        }
    }

    public List<GameSound> getGameSoundList() {
        return this.gameSoundList;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
}


