package easel.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class GraphicsHelper {
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
                width * Settings.xScale,
                height * Settings.yScale);
    }
}
