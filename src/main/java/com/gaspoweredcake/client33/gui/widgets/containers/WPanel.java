package com.gaspoweredcake.client33.gui.widgets.containers;

import com.gaspoweredcake.client33.gui.renderer.GuiRenderer;
import com.gaspoweredcake.client33.utils.render.color.Color;

/** A quiet inset surface for related controls. Child cells supply their padding. */
public class WPanel extends WVerticalList {
    private static final Color BACKGROUND = new Color(13, 14, 18, 245);
    private static final Color BORDER = new Color(48, 49, 57, 255);
    @Override protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        renderer.quad(x, y, width, height, BACKGROUND);
        renderer.quad(x, y, width, theme.scale(1), BORDER);
        renderer.quad(x, y + height - theme.scale(1), width, theme.scale(1), BORDER);
    }
}
