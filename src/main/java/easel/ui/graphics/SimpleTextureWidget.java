package easel.ui.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.Settings;
import easel.ui.AbstractWidget;

/**
 * Quick and easy way to render widgetized textures. Will render the texture stretched to fit the entire content area, so be aware when attempting to render a texture with an aspect ratio different than the one defined by this widget's {@link #getContentWidth()} and {@link #getContentHeight()}.
 */
public class SimpleTextureWidget extends AbstractWidget<SimpleTextureWidget> {
    private float width;
    private float height;

    private final TextureRegion textureRegion;
    private Color renderColor = Color.WHITE;

    // --------------------------------------------------------------------------------

    /**
     * A convenience constructor using a Texture instead of a TextureRegion. Note that internally: the given texture is converted into a TextureRegion before being used, so if you have a texture region to begin with (e.g. from a texture atlas), you should use the other constructor.
     * @param width the desired width of the rendered texture
     * @param height the desired height of the rendered texture
     * @param texture the texture to render, stretched to the given width and height
     * @see #SimpleTextureWidget(float, float, TextureRegion)
     */
    public SimpleTextureWidget(float width, float height, Texture texture) {
        this.width = width;
        this.height = height;
        this.textureRegion = new TextureRegion(texture);
    }

    /**
     * Constructs a new widget that will render the provided texture region with the supplied dimensions.
     * @param width the desired width of the rendered texture
     * @param height the desired height of the rendered texture
     * @param textureRegion the texture to render, stretched to the given width and height
     * @see #SimpleTextureWidget(float, float, Texture)
     */
    public SimpleTextureWidget(float width, float height, TextureRegion textureRegion) {
        this.width = width;
        this.height = height;
        this.textureRegion = textureRegion;
    }

    // --------------------------------------------------------------------------------

    /**
     * Use a custom color (useful for tinting) before rendering. If not set, defaults to Color.WHITE, which will result in the texture rendering completely normally. If you pass in a color with an alpha less than 1.0f, e.g. from <code>new Color(1.0f, 1.0f, 1.0f, 0.5f)</code>, the texture will be rendered transparently and blend with the background.
     * @param renderColor the color used to tint the rendering of this widget
     * @return this widget
     */
    public SimpleTextureWidget withColor(Color renderColor) {
        this.renderColor = renderColor;
        return this;
    }

    // --------------------------------------------------------------------------------

    /**
     * Update the horizontal width post-constructor. May require re-anchoring afterwards.
     * @param width the desired new content width
     * @return this widget
     * @see #withHeight(float)
     * @see #withDimensions(float, float)
     */
    public SimpleTextureWidget withWidth(float width) {
        this.width = width;
        scaleHitboxToContent();
        return this;
    }

    /**
     * Update the vertical height post-constructor. May require re-anchoring afterwards.
     * @param height the desired new content height
     * @return this widget
     * @see #withWidth(float)
     * @see #withDimensions(float, float)
     */
    public SimpleTextureWidget withHeight(float height) {
        this.height = height;
        scaleHitboxToContent();
        return this;
    }

    /**
     * Update the size post-constructor. May require re-anchoring afterwards.
     * @param width the desired new content width
     * @param height the desired new content height
     * @return this widget
     * @see #withWidth(float)
     * @see #withHeight(float)
     */
    public SimpleTextureWidget withDimensions(float width, float height) {
        this.width = width;
        this.height = height;
        scaleHitboxToContent();
        return this;
    }

    // --------------------------------------------------------------------------------

    @Override public float getContentWidth() { return width; }
    @Override public float getContentHeight() { return height; }

    @Override
    protected void renderWidget(SpriteBatch sb) {
        sb.setColor(renderColor);
        sb.draw(textureRegion,
                getContentLeft() * Settings.xScale,
                getContentBottom() * Settings.yScale,
                getContentWidth() * Settings.xScale,
                getContentHeight() * Settings.yScale);
    }
}
