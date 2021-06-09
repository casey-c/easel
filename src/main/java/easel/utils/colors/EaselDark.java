package easel.utils.colors;

import com.badlogic.gdx.graphics.Color;

/**
 * The default color palette used by easel (a light foreground on dark background).
 */
public class EaselDark implements ColorPalette {
    private static final Color TOOLTIP_BASE = new Color(0.125f, 0.153f, 0.169f, 1.000f);
    private static final Color TOOLTIP_TRIM = new Color(0.318f, 0.341f, 0.365f, 1.000f);
    private static final Color TOOLTIP_TRIM_HIGHLIGHT = new Color(0.435f, 0.518f, 0.537f, 0.400f);

    // --------------------------------------------------------------------------------

    private static final Color QUAL_RED = new Color(0.839f, 0.400f, 0.443f, 1.000f);
    private static final Color QUAL_GREEN = new Color(0.329f, 0.451f, 0.349f, 1.000f);
    private static final Color QUAL_BLUE = new Color(0.475f, 0.631f, 0.804f, 1.000f);
    private static final Color QUAL_PURPLE = new Color(0.600f, 0.478f, 0.675f, 1.000f);
    private static final Color QUAL_YELLOW = new Color(0.765f, 0.729f, 0.420f, 1.000f);

    // --------------------------------------------------------------------------------

    private static final Color SEQ_RED_0 = new Color(0.839f, 0.706f, 0.690f, 1.000f);
    private static final Color SEQ_RED_1 = new Color(0.847f, 0.573f, 0.514f, 1.000f);
    private static final Color SEQ_RED_2 = new Color(0.851f, 0.447f, 0.345f, 1.000f);
    private static final Color SEQ_RED_3 = new Color(0.859f, 0.333f, 0.192f, 1.000f);
    private static final Color SEQ_RED_4 = new Color(0.867f, 0.208f, 0.024f, 1.000f);

    private static final Color SEQ_BLUE_0 = new Color(0.690f, 0.839f, 0.765f, 1.000f);
    private static final Color SEQ_BLUE_1 = new Color(0.620f, 0.804f, 0.804f, 1.000f);
    private static final Color SEQ_BLUE_2 = new Color(0.525f, 0.678f, 0.753f, 1.000f);
    private static final Color SEQ_BLUE_3 = new Color(0.424f, 0.529f, 0.698f, 1.000f);
    private static final Color SEQ_BLUE_4 = new Color(0.259f, 0.325f, 0.616f, 1.000f);

    private static final Color SEQ_GREEN_0 = new Color(0.788f, 0.839f, 0.690f, 1.000f);
    private static final Color SEQ_GREEN_1 = new Color(0.616f, 0.725f, 0.510f, 1.000f);
    private static final Color SEQ_GREEN_2 = new Color(0.494f, 0.647f, 0.380f, 1.000f);
    private static final Color SEQ_GREEN_3 = new Color(0.376f, 0.569f, 0.259f, 1.000f);
    private static final Color SEQ_GREEN_4 = new Color(0.184f, 0.494f, 0.137f, 1.000f);

    private static final Color SEQ_PURPLE_0 = new Color(0.761f, 0.698f, 0.776f, 1.000f);
    private static final Color SEQ_PURPLE_1 = new Color(0.678f, 0.573f, 0.737f, 1.000f);
    private static final Color SEQ_PURPLE_2 = new Color(0.663f, 0.463f, 0.820f, 1.000f);
    private static final Color SEQ_PURPLE_3 = new Color(0.443f, 0.298f, 0.624f, 1.000f);
    private static final Color SEQ_PURPLE_4 = new Color(0.306f, 0.192f, 0.502f, 1.000f);

    private static final Color SEQ_YELLOW_0 = new Color(0.694f, 0.678f, 0.565f, 1.000f);
    private static final Color SEQ_YELLOW_1 = new Color(0.761f, 0.678f, 0.427f, 1.000f);
    private static final Color SEQ_YELLOW_2 = new Color(0.800f, 0.635f, 0.322f, 1.000f);
    private static final Color SEQ_YELLOW_3 = new Color(0.882f, 0.600f, 0.094f, 1.000f);
    private static final Color SEQ_YELLOW_4 = new Color(0.910f, 0.549f, 0.145f, 1.000f);

    // --------------------------------------------------------------------------------

    private static final Color HEADER_RED = new Color(0.231f, 0.137f, 0.176f, 1.000f);
    private static final Color HEADER_BLUE = new Color(0.125f, 0.161f, 0.208f, 1.000f);
    private static final Color HEADER_GREEN = new Color(0.145f, 0.192f, 0.145f, 1.000f);
    private static final Color HEADER_PURPLE = new Color(0.216f, 0.176f, 0.235f, 1.000f);

    private static final Color HEADER_STRONG_RED = new Color(0.333f, 0.157f, 0.157f, 1.000f);
    private static final Color HEADER_STRONG_BLUE = new Color(0.173f, 0.200f, 0.318f, 1.000f);
    private static final Color HEADER_STRONG_GREEN = new Color(0.200f, 0.290f, 0.200f, 1.000f);
    private static final Color HEADER_STRONG_PURPLE = new Color(0.212f, 0.149f, 0.235f, 1.000f);

    private static final Color HEADER_DEEP_BLUE = new Color(0.102f, 0.122f, 0.157f, 1.000f);
    private static final Color HEADER_SLATE = new Color(0.176f, 0.212f, 0.235f, 1.000f);
    private static final Color HEADER_SEA_GLASS = new Color(0.169f, 0.231f, 0.231f, 1.000f);
    private static final Color HEADER_WOOD = new Color(0.235f, 0.176f, 0.176f, 1.000f);
    private static final Color HEADER_LIGHT_ALGAE = new Color(0.286f, 0.294f, 0.188f, 1.000f);
    private static final Color HEADER_DARK_ALGAE = new Color(0.224f, 0.235f, 0.180f, 1.000f);

    // --------------------------------------------------------------------------------

    @Override public Color TOOLTIP_BASE() { return TOOLTIP_BASE; }
    @Override public Color TOOLTIP_TRIM() { return TOOLTIP_TRIM; }
    @Override public Color TOOLTIP_TRIM_HIGHLIGHT() { return TOOLTIP_TRIM_HIGHLIGHT; }

    // --------------------------------------------------------------------------------

    @Override public Color QUAL_RED() { return QUAL_RED; }
    @Override public Color QUAL_GREEN() { return QUAL_GREEN; }
    @Override public Color QUAL_BLUE() { return QUAL_BLUE; }
    @Override public Color QUAL_PURPLE() { return QUAL_PURPLE; }
    @Override public Color QUAL_YELLOW() { return QUAL_YELLOW; }

    // --------------------------------------------------------------------------------

    @Override public Color SEQ_RED_0() { return SEQ_RED_0; }
    @Override public Color SEQ_RED_1() { return SEQ_RED_1; }
    @Override public Color SEQ_RED_2() { return SEQ_RED_2; }
    @Override public Color SEQ_RED_3() { return SEQ_RED_3; }
    @Override public Color SEQ_RED_4() { return SEQ_RED_4; }

    @Override public Color SEQ_BLUE_0() { return SEQ_BLUE_0; }
    @Override public Color SEQ_BLUE_1() { return SEQ_BLUE_1; }
    @Override public Color SEQ_BLUE_2() { return SEQ_BLUE_2; }
    @Override public Color SEQ_BLUE_3() { return SEQ_BLUE_3; }
    @Override public Color SEQ_BLUE_4() { return SEQ_BLUE_4; }

    @Override public Color SEQ_GREEN_0() { return SEQ_GREEN_0; }
    @Override public Color SEQ_GREEN_1() { return SEQ_GREEN_1; }
    @Override public Color SEQ_GREEN_2() { return SEQ_GREEN_2; }
    @Override public Color SEQ_GREEN_3() { return SEQ_GREEN_3; }
    @Override public Color SEQ_GREEN_4() { return SEQ_GREEN_4; }

    @Override public Color SEQ_PURPLE_0() { return SEQ_PURPLE_0; }
    @Override public Color SEQ_PURPLE_1() { return SEQ_PURPLE_1; }
    @Override public Color SEQ_PURPLE_2() { return SEQ_PURPLE_2; }
    @Override public Color SEQ_PURPLE_3() { return SEQ_PURPLE_3; }
    @Override public Color SEQ_PURPLE_4() { return SEQ_PURPLE_4; }

    @Override public Color SEQ_YELLOW_0() { return SEQ_YELLOW_0; }
    @Override public Color SEQ_YELLOW_1() { return SEQ_YELLOW_1; }
    @Override public Color SEQ_YELLOW_2() { return SEQ_YELLOW_2; }
    @Override public Color SEQ_YELLOW_3() { return SEQ_YELLOW_3; }
    @Override public Color SEQ_YELLOW_4() { return SEQ_YELLOW_4; }

    // --------------------------------------------------------------------------------

    @Override public Color HEADER_RED() { return HEADER_RED; }
    @Override public Color HEADER_BLUE() { return HEADER_BLUE; }
    @Override public Color HEADER_GREEN() { return HEADER_GREEN; }
    @Override public Color HEADER_PURPLE() { return HEADER_PURPLE; }

    @Override public Color HEADER_STRONG_RED() { return HEADER_STRONG_RED; }
    @Override public Color HEADER_STRONG_BLUE() { return HEADER_STRONG_BLUE; }
    @Override public Color HEADER_STRONG_GREEN() { return HEADER_STRONG_GREEN; }
    @Override public Color HEADER_STRONG_PURPLE() { return HEADER_STRONG_PURPLE; }

    @Override public Color HEADER_DEEP_BLUE() { return HEADER_DEEP_BLUE; }
    @Override public Color HEADER_SLATE() { return HEADER_SLATE; }
    @Override public Color HEADER_SEA_GLASS() { return HEADER_SEA_GLASS; }
    @Override public Color HEADER_WOOD() { return HEADER_WOOD; }
    @Override public Color HEADER_LIGHT_ALGAE() { return HEADER_LIGHT_ALGAE; }
    @Override public Color HEADER_DARK_ALGAE() { return HEADER_DARK_ALGAE; }

    // --------------------------------------------------------------------------------
}
