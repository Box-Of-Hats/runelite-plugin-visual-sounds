package com.visualsounds;

import com.google.gson.Gson;
import java.util.Map;

public class SoundNames
{
	private static final String RESOURCE = "sound_names.json";

	private final Map<Integer, String> soundNames;

	public SoundNames(Gson gson)
	{
		this.soundNames = SoundNameLoader.load(gson, RESOURCE);
	}

	public String GetSoundName(Integer soundId)
	{
		return this.soundNames.get(soundId);
	}
}
