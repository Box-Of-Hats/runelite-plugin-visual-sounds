package com.visualsounds;

import java.util.Map;

public class SoundNames
{
	private static final String RESOURCE = "sound_names.json";

	private final Map<Integer, String> soundNames = SoundNameLoader.load(RESOURCE);

	public String GetSoundName(Integer soundId)
	{
		return this.soundNames.get(soundId);
	}
}
