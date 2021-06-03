package easel.ui.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import easel.ui.AbstractWidget;

public class SimpleTextureWidget extends AbstractWidget<SimpleTextureWidget> {
    private float width;
    private float height;

    private Texture texture;
    private Color renderColor = Color.WHITE;

    // --------------------------------------------------------------------------------

    public SimpleTextureWidget(float width, float height, Texture texture) {
        this.width = width;
        this.height = height;
        this.texture = texture;
    }

    // --------------------------------------------------------------------------------

    public SimpleTextureWidget withColor(Color renderColor) {
        this.renderColor = renderColor;
        return this;
    }

    // --------------------------------------------------------------------------------

    public SimpleTextureWidget withWidth(float width) {
        this.width = width;
        return this;
    }

    public SimpleTextureWidget withHeight(float height) {
        this.height = height;
        return this;
    }

    public SimpleTextureWidget withDimensions(float width, float height) {
        this.width = width;
        this.height = height;
        return this;
    }

    // --------------------------------------------------------------------------------

    @Override public float getContentWidth() { return width; }
    @Override public float getContentHeight() { return height; }

    @Override
    protected void renderWidget(SpriteBatch sb) {
        sb.setColor(renderColor);
        sb.draw(texture,
                getContentLeft() * Settings.xScale,
                getContentBottom() * Settings.yScale,
                getContentWidth() * Settings.xScale,
                getContentHeight() * Settings.yScale);
    }
}
