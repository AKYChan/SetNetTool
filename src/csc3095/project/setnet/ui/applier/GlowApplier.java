package csc3095.project.setnet.ui.applier;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public class GlowApplier
{
    private final static double DEFAULT_X_AND_Y_OFFSET = 0.0;
    private final static double DEFAULT_GLOW_WIDTH_AND_HEIGHT = 20.0;
    private final static double DEFAULT_NO_GLOW_WIDTH_AND_HEIGHT = 0.0;
    private final static Color DEFAULT_HIGHLIGHT_COLOUR = Color.BLUE;

    public static DropShadow highlightSelected()
    {
        DropShadow glow = initialiseGlow();
        glow.setWidth(DEFAULT_GLOW_WIDTH_AND_HEIGHT);
        glow.setHeight(DEFAULT_GLOW_WIDTH_AND_HEIGHT);

        return glow;
    }

    public static DropShadow unhighlightSelected()
    {
        DropShadow glow = initialiseGlow();
        glow.setWidth(DEFAULT_NO_GLOW_WIDTH_AND_HEIGHT);
        glow.setHeight(DEFAULT_NO_GLOW_WIDTH_AND_HEIGHT);

        return glow;
    }

    private static DropShadow initialiseGlow()
    {
        DropShadow glow = new DropShadow();
        glow.setColor(DEFAULT_HIGHLIGHT_COLOUR);
        glow.setOffsetX(DEFAULT_X_AND_Y_OFFSET);
        glow.setOffsetY(DEFAULT_X_AND_Y_OFFSET);

        return glow;
    }
}
