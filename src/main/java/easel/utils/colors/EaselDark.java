package easel.utils.colors;

import com.badlogic.gdx.graphics.Color;

/**
 * The default color palette used by easel (a light foreground on dark background).
 */
public class EaselDark implements ColorPalette {
    public static final Color TOOLTIP_BASE = new Color(0.125f, 0.153f, 0.169f, 1.000f);
    public static final Color TOOLTIP_TRIM = new Color(0.318f, 0.341f, 0.365f, 1.000f);

    public static final Color QUAL_RED = new Color(0.839f, 0.400f, 0.443f, 1.000f);
    public static final Color QUAL_GREEN = new Color(0.329f, 0.451f, 0.349f, 1.000f);
    public static final Color QUAL_BLUE = new Color(0.475f, 0.631f, 0.804f, 1.000f);
    public static final Color QUAL_PURPLE = new Color(0.600f, 0.478f, 0.675f, 1.000f);
    public static final Color QUAL_YELLOW = new Color(0.765f, 0.729f, 0.420f, 1.000f);

    // --------------------------------------------------------------------------------

    @Override public Color TOOLTIP_BASE() { return TOOLTIP_BASE; }
    @Override public Color TOOLTIP_TRIM() { return TOOLTIP_TRIM; }

    @Override public Color QUAL_RED() { return QUAL_RED; }
    @Override public Color QUAL_GREEN() { return QUAL_GREEN; }
    @Override public Color QUAL_BLUE() { return QUAL_BLUE; }
    @Override public Color QUAL_PURPLE() { return QUAL_PURPLE; }
    @Override public Color QUAL_YELLOW() { return QUAL_YELLOW; }
}
