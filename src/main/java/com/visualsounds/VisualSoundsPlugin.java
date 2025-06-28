package com.visualsounds;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.AmbientSoundEffect;
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

    public GameSoundList gameSoundList = new GameSoundList(15, false);
    public GameSoundList ambientSounds = new GameSoundList(30, true);

    /**
     * Is the plugin currently disabled due to the player being in a blocked area?
     */
    public boolean isDisabled = false;

    private SoundNames soundNames;

    /**
     * Overlay for area and sound effects
     */
    @Inject
    private VisualSoundsOverlay visualSoundsOverlay;

    /**
     * Overlay for ambient sounds
     */
    @Inject
    private AmbientSoundsOverlay ambientSoundsOverlay;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private ConfigManager configManager;

    private static final String OLD_CONFIG_GROUP = "example";
    private static final String CONFIG_GROUP = "visualsounds";

    private HashMap<Integer, Color> soundColors = new HashMap<>();
    private HashMap<Integer, String> customLabels = new HashMap<>();
    private Set<Integer> ignoredSounds = new HashSet<>();

    private boolean displaySoundEffects = true;
    private boolean displayAreaEffects = false;
    private boolean displayAmbientEffects = false;
    private boolean showOnlyTagged = false;

    private int regionId = -1;

    @Override
    protected void startUp() throws Exception {
        log.info("Visual sounds started!");
        this.migrateOldConfigItems();
        this.overlayManager.add(visualSoundsOverlay);
        this.overlayManager.add(ambientSoundsOverlay);
        this.soundNames = new SoundNames();
        this.reload();
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged configChangedEvent) {
        if ("visualsounds".equals(configChangedEvent.getGroup())) {
            this.reload();
        }
    }

    @Override
    protected void shutDown() throws Exception {
        log.info("Visual sounds stopped!");
        this.overlayManager.remove(visualSoundsOverlay);
        this.overlayManager.remove(ambientSoundsOverlay);
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
        //this.gameSoundList.add(new GameSound());
        this.ignoredSounds = getNumbersFromConfig(this.config.ignoredSounds());
        this.soundColors = new HashMap<>();
        // Parse custom labels using colon as the delimiter.
        this.customLabels = parseCustomLabels(config.customSoundLabels());
        addColors(this.config.taggedSoundsCat1(), this.config.category1SoundColor());
        addColors(this.config.taggedSoundsCat2(), this.config.category2SoundColor());
        addColors(this.config.taggedSoundsCat3(), this.config.category3SoundColor());
        addColors(this.config.taggedSoundsCat4(), this.config.category4SoundColor());
        addColors(this.config.taggedSoundsCat5(), this.config.category5SoundColor());
        addColors(this.config.taggedSoundsCat6(), this.config.category6SoundColor());

        displaySoundEffects = config.displaySoundEffects();
        displayAreaEffects = config.displayAreaSounds();
        displayAmbientEffects = config.displayAmbientSounds();
        showOnlyTagged = config.showOnlyTagged();

        //Hide ambient sounds overlay if not enabled in config
        if (this.config.displayAmbientSounds()) {
            this.overlayManager.add(ambientSoundsOverlay);
        } else {
            this.overlayManager.remove(ambientSoundsOverlay);
        }

        //Hide sounds overlay if not enabled in config
        if (this.config.displayAreaSounds() || this.config.displaySoundEffects()){
            this.overlayManager.add(visualSoundsOverlay);
        } else {
            this.overlayManager.remove(visualSoundsOverlay);
        }

    }

    private static Set<Integer> getNumbersFromConfig(String source) {
        return Arrays.stream(source.split(","))
                .map(String::trim)
                .filter(NumberUtils::isParsable)
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }

    // New method to parse custom labels using colon as the delimiter.
    private HashMap<Integer, String> parseCustomLabels(String configValue) {
        HashMap<Integer, String> labelsMap = new HashMap<>();
        if (configValue == null || configValue.isEmpty()) {
            return labelsMap;
        }
        for (String entry : configValue.split("[,\\n]+")) {
            String[] parts = entry.split(":");
            if (parts.length == 2 && NumberUtils.isDigits(parts[0].trim())) {
                labelsMap.put(Integer.parseInt(parts[0].trim()), parts[1].trim());
            }
        }
        return labelsMap;
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        this.regionId = WorldPoint.fromLocalInstance(client, client.getLocalPlayer().getLocalLocation()).getRegionID();

        // Ambient sound effects don't play in the same manner as other sounds. Handle them every tick
        if (displayAmbientEffects) {
            this.ambientSounds.clear();
            for (AmbientSoundEffect ambientSoundEffect : client.getAmbientSoundEffects()) {
                handleAmbientSoundEffect(ambientSoundEffect);
            }
        }

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
     *
     * @param soundId The id value of the sound
     */
    private void handleSoundEffect(int soundId) {
        GameSound gameSound = getGameSound(soundId);

        if (gameSound != null) {
            gameSoundList.add(gameSound);
        }
    }

    /**
     * Handle a given ambient sound effect.
     *
     * @param ambientSoundEffect The sound effect to handle
     */
    private void handleAmbientSoundEffect(AmbientSoundEffect ambientSoundEffect) {
        int[] backgroundSoundEffectIds = ambientSoundEffect.getBackgroundSoundEffectIds();
        if (backgroundSoundEffectIds != null) {
            for (int backgroundSoundEffectId : backgroundSoundEffectIds) {
                GameSound gameSound = getGameSound(backgroundSoundEffectId);
                if (gameSound != null) {
                    ambientSounds.add(gameSound);
                }
            }
        }

        GameSound gameSound = getGameSound(ambientSoundEffect.getSoundEffectId());
        if (gameSound != null) {
            ambientSounds.add(gameSound);
        }
    }

    /**
     * Get a GameSound for a given sound ID. Null will be returned if the sound ID cannot be handled
     */
    private GameSound getGameSound(int soundId) {
        if (ignoreBoss()) {
            isDisabled = true;
            return null;
        }
        isDisabled = false;

        if (ignoredSounds.contains(soundId)) {
            return null;
        }

        if (showOnlyTagged && !soundColors.containsKey(soundId)) {
            return null;
        }

        if (soundId == 0) {
            return null;
        }

        Color soundColor = soundColors.getOrDefault(soundId, Color.white);
        GameSound gameSound = new GameSound(soundId, soundColor);

        // If a custom label exists, use it (displaying only the custom text).
        if (customLabels.containsKey(soundId)) {
            gameSound.label = customLabels.get(soundId);
        } else if (config.showSoundNames()) {
            String soundName = this.soundNames.GetSoundName(soundId);
            if (soundName != null) {
                gameSound.label = String.format("%s (%d)", soundName, soundId);
            }
        }

        return gameSound;
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
}
