package com.visualsounds;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.AreaSoundEffectPlayed;
import net.runelite.api.events.SoundEffectPlayed;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import org.apache.commons.lang3.math.NumberUtils;

import javax.inject.Inject;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@PluginDescriptor(
        name = "Visual Sounds"
)
public class VisualSoundsPlugin extends Plugin {
    @Inject
    private Client client;

    @Inject
    private VisualSoundsConfig config;

    public GameSoundList gameSoundList = new GameSoundList();

    @Inject
    private VisualSoundsOverlay overlay;

    @Inject
    private OverlayManager overlayManager;

    private HashMap<Integer, Color> soundColors = new HashMap<>();

    private Set<Integer> ignoredSounds = new HashSet<>();

    private boolean displaySoundEffects = true;
    private boolean displayAreaEffects = false;
    private boolean showOnlyTagged = false;

    @Override
    protected void startUp() throws Exception {
        log.info("Visual sounds started!");
        this.overlayManager.add(overlay);
        this.reload();
    }

    @Subscribe
    protected void onConfigChanged(ConfigChanged configChangedEvent) {
        this.reload();
    }

    @Override
    protected void shutDown() throws Exception {
        log.info("Visual sounds stopped!");
        this.overlayManager.remove(overlay);
    }

    /**
     * Reload config options
     */
    private void reload() {
        this.gameSoundList.setMaxLength(config.soundCountLimit());
        this.gameSoundList.add(new GameSound());
        this.ignoredSounds = getNumbersFromConfig(this.config.ignoredSounds());
        this.soundColors = new HashMap<>();
        addColors(this.config.taggedSoundsCat1(), this.config.category1SoundColor());
        addColors(this.config.taggedSoundsCat2(), this.config.category2SoundColor());
        addColors(this.config.taggedSoundsCat3(), this.config.category3SoundColor());

        displaySoundEffects = config.displaySoundEffects();
        displayAreaEffects = config.displayAreaSounds();
        showOnlyTagged = config.showOnlyTagged();
    }

    @Subscribe
    public void onSoundEffectPlayed(SoundEffectPlayed soundEffectPlayed) {
        if (!displaySoundEffects)
            return;

        handleSoundEffect(soundEffectPlayed.getSoundId());
    }

    @Subscribe
    public void onAreaSoundEffectPlayed(AreaSoundEffectPlayed areaSoundEffectPlayed) {
        if (!displayAreaEffects)
            return;

        handleSoundEffect(areaSoundEffectPlayed.getSoundId());
    }

    /**
     * Handle a sound effect based on its id value
     * @param soundId The id value of the sound
     */
    private void handleSoundEffect(int soundId) {
        if (ignoredSounds.contains(soundId))
            return;
        if (showOnlyTagged && !soundColors.containsKey(soundId))
            return;

        Color soundColor = soundColors.getOrDefault(soundId, Color.white);

        gameSoundList.add(new GameSound(soundId, soundColor));
    }

    @Provides
    VisualSoundsConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(VisualSoundsConfig.class);
    }

    /**
     * Add colors from a config value to the loaded config
     *
     * @param source     The config source to use
     * @param colorToAdd The color that should be used
     */
    private void addColors(String source, Color colorToAdd) {
        Set<Integer> categorySounds = getNumbersFromConfig(source);
        for (Integer categorySound : categorySounds) {
            this.soundColors.computeIfAbsent(categorySound, o -> colorToAdd);
        }
    }

    /**
     * Get a list of integers from a source string, separated by commas
     *
     * @param source The configuration source string
     * @return A set of integers parsed from the source string
     */
    private static Set<Integer> getNumbersFromConfig(String source) {
        return Arrays.stream(source.split(","))
                .map(String::trim)
                .filter(NumberUtils::isParsable)
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }
}
