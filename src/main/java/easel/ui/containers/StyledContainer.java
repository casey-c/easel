package easel.ui.containers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.Settings;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;
import easel.ui.graphics.ninepatch.NinePatchWidget;
import easel.ui.layouts.VerticalLayout;
import easel.ui.text.Label;
import easel.utils.EaselFonts;
import easel.utils.colors.EaselColors;
import easel.utils.textures.TextureAtlasDatabase;
import easel.utils.textures.TextureDatabase;

public class StyledContainer extends AbstractWidget<StyledContainer> {
    private float width;
    private float height;

    private boolean hasHeader = false;
    private boolean hasCustomHeader = false;

    private AnchorPosition headerAnchor = AnchorPosition.CENTER;
    private AnchorPosition contentAnchor = AnchorPosition.CENTER;

    private static final TextureAtlas atlas = TextureAtlasDatabase.STYLED_CONTAINER.getAtlas();

    private NinePatchWidget npFullBase;
    private NinePatchWidget npFullTrim;
    private NinePatchWidget npHeaderBase;
    private NinePatchWidget npHeaderTrim;

    private VerticalLayout defaultHeader;
    private AbstractWidget customHeader;
    private AbstractWidget content;

    private Color baseColor = EaselColors.TOOLTIP_BASE();
    private Color trimColor = EaselColors.TOOLTIP_TRIM();
    private Color headerColor = EaselColors.HEADER_PURPLE();

    // Shadows
    private static final float OUTER_TRIM_SIZE = 4;
    private static final float SHADOW_SIZE = 4;
    private static final Texture SHADOW_TEXTURE = TextureDatabase.BLACK_GRADIENT_VERTICAL.getTexture();

    // --------------------------------------------------------------------------------

    public StyledContainer(float width, float height) {
        this.npFullBase = new NinePatchWidget(width, height, atlas.findRegion("base"))
                .withColor(baseColor);

        this.npFullTrim = new NinePatchWidget(width, height, atlas.findRegion("trim"))
                .withColor(trimColor);

        this.width = width;
        this.height = height;
    }

    // --------------------------------------------------------------------------------

    private void constructHeaderNP() {
        AbstractWidget headerContents = (hasCustomHeader) ? customHeader : defaultHeader;

        this.npHeaderBase = new NinePatchWidget(width, headerContents.getHeight(), atlas.findRegion("header_base"))
                .withColor(headerColor);

        this.npHeaderTrim = new NinePatchWidget(width, headerContents.getHeight(), atlas.findRegion("header_trim"))
                .withColor(trimColor);
    }

    public StyledContainer withHeader(String title) {
        this.hasHeader = true;
        this.hasCustomHeader = false;

        this.defaultHeader = new VerticalLayout(width, 0)
                .withMargins(40, 20)
                .withDefaultChildAnchorPosition(headerAnchor)
                .withChild(new Label(title, EaselFonts.SMALLER_TIP_BODY, Settings.CREAM_COLOR))
                .resizeWidthToWidestChild();

        constructHeaderNP();

        return this;
    }

    public StyledContainer withHeader(String title, String subtitle) {
        this.hasHeader = true;
        this.hasCustomHeader = false;

        this.defaultHeader = new VerticalLayout(width, 0)
                .withMargins(40, 20)
                .withDefaultChildAnchorPosition(headerAnchor)
                .withChild(new Label(title, EaselFonts.SMALLER_TIP_BODY, Settings.CREAM_COLOR))
                .withChild(new Label(subtitle, EaselFonts.MEDIUM_ITALIC, Color.GRAY))
                .resizeWidthToWidestChild();

        constructHeaderNP();

        return this;
    }

    public StyledContainer withHeader(AbstractWidget customHeader, boolean autoAddMargins) {
        this.hasHeader = true;
        this.hasCustomHeader = true;

        this.customHeader = customHeader;

        if (autoAddMargins)
            this.customHeader.withMargins(40, 20);

        constructHeaderNP();

        return this;
    }

    // --------------------------------------------------------------------------------

    public StyledContainer withHeaderColor(Color headerColor) {
        this.headerColor = headerColor;

        if (hasHeader) {
            npHeaderBase.withColor(headerColor);
        }

        return this;
    }

    public StyledContainer withBaseColor(Color baseColor) {
        this.baseColor = baseColor;
        this.npFullBase.withColor(baseColor);
        return this;
    }

    public StyledContainer withTrimColor(Color trimColor) {
        this.trimColor = trimColor;
        this.npFullTrim.withColor(trimColor);

        if (hasHeader)
            this.npHeaderTrim.withColor(trimColor);

        return this;
    }

    // --------------------------------------------------------------------------------

    public StyledContainer withHeaderAnchor(AnchorPosition headerAnchor) {
        this.headerAnchor = headerAnchor;

        if (hasHeader && !hasCustomHeader) {
            defaultHeader.forceChildAnchors(headerAnchor);
        }

        // TODO: is this really necessary here? Will users ever call withHeaderAnchor() after already anchored?
        //   Cause if not, then this is potentially anchoring twice. Should document this in the javadoc!
//        refreshAnchor();

        return this;
    }

    public StyledContainer withContentAnchor(AnchorPosition contentAnchor) {
        this.contentAnchor = contentAnchor;
        return this;
    }

    // --------------------------------------------------------------------------------

    public StyledContainer withContent(AbstractWidget content, boolean autoAddMargins) {
        this.content = content;

        if (autoAddMargins)
            this.content.withMargins(40);

        return this;
    }

    public StyledContainer scaleToContentWidth() {
        if (content == null)
            return this;

        float newWidth = content.getWidth();

        // Account for the header width as well
        if (hasHeader) {
            if (hasCustomHeader)
                newWidth = Math.max(newWidth, customHeader.getWidth());
            else
                newWidth = Math.max(newWidth, defaultHeader.getWidth());
        }

        npFullBase.withWidth(newWidth);
        npFullTrim.withWidth(newWidth);

        if (hasHeader) {
            npHeaderBase.withWidth(newWidth);
            npHeaderTrim.withWidth(newWidth);
        }

        this.width = newWidth;
        scaleHitboxToContent();

        return this;
    }

    public StyledContainer scaleToContentHeight() {
        if (content == null)
            return this;

        float newHeight = content.getHeight() + getHeaderHeight();

        npFullBase.withHeight(newHeight);
        npFullTrim.withHeight(newHeight);

        this.height = newHeight;
        scaleHitboxToContent();

        return this;
    }

    public StyledContainer scaleToContent() {
        scaleToContentWidth();
        return scaleToContentHeight();
    }

    // --------------------------------------------------------------------------------

    @Override public float getContentWidth() { return width; }
    @Override public float getContentHeight() { return height; }

    // --------------------------------------------------------------------------------

    private float getHeaderHeight() {
        return hasHeader ? npHeaderBase.getHeight() : 0.0f;
    }

    private float getMainContentAreaHeight() {
        return getContentHeight() - getHeaderHeight();
    }

    // --------------------------------------------------------------------------------

    @Override
    public StyledContainer anchoredAt(float x, float y, AnchorPosition anchorPosition, InterpolationSpeed withDelay) {
        super.anchoredAt(x, y, anchorPosition, withDelay);

        npFullBase.anchoredAt(getContentLeft(), getContentTop(), AnchorPosition.LEFT_TOP, withDelay);
        npFullTrim.anchoredAt(getContentLeft(), getContentTop(), AnchorPosition.LEFT_TOP, withDelay);

        // Header
        if (hasHeader) {
            npHeaderBase.anchoredAt(getContentLeft(), getContentTop(), AnchorPosition.LEFT_TOP, withDelay);
            npHeaderTrim.anchoredAt(getContentLeft(), getContentTop(), AnchorPosition.LEFT_TOP, withDelay);

            float hx = headerAnchor.getXFromLeft(getContentLeft(), getContentWidth());
            float hy = headerAnchor.getYFromTop(getContentTop(), getHeaderHeight());

            if (hasCustomHeader)
                customHeader.anchoredAt(hx, hy, headerAnchor, withDelay);
            else
                defaultHeader.anchoredAt(hx, hy, headerAnchor, withDelay);
        }

        // Content
        if (content != null) {
            float cx = contentAnchor.getXFromLeft(getContentLeft(), getContentWidth());
            float cy = contentAnchor.getYFromBottom(getContentBottom(), getMainContentAreaHeight());

            content.anchoredAt(cx, cy, contentAnchor, withDelay);
        }

        return this;
    }


    // --------------------------------------------------------------------------------

    @Override
    protected void updateWidget() {
        super.updateWidget();

        if (hasHeader && hasCustomHeader)
            customHeader.update();

        if (content != null)
            content.update();
    }

    @Override
    protected void renderWidget(SpriteBatch sb) {
        npFullBase.render(sb);

        if (hasHeader) {
            npHeaderBase.render(sb);

            if (hasCustomHeader)
                customHeader.render(sb);
            else
                defaultHeader.render(sb);

            npHeaderTrim.render(sb);
        }

        if (content != null)
            content.render(sb);

        if (hasHeader) {
            // Vertical shadow under header (this will render on top of the content, but it probably never ends up mattering)
            float left = (getContentLeft() + OUTER_TRIM_SIZE) * Settings.xScale;
            float bottom = (getContentTop() - getHeaderHeight() - SHADOW_SIZE) * Settings.yScale;
            float width = (getContentWidth() - 2 * OUTER_TRIM_SIZE) * Settings.xScale;
            float height = SHADOW_SIZE;

            sb.setColor(EaselColors.HALF_TRANSPARENT_WHITE);
            sb.draw(SHADOW_TEXTURE, left, bottom, width, height);
        }

        npFullTrim.render(sb);
    }
}
