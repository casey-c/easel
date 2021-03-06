package easel.utils.colors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import easel.ui.InterpolationSpeed;

/**
 * <p>
 * Contains various color-related helper functions and a collection of pre-chosen colors. Choosing a color from the provided sets (e.g. the <code>QUAL_</code> series) should be determined by various concerns regarding readability and purpose - don't just choose your colors randomly based on what you think might look nice! Included colors are designed to be readable against a specific background color, {@link #TOOLTIP_BASE}, and may NOT be as readable if you have something different. Accessibility concerns involving colorblindness are left for you to handle.
 * </p>
 * <p>
 * The "qualitative" <code>QUAL_</code> series of colors are intended to be used to differentiate elements of distinct categories. Use this set when you have multiple, unrelated items needing to be visually distinct from each other without sacrificing readability.
 * </p>
 * <p>
 * The "sequential" <code>SEQ_</code> series of colors are intended to be used to indicate a progression of elements from the same category. Use elements of these sets (e.g. {@link #SEQ_BLUE_0()}, {@link #SEQ_BLUE_1()}, {@link #SEQ_BLUE_2()}) to indicate ordered values, with the lighter more desaturated colors (close to 0) typically depicting something smaller in magnitude, with the darker more saturated colors (close to 4) depicting something larger. You may not need all colors inside a set and can skip intermediate ramps if it aids visual clarity (though make sure the order is increasing in the proper way). You typically don't want to mix and match colors from different sequential sets (i.e. don't use {@link #SEQ_BLUE_0()} followed by {@link #SEQ_RED_1()}: use only blue or only red), as that conveys different meaning (you may want a qualitative set instead).
 * </p>
 * <p>
 * A third, but less common, series of color ramps ("diverging" palettes) can be used to indicate a progression of values as they blend between two distinct categories. As this set isn't as applicable to the vast majority of mods as the other sets, Easel does not include any colors for this type. You may simulate it by taking the pieces of one sequential set (e.g. {@link #SEQ_BLUE_4()}, {@link #SEQ_BLUE_2()}), passing it through a neutral white color ({@link Color#WHITE}, and then taking pieces of a second sequential set (e.g. {@link #SEQ_RED_2}, {@link #SEQ_RED_4()} for the other side. If you use this approach, the darker, more saturated colors are for the extremes and they fade to desaturated white in the middle. E.g.: [dark blue ??? light blue ??? white ??? light red ??? dark red].
 * </p>
 * <p>
 * More details about these types of color palettes and their intended use can be found by searching for "Color Brewer palettes" on your search engine, or by reading over the original paper (Harrower, Mark, and Cynthia A. Brewer. "ColorBrewer.org: an online tool for selecting colour schemes for maps." The Cartographic Journal 40.1 (2003))
 * </p>
 */
public class EaselColors {
    // --------------------------------------------------------------------------------
    // Helpers
    // --------------------------------------------------------------------------------

    /**
     * Creates a copy of the given color with the new alpha transparency. Alpha values range from 0.0f (invisible) to 1.0f (fully visible), with intermediate values allowing for blending with colors previously rendered underneath.
     * @param color the source color (RGB remains intact)
     * @param alpha the desired opacity in [0.0, 1.0].
     * @return a new color with the same RGB as the original color but with a new alpha transparency
     * @see #withOpacity(Color, float, InterpolationSpeed)
     */
    public static Color withOpacity(Color color, float alpha) {
        Color c = color.cpy();
        c.a = alpha;
        return c;
    }

    /**
     * Creates a copy of the given color with a new alpha transparency interpolated towards the <code>targetAlpha</code>. The new alpha is determined by the InterpolationSpeed's blending of the original <code>color.a</code> and <code>targetAlpha</code>; once the alpha is "sufficiently close", the interpolation snaps exactly to the <code>targetAlpha</code>. This function is intended to be called repeatedly each frame to interpolate the color successfully.
     * @param color the source color (RGB remains intact)
     * @param targetAlpha the desired opacity in [0.0, 1.0]
     * @param withDelay how quickly the source alpha morphs towards the targetAlpha
     * @return a new color with the same RGB as the original color but with a new alpha transparency
     * @see #withOpacity(Color, float)
     */
    public static Color withOpacity(Color color, float targetAlpha, InterpolationSpeed withDelay) {
        Color c = color.cpy();

        c.a = withDelay.interpolate(c.a, targetAlpha);

        return c;
    }

    /**
     * Creates a copy of the given color lightened by a given amount. Lightening is performed naively by adding <code>amt</code> to each channel (red, green, and blue), to shift it towards white. As RGB is NOT a perceptually uniform color space, this lightening is not be guaranteed to look perfect, but for most uses it works well enough. The closer that <code>amt</code> is to 1.0f, the more white that will be applied. Negative values have the effect of darkening towards black (see {@link #darken(Color, float)})
     * @param color the source color to lighten
     * @param amt the amount to lighten, inside [0.0, 1.0]
     * @return a new color which appears brighter / closer to white
     * @see #darken(Color, float)
     * @see #lighten(Color)
     */
    public static Color lighten(Color color, float amt) {
        Color c = color.cpy();

        c.r = color.r + amt;
        c.g = color.g + amt;
        c.b = color.b + amt;

        c.clamp();

        return c;
    }

    /**
     * Convenience function to make colors ~10% brighter. Simply calls {@link #lighten(Color, float)} with an <code>amt = 0.1f</code>.
     * @param color the source color to lighten
     * @return a new color which attempts to be 10% closer to white
     * @see #lighten(Color, float)
     */
    public static Color lighten(Color color) {
        return lighten(color, 0.1f);
    }

    /**
     * Creates a copy of the given color darkened by a given amount. This is simply a convenience function which calls {@link #lighten(Color, float)} with an inverted <code>amt</code>, making it subtract from each RGB channel instead of add.
     * @param color the source color to darken
     * @param amt the amount to darken, inside [0.0, 1.0]
     * @return a new color which appears darker / closer to black
     * @see #lighten(Color, float)
     * @see #darken(Color)
     */
    public static Color darken(Color color, float amt) {
        return lighten(color, -amt);
    }

    /**
     * Convenience function to make colors ~10% darker. Simply calls {@link #darken(Color, float)} with an <code>amt = 0.1f</code>.
     * @param color the source color to darken
     * @return a new color which attempts to be 10% closer to black
     * @see #darken(Color, float)
     */
    public static Color darken(Color color) {
        return darken(color, 0.1f);
    }

//    private static float twoCharHexToFloat(String xy) {
//        int val = Integer.parseInt(xy, 16);
//        return (float) val / 255.0f;
//    }

//    /**
//     * <p>
//     * Creates a libgdx Color from a six digit hex "rrggbb" or eight digit hex "rrggbbaa". If no alpha value aa is
//     * provided, the resulting color will not have any transparency. You may include an optional "#" prefix in the form
//     * of "#rrggbb" or "#rrggbbaa" which will be automatically stripped. This function attempts to do minor error
//     * handling by ensuring you give a string of the proper length. If this is not the case, the function returns a pure
//     * black color.
//     * </p>
//     * <p>
//     * Should the given string not be of the proper hexadecimal format (e.g. using characters other than A-F, 0-9), this
//     * function can throw a NumberFormatException.
//     * </p>
//     *
//     * @param hex a case-insensitive hexadecimal string describing the color; may have "#" as a prefix, but otherwise
//     *            should be 6 characters long or eight characters long, depending on whether to include transparency
//     * @return a color corresponding to this hex string
//     */
//    public static Color fromHexString(String hex) {
//        float r = 0.0f;
//        float g = 0.0f;
//        float b = 0.0f;
//
//        float a = 1.0f;
//
//        String work = (hex.startsWith("#")) ? hex.substring(1) : hex;
//
//        if (work.length() == 6 || work.length() == 8) {
//            r = twoCharHexToFloat(work.substring(0, 2));
//            g = twoCharHexToFloat(work.substring(2, 4));
//            b = twoCharHexToFloat(work.substring(4, 6));
//        }
//        if (work.length() == 8) {
//            a = twoCharHexToFloat(work.substring(6, 8));
//        }
//
//        return new Color(r, g, b, a);
//    }

    // --------------------------------------------------------------------------------
    // Common colors
    // --------------------------------------------------------------------------------

    public static final Color ONE_TENTH_TRANSPARENT_BLACK = new Color(0.0f, 0.0f, 0.0f, 0.1f);
    public static final Color HALF_TRANSPARENT_WHITE = new Color(1.0f, 1.0f, 1.0f, 0.5f);
    public static final Color EIGHTH_TRANSPARENT_WHITE = new Color(1.0f, 1.0f, 1.0f, 0.125f);

    private static ColorPalette activePalette = new EaselDark();

    /**
     * Sets the active color palette to be used by ALL mods using easel.
     * @param palette the new palette choice
     */
    static void setActivePalette(ColorPalette palette) {
        activePalette = palette;
    }

    public static Color rainbow() {
        float r = (MathUtils.cosDeg((float) (System.currentTimeMillis() / 10L % 360L)) + 1.25F) / 2.3F;
        float g = (MathUtils.cosDeg((float)((System.currentTimeMillis() + 1000L) / 10L % 360L)) + 1.25F) / 2.3F;
        float b = (MathUtils.cosDeg((float)((System.currentTimeMillis() + 2000L) / 10L % 360L)) + 1.25F) / 2.3F;
        return new Color(r, g, b, 1.0f);
    }

    // --------------------------------------------------------------------------------

    /**
     * @return the background color of a standard tooltip
     */
    public static Color TOOLTIP_BASE() { return activePalette.TOOLTIP_BASE(); }

    /**
     * @return the trim (border) color of a standard tooltip
     */
    public static Color TOOLTIP_TRIM() { return activePalette.TOOLTIP_TRIM(); }

    public static Color TOOLTIP_TRIM_HIGHLIGHT() { return activePalette.TOOLTIP_TRIM_HIGHLIGHT(); }

    // --------------------------------------------------------------------------------
    // Qualitative colors

    /**
     * @return a "qualitative"-palette friendly red color
     */
    public static Color QUAL_RED() { return activePalette.QUAL_RED(); }

    /**
     * @return a "qualitative"-palette friendly green color
     */
    public static Color QUAL_GREEN() { return activePalette.QUAL_GREEN(); }

    /**
     * @return a "qualitative"-palette friendly blue color
     */
    public static Color QUAL_BLUE() { return activePalette.QUAL_BLUE(); }

    /**
     * @return a "qualitative"-palette friendly purple color
     */
    public static Color QUAL_PURPLE() { return activePalette.QUAL_PURPLE(); }

    /**
     * @return a "qualitative"-palette friendly yellow color
     */
    public static Color QUAL_YELLOW() { return activePalette.QUAL_YELLOW(); }

    // --------------------------------------------------------------------------------
    // More saturated header colors (shouldn't be used with excessive colors around, as they tend to dominate)

    /**
     * @return a strongly saturated red color; avoid using lots of other colors nearby, as this tends to dominate
     */
    public static Color HEADER_STRONG_RED() { return activePalette.HEADER_STRONG_RED(); }

    /**
     * @return a strongly saturated blue color; avoid using lots of other colors nearby, as this tends to dominate
     */
    public static Color HEADER_STRONG_BLUE() { return activePalette.HEADER_STRONG_BLUE(); }

    /**
     * @return a strongly saturated green color; avoid using lots of other colors nearby, as this tends to dominate
     */
    public static Color HEADER_STRONG_GREEN() { return activePalette.HEADER_STRONG_GREEN(); }

    /**
     * @return a strongly saturated purple color; avoid using lots of other colors nearby, as this tends to dominate
     */
    public static Color HEADER_STRONG_PURPLE() { return activePalette.HEADER_STRONG_PURPLE(); }

    // --------------------------------------------------------------------------------
    // The "default" header colors (mostly desaturated) - can be used with other colors as they won't compete as much

    /**
     * @return a desaturated red for standard header background use
     */
    public static Color HEADER_RED() { return activePalette.HEADER_RED(); }

    /**
     * @return a desaturated blue for standard header background use
     */
    public static Color HEADER_BLUE() { return activePalette.HEADER_BLUE(); }

    /**
     * @return a desaturated green for standard header background use
     */
    public static Color HEADER_GREEN() { return activePalette.HEADER_GREEN(); }

    /**
     * @return a desaturated purple for standard header background use
     */
    public static Color HEADER_PURPLE() { return activePalette.HEADER_PURPLE(); }

    // --------------------------------------------------------------------------------
    // The "special" header colors - not quite "strong" as they can be used with other colors but unusual enough to not
    // be super common

    /**
     * @return a very dark blue
     */
    public static Color HEADER_DEEP_BLUE() { return activePalette.HEADER_DEEP_BLUE(); }

    /**
     * @return a lighter, paleish blue
     */
    public static Color HEADER_SLATE() { return activePalette.HEADER_SLATE(); }

    /**
     * @return a lighter, blueish green
     */
    public static Color HEADER_SEA_GLASS() { return activePalette.HEADER_SEA_GLASS(); }

    /**
     * @return a maroonish, pale brownish red
     */
    public static Color HEADER_WOOD() { return activePalette.HEADER_WOOD(); }

    /**
     * @return a light yellow green, can be considered suitable for "yellow"
     */
    public static Color HEADER_LIGHT_ALGAE() { return activePalette.HEADER_LIGHT_ALGAE(); }

    /**
     * @return a dark yellow green
     */
    public static Color HEADER_DARK_ALGAE() { return activePalette.HEADER_DARK_ALGAE(); }

    // --------------------------------------------------------------------------------
    // Sequential palettes

    /**
     * @return the first (lightest/desaturated) color in the red sequential palette
     */
    public static Color SEQ_RED_0() { return activePalette.SEQ_RED_0(); }
    /**
     * @return the second (lightest/desaturated) color in the red sequential palette
     */
    public static Color SEQ_RED_1() { return activePalette.SEQ_RED_1(); }
    /**
     * @return the third (lightest/desaturated) color in the red sequential palette
     */
    public static Color SEQ_RED_2() { return activePalette.SEQ_RED_2(); }
    /**
     * @return the fourth (lightest/desaturated) color in the red sequential palette
     */
    public static Color SEQ_RED_3() { return activePalette.SEQ_RED_3(); }
    /**
     * @return the final (darkest/most saturated) color in the red sequential palette
     */
    public static Color SEQ_RED_4() { return activePalette.SEQ_RED_4(); }

    // --------------------------------------------------------------------------------

    /**
     * @return the first (lightest/desaturated) color in the blue sequential palette
     */
    public static Color SEQ_BLUE_0() { return activePalette.SEQ_BLUE_0(); }
    /**
     * @return the second (lightest/desaturated) color in the blue sequential palette
     */
    public static Color SEQ_BLUE_1() { return activePalette.SEQ_BLUE_1(); }
    /**
     * @return the third (lightest/desaturated) color in the blue sequential palette
     */
    public static Color SEQ_BLUE_2() { return activePalette.SEQ_BLUE_2(); }
    /**
     * @return the fourth (lightest/desaturated) color in the blue sequential palette
     */
    public static Color SEQ_BLUE_3() { return activePalette.SEQ_BLUE_3(); }
    /**
     * @return the final (darkest/most saturated) color in the blue sequential palette
     */
    public static Color SEQ_BLUE_4() { return activePalette.SEQ_BLUE_4(); }

    // --------------------------------------------------------------------------------

    /**
     * @return the first (lightest/desaturated) color in the green sequential palette
     */
    public static Color SEQ_GREEN_0() { return activePalette.SEQ_GREEN_0(); }
    /**
     * @return the second (lightest/desaturated) color in the green sequential palette
     */
    public static Color SEQ_GREEN_1() { return activePalette.SEQ_GREEN_1(); }
    /**
     * @return the third (lightest/desaturated) color in the green sequential palette
     */
    public static Color SEQ_GREEN_2() { return activePalette.SEQ_GREEN_2(); }
    /**
     * @return the fourth (lightest/desaturated) color in the green sequential palette
     */
    public static Color SEQ_GREEN_3() { return activePalette.SEQ_GREEN_3(); }
    /**
     * @return the final (darkest/most saturated) color in the green sequential palette
     */
    public static Color SEQ_GREEN_4() { return activePalette.SEQ_GREEN_4(); }

    // --------------------------------------------------------------------------------

    /**
     * @return the first (lightest/desaturated) color in the purple sequential palette
     */
    public static Color SEQ_PURPLE_0() { return activePalette.SEQ_PURPLE_0(); }
    /**
     * @return the second (lightest/desaturated) color in the purple sequential palette
     */
    public static Color SEQ_PURPLE_1() { return activePalette.SEQ_PURPLE_1(); }
    /**
     * @return the third (lightest/desaturated) color in the purple sequential palette
     */
    public static Color SEQ_PURPLE_2() { return activePalette.SEQ_PURPLE_2(); }
    /**
     * @return the fourth (lightest/desaturated) color in the purple sequential palette
     */
    public static Color SEQ_PURPLE_3() { return activePalette.SEQ_PURPLE_3(); }
    /**
     * @return the final (darkest/most saturated) color in the purple sequential palette
     */
    public static Color SEQ_PURPLE_4() { return activePalette.SEQ_PURPLE_4(); }

    // --------------------------------------------------------------------------------

    /**
     * @return the first (lightest/desaturated) color in the yellow sequential palette
     */
    public static Color SEQ_YELLOW_0() { return activePalette.SEQ_YELLOW_0(); }
    /**
     * @return the second (lightest/desaturated) color in the yellow sequential palette
     */
    public static Color SEQ_YELLOW_1() { return activePalette.SEQ_YELLOW_1(); }
    /**
     * @return the third (lightest/desaturated) color in the yellow sequential palette
     */
    public static Color SEQ_YELLOW_2() { return activePalette.SEQ_YELLOW_2(); }
    /**
     * @return the fourth (lightest/desaturated) color in the yellow sequential palette
     */
    public static Color SEQ_YELLOW_3() { return activePalette.SEQ_YELLOW_3(); }
    /**
     * @return the final (darkest/most saturated) color in the yellow sequential palette
     */
    public static Color SEQ_YELLOW_4() { return activePalette.SEQ_YELLOW_4(); }
}
