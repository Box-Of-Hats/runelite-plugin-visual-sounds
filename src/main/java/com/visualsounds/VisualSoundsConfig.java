package com.visualsounds;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

import java.awt.*;

@ConfigGroup("example")
public interface VisualSoundsConfig extends Config {
    @ConfigItem(
            keyName = "displaySoundEffects",
            name = "Sound Effects",
            description = "Visualize the sound effects",
            position = 0
    )
    default boolean displaySoundEffects() {
        return true;
    }

    @ConfigItem(
            keyName = "displayAreaSounds",
            name = "Area Sounds",
            description = "Visualize the area sounds",
            position = 1
    )
    default boolean displayAreaSounds() {
        return false;
    }

    @Range(
            min = 0,
            max = 15
    )
    @ConfigItem(
            keyName = "soundCountLimit",
            name = "Sound Count Limit",
            description = "The max number of sounds to show in the overlay",
            position = 2
    )
    default int soundCountLimit() {
        return 5;
    }

    @ConfigItem(
            keyName = "category1SoundColor",
            name = "Sound Color (C1)",
            description = "The color to show tagged sounds in",
            position = 3
    )
    default Color category1SoundColor() {
        return Color.RED;
    }

    @ConfigItem(
            keyName = "taggedSoundsCat1",
            name = "Tagged Sounds (C1)",
            description = "A list of sounds that should be recoloured. Separate ids with commas (,)",
            position = 4
    )
    default String taggedSoundsCat1() {
        return "";
    }

    @ConfigItem(
            keyName = "category2SoundColor",
            name = "Sound Color (C2)",
            description = "The color to show tagged sounds in",
            position = 5
    )
    default Color category2SoundColor() {
        return Color.BLUE;
    }

    @ConfigItem(
            keyName = "taggedSoundsCat2",
            name = "Tagged Sounds (C2)",
            description = "A list of sounds that should be recoloured. Separate ids with commas (,)",
            position = 6
    )
    default String taggedSoundsCat2() {
        return "";
    }

    @ConfigItem(
            keyName = "category3SoundColor",
            name = "Sound Color (C3)",
            description = "The color to show tagged sounds in",
            position = 7
    )
    default Color category3SoundColor() {
        return Color.YELLOW;
    }

    @ConfigItem(
            keyName = "taggedSoundsCat3",
            name = "Tagged Sounds (C3)",
            description = "A list of sounds that should be recoloured. Separate ids with commas (,)",
            position = 8
    )
    default String taggedSoundsCat3() {
        return "";
    }

    @ConfigItem(
            keyName = "ignoredSounds",
            name = "Ignored Sounds",
            description = "A list of sounds that should be ignored. Separate ids with commas (,)",
            position = 9
    )
    default String ignoredSounds() {
        return "";
    }
}
