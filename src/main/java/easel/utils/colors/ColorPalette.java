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

    /**
     * @return the trim (border) highlight color of a standard tooltip
     */
    Color TOOLTIP_TRIM_HIGHLIGHT();

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
    // Sequential colors
    // --------------------------------------------------------------------------------

    /**
     * @return the first (lightest/desaturated) color in the blue sequential palette
     */
    Color SEQ_BLUE_0();
    /**
     * @return the second (lightest/desaturated) color in the blue sequential palette
     */
    Color SEQ_BLUE_1();
    /**
     * @return the third (lightest/desaturated) color in the blue sequential palette
     */
    Color SEQ_BLUE_2();
    /**
     * @return the fourth (lightest / desaturated) color in the blue sequential palette
     */
    Color SEQ_BLUE_3();
    /**
     * @return the final (darkest / most saturated) color in the blue sequential palette
     */
    Color SEQ_BLUE_4();

    // --------------------------------------------------------------------------------

    /**
     * @return the first (lightest/desaturated) color in the red sequential palette
     */
    Color SEQ_RED_0();
    /**
     * @return the second (lightest/desaturated) color in the red sequential palette
     */
    Color SEQ_RED_1();
    /**
     * @return the third (lightest/desaturated) color in the red sequential palette
     */
    Color SEQ_RED_2();
    /**
     * @return the fourth (lightest / desaturated) color in the red sequential palette
     */
    Color SEQ_RED_3();
    /**
     * @return the final (darkest / most saturated) color in the red sequential palette
     */
    Color SEQ_RED_4();

    // --------------------------------------------------------------------------------

    /**
     * @return the first (lightest/desaturated) color in the purple sequential palette
     */
    Color SEQ_PURPLE_0();
    /**
     * @return the second (lightest/desaturated) color in the purple sequential palette
     */
    Color SEQ_PURPLE_1();
    /**
     * @return the third (lightest/desaturated) color in the purple sequential palette
     */
    Color SEQ_PURPLE_2();
    /**
     * @return the fourth (lightest / desaturated) color in the purple sequential palette
     */
    Color SEQ_PURPLE_3();
    /**
     * @return the final (darkest / most saturated) color in the purple sequential palette
     */
    Color SEQ_PURPLE_4();

    // --------------------------------------------------------------------------------

    /**
     * @return the first (lightest/desaturated) color in the yellow sequential palette
     */
    Color SEQ_YELLOW_0();
    /**
     * @return the second (lightest/desaturated) color in the yellow sequential palette
     */
    Color SEQ_YELLOW_1();
    /**
     * @return the third (lightest/desaturated) color in the yellow sequential palette
     */
    Color SEQ_YELLOW_2();
    /**
     * @return the fourth (lightest / desaturated) color in the yellow sequential palette
     */
    Color SEQ_YELLOW_3();
    /**
     * @return the final (darkest / most saturated) color in the yellow sequential palette
     */
    Color SEQ_YELLOW_4();

    // --------------------------------------------------------------------------------

    /**
     * @return the first (lightest/desaturated) color in the green sequential palette
     */
    Color SEQ_GREEN_0();
    /**
     * @return the second (lightest/desaturated) color in the green sequential palette
     */
    Color SEQ_GREEN_1();
    /**
     * @return the third (lightest/desaturated) color in the green sequential palette
     */
    Color SEQ_GREEN_2();
    /**
     * @return the fourth (lightest / desaturated) color in the green sequential palette
     */
    Color SEQ_GREEN_3();
    /**
     * @return the final (darkest / most saturated) color in the green sequential palette
     */
    Color SEQ_GREEN_4();

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
