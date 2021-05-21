package easel.ui.text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import easel.ui.AbstractWidget;

/**
 * <p>
 * Labels are the quick and easy way to render text with a widget. Labels can change their text dynamically with {@link #withText(String)}, but doing so will alter the size of the widget and may require re-anchoring.
 * </p>
 * <p>
 * Labels won't handle "smart text" effects (e.g. text wrapping, inline use of color modifiers, " NL " for newlines, etc.), as this widget is just a simple wrapper over <code>FontHelper.renderFontLeftDownAligned()</code>. For more flexible text rendering, consider using a SmartLabel instead (not yet implemented).
 * </p>
 */
public class Label extends AbstractWidget<Label> {
    private String text;

    private BitmapFont font;
    private Color color;

    private float textWidth;
    private float textHeight;

    public Label(String text) {
        this(text, FontHelper.tipBodyFont, Settings.CREAM_COLOR);
    }

    public Label(String text, Color color) {
        this(text, FontHelper.tipBodyFont, color);
    }

    public Label(String text, BitmapFont font, Color color) {
        this.font = font;
        this.color = color;
        withText(text);
    }

    public Label withText(String text) {
        this.text = text;

        this.textWidth = FontHelper.getWidth(font, text, 1);
        this.textHeight = font.getLineHeight();

        scaleHitboxToContent();

        return this;
    }

    public Label withColor(Color color) {
        this.color = color;
        return this;
    }

    @Override public float getContentWidth() { return textWidth; }
    @Override public float getContentHeight() { return textHeight; }

    @Override
    protected void renderWidget(SpriteBatch sb) {
        FontHelper.renderFontLeftDownAligned(sb,
                font,
                text,
                getContentLeft() * Settings.xScale,
                getContentBottom() * Settings.yScale,
                color);
    }
}
