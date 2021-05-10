package com.visualSounds;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;

import net.runelite.api.*;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;
import net.runelite.client.ui.overlay.components.LineComponent;

public class VisualSoundsOverlay extends OverlayPanel {
    private final VisualSoundsPlugin plugin;
    private final VisualSoundsConfig config;
    private final Client client;

    @Inject
    public VisualSoundsOverlay(VisualSoundsPlugin plugin, Client client, VisualSoundsConfig config) {
        super(plugin);

        setPosition(OverlayPosition.DYNAMIC);
        setPosition(OverlayPosition.DETACHED);
        setPosition(OverlayPosition.TOP_LEFT);
        setPreferredSize(new Dimension(100, 600));
        this.plugin = plugin;
        this.client = client;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D graphics2D) {
        List<LayoutableRenderableEntity> renderableEntities = panelComponent.getChildren();
        renderableEntities.clear();

        List<GameSound> gameSoundList = this.plugin.gameSoundList.getGameSoundList();

        for (GameSound gs : gameSoundList) {
            if (shouldIgnore(gs.soundId)) {
                continue;
            }

            Color soundColor = getColor(gs.soundId);
            renderableEntities.add(
                    LineComponent.builder()
                            .leftColor(soundColor).left(gs.actorName)
                            .rightColor(soundColor).right(gs.soundId + "")
                            .build());
        }

        return super.render(graphics2D);
    }

    private boolean shouldIgnore(int soundId) {
        String soundIdAsString = soundId + "";
        return Arrays.stream(config.ignoredSounds().split(",")).anyMatch(soundIdAsString::equals);
    }

    private Color getColor(int soundId) {
        String soundIdAsString = soundId + "";

        Boolean isIdCat1 = Arrays.stream(config.taggedSoundsCat1().split(",")).anyMatch(soundIdAsString::equals);
        if (isIdCat1) {
            return config.category1SoundColor();
        }
        Boolean isIdCat2 = Arrays.stream(config.taggedSoundsCat2().split(",")).anyMatch(soundIdAsString::equals);
        if (isIdCat2) {
            return config.category2SoundColor();
        }

        Boolean isIdCat3 = Arrays.stream(config.taggedSoundsCat3().split(",")).anyMatch(soundIdAsString::equals);
        if (isIdCat3) {
            return config.category3SoundColor();
        }

        return Color.white;
    }
}
