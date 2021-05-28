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
}
