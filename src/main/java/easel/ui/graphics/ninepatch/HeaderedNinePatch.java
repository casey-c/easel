package easel.ui.graphics.ninepatch;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;
import easel.utils.colors.EaselColors;

public abstract class HeaderedNinePatch<T extends HeaderedNinePatch<T>> extends AbstractWidget<T> {
    private float width;
    private float height;

    private NinePatchWidget trim;
    private NinePatchWidget base;
    private NinePatchWidget header;

    private static final Color DEFAULT_TRIM_COLOR = EaselColors.TOOLTIP_TRIM();
    private static final Color DEFAULT_BASE_COLOR = EaselColors.TOOLTIP_BASE();
    private static final Color DEFAULT_HEADER_COLOR = EaselColors.HEADER_SLATE();

    public HeaderedNinePatch(float width, float height, int patchLeft, int patchRight, int patchTop, int patchBottom, TextureAtlas atlas) {
        this.base = new NinePatchWidget(width,
                height,
                patchLeft,
                patchRight,
                patchTop,
                patchBottom,
                atlas.findRegion("base"))
                .withColor(DEFAULT_BASE_COLOR)
        ;

        this.header = new NinePatchWidget(width,
                height,
                patchLeft,
                patchRight,
                patchTop,
                patchBottom,
                atlas.findRegion("header"))
                .withColor(DEFAULT_HEADER_COLOR)
        ;

        this.trim = new NinePatchWidget(width,
                height,
                patchLeft,
                patchRight,
                patchTop,
                patchBottom,
                atlas.findRegion("trim"))
                .withColor(DEFAULT_TRIM_COLOR)
        ;

        this.width = width;
        this.height = height;
    }

    // --------------------------------------------------------------------------------

    public T withHeaderColor(Color headerColor) {
        this.header.withColor(headerColor);
        return (T)this;
    }

    public T withBaseColor(Color baseColor) {
        this.base.withColor(baseColor);
        return (T)this;
    }

    public T withTrimColor(Color trimColor) {
        this.trim.withColor(trimColor);
        return (T)this;
    }

    // --------------------------------------------------------------------------------

    @Override
    public T anchoredAt(float x, float y, AnchorPosition anchorPosition, InterpolationSpeed withDelay) {
        super.anchoredAt(x, y, anchorPosition, withDelay);

        base.anchoredAt(x, y, anchorPosition, withDelay);
        header.anchoredAt(x, y, anchorPosition, withDelay);
        trim.anchoredAt(x, y, anchorPosition, withDelay);

        return (T)this;
    }

    // --------------------------------------------------------------------------------

    @Override public float getContentWidth() { return width; }
    @Override public float getContentHeight() { return height; }

    @Override
    protected void renderWidget(SpriteBatch sb) {
        base.render(sb);
        header.render(sb);
        trim.render(sb);
    }
}
