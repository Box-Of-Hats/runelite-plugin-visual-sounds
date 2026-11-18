package com.visualsounds;

import com.google.gson.Gson;
import java.util.Map;

public class AmbientSoundNames
{
	private static final String RESOURCE = "ambient_sound_names.json";

	private final Map<Integer, String> soundNames;

	public AmbientSoundNames(Gson gson)
	{
		this.soundNames = SoundNameLoader.load(gson, RESOURCE);
	}

	public String GetSoundName(Integer soundId)
	{
		return this.soundNames.get(soundId);
	}
}
