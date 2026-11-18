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
import java.util.ArrayList;
import net.runelite.client.util.ColorUtil;

public class AmbientSoundsOverlay extends OverlayPanel {
    private static final int PANEL_WIDTH = 250;
    private static final int PANEL_BORDER = 8;
    private static final Color CONTAINER_COLOR = Color.cyan;
    private static final Color ALTERNATE_COLOR = new Color(0xFFC864);
    private static final Color GRAY_COLOR = new Color(0xA0A0A0);

    private final VisualSoundsPlugin plugin;
    public boolean displayHeader;

    @Inject
    public AmbientSoundsOverlay(VisualSoundsPlugin plugin) {
        super(plugin);

        setPosition(OverlayPosition.DYNAMIC);
        setMovable(true);
        setPosition(OverlayPosition.BOTTOM_LEFT);
        setPreferredSize(new Dimension(PANEL_WIDTH, 0));
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
		FontMetrics metrics = graphics2D.getFontMetrics();
		// The overlay is resizable and RuneLite restores the user's saved size, so wrap to the
		// width the panel will actually be drawn at, not to our default.
		Dimension panelSize = getPreferredSize();
		int maxTextWidth = (panelSize != null ? panelSize.width : PANEL_WIDTH) - PANEL_BORDER;
		for (GameSound key : sounds.keySet()) {
			List<String[]> tokens = new ArrayList<>();
			List<GameSound> subSounds = new ArrayList<>(sounds.get(key).getGameSounds());
			boolean hasSubs = !subSounds.isEmpty();

			tokens.add(new String[]{
				ColorUtil.prependColorTag(key.label, key.color) + (hasSubs ? ColorUtil.prependColorTag(",", CONTAINER_COLOR) : ""),
				key.label + (hasSubs ? "," : "")});

			for (int i = 0; i < subSounds.size(); i++) {
				GameSound gs = subSounds.get(i);
				boolean tagged = !Color.white.equals(gs.color);
				Color color = tagged ? gs.color : (i % 2 == 0 ? GRAY_COLOR : ALTERNATE_COLOR);
				String open = i == 0 ? "[" : "";
				String close = i == subSounds.size() - 1 ? "]" : ",";
				tokens.add(new String[]{
					ColorUtil.prependColorTag(open, CONTAINER_COLOR)
						+ ColorUtil.prependColorTag(gs.label, color)
						+ ColorUtil.prependColorTag(close, CONTAINER_COLOR),
					open + gs.label + close});
			}

			// Greedy wrap between tokens: an item never gets split across lines.
			StringBuilder line = new StringBuilder();
			int lineWidth = 0;
			int spaceWidth = metrics.stringWidth(" ");
			for (String[] token : tokens) {
				int width = metrics.stringWidth(token[1]);
				if (line.length() > 0 && lineWidth + spaceWidth + width > maxTextWidth) {
					renderableEntities.add(LineComponent.builder().left(line.toString()).build());
					line.setLength(0);
					lineWidth = 0;
				}
				if (line.length() > 0) {
					line.append(' ');
					lineWidth += spaceWidth;
				}
				line.append(token[0]);
				lineWidth += width;
			}
			if (line.length() > 0) {
				renderableEntities.add(LineComponent.builder().left(line.toString()).build());
			}
		}

        return super.render(graphics2D);
    }
}
