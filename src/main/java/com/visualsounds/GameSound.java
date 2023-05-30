package com.visualsounds;

import java.awt.*;

public class GameSound {
    public int soundId = 0;
    public Color color = Color.white;
    public String label = "";

    public GameSound() {
    }

    public GameSound(int soundId, Color color) {
        this.soundId = soundId;
        this.color = color;
        this.label = Integer.toString(soundId);
    }

}
