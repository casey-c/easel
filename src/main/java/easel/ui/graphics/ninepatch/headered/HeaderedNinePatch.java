package easel.ui.graphics.ninepatch.headered;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.Settings;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;
import easel.ui.graphics.ninepatch.LayeredNinePatch;
import easel.utils.colors.EaselColors;
import easel.utils.textures.TextureDatabase;

public abstract class HeaderedNinePatch<T extends HeaderedNinePatch<T>> extends AbstractWidget<T> {
    private LayeredNinePatch patches;

    private static final Color DEFAULT_TRIM_COLOR = EaselColors.TOOLTIP_TRIM();
    private static final Color DEFAULT_BASE_COLOR = EaselColors.TOOLTIP_BASE();
    private static final Color DEFAULT_HEADER_COLOR = EaselColors.HEADER_SLATE();

    private enum Layers {
        BASE, HEADER, TRIM
    }

    protected boolean shadows = true;
    private static final float SHADOW_SIZE = 4;
    private static final Texture shadowVertical = TextureDatabase.BLACK_GRADIENT_VERTICAL.getTexture();

    private static final float OUTER_TRIM_SIZE = 4;
    private static final float INNER_TRIM_SIZE = 2;

    public HeaderedNinePatch(float width, float height, int patchLeft, int patchRight, int patchTop, int patchBottom, TextureAtlas atlas) {
        this.patches = new LayeredNinePatch(width, height, patchLeft, patchRight, patchTop, patchBottom)
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

    public T withShadows(boolean showShadows) {
        this.shadows = showShadows;
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
        scaleHitboxToContent();
        return (T) this;
    }

    public T withDimensions(float width, float height) {
        patches.withDimensions(width, height);
        return (T) this;
    }

    public T withWidth(float width) {
        patches.withWidth(width);
        return (T) this;
    }

    public T withHeight(float height) {
        patches.withHeight(height);
        return (T) this;
    }

    // --------------------------------------------------------------------------------

    @Override public float getContentWidth() { return patches.getContentWidth(); }
    @Override public float getContentHeight() { return patches.getContentHeight(); }

    @Override
    protected void renderWidget(SpriteBatch sb) {
        patches.render(sb);

        if (shadows) {
            // Vertical shadow under header
            float left = (getContentLeft() + OUTER_TRIM_SIZE) * Settings.xScale;
            float bottom = (getContentTop() - getHeaderHeight() - SHADOW_SIZE) * Settings.yScale;
            float width = (getContentWidth() - 2 * OUTER_TRIM_SIZE) * Settings.xScale;
            float height = SHADOW_SIZE;

            sb.setColor(EaselColors.HALF_TRANSPARENT_WHITE);
            sb.draw(shadowVertical, left, bottom, width, height);
        }
    }
}
