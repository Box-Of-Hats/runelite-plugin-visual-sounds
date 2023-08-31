package com.visualsounds;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Varbits;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.AreaSoundEffectPlayed;
import net.runelite.api.events.GameTick;
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

    private SoundNames soundNames;

    @Inject
    private VisualSoundsOverlay overlay;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private ConfigManager configManager;

    private static final String OLD_CONFIG_GROUP = "example";
    private static final String CONFIG_GROUP = "visualsounds";

    private HashMap<Integer, Color> soundColors = new HashMap<>();

    private Set<Integer> ignoredSounds = new HashSet<>();

    private boolean displaySoundEffects = true;
    private boolean displayAreaEffects = false;
    private boolean showOnlyTagged = false;

    private int regionId = -1;

    @Override
    protected void startUp() throws Exception {
        log.info("Visual sounds started!");
        this.migrateOldConfigItems();
        this.overlayManager.add(overlay);
        this.soundNames = new SoundNames();
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

    private <T> void migrateOldConfigItem(String key, Class<T> clazz) {
        T old = this.configManager.getConfiguration(OLD_CONFIG_GROUP, key, clazz);
        if (old != null) {
            log.debug("Importing config item {}: {}", key, old);
            this.configManager.setConfiguration(CONFIG_GROUP, key, old);
            this.configManager.unsetConfiguration(OLD_CONFIG_GROUP, key);
        }
    }

    private void migrateOldConfigItems() {
        this.migrateOldConfigItem("displaySoundEffects", Boolean.TYPE);
        this.migrateOldConfigItem("displayAreaSounds", Boolean.TYPE);
        this.migrateOldConfigItem("soundCountLimit", Integer.TYPE);

        this.migrateOldConfigItem("category1SoundColor", Color.class);
        this.migrateOldConfigItem("taggedSoundsCat1", String.class);
        this.migrateOldConfigItem("category2SoundColor", Color.class);
        this.migrateOldConfigItem("taggedSoundsCat2", String.class);
        this.migrateOldConfigItem("category3SoundColor", Color.class);
        this.migrateOldConfigItem("taggedSoundsCat3", String.class);

        this.migrateOldConfigItem("ignoredSounds", String.class);
        this.migrateOldConfigItem("showOnlyTagged", Boolean.TYPE);
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
    public void onGameTick(GameTick event) {
        this.regionId = WorldPoint.fromLocalInstance(client, client.getLocalPlayer().getLocalLocation()).getRegionID();
    }

    @Subscribe
    public void onSoundEffectPlayed(SoundEffectPlayed soundEffectPlayed) {
        if (!displaySoundEffects) {
            return;
        }

        handleSoundEffect(soundEffectPlayed.getSoundId());
    }

    @Subscribe
    public void onAreaSoundEffectPlayed(AreaSoundEffectPlayed areaSoundEffectPlayed) {
        if (!displayAreaEffects) {
            return;
        }

        handleSoundEffect(areaSoundEffectPlayed.getSoundId());
    }

    /**
     * Handle a sound effect based on its id value
     * @param soundId The id value of the sound
     */
    private void handleSoundEffect(int soundId) {
        if (ignoredSounds.contains(soundId) || ignoreBoss()) {
            return;
        }
        if (showOnlyTagged && !soundColors.containsKey(soundId)) {
            return;
        }

        Color soundColor = soundColors.getOrDefault(soundId, Color.white);

        GameSound gameSound = new GameSound(soundId, soundColor);

        if (config.showSoundNames()){
            // Attempt to add the name of the sound to the display
            String soundName = this.soundNames.GetSoundName(soundId);
            if (soundName != null) {
                //gameSound.label = String.format("%i (%s)",soundName, gameSound.soundId);
                gameSound.label = String.format("%s (%d)",soundName, gameSound.soundId);
            }
        }

        gameSoundList.add(gameSound);
    }

    /**
     * @return whether the plugin should be temporarily disabled to comply with third-party guidelines
     * @see <a href="https://secure.runescape.com/m=news/third-party-client-guidelines?oldschool=1">Guidelines</a>
     */
    private boolean ignoreBoss() {
        // disable plugin for Alchemical Hydra
        if (regionId == 5536) {
            return true;
        }

        // disable plugin for DT2 bosses (Vardorvis, Leviathan, Whisperer, Sucellus)
        if (regionId == 4405 || regionId == 8291 || regionId == 10595 || regionId == 12132) {
            return true;
        }

        // disable plugin for Vorkath
        if (regionId == 9023) {
            return true;
        }

        // disable plugin in Inferno & TzHaar Fight Cave
        if (regionId == 9043 || regionId == 9551) {
            return true;
        }

        // disable plugin in raids
        return client.getVarbitValue(Varbits.IN_RAID) > 0
                || client.getVarbitValue(Varbits.TOA_RAID_LEVEL) > 0
                || client.getVarbitValue(Varbits.THEATRE_OF_BLOOD) > 0;
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
