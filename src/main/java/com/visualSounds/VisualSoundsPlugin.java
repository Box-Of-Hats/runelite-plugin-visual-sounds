package com.visualSounds;

import com.google.inject.Provides;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.SoundEffectPlayed;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

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

    @Override
    protected void startUp() throws Exception {
        log.info("Visual sounds started!");
        this.overlayManager.add(overlay);
        this.gameSoundList.setMaxLength(config.soundCountLimit());
    }

    @Subscribe
    protected void onConfigChanged(ConfigChanged configChangedEvent) {
        this.gameSoundList.setMaxLength(config.soundCountLimit());
        this.gameSoundList.add(new GameSound());
    }

    @Override
    protected void shutDown() throws Exception {
        log.info("Visual sounds stopped!");
        this.overlayManager.remove(overlay);
    }

    @Subscribe
    public void onSoundEffectPlayed(SoundEffectPlayed soundEffectPlayed) {
        int soundId = soundEffectPlayed.getSoundId();
        Actor actor = soundEffectPlayed.getSource();
        String actorName = "-";
        if (actor != null) {
            actorName = actor.getName();
        }
        gameSoundList.add(new GameSound(actorName, soundId));
    }

    @Provides
    VisualSoundsConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(VisualSoundsConfig.class);
    }
}

