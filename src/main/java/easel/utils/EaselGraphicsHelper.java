package easel.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import easel.ui.AbstractWidget;
import easel.ui.debug.DebugWidget;

public class EaselGraphicsHelper {
    /**
     * Draws a simple rectangle onto the SpriteBatch. Takes in unscaled position/dimension data (i.e. will scale by Settings.xScale and Settings.yScale in the render step).
     * @param sb the SpriteBatch to render on
     * @param left the left most x-coordinate of the rectangle
     * @param bottom the bottom most y-coordinate of the rectangle
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @param color the color of the rectangle
     */
    public static void drawRect(SpriteBatch sb, float left, float bottom, float width, float height, Color color) {
        sb.setColor(color);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG,
                left * Settings.xScale,
                bottom * Settings.yScale,
                width * Settings.scale,
                height * Settings.scale);
    }

    /**
     * Draws a simple rectangle onto the SpriteBatch using the given widget's inner content dimensions (e.g. {@link AbstractWidget#getContentLeft()}, {@link AbstractWidget#getContentHeight()}, etc.) or the widget's full dimensions (e.g. {@link AbstractWidget#getLeft()}, {@link AbstractWidget#getWidth()}, etc.) as the bounds. This helper is mostly for debug purposes and assumes the widget has been properly anchored. See the notes on {@link AbstractWidget#withMargins(float)} for details about the difference between the full dimensions and the inner content dimensions.
     * @param sb the SpriteBatch to render on
     * @param widget the widget whose dimensions will be used as the rectangle source
     * @param useFull whether to use the full dimensions (with margins) [<code>useFull == true</code>] or just the inner content dimensions (excludes margins) [<code>useFull == false</code>]
     * @param color the color of the rectangle
     */
    public static void drawRect(SpriteBatch sb, AbstractWidget widget, boolean useFull, Color color) {
        if (useFull) {
            drawRect(sb,
                    widget.getLeft(),
                    widget.getBottom(),
                    widget.getWidth(),
                    widget.getHeight(),
                    color);
        }
        else {
            drawRect(sb,
                    widget.getContentLeft(),
                    widget.getContentBottom(),
                    widget.getContentWidth(),
                    widget.getContentHeight(),
                    color);
        }
    }

    /**
     * (DEBUG) Draw both sets of dimensions for the given widget (full and inner content) as rectangles. Convenience helper that uses {@link #drawRect(SpriteBatch, AbstractWidget, boolean, Color)} twice, first for the full size and then secondly for the inner content.
     * @param sb the SpriteBatch to draw on
     * @param widget the widget whose dimensions to use
     */
    public static void drawDebugRects(SpriteBatch sb, AbstractWidget widget) {
        drawRect(sb, widget, true, DebugWidget.DEBUG_COLOR_0);
        drawRect(sb, widget, false, DebugWidget.DEBUG_COLOR_1);
    }

    private static final Color DEBUG_DIM_COLOR = new Color(0f, 0f, 0f, 0.6f);

    /**
     * DEBUG: Draws a black rectangle over the entire screen, useful for dimming or obscuring the background. The dim percentage is NOT configurable (it's either fully black or 60% black), so this function isn't intended for anything other than debugging. The code itself is simple enough to repurpose in something more flexible, should it be needed (make sure you cache the color and not re-construct it every frame).
     * @param sb the SpriteBatch to draw the black rectangle on
     * @param fullyDark if true, will render a fully black rectangle which will completely hide everything underneath; if false, the
     */
    public static void dimFullScreen(SpriteBatch sb, boolean fullyDark) {
        Color color = fullyDark ? Color.BLACK : DEBUG_DIM_COLOR;
        drawRect(sb, 0, 0, Settings.WIDTH, Settings.HEIGHT, color);
    }
}
