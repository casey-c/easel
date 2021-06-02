package easel.ui.graphics.ninepatch;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;

import java.util.ArrayList;

/**
 * <p>
 * A collection of {@link NinePatchWidget}s stacked on top of one another. Each {@link NinePatchWidget} managed by this widget shares the same nine patch parameters (e.g. how far to the right the leftmost edge goes for the patch, etc.) and the same dimensions (width, height). This widget makes it easy to synchronize several {@link NinePatchWidget} together, such as when multiple textures combine to make a dynamically colored, multi-part whole. An example usage of this widget is in the {@link easel.ui.graphics.ninepatch.headered.HeaderedNinePatch}, which constructs a triplet of {@link NinePatchWidget}s consisting of a base layer, a header layer, and a trim layer.
 * </p>
 * <p>
 * This widget includes a couple helper functions to push layers onto the structure (see {@link #withLayer(Texture)} etc., which add a layer above any existing layers), and to manipulate the dimensions of the overall group, along with some basic methods to tweak the layer colors based on the index of when they were added.
 * </p>
 */
public class LayeredNinePatch extends AbstractWidget<LayeredNinePatch> {
    private float width;
    private float height;

    private int patchLeft;
    private int patchRight;
    private int patchTop;
    private int patchBottom;

    protected ArrayList<NinePatchWidget> layers = new ArrayList<>();

    /**
     * Construct a new layered nine patch with the given width and height. The NinePatch parameters (e.g. how far the corners of the patch stretch out, see {@link NinePatchWidget} for more details) are defaulted to 32 px from each side. All future layers added (with {@link #withLayer(TextureRegion)} etc.) will use these dimensions.
     * @param width the width of all layers
     * @param height the height of all layers
     * @see #LayeredNinePatch(float, float, int, int, int, int)
     * @see com.badlogic.gdx.graphics.g2d.NinePatch#NinePatch(Texture)
     * @see #withLayer(TextureRegion)
     */
    public LayeredNinePatch(float width, float height) {
        this(width, height, 32, 32, 32, 32);
    }

    /**
     * Construct a new layered nine patch with the given dimensions and patch parameters. All future layers added (with {@link #withLayer(TextureRegion)} etc.) will use these dimensions.
     * @param width the width of all layers
     * @param height the height of all layers
     * @param patchLeft the width of the left-most edge of the ninepatch
     * @param patchRight the width of the right-most edge of the ninepatch
     * @param patchTop the height of the top edge of the ninepatch
     * @param patchBottom the height of the bottom edge of the ninepatch
     * @see #LayeredNinePatch(float, float)
     * @see com.badlogic.gdx.graphics.g2d.NinePatch#NinePatch(Texture, int, int, int, int)
     * @see #withLayer(TextureRegion)
     */
    public LayeredNinePatch(float width, float height, int patchLeft, int patchRight, int patchTop, int patchBottom) {
        this.width = width;
        this.height = height;

        this.patchLeft = patchLeft;
        this.patchRight = patchRight;
        this.patchTop = patchTop;
        this.patchBottom = patchBottom;
    }

    // --------------------------------------------------------------------------------

    /**
     * Pushes a new {@link NinePatchWidget} on top of the rest using a textureRegion. The render color of this layer defaults to WHITE.
     * @param textureRegion the texture of the newest ninepatch
     * @return this widget
     * @see #withLayer(Texture)
     * @see #withLayer(TextureRegion, Color)
     */
    public LayeredNinePatch withLayer(TextureRegion textureRegion) {
        layers.add(new NinePatchWidget(width, height, patchLeft, patchRight, patchTop, patchBottom, textureRegion));
        return this;
    }

    /**
     * Pushes a new {@link NinePatchWidget} with the given color on top of the rest using a textureRegion.
     * @param textureRegion the texture of the newest ninepatch
     * @param color which color is given to the nine patch for rendering, see: {@link NinePatchWidget#withColor(Color)}
     * @return this widget
     * @see #withLayer(TextureRegion)
     */
    public LayeredNinePatch withLayer(TextureRegion textureRegion, Color color) {
        layers.add(new NinePatchWidget(width, height, patchLeft, patchRight, patchTop, patchBottom, textureRegion).withColor(color));
        return this;
    }

    /**
     * Pushes a new {@link NinePatchWidget} on top of the rest using a texture. The render color of this layer defaults to WHITE.
     * @param texture the texture of the newest ninepatch
     * @return this widget
     * @see #withLayer(TextureRegion)
     * @see #withLayer(Texture, Color)
     */
    public LayeredNinePatch withLayer(Texture texture) {
        layers.add(new NinePatchWidget(width, height, patchLeft, patchRight, patchTop, patchBottom, texture));
        return this;
    }

    /**
     * Pushes a new {@link NinePatchWidget} with the given color on top of the rest using a texture.
     * @param texture the texture of the newest ninepatch
     * @param color which color is given to the nine patch for rendering, see: {@link NinePatchWidget#withColor(Color)}
     * @return this widget
     * @see #withLayer(Texture)
     * @see #withLayer(TextureRegion)
     */
    public LayeredNinePatch withLayer(Texture texture, Color color) {
        layers.add(new NinePatchWidget(width, height, patchLeft, patchRight, patchTop, patchBottom, texture).withColor(color));
        return this;
    }

    // --------------------------------------------------------------------------------

    /**
     * Attempts to set the render color ({@link NinePatchWidget#withColor(Color)}) of the layer with the given index, if it exists. The layer index is determined by the order added to the overall structure, starting at 0. E.g. the first {@link #withLayer(TextureRegion)} call (or any related function in the withLayer family) will have an index of 0, the second an index of 1, etc. If given an index outside the bounds, this function will do nothing.
     * @param index the non-negative index of the layer to adjust, determined by the add order
     * @param color the color for that particular layer to render with
     * @return this widget
     */
    public LayeredNinePatch withLayerColor(int index, Color color) {
        if (index >= 0 && index < layers.size()) {
            layers.get(index).withColor(color);
        }

        return this;
    }

    // --------------------------------------------------------------------------------

    /**
     * Scales all currently tracked layers to the target widget's full dimensions (e.g. {@link AbstractWidget#getWidth()} etc.). May require re-anchoring afterwards.
     * @param widget the widget whose full size dimensions we wish to copy
     * @return this widget
     * @see NinePatchWidget#scaleToFullWidget(AbstractWidget)
     */
    public LayeredNinePatch scaleToFullWidget(AbstractWidget widget) {
        withDimensions(widget.getWidth(), widget.getHeight());
        return this;
    }

    /**
     * Scales all currently tracked layers to the given width and height. May require re-anchoring afterwards.
     * @param width the new width, in pixels
     * @param height the new height, in pixels
     * @return this widget
     * @see NinePatchWidget#withDimensions(float, float)
     */
    public LayeredNinePatch withDimensions(float width, float height) {
        this.width = width;
        this.height = height;

        scaleHitboxToContent();

        layers.forEach(layer -> layer.withDimensions(width, height));

        return this;
    }

    @Override
    public LayeredNinePatch anchoredAt(float x, float y, AnchorPosition anchorPosition, InterpolationSpeed withDelay) {
        super.anchoredAt(x, y, anchorPosition, withDelay);
        layers.forEach(layer -> layer.anchoredAt(x, y, anchorPosition, withDelay));
        return this;
    }

    // --------------------------------------------------------------------------------

    @Override public float getContentWidth() { return width; }
    @Override public float getContentHeight() { return height; }

    @Override
    protected void renderWidget(SpriteBatch sb) {
        layers.forEach(layer -> layer.render(sb));
    }
}
