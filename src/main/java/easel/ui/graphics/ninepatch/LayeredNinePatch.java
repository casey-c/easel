package easel.ui.graphics.ninepatch;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import easel.ui.AbstractWidget;

import java.util.ArrayList;

public class LayeredNinePatch extends AbstractWidget<LayeredNinePatch> {
    private float width;
    private float height;

    private int patchLeft;
    private int patchRight;
    private int patchTop;
    private int patchBottom;

    protected ArrayList<NinePatchWidget> layers = new ArrayList<>();

    public LayeredNinePatch(float width, float height) {
        this(width, height, 32, 32, 32, 32);
    }

    public LayeredNinePatch(float width, float height, int patchLeft, int patchRight, int patchTop, int patchBottom) {
        this.width = width;
        this.height = height;

        this.patchLeft = patchLeft;
        this.patchRight = patchRight;
        this.patchTop = patchTop;
        this.patchBottom = patchBottom;
    }

    // --------------------------------------------------------------------------------

    public LayeredNinePatch withLayer(TextureRegion textureRegion) {
        layers.add(new NinePatchWidget(width, height, patchLeft, patchRight, patchTop, patchBottom, textureRegion));
        return this;
    }

    public LayeredNinePatch withLayer(TextureRegion textureRegion, Color color) {
        layers.add(new NinePatchWidget(width, height, patchLeft, patchRight, patchTop, patchBottom, textureRegion).withColor(color));
        return this;
    }

    public LayeredNinePatch withLayer(Texture texture) {
        layers.add(new NinePatchWidget(width, height, patchLeft, patchRight, patchTop, patchBottom, texture));
        return this;
    }

    public LayeredNinePatch withLayer(Texture texture, Color color) {
        layers.add(new NinePatchWidget(width, height, patchLeft, patchRight, patchTop, patchBottom, texture).withColor(color));
        return this;
    }

    // --------------------------------------------------------------------------------

    public LayeredNinePatch withLayerColor(int index, Color color) {
        if (index < layers.size()) {
            layers.get(index).withColor(color);
        }

        return this;
    }

    // --------------------------------------------------------------------------------

    public LayeredNinePatch scaleToFullWidget(AbstractWidget widget) {
        layers.forEach(layer -> layer.scaleToFullWidget(widget));
        return this;
    }

    public LayeredNinePatch withDimensions(float width, float height) {
        this.width = width;
        this.height = height;

        scaleHitboxToContent();

        layers.forEach(layer -> layer.withDimensions(width, height));

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
