package com.visualsounds;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * Loads sound id -> name maps from JSON resources bundled with the plugin.
 */
@Slf4j
final class SoundNameLoader
{
	private static final Type MAP_TYPE = new TypeToken<Map<Integer, String>>() {}.getType();

	private SoundNameLoader()
	{
	}

	static Map<Integer, String> load(String resourceName)
	{
		try (InputStream in = SoundNameLoader.class.getResourceAsStream(resourceName))
		{
			if (in == null)
			{
				log.warn("Sound name resource not found: {}", resourceName);
				return Collections.emptyMap();
			}
			try (Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8))
			{
				Map<Integer, String> names = new Gson().fromJson(reader, MAP_TYPE);
				return names != null ? names : Collections.emptyMap();
			}
		}
		catch (IOException | RuntimeException e)
		{
			log.warn("Failed to load sound names from {}", resourceName, e);
			return Collections.emptyMap();
		}
	}
}
