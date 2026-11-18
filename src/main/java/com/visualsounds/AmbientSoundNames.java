package com.visualsounds;

import java.util.Map;

public class AmbientSoundNames
{
	private static final String RESOURCE = "ambient_sound_names.json";

	private final Map<Integer, String> soundNames = SoundNameLoader.load(RESOURCE);

	public String GetSoundName(Integer soundId)
	{
		return this.soundNames.get(soundId);
	}
}
