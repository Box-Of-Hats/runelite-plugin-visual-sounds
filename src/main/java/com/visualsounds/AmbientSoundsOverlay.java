package com.visualsounds;

import java.util.HashMap;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;
import net.runelite.client.ui.overlay.components.LineComponent;

import javax.inject.Inject;
import java.awt.*;
import java.util.List;

public class AmbientSoundsOverlay extends OverlayPanel {
    private final VisualSoundsPlugin plugin;
    public boolean displayHeader;

    @Inject
    public AmbientSoundsOverlay(VisualSoundsPlugin plugin) {
        super(plugin);

        setPosition(OverlayPosition.DYNAMIC);
        setMovable(true);
        setPosition(OverlayPosition.BOTTOM_LEFT);
        setPreferredSize(new Dimension(30, 200));
        setLayer(OverlayLayer.UNDER_WIDGETS);
        setPriority(PRIORITY_LOW);
        this.plugin = plugin;
        displayHeader = plugin.displayOverlayHeaders;
    }

    @Override
    public Dimension render(Graphics2D graphics2D) {
        List<LayoutableRenderableEntity> renderableEntities = panelComponent.getChildren();
        renderableEntities.clear();


        if (displayHeader) {
            renderableEntities.add(
                    LineComponent.builder()
                            .left("Ambient sounds")
                            .leftColor(Color.yellow)
                            .build());
        }

        if (this.plugin.isDisabled) {
            // Show disabled message if player is in a blocked area
            renderableEntities.add(
                    LineComponent.builder()
                            .left("Plugin disabled in this area")
                            .build());
            return super.render(graphics2D);
        }

		HashMap<GameSound, GameSoundList> sounds = this.plugin.ambientSounds;
		for (GameSound key : sounds.keySet()) {
			renderableEntities.add(
				LineComponent.builder()
					.leftColor(key.color).left(key.label)
					.build());

			// these are sub ambient sounds
			Iterable<GameSound> gameSoundList = sounds.get(key).getGameSounds();
			for (GameSound gs : gameSoundList) {
				renderableEntities.add(
					LineComponent.builder()
						.leftColor(gs.color).right(gs.label)
						.build());
			}
		}

        return super.render(graphics2D);
    }
}
