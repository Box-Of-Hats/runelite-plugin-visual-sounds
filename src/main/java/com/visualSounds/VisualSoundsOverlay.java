package com.visualSounds;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;
import net.runelite.client.ui.overlay.components.LineComponent;

import javax.inject.Inject;
import java.awt.*;
import java.util.List;

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
            renderableEntities.add(
                    LineComponent.builder()
                            .leftColor(gs.color).right(gs.soundId + "")
                            .build());
        }

        return super.render(graphics2D);
    }
}
