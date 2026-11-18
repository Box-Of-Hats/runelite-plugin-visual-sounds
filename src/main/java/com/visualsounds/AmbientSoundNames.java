package com.visualsounds;

import java.util.HashMap;
import java.util.Map;

public class AmbientSoundNames
{
	private final Map<Integer, String> soundNames = new HashMap<>();

	public AmbientSoundNames()
	{
		initializeSoundMap();
	}

	public String GetSoundName(Integer soundId)
	{
		return this.soundNames.get(soundId);
	}

	private void initializeSoundMap()
	{
		soundNames.put(1, "castle_drakan");
		soundNames.put(2, "sangvesti");
		soundNames.put(3, "sotfa_forest");
		soundNames.put(4, "sotfa_forest_underground");
		soundNames.put(5, "sugadinti_hideout_inner");
		soundNames.put(6, "sugadinti_hideout_outer");
		soundNames.put(7, "castle_drakan_roof");
		soundNames.put(8, "vampyrium");
		soundNames.put(9, "wyrmscraig");
		soundNames.put(10, "wyrmscraig_underground");
		soundNames.put(11, "tfa_ezekiel_finale");
		soundNames.put(12, "tfa_vault");
		soundNames.put(13, "tfa_ocellus");
		soundNames.put(14, "tfa_nexus");
		soundNames.put(15, "tfa_lobby");
		soundNames.put(16, "tfa_bork");
		soundNames.put(17, "tfa_xarth");
		soundNames.put(18, "tfa_dagonhai");
		soundNames.put(19, "tfa_jelly");
		soundNames.put(20, "tfa_moia");
		soundNames.put(21, "tfa_ezekiel");
	}

}
