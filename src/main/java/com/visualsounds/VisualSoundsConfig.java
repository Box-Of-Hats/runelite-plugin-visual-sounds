package com.visualsounds;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

import java.awt.*;

@ConfigGroup("visualsounds")
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

    @ConfigItem(
            keyName = "displayAmbientSounds",
            name="Ambient Sounds",
            description = "Visualize ambient sounds",
            position = 2
    )
    default boolean displayAmbientSounds() { return false; }

    @Range(
            min = 0,
            max = 15
    )
    @ConfigItem(
            keyName = "soundCountLimit",
            name = "Sound Count Limit",
            description = "The max number of sounds to show in the overlay",
            position = 3
    )
    default int soundCountLimit() {
        return 5;
    }

    @ConfigItem(
            keyName = "category1SoundColor",
            name = "Sound Color (C1)",
            description = "The color to show tagged sounds in",
            position = 4
    )
    default Color category1SoundColor() {
        return Color.RED;
    }

    @ConfigItem(
            keyName = "taggedSoundsCat1",
            name = "Tagged Sounds (C1)",
            description = "A list of sounds that should be recoloured. Separate ids with commas (,)",
            position = 5
    )
    default String taggedSoundsCat1() {
        return "";
    }

    @ConfigItem(
            keyName = "category2SoundColor",
            name = "Sound Color (C2)",
            description = "The color to show tagged sounds in",
            position = 6
    )
    default Color category2SoundColor() {
        return Color.BLUE;
    }

    @ConfigItem(
            keyName = "taggedSoundsCat2",
            name = "Tagged Sounds (C2)",
            description = "A list of sounds that should be recoloured. Separate ids with commas (,)",
            position = 7
    )
    default String taggedSoundsCat2() {
        return "";
    }

    @ConfigItem(
            keyName = "category3SoundColor",
            name = "Sound Color (C3)",
            description = "The color to show tagged sounds in",
            position = 8
    )
    default Color category3SoundColor() {
        return Color.YELLOW;
    }

    @ConfigItem(
            keyName = "taggedSoundsCat3",
            name = "Tagged Sounds (C3)",
            description = "A list of sounds that should be recoloured. Separate ids with commas (,)",
            position = 9
    )
    default String taggedSoundsCat3() {
        return "";
    }

    @ConfigItem(
            keyName = "category4SoundColor",
            name = "Sound Color (C4)",
            description = "The color to show tagged sounds in",
            position = 10
    )
    default Color category4SoundColor() {
        return Color.GREEN;
    }

    @ConfigItem(
            keyName = "taggedSoundsCat4",
            name = "Tagged Sounds (C4)",
            description = "A list of sounds that should be recoloured. Separate ids with commas (,)",
            position = 11
    )
    default String taggedSoundsCat4() {
        return "";
    }

    @ConfigItem(
            keyName = "category5SoundColor",
            name = "Sound Color (C5)",
            description = "The color to show tagged sounds in",
            position = 12
    )
    default Color category5SoundColor() {
        return Color.MAGENTA;
    }

    @ConfigItem(
            keyName = "taggedSoundsCat5",
            name = "Tagged Sounds (C5)",
            description = "A list of sounds that should be recoloured. Separate ids with commas (,)",
            position = 13
    )
    default String taggedSoundsCat5() {
        return "";
    }

    @ConfigItem(
            keyName = "category6SoundColor",
            name = "Sound Color (C6)",
            description = "The color to show tagged sounds in",
            position = 14
    )
    default Color category6SoundColor() {
        return Color.CYAN;
    }


    @ConfigItem(
            keyName = "taggedSoundsCat6",
            name = "Tagged Sounds (C6)",
            description = "A list of sounds that should be recoloured. Separate ids with commas (,)",
            position = 15
    )
    default String taggedSoundsCat6() {
        return "";
    }

    @ConfigItem(
            keyName = "ignoredSounds",
            name = "Ignored Sounds",
            description = "A list of sounds that should be ignored. Separate ids with commas (,)",
            position = 16
    )
    default String ignoredSounds() {
        return "";
    }

    @ConfigItem(
            keyName = "showOnlyTagged",
            name = "Show Only Tagged Sounds",
            description = "Show only the sounds that are recoloured.",
            position = 17
    )
    default boolean showOnlyTagged() {
        return false;
    }

    @ConfigItem(
            keyName = "showSoundNames",
            name = "Show Sound Names",
            description = "Show the names of sounds where possible",
            position = 18
    )
    default boolean showSoundNames() {
        return true;
    }

    @ConfigItem(
            keyName = "customSoundLabels",
            name = "Custom Sound Labels",
            description = "A list of custom sound labels. Format: soundId:label, separated by a new line (e.g. 369:Cow attacks)",
            position = 19
    )
    default String customSoundLabels() {
        return "";
    }
}
