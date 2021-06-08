package easel.ui.debug;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import easel.ui.AbstractWidget;
import easel.utils.EaselGraphicsHelper;

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
//        GraphicsHelper.drawRect(sb, this, true, DEBUG_COLOR_3);
        EaselGraphicsHelper.drawRect(sb, this, false, color);
    }
}
