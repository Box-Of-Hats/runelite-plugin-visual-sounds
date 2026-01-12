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

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof GameSound))
			return false;
		if (obj == this)
			return true;

		GameSound rhs = (GameSound) obj;
		return rhs.soundId == this.soundId
			&& rhs.color == this.color
			&& rhs.label.equals(this.label);
	}
}
