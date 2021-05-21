package easel.ui.graphics.ninepatch;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;

/**
 * <p>
 * A special two-tone extension of the {@link NinePatchWidget}, included as convenience. This widget composes two {@link NinePatchWidget}s together: a base texture, and a trim texture. The "base" gets rendered first (the bottom layer) and should be considered the primary color of the patch - the "trim" gets rendered on top, and serves as a secondary color. This class is included with the library as it is a reasonably common pattern for dynamically colored screens/tooltips/other usual NinePatch uses.
 * </p>
 * <p>
 * Typical use is to have the base texture be the majority of the area needing coverage (the inside pieces), colored in a way where it won't conflict with the foreground elements (e.g. the base can be dark colored and you would make any text rendered on top be lighter). As your foreground elements render directly on top of the base texture in this example use, you want to be cognizant of readability when choosing colors or using some custom textures. The trim piece is typically used for an outer border around the outside of the base area. In this case, the trim would have a transparent center to allow the base to shine through underneath - while the border itself would allow you to add some sort of color cue to the overall combined widget. The color of the trim usually should contrast against the base color (and against the "real" background behind the entire widget) - in our examples above, this would mean the trim color would be lighter (much like the foreground pieces) in order to aid the separation between the overall widget and the background it lays on.
 * </p>
 * <p>
 * You are not limited to this standard use however. There can be plenty of other use cases where having two NinePatches linked together in size but varying in color can be just as useful.
 * </p>
 */
public class DualNinePatchWidget extends AbstractWidget<DualNinePatchWidget> {
    private NinePatchWidget base;
    private NinePatchWidget trim;

    // --------------------------------------------------------------------------------

    /**
     * Convenience for including NinePatchWidgets built elsewhere. This constructor will NOT adjust the existing NinePatchWidget sizes - if the given <code>base</code> and <code>trim</code> are not the same width and height, the overall widget will just assume to follow the dimensions of the base widget. You can use {@link #withDimensions(float, float)} to fix both widgets to be the same size, if desired.
     * @param base an existing NinePatchWidget for the bottom layer
     * @param trim an existing NinePatchWidget for the top layer
     */
    public DualNinePatchWidget(NinePatchWidget base, NinePatchWidget trim) {
        this.base = base;
        this.trim = trim;
    }

    /**
     * Builds using texture regions. Uses {@link NinePatchWidget#NinePatchWidget(float, float, TextureRegion)}.
     * @param width the width of the final rendered widget (both base and trim will be this same size)
     * @param height the height of the final rendered widget (both base and trim will be this same size)
     * @param baseTextureRegion the base texture (assumes the NinePatch has 32px by 32px corners)
     * @param trimTextureRegion the trim texture (assumes the NinePatch has 32px by 32px corners)
     */
    public DualNinePatchWidget(float width, float height, TextureRegion baseTextureRegion, TextureRegion trimTextureRegion) {
        this.base = new NinePatchWidget(width, height, baseTextureRegion);
        this.trim = new NinePatchWidget(width, height, trimTextureRegion);
    }

    /**
     * Builds using textures. Uses {@link NinePatchWidget#NinePatchWidget(float, float, Texture)}.
     * @param width the width of the final rendered widget (both base and trim will be this same size)
     * @param height the height of the final rendered widget (both base and trim will be this same size)
     * @param baseTexture the base texture (assumes the NinePatch has 32px by 32px corners)
     * @param trimTexture the trim texture (assumes the NinePatch has 32px by 32px corners)
     */
    public DualNinePatchWidget(float width, float height, Texture baseTexture, Texture trimTexture) {
        this.base = new NinePatchWidget(width, height, baseTexture);
        this.trim = new NinePatchWidget(width, height, trimTexture);
    }

    /**
     * Fully specify the NinePatch parameters and use texture regions. Uses {@link NinePatchWidget#NinePatchWidget(float, float, int, int, int, int, TextureRegion)}.
     * @param width the width of the final rendered widget (both base and trim will be this same size)
     * @param height the height of the final rendered widget (both base and trim will be this same size)
     * @param patchLeft the number of pixels from the left which define the left patch edge
     * @param patchRight the number of pixels from the right which define the right patch edge
     * @param patchTop the number of pixels from the top which define the top patch edge
     * @param patchBottom the number of pixels from the bottom which define the bottom patch edge
     * @param baseTextureRegion the base texture
     * @param trimTextureRegion the trim texture
     */
    public DualNinePatchWidget(float width, float height, int patchLeft, int patchRight, int patchTop, int patchBottom, TextureRegion baseTextureRegion, TextureRegion trimTextureRegion) {
        this.base = new NinePatchWidget(width, height, patchLeft, patchRight, patchTop, patchBottom, baseTextureRegion);
        this.trim = new NinePatchWidget(width, height, patchLeft, patchRight, patchTop, patchBottom, trimTextureRegion);
    }

    /**
     * Fully specify the NinePatch parameters. Uses {@link NinePatchWidget#NinePatchWidget(float, float, int, int, int, int, Texture)}.
     * @param width the width of the final rendered widget (both base and trim will be this same size)
     * @param height the height of the final rendered widget (both base and trim will be this same size)
     * @param patchLeft the number of pixels from the left which define the left patch edge
     * @param patchRight the number of pixels from the right which define the right patch edge
     * @param patchTop the number of pixels from the top which define the top patch edge
     * @param patchBottom the number of pixels from the bottom which define the bottom patch edge
     * @param baseTextureRegion the base texture
     * @param trimTextureRegion the trim texture
     */
    public DualNinePatchWidget(float width, float height, int patchLeft, int patchRight, int patchTop, int patchBottom, Texture baseTextureRegion, Texture trimTextureRegion) {
        this.base = new NinePatchWidget(width, height, patchLeft, patchRight, patchTop, patchBottom, baseTextureRegion);
        this.trim = new NinePatchWidget(width, height, patchLeft, patchRight, patchTop, patchBottom, trimTextureRegion);
    }

    // --------------------------------------------------------------------------------

    /**
     * Resize both the base and the trim to be the given dimensions. Note: every constructor except {@link #DualNinePatchWidget(NinePatchWidget, NinePatchWidget)} will already link the base and the trim to be the same size (with that constructor letting the sizes possibly be different). You can use this setter to guarantee the two widgets to be linked in size if that constructor was used, or if the dimensions change later on.
     * @param width the width of the final rendered widget (both base and trim will be this same size)
     * @param height the height of the final rendered widget (both base and trim will be this same size)
     * @return this widget
     */
    public DualNinePatchWidget withDimensions(float width, float height) {
        base.withDimensions(width, height);
        trim.withDimensions(width, height);

        scaleHitboxToContent();

        return this;
    }

    // --------------------------------------------------------------------------------

    /**
     * Set the color of the base (lower) layer.
     * @param baseColor a color used to render the base texture
     * @return this widget
     * @see #withColors(Color, Color)
     */
    public DualNinePatchWidget withBaseColor(Color baseColor) {
        base.withColor(baseColor);
        return this;
    }

    /**
     * Set the color of the trim (upper) layer.
     * @param trimColor a color used to render the trim texture
     * @return this widget
     * @see #withColors(Color, Color)
     */
    public DualNinePatchWidget withTrimColor(Color trimColor) {
        trim.withColor(trimColor);
        return this;
    }

    /**
     * Sets colors for both widgets simultaneously (convenience).
     * @param baseColor a color used to render the base texture
     * @param trimColor a color used to render the trim texture
     * @return this widget
     * @see #withBaseColor(Color)
     * @see #withTrimColor(Color)
     */
    public DualNinePatchWidget withColors(Color baseColor, Color trimColor) {
        withBaseColor(baseColor);
        withTrimColor(trimColor);
        return this;
    }

    // --------------------------------------------------------------------------------

    /**
     * Resizes both the base and the trim to be the same full size as the given widget. See {@link NinePatchWidget#scaleToFullWidget(AbstractWidget)} for further details.
     * @param widget the widget to take the size from
     * @return this widget
     */
    public DualNinePatchWidget scaleToFullWidget(AbstractWidget widget) {
        base.scaleToFullWidget(widget);
        trim.scaleToFullWidget(widget);
        scaleHitboxToContent();
        return this;
    }

    @Override
    public DualNinePatchWidget anchoredAt(float x, float y, AnchorPosition anchorPosition, InterpolationSpeed withDelay) {
        super.anchoredAt(x, y, anchorPosition, withDelay);
        base.anchoredAt(x, y, anchorPosition, withDelay);
        trim.anchoredAt(x, y, anchorPosition, withDelay);
        return this;
    }

    // --------------------------------------------------------------------------------

    @Override public float getContentWidth() { return base.getContentWidth(); }
    @Override public float getContentHeight() { return base.getContentHeight(); }

    @Override
    protected void updateWidget() {
        super.updateWidget();

        base.update();
        trim.update();
    }

    @Override
    protected void renderWidget(SpriteBatch sb) {
        base.render(sb);
        trim.render(sb);
    }
}
