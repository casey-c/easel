package easel.ui.misc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import easel.ui.AbstractWidget;
import easel.ui.debug.DebugWidget;
import easel.utils.GraphicsHelper;

/**
 * A simple invisible widget that occupies an amount of space but has no children and no rendering. Can make some basic layouts (e.g. {@link easel.ui.layouts.HorizontalLayout}) or some other niche cases easier to work with, as PlaceholderWidgets allow you to leave a "gap" of space of a fixed size. The actual need for placeholders is probably pretty rare and overuse may be a code smell. If a situation requires a lot of placeholders, it may be time to use a more powerful layout (e.g. {@link easel.ui.layouts.GridLayout}) with more flexible spacing ability, or improve your usage of the built-in margin code (e.g. {@link AbstractWidget#withMargins(float, float, float, float)}). That being said, placeholders can be extremely clean and effective in the niche uses where they may be necessary.
 */
public class PlaceholderWidget extends AbstractWidget<PlaceholderWidget> {
    private float width;
    private float height;

    /**
     * Constructs an invisible widget with the given dimensions.
     * @param width how many pixels wide the placeholder reserves
     * @param height how many pixels tall the placeholder reserves
     */
    public PlaceholderWidget(float width, float height) {
        this.width = width;
        this.height = height;
    }

    @Override public float getContentWidth() { return width; }
    @Override public float getContentHeight() { return height; }

    @Override protected void renderWidget(SpriteBatch sb) {
        // TODO: debug only
        GraphicsHelper.drawRect(sb, this, false, DebugWidget.DEBUG_COLOR_0);
    }
}
