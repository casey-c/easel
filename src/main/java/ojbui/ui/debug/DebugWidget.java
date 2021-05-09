package ojbui.ui.debug;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import ojbui.ui.AbstractWidget;

public class DebugWidget extends AbstractWidget<DebugWidget> {
    public static final Color DEBUG_COLOR_0 = new Color(0.384f, 0.690f, 0.388f, 0.500f);
    public static final Color DEBUG_COLOR_1 = new Color(0.384f, 0.388f, 0.690f, 0.500f);
    public static final Color DEBUG_COLOR_2 = new Color(0.690f, 0.384f, 0.388f, 0.500f);

    private float width, height;
    private Color color;


    public DebugWidget() {
        this(100, 100, DEBUG_COLOR_0);
    }

    public DebugWidget(float width, float height) {
        this(width, height, DEBUG_COLOR_0);
    }

    public DebugWidget(float width, float height, Color color) {
        this.width = width;
        this.height = height;
        this.color = color;
    }

    @Override public float getContentWidth() { return width; }
    @Override public float getContentHeight() { return height; }

    @Override
    protected void renderWidget(SpriteBatch sb) {
        sb.setColor(color);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG,
                getContentLeft() * Settings.xScale,
                getContentBottom() * Settings.yScale,
                width * Settings.xScale,
                height * Settings.yScale);
    }
}
