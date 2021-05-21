package easel.ui.graphics.ninepatch;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.Settings;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;

/**
 * <p>
 * A "widgetized" version of libgdx's NinePatch ({@link NinePatch}). NinePatches are a convenient way to seamlessly scalable textures that won't make the corners stretch poorly. A nine patch is the jargon term for a texture which has nine regions: top left, top middle, top right, center left, center, center right, bottom left, bottom center, and bottom right. All four corners in this nine patch can be considered "fixed" and won't grow - the inner pieces will either grow horizontally (in terms on the top and bottom edges) or vertically (the left or right edge) or both (the very center piece). This lets you have dynamically sized textures where the corners don't get stretched awkwardly or get super pixelated, as long as you've set up your initial ninepatch texture correctly.
 * </p>
 * <p>
 * This library will typically use 128px by 128px textures (with the corners 32px in size) to make its default NinePatches - and so there are a few convenience constructors that use these sizes. However, you are not limited to just these dimensions and can specify the full dimensions of your nine patch if you need to alter it at all.
 * </p>
 */
public class NinePatchWidget extends AbstractWidget<NinePatchWidget> {
    private NinePatch np;

    private float prefWidth;
    private float prefHeight;

    private Color renderColor = Color.WHITE;

    /**
     * Constructs a new NinePatch widget using a texture region. This convenience constructor assumes the corners to be 32px by 32px.
     * @param width the desired width of the final widget (what we render)
     * @param height the desired height of the final widget (what we render)
     * @param texRegion the texture region containing the entire nine patch
     * @see #NinePatchWidget(float, float, Texture)
     * @see #NinePatchWidget(float, float, int, int, int, int, TextureRegion)
     */
    public NinePatchWidget( float width, float height, TextureRegion texRegion) {
        this(width, height, 32, 32, 32, 32, texRegion);
    }

    /**
     * Constructs a new NinePatch widget using a full texture. This convenience constructor assumes the corners to be 32px by 32px.
     * @param width the desired width of the final widget (what we render)
     * @param height the desired height of the final widget (what we render)
     * @param texture the texture containing the entire nine patch
     * @see #NinePatchWidget(float, float, TextureRegion)
     * @see #NinePatchWidget(float, float, int, int, int, int, Texture)
     */
    public NinePatchWidget( float width, float height, Texture texture) {
        this(width, height, 32, 32, 32, 32, texture);
    }

    /**
     * Constructs a new NinePatch widget using a texture region and specifying all corner sizes for the patch. See {@link NinePatch} from the libgdx library for more details about what NinePatches are or what the corner values mean.
     * @param width the desired width of the final widget (what we render)
     * @param height the desired height of the final widget (what we render)
     * @param patchLeft the number of pixels from the left which define the left patch edge
     * @param patchRight the number of pixels from the right which define the right patch edge
     * @param patchTop the number of pixels from the top which define the top patch edge
     * @param patchBottom the number of pixels from the bottom which define the bottom patch edge
     * @param texRegion the texture region containing the entire nine patch
     * @see #NinePatchWidget(float, float, int, int, int, int, Texture)
     * @see #NinePatchWidget(float, float, TextureRegion)
     */
    public NinePatchWidget(float width, float height, int patchLeft, int patchRight, int patchTop, int patchBottom, TextureRegion texRegion) {
        this.np = new NinePatch(texRegion, patchLeft, patchRight, patchTop, patchBottom);

        this.prefWidth = width;
        this.prefHeight = height;
    }

    /**
     * Constructs a new NinePatch widget using a texture and specifying all corner sizes for the patch. See {@link NinePatch} from the libgdx library for more details about what NinePatches are or what the corner values mean.
     * @param width the desired width of the final widget (what we render)
     * @param height the desired height of the final widget (what we render)
     * @param patchLeft the number of pixels from the left which define the left patch edge
     * @param patchRight the number of pixels from the right which define the right patch edge
     * @param patchTop the number of pixels from the top which define the top patch edge
     * @param patchBottom the number of pixels from the bottom which define the bottom patch edge
     * @param texture the texture containing the entire nine patch
     * @see #NinePatchWidget(float, float, int, int, int, int, TextureRegion)
     * @see #NinePatchWidget(float, float, Texture)
     */
    public NinePatchWidget(float width, float height, int patchLeft, int patchRight, int patchTop, int patchBottom, Texture texture) {
        this.np = new NinePatch(texture, patchLeft, patchRight, patchTop, patchBottom);

        this.prefWidth = width;
        this.prefHeight = height;
    }

    /**
     * Sets the color used to render this nine patch.
     * @param renderColor the color to render with
     * @return this widget
     */
    public NinePatchWidget withColor(Color renderColor) {
        this.renderColor = renderColor;
        return this;
    }

    /**
     * Resizes this widget (will likely require another anchoring, e.g. {@link #anchoredAt(float, float, AnchorPosition)}). The final rendered output will depend on the width/height provided here (or from the constructor, if this does not exist). This function will override the constructor's width/height and will also rebuild hitboxes if required.
     * @param width the new width of the widget
     * @param height the new height of the widget
     * @return this widget
     * @see #scaleToFullWidget(AbstractWidget)
     */
    public NinePatchWidget withDimensions(float width, float height) {
        this.prefWidth = width;
        this.prefHeight = height;

        scaleHitboxToContent();

        return this;
    }

    /**
     * <p>
     * Resizes this widget to adjust to the full size of the provided widget (convenience). E.g. the new width of this widget when rendered is the value of {@link AbstractWidget#getWidth()} at the time of this function call (it will not update automatically if the source widget changes in size in the future). Note that this takes the full size of the given widget (including margins), and not the inner content sizes (e.g. it does NOT look at {@link AbstractWidget#getContentWidth()}). This is because you often want to scale your nine patch to be a bit bigger than the widget(s) it is supposed to be surrounding, and margins ({@link AbstractWidget#withMargins(float)}) let you do that quite easily. You can use {@link #withDimensions(float, float)} to use precisely specified width/height should you need more control.
     * </p>
     * <p>
     * Like {@link #withDimensions(float, float)}, you'll probably want to call some sort of {@link #anchoredAt(float, float, AnchorPosition)} to put the widget in the proper place afterwards.
     * </p>
     * @param widget the widget whose total width/height dimensions will be copied
     * @return this widget
     * @see #withDimensions(float, float)
     */
    public NinePatchWidget scaleToFullWidget(AbstractWidget widget) {
        return withDimensions(widget.getWidth(), widget.getHeight());
    }

    @Override public float getContentWidth() { return prefWidth; }
    @Override public float getContentHeight() { return prefHeight; }

    @Override
    protected void renderWidget(SpriteBatch sb) {
        sb.setColor(renderColor);
        np.draw(sb,
                getContentLeft() * Settings.xScale,
                getContentBottom() * Settings.yScale,
                getContentWidth() * Settings.xScale,
                getContentHeight() * Settings.yScale);
    }
}
