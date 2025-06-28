package com.visualsounds;

import java.awt.Color;

public class GameSound {
    public final int soundId;
    public Color color;
    public String label;

    public GameSound(int soundId, Color color) {
        this.soundId = soundId;
        this.color = color;
        this.label = Integer.toString(soundId);
    }

}
