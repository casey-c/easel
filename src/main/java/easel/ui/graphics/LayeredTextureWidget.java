package easel.ui.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;

import java.util.ArrayList;

public class LayeredTextureWidget extends AbstractWidget<LayeredTextureWidget> {
    private float width;
    private float height;

    private ArrayList<SimpleTextureWidget> layers = new ArrayList<>();

    public LayeredTextureWidget(float width, float height) {
        this.width = width;
        this.height = height;
    }

    // --------------------------------------------------------------------------------

    public LayeredTextureWidget withLayer(TextureRegion textureRegion) {
        layers.add(new SimpleTextureWidget(width, height, textureRegion));
        return this;
    }

    public LayeredTextureWidget withLayer(TextureRegion textureRegion, Color renderColor) {
        layers.add(new SimpleTextureWidget(width, height, textureRegion).withColor(renderColor));
        return this;
    }

    public LayeredTextureWidget withLayer(Texture texture) {
        layers.add(new SimpleTextureWidget(width, height, texture));
        return this;
    }

    public LayeredTextureWidget withLayer(Texture texture, Color renderColor) {
        layers.add(new SimpleTextureWidget(width, height, texture).withColor(renderColor));
        return this;
    }

    // --------------------------------------------------------------------------------

    public LayeredTextureWidget withLayerColor(int index, Color renderColor) {
        if (index >= 0 && index < layers.size()) {
            layers.get(index).withColor(renderColor);
        }

        return this;
    }

    public LayeredTextureWidget withWidth(float newWidth) {
        this.width = newWidth;

        scaleHitboxToContent();

        for (SimpleTextureWidget layer : layers) {
            layer.withWidth(newWidth);
        }

        return this;
    }

    public LayeredTextureWidget withHeight(float newHeight) {
        this.height = newHeight;

        scaleHitboxToContent();

        for (SimpleTextureWidget layer : layers) {
            layer.withHeight(newHeight);
        }

        return this;
    }

    public LayeredTextureWidget withDimensions(float newWidth, float newHeight) {
        withWidth(newWidth);
        return withHeight(newHeight);
    }

    // --------------------------------------------------------------------------------

    @Override
    public LayeredTextureWidget anchoredAt(float x, float y, AnchorPosition anchorPosition, InterpolationSpeed movementSpeed) {
        super.anchoredAt(x, y, anchorPosition, movementSpeed);
        layers.forEach(layer -> layer.anchoredAt(x, y, anchorPosition, movementSpeed));
        return this;
    }


    // --------------------------------------------------------------------------------

    @Override public float getContentWidth() { return width; }
    @Override public float getContentHeight() { return height; }

    // --------------------------------------------------------------------------------

    @Override
    protected void renderWidget(SpriteBatch sb) {

    }
}
