package easel.utils.colors;

import com.badlogic.gdx.graphics.Color;

public interface ColorPalette {
    /**
     * @return the background color of a standard tooltip
     */
    Color TOOLTIP_BASE();

    /**
     * @return the trim (border) color of a standard tooltip
     */
    Color TOOLTIP_TRIM();

    // --------------------------------------------------------------------------------
    // Qualitative colors
    // --------------------------------------------------------------------------------

    /**
     * @return a "qualitative"-palette friendly red color
     */
    Color QUAL_RED();

    /**
     * @return a "qualitative"-palette friendly green color
     */
    Color QUAL_GREEN();

    /**
     * @return a "qualitative"-palette friendly blue color
     */
    Color QUAL_BLUE();

    /**
     * @return a "qualitative"-palette friendly purple color
     */
    Color QUAL_PURPLE();

    /**
     * @return a "qualitative"-palette friendly yellow color
     */
    Color QUAL_YELLOW();

    // --------------------------------------------------------------------------------
    // Header colors
    // --------------------------------------------------------------------------------

    // --------------------------------------------------------------------------------
    // More saturated header colors (shouldn't be used with excessive colors around, as they tend to dominate)

    /**
     * @return a strongly saturated red color; avoid using lots of other colors nearby, as this tends to dominate
     */
    Color HEADER_STRONG_RED();

    /**
     * @return a strongly saturated blue color; avoid using lots of other colors nearby, as this tends to dominate
     */
    Color HEADER_STRONG_BLUE();

    /**
     * @return a strongly saturated green color; avoid using lots of other colors nearby, as this tends to dominate
     */
    Color HEADER_STRONG_GREEN();

    /**
     * @return a strongly saturated purple color; avoid using lots of other colors nearby, as this tends to dominate
     */
    Color HEADER_STRONG_PURPLE();

    // --------------------------------------------------------------------------------
    // The "default" header colors (mostly desaturated) - can be used with other colors as they won't compete as much

    /**
     * @return a desaturated red for standard header background use
     */
    Color HEADER_RED();

    /**
     * @return a desaturated blue for standard header background use
     */
    Color HEADER_BLUE();

    /**
     * @return a desaturated green for standard header background use
     */
    Color HEADER_GREEN();

    /**
     * @return a desaturated purple for standard header background use
     */
    Color HEADER_PURPLE();

    // --------------------------------------------------------------------------------
    // The "special" header colors - not quite "strong" as they can be used with other colors but unusual enough to not
    // be super common

    /**
     * @return a very dark blue
     */
    Color HEADER_DEEP_BLUE();

    /**
     * @return a lighter, paleish blue
     */
    Color HEADER_SLATE();

    /**
     * @return a lighter, blueish green
     */
    Color HEADER_SEA_GLASS();

    /**
     * @return a maroonish, pale brownish red
     */
    Color HEADER_WOOD();

    /**
     * @return a light yellow green, can be considered suitable for "yellow"
     */
    Color HEADER_LIGHT_ALGAE();

    /**
     * @return a dark yellow green
     */
    Color HEADER_DARK_ALGAE();
}
