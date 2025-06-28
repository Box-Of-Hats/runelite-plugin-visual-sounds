package com.visualsounds;

import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;
import net.runelite.client.ui.overlay.components.LineComponent;

import javax.inject.Inject;
import java.awt.*;
import java.util.List;

public class VisualSoundsOverlay extends OverlayPanel {
    private final VisualSoundsPlugin plugin;

    @Inject
    public VisualSoundsOverlay(VisualSoundsPlugin plugin) {
        super(plugin);

        setPosition(OverlayPosition.DYNAMIC);
        setMovable(true);
        setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
        setPreferredSize(new Dimension(30, 200));
        setLayer(OverlayLayer.UNDER_WIDGETS);
        setPriority(PRIORITY_LOW);
        this.plugin = plugin;
    }

    @Override
    public Dimension render(Graphics2D graphics2D) {
        List<LayoutableRenderableEntity> renderableEntities = panelComponent.getChildren();
        renderableEntities.clear();

        List<GameSound> gameSoundList = this.plugin.gameSoundList.getGameSoundList();

        renderableEntities.add(
                LineComponent.builder()
                        .left("Visual sounds")
                        .leftColor(Color.yellow)
                        .build());

        if (this.plugin.isDisabled) {
            // Show disabled message if player is in a blocked area
            renderableEntities.add(
                    LineComponent.builder()
                            .left("Plugin disabled in this area")
                            .build());
            return super.render(graphics2D);
        }

        for (GameSound gs : gameSoundList) {
            renderableEntities.add(
                    LineComponent.builder()
                            .leftColor(gs.color).left(gs.label)
                            .build());
        }

        return super.render(graphics2D);
    }
}
