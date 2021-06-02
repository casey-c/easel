package easel.ui.graphics.ninepatch.headered;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;
import easel.utils.colors.EaselColors;

public abstract class HeaderedNinePatch<T extends HeaderedNinePatch<T>> extends AbstractWidget<T> {
    private easel.ui.graphics.ninepatch.HeaderedNinePatch patches;

    private static final Color DEFAULT_TRIM_COLOR = EaselColors.TOOLTIP_TRIM();
    private static final Color DEFAULT_BASE_COLOR = EaselColors.TOOLTIP_BASE();
    private static final Color DEFAULT_HEADER_COLOR = EaselColors.HEADER_SLATE();

    private enum Layers {
        BASE, HEADER, TRIM
    }

    public HeaderedNinePatch(float width, float height, int patchLeft, int patchRight, int patchTop, int patchBottom, TextureAtlas atlas) {
        this.patches = new easel.ui.graphics.ninepatch.HeaderedNinePatch(width, height, patchLeft, patchRight, patchTop, patchBottom)
                .withLayer(atlas.findRegion("base"), DEFAULT_BASE_COLOR)
                .withLayer(atlas.findRegion("header"), DEFAULT_HEADER_COLOR)
                .withLayer(atlas.findRegion("trim"), DEFAULT_TRIM_COLOR);
    }

    // --------------------------------------------------------------------------------

    public T withHeaderColor(Color headerColor) {
        this.patches.withLayerColor(Layers.HEADER.ordinal(), headerColor);
        return (T)this;
    }

    public T withBaseColor(Color baseColor) {
        this.patches.withLayerColor(Layers.BASE.ordinal(), baseColor);
        return (T)this;
    }

    public T withTrimColor(Color trimColor) {
        this.patches.withLayerColor(Layers.TRIM.ordinal(), trimColor);
        return (T)this;
    }

    // --------------------------------------------------------------------------------

    public abstract int getHeaderHeight();

    // --------------------------------------------------------------------------------

    @Override
    public T anchoredAt(float x, float y, AnchorPosition anchorPosition, InterpolationSpeed withDelay) {
        super.anchoredAt(x, y, anchorPosition, withDelay);

        patches.anchoredAt(x, y, anchorPosition, withDelay);

        return (T)this;
    }

    // --------------------------------------------------------------------------------

    public T scaleToFullWidget(AbstractWidget widget) {
        patches.scaleToFullWidget(widget);
        return (T) this;
    }

    public T withDimensions(float width, float height) {
        patches.withDimensions(width, height);
        return (T) this;
    }

    // --------------------------------------------------------------------------------

    @Override public float getContentWidth() { return patches.getContentWidth(); }
    @Override public float getContentHeight() { return patches.getContentHeight(); }

    @Override
    protected void renderWidget(SpriteBatch sb) {
        patches.render(sb);
    }
}
