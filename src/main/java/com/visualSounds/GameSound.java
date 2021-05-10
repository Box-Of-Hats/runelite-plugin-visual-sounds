package com.visualSounds;

public class GameSound {
    public String actorName = "-";
    public int soundId = 0;

    public GameSound(){
    }
    public GameSound(String actorName, int soundId) {
        this.actorName = actorName;
        this.soundId = soundId;
    }
}
