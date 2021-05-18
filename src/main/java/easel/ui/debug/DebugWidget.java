package easel.ui.debug;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import easel.ui.AbstractWidget;

public class DebugWidget extends AbstractWidget<DebugWidget> {
    public static final Color DEBUG_COLOR_0 = new Color(0.384f, 0.690f, 0.388f, 0.500f);
    public static final Color DEBUG_COLOR_1 = new Color(0.384f, 0.388f, 0.690f, 0.500f);
    public static final Color DEBUG_COLOR_2 = new Color(0.690f, 0.384f, 0.388f, 0.500f);
    public static final Color DEBUG_COLOR_3 = new Color(0.690f, 0.684f, 0.688f, 0.500f);

    private float width, height;
    private Color color;


    public DebugWidget() {
        this(100, 100, DEBUG_COLOR_0);
    }

    public DebugWidget(Color color) {
        this(100, 100, color);
    }

    public DebugWidget(float width, float height) {
        this(width, height, DEBUG_COLOR_0);
    }

    public DebugWidget(float width, float height, Color color) {
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override public float getContentWidth() { return width; }
    @Override public float getContentHeight() { return height; }

    @Override
    protected void renderWidget(SpriteBatch sb) {
        sb.setColor(DEBUG_COLOR_3);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG,
                getLeft() * Settings.xScale,
                getBottom() * Settings.yScale,
                getWidth() * Settings.xScale,
                getHeight() * Settings.yScale);

        sb.setColor(color);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG,
                getContentLeft() * Settings.xScale,
                getContentBottom() * Settings.yScale,
                getContentWidth() * Settings.xScale,
                getContentHeight() * Settings.yScale);
    }
}
