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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * A container with a nice textured background and borders. Contains some helpers for making it have a nice header and for scaling everything to fit the given contents. Like layouts or other containers: anchoring, rendering, updates and the other usual suspects will be passed down the hierarchy, so calling them once on the container itself will be sufficient to handle the content and header widgets managed by this container. StyledContainers work nicely with {@link MoveContainer}s, and are designed as a "super" tooltip to give the base game tooltips a bit more flexibility. They can also be used as larger screens as well.
 * </p>
 * <p>
 * Example use:
 * </p>
 * <pre>
 * {@code
 * StyledContainer c = new StyledContainer(500, 500)
 *     .withHeader("Title", "Subtitle")
 *     .withHeaderAnchor(AnchorPosition.LEFT_CENTER)
 *     .withHeaderColor(EaselColors.HEADER_PURPLE())
 *     .withContent(
 *         new VerticalLayout(20)
 *             .withChild(new Label("Content One"))
 *             .withChild(new Label("Content Two"))
 *     )
 *     .scaleToContent()
 *     .anchoredCenteredOnScreen();
 * }
 * </pre>
 */
public class StyledContainer extends AbstractWidget<StyledContainer> {
    private float width;
    private float height;

    private boolean hasHeader = false;
    private boolean hasCustomHeader = false;

    private AnchorPosition headerAnchor = AnchorPosition.CENTER;
    private AnchorPosition contentAnchor = AnchorPosition.CENTER;

    private static final TextureAtlas atlas = TextureAtlasDatabase.STYLED_CONTAINER.getTextureAtlas();

    private NinePatchWidget npFullShadow;
    private NinePatchWidget npFullBase;
    private NinePatchWidget npFullTrim;
    private NinePatchWidget npFullTrimHighlight;

    private NinePatchWidget npHeaderBase;
    private NinePatchWidget npHeaderTrim;

    private VerticalLayout defaultHeader;
    private AbstractWidget customHeader;
    private AbstractWidget content;

    private Color baseColor = EaselColors.TOOLTIP_BASE();
    private Color trimColor = EaselColors.TOOLTIP_TRIM();
    private Color trimHighlightColor = EaselColors.TOOLTIP_TRIM_HIGHLIGHT();
    private Color headerColor = EaselColors.HEADER_BLUE();

    // Shadows
    private boolean renderFullShadows = false;

    private static final float SHADOW_OFFSET_X = 7;
    private static final float SHADOW_OFFSET_Y = 7;

    private static final float OUTER_TRIM_SIZE = 4;
    private static final float SHADOW_SIZE = 4;
    private static final Texture SHADOW_TEXTURE = TextureDatabase.BLACK_GRADIENT_VERTICAL.getTexture();

    // --------------------------------------------------------------------------------

    /**
     * Constructs a new styled container with the given dimensions. These dimensions can be changed later (e.g. scaled to content with {@link #scaleToContent()} or resized manually with {@link #withDimensions(float, float)}).
     * @param width the entire width of the container
     * @param height the entire height of the container
     */
    public StyledContainer(float width, float height) {
        this.npFullShadow = new NinePatchWidget(width, height, atlas.findRegion("shadow"))
                .withColor(Settings.QUARTER_TRANSPARENT_WHITE_COLOR);

        this.npFullBase = new NinePatchWidget(width, height, atlas.findRegion("base"))
                .withColor(baseColor);

        this.npFullTrim = new NinePatchWidget(width, height, atlas.findRegion("trim"))
                .withColor(trimColor);

        this.npFullTrimHighlight = new NinePatchWidget(width, height, atlas.findRegion("trim_highlight"))
                .withColor(trimHighlightColor);

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

    /**
     * Add a single-line title to this container with the given text. Header text position is affected by {@link #withHeaderAnchor(AnchorPosition)}.
     * @param title the text to serve as the header
     * @return this widget
     */
    public StyledContainer withHeader(String title) {
        this.hasHeader = true;
        this.hasCustomHeader = false;

        this.defaultHeader = new VerticalLayout(width, 0)
                .withMargins(40, 20)
                .withDefaultChildAnchorPosition(headerAnchor)
                .withChild(new Label(title, EaselFonts.SMALLER_TIP_BODY, Settings.CREAM_COLOR))
                .scaleToWidestChild();

        constructHeaderNP();

        return this;
    }

    /**
     * Add a double-line title/subtitle to this container with the given text. Header text position is affected by {@link #withHeaderAnchor(AnchorPosition)}.
     * @param title the text to serve as the header title
     * @param subtitle the text to serve as the header subtitle
     * @return this widget
     */
    public StyledContainer withHeader(String title, String subtitle) {
        this.hasHeader = true;
        this.hasCustomHeader = false;

        this.defaultHeader = new VerticalLayout(width, 0)
                .withMargins(40, 20)
                .withDefaultChildAnchorPosition(headerAnchor)
                .withChild(new Label(title, EaselFonts.SMALLER_TIP_BODY, Settings.CREAM_COLOR))
                .withChild(new Label(subtitle, EaselFonts.MEDIUM_ITALIC, Color.GRAY))
                .scaleToWidestChild();

        constructHeaderNP();

        return this;
    }

    /**
     * Add a custom child widget to serve as this containers header. Header position is affected by {@link #withHeaderAnchor(AnchorPosition)}.
     * @param customHeader the widget which will sit in the header region (which automatically scales to surround this custom header)
     * @param autoAddMargins if true, will call {@link AbstractWidget#withMargins(float, float)} on the <code>customHeader</code> with some suggested defaults (the header needs some sort of margins to not touch the borders of the container)
     * @return this widget
     */
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

    /**
     * The background color of the header, if it exists (use with one of the header builder functions: {@link #withHeader(String)} etc.). The header colors can be picked from the <code>EaselColors.HEADER_</code> family of colors, such as {@link EaselColors#HEADER_RED()}. These header colors were chosen since they can fit well with the default background and trim colors of the overall container, and will work with the default header text colors. If not set, defaults to {@link EaselColors#HEADER_BLUE()}.
     * @param headerColor the desired color of the header
     * @return this widget
     */
    public StyledContainer withHeaderColor(Color headerColor) {
        this.headerColor = headerColor;

        if (hasHeader) {
            npHeaderBase.withColor(headerColor);
        }

        return this;
    }

    /**
     * The background color of the entire container texture. Defaults to {@link EaselColors#TOOLTIP_BASE()} if not set.
     * @param baseColor the new background color
     * @return this widget
     */
    public StyledContainer withBaseColor(Color baseColor) {
        this.baseColor = baseColor;
        this.npFullBase.withColor(baseColor);
        return this;
    }

    /**
     * The trim colors of the entire container texture (for the outermost borders). The styled container mimics some of the base game border style by having a regular trim color and a specific highlight color to contrast against it. Defaults to {@link EaselColors#TOOLTIP_TRIM()} and {@link EaselColors#TOOLTIP_TRIM_HIGHLIGHT()} if not set.
     * @param trimColor the basic border color
     * @param trimHighlightColor the highlight color for the border
     * @return this widget
     */
    public StyledContainer withTrimColors(Color trimColor, Color trimHighlightColor) {
        this.trimColor = trimColor;
        this.trimHighlightColor = trimHighlightColor;

        this.npFullTrim.withColor(trimColor);
        this.npFullTrimHighlight.withColor(trimHighlightColor);

        if (hasHeader)
            this.npHeaderTrim.withColor(trimColor);

        return this;
    }

    // --------------------------------------------------------------------------------

    // TODO remember to talk about the need to refreshAnchor() afterwards

    /**
     * Sets the position of the header contents inside the header region. Requires anchoring after calling this in order for the changes to take effect.
     * @param headerAnchor where the header contents are anchored inside the header region
     * @return this widget
     */
    public StyledContainer withHeaderAnchor(AnchorPosition headerAnchor) {
        this.headerAnchor = headerAnchor;

        if (hasHeader && !hasCustomHeader) {
            defaultHeader.forceChildAnchors(headerAnchor);
        }

        return this;
    }

    /**
     * Sets the position of the main contents inside the bottom region. Requires anchoring afterwards for the changes to take effect.
     * @param contentAnchor where the main contents are anchored in the bottom section of the container
     * @return this widget
     */
    public StyledContainer withContentAnchor(AnchorPosition contentAnchor) {
        this.contentAnchor = contentAnchor;
        return this;
    }

    /**
     * Set whether a drop shadow is rendered underneath the container. Defaults to false if not set, and recommended to not alter this manually. Toggled by {@link MoveContainer} when moving styled containers around to improve the aesthetics a bit.
     * @param enabled if true, will render shadows beneath the entire widget
     * @return this widget
     */
    public StyledContainer withShadows(boolean enabled) {
        this.renderFullShadows = enabled;
        return this;
    }

    // --------------------------------------------------------------------------------

    /**
     * Set the main content of this container. This gets rendered underneath the header (if it exists) and takes up the primary area of the container. The content itself is often a layout containing its own widget hierarchy. Like custom header widgets, this contains a <code>autoAddMargins</code> flag which can be used to automatically give the new content a reasonable set of default margins. Margins on the content widget itself are required for the content to not touch the sides of the container. Requires re-anchoring.
     * @param content the main widget that is the focus of this container
     * @param autoAddMargins if true, will call {@link AbstractWidget#withMargins(float)} on the content widget with some suggested values
     * @return this widget
     */
    public StyledContainer withContent(AbstractWidget content, boolean autoAddMargins) {
        this.content = content;

        if (autoAddMargins)
            this.content.withMargins(40);

        return this;
    }

    /**
     * Set the new width of the full container to a new value. Requires re-anchoring.
     * @param newWidth the new width
     * @return this widget
     */
    public StyledContainer withWidth(float newWidth) {
        npFullShadow.withWidth(newWidth);
        npFullBase.withWidth(newWidth);
        npFullTrim.withWidth(newWidth);
        npFullTrimHighlight.withWidth(newWidth);

        if (hasHeader) {
            npHeaderBase.withWidth(newWidth);
            npHeaderTrim.withWidth(newWidth);
        }

        this.width = newWidth;
        scaleHitboxToContent();

        return this;
    }

    /**
     * Set the new height of the full container to a new value Requires re-anchoring.
     * @param newHeight the new height
     * @return this widget
     */
    public StyledContainer withHeight(float newHeight) {
        npFullShadow.withHeight(newHeight);
        npFullBase.withHeight(newHeight);
        npFullTrim.withHeight(newHeight);
        npFullTrimHighlight.withHeight(newHeight);

        this.height = newHeight;
        scaleHitboxToContent();

        return this;
    }

    /**
     * Set new dimensions for the full container. Convenience for calling both {@link #withWidth(float)} and {@link #withHeight(float)} at the same time. Requires re-anchoring.
     * @param newWidth the new width
     * @param newHeight the new height
     * @return this widget
     */
    public StyledContainer withDimensions(float newWidth, float newHeight) {
        withWidth(newWidth);
        return withHeight(newHeight);
    }

    /**
     * Automatically scale the container to fit its main content (and header) width. Does nothing if no content widget has been set. Requires re-anchoring.
     * @return this widget
     */
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

        return withWidth(newWidth);
    }

    /**
     * Automatically scale the container to fit its main content (and header) height. Does nothing if no content widget has been set. Requires re-anchoring.
     * @return this widget
     */
    public StyledContainer scaleToContentHeight() {
        if (content == null)
            return this;

        return withHeight(content.getHeight() + getHeaderHeight());
    }

    /**
     * Automatically scale the container to fit the main contents. Calling this after setting up the container's content and headers is extremely common, as it scales the container itself to fit the widgets you've added.
     * @return this widget
     */
    public StyledContainer scaleToContent() {
        scaleToContentWidth();
        return scaleToContentHeight();
    }

    // --------------------------------------------------------------------------------

    /**
     * See {@link #syncContainerWidths(boolean, Stream)}. Will not automatically scale to content first.
     * @param containers a variadic list of containers to scale
     * @see #syncContainerWidths(boolean, Stream)
     */
    public static void syncContainerWidths(StyledContainer... containers) {
        syncContainerWidths(false, containers);
    }

    /**
     * See {@link #syncContainerWidths(boolean, Stream)}. Will not automatically scale to content first.
     * @param containers a stream of containers to scale
     * @see #syncContainerWidths(boolean, Stream)
     */
    public static void syncContainerWidths(Stream<StyledContainer> containers) {
        syncContainerWidths(false, containers);
    }

    /**
     * See {@link #syncContainerWidths(boolean, Stream)}. Will not automatically scale to content first.
     * @param containers a list of containers to scale
     * @see #syncContainerWidths(boolean, Stream)
     */
    public static void syncContainerWidths(List<StyledContainer> containers) {
        syncContainerWidths(false, containers);
    }

    /**
     * See {@link #syncContainerWidths(boolean, Stream)}
     * @param scaleToContentFirst whether to call {@link #scaleToContent()} on each container first before scaling all widths to the maximum
     * @param containers a variadic list of containers to scale
     * @see #syncContainerWidths(boolean, Stream)
     */
    public static void syncContainerWidths(boolean scaleToContentFirst, StyledContainer... containers) {
        syncContainerWidths(scaleToContentFirst, Stream.of(containers));
    }

    /**
     * <p>
     * Makes all StyledContainers in the stream share the width of the widest one. As this is a width altering function, each affected widget will require re-anchoring before being used. The <code>scaleToContentFirst</code> flag is mostly for convenience and if set, will call {@link #scaleToContent()} on all widgets before computing the the final new width.
     * </p>
     * <p>
     * An example use:
     * </p>
     * <pre>
     * {@code
     * // This is some layout which contains the StyledContainers as children:
     * layout = ...;
     *
     * // We want to sync up all the containers to have the same width
     * // the first argument "true" simply calls .scaleToContent() on each container
     * // before computing the width to scale everything to
     * StyledContainer.syncContainerWidths(true, layout.iteratorOfType(StyledContainer.class));
     *
     * // We've (probably) adjusted the dimensions of multiple styled containers, so be sure
     * // to reanchor everything affected. Since we've stored the containers in a layout, you
     * // can just perform your anchoring step on it now
     * layout.anchoredCenteredOnScreen();
     * }
     * </pre>
     * @param scaleToContentFirst whether to call {@link #scaleToContent()} on each container first before scaling all widths to the maximum
     * @param containers a stream of containers to scale
     * @see #syncContainerHeights(boolean, Stream)
     * @see #syncContainerWidths(boolean, StyledContainer...)
     * @see #syncContainerWidths(boolean, List)
     * @see #syncContainerWidths(Stream)
     * @see #syncContainerWidths(List)
     * @see #syncContainerWidths(StyledContainer...)
     */
    public static void syncContainerWidths(boolean scaleToContentFirst, Stream<StyledContainer> containers) {
        syncContainerWidths(scaleToContentFirst, containers.collect(Collectors.toList()));
    }

    /**
     * See {@link #syncContainerWidths(boolean, Stream)}
     * @param scaleToContentFirst whether to call {@link #scaleToContent()} on each container first before scaling all widths to the maximum
     * @param containers a list of containers to scale
     * @see #syncContainerWidths(boolean, Stream)
     */
    public static void syncContainerWidths(boolean scaleToContentFirst, List<StyledContainer> containers) {
        if (scaleToContentFirst)
            containers.forEach(StyledContainer::scaleToContent);

        float maxWidth = containers.stream().map(container -> container.width)
                .max(Float::compareTo)
                .orElse(0.0f);

        containers.forEach(container -> container.withWidth(maxWidth));
    }

    // --------------------------------------------------------------------------------

    /**
     * See {@link #syncContainerHeights(boolean, Stream)}. Will not automatically scale to content first.
     * @param containers a variadic list of containers to scale
     * @see #syncContainerHeights(boolean, Stream)
     */
    public static void syncContainerHeights(StyledContainer... containers) {
        syncContainerHeights(false, containers);
    }

    /**
     * See {@link #syncContainerHeights(boolean, Stream)}. Will not automatically scale to content first.
     * @param containers a stream of containers to scale
     * @see #syncContainerHeights(boolean, Stream)
     */
    public static void syncContainerHeights(Stream<StyledContainer> containers) {
        syncContainerHeights(false, containers);
    }

    /**
     * See {@link #syncContainerHeights(boolean, Stream)}. Will not automatically scale to content first.
     * @param containers a list of containers to scale
     * @see #syncContainerHeights(boolean, Stream)
     */
    public static void syncContainerHeights(List<StyledContainer> containers) {
        syncContainerHeights(false, containers);
    }

    /**
     * See {@link #syncContainerHeights(boolean, Stream)}.
     * @param scaleToContentFirst whether to call {@link #scaleToContent()} on each container first before scaling all heights to the maximum
     * @param containers a stream of containers to scale
     * @see #syncContainerHeights(boolean, Stream)
     */
    public static void syncContainerHeights(boolean scaleToContentFirst, StyledContainer... containers) {
        syncContainerHeights(scaleToContentFirst, Stream.of(containers));
    }

    /**
     * Makes all containers share the height of the tallest container. Not as common as {@link #syncContainerWidths(boolean, Stream)}, but can be used in a similar manner.
     * @param scaleToContentFirst whether to call {@link #scaleToContent()} on each container first before scaling all heights to the maximum
     * @param containers a stream of containers to scale heights of
     * @see #syncContainerWidths(boolean, Stream)
     * @see #syncContainerHeights(boolean, StyledContainer...)
     * @see #syncContainerHeights(boolean, List)
     * @see #syncContainerHeights(Stream)
     * @see #syncContainerHeights(StyledContainer...)
     * @see #syncContainerHeights(List)
     */
    public static void syncContainerHeights(boolean scaleToContentFirst, Stream<StyledContainer> containers) {
        syncContainerHeights(scaleToContentFirst, containers.collect(Collectors.toList()));
    }

    /**
     * See {@link #syncContainerHeights(boolean, Stream)}.
     * @param scaleToContentFirst whether to call {@link #scaleToContent()} on each container first before scaling all heights to the maximum
     * @param containers a list of containers to scale
     * @see #syncContainerHeights(boolean, Stream)
     */
    public static void syncContainerHeights(boolean scaleToContentFirst, List<StyledContainer> containers) {
        if (scaleToContentFirst)
            containers.forEach(StyledContainer::scaleToContent);

        float maxHeight = containers.stream().map(container -> container.height)
                .max(Float::compareTo)
                .orElse(0.0f);

        containers.forEach(container -> container.withHeight(maxHeight));
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
    public StyledContainer anchoredAt(float x, float y, AnchorPosition anchorPosition, InterpolationSpeed movementSpeed) {
        super.anchoredAt(x, y, anchorPosition, movementSpeed);

        npFullShadow.anchoredAt(getContentLeft() + SHADOW_OFFSET_X, getContentTop() - SHADOW_OFFSET_Y, AnchorPosition.LEFT_TOP, movementSpeed);
        npFullBase.anchoredAt(getContentLeft(), getContentTop(), AnchorPosition.LEFT_TOP, movementSpeed);
        npFullTrim.anchoredAt(getContentLeft(), getContentTop(), AnchorPosition.LEFT_TOP, movementSpeed);
        npFullTrimHighlight.anchoredAt(getContentLeft(), getContentTop(), AnchorPosition.LEFT_TOP, movementSpeed);

        // Header
        if (hasHeader) {
            npHeaderBase.anchoredAt(getContentLeft(), getContentTop(), AnchorPosition.LEFT_TOP, movementSpeed);
            npHeaderTrim.anchoredAt(getContentLeft(), getContentTop(), AnchorPosition.LEFT_TOP, movementSpeed);

            float hx = headerAnchor.getXFromLeft(getContentLeft(), getContentWidth());
            float hy = headerAnchor.getYFromTop(getContentTop(), getHeaderHeight());

            if (hasCustomHeader)
                customHeader.anchoredAt(hx, hy, headerAnchor, movementSpeed);
            else
                defaultHeader.anchoredAt(hx, hy, headerAnchor, movementSpeed);
        }

        // Content
        if (content != null) {
            float cx = contentAnchor.getXFromLeft(getContentLeft(), getContentWidth());
            float cy = contentAnchor.getYFromBottom(getContentBottom(), getMainContentAreaHeight());

            // Note: if the content width is thinner than the header width (e.g. from defaultHeader.getWidth()),
            //   the content anchoring may shift off a few pixels to the left and right and because of this
            //   scaleToContent() WILL no longer make the contentAnchor obsolete

            content.anchoredAt(cx, cy, contentAnchor, movementSpeed);
        }

        return this;
    }


    // --------------------------------------------------------------------------------

    @Override
    protected void cancelMovementQueueForAllChildren(boolean shouldTryAndResolveOneLastTime) {
        Stream.of(npFullBase, npFullShadow, npFullTrim, npFullTrimHighlight)
                .forEach(elt -> elt.cancelMovementQueue(shouldTryAndResolveOneLastTime));
        if (hasHeader) {
            npHeaderBase.cancelMovementQueue(shouldTryAndResolveOneLastTime);
            npHeaderTrim.cancelMovementQueue(shouldTryAndResolveOneLastTime);

            if (hasCustomHeader)
                customHeader.cancelMovementQueue(shouldTryAndResolveOneLastTime);
            else
                defaultHeader.cancelMovementQueue(shouldTryAndResolveOneLastTime);
        }

        if (content != null)
            content.cancelMovementQueue(shouldTryAndResolveOneLastTime);
    }

    @Override
    protected void setChildrenDelayedMovement(float deltaX, float deltaY, InterpolationSpeed movementSpeed, long startingTimeMillis) {
        Stream.of(npFullBase, npFullShadow, npFullTrim, npFullTrimHighlight)
                .forEach(elt -> elt.setAllDelayedMovement(deltaX, deltaY, movementSpeed, startingTimeMillis));

        if (hasHeader) {
            npHeaderBase.setAllDelayedMovement(deltaX, deltaY, movementSpeed, startingTimeMillis);
            npHeaderTrim.setAllDelayedMovement(deltaX, deltaY, movementSpeed, startingTimeMillis);

            if (hasCustomHeader)
                customHeader.setAllDelayedMovement(deltaX, deltaY, movementSpeed, startingTimeMillis);
            else
                defaultHeader.setAllDelayedMovement(deltaX, deltaY, movementSpeed, startingTimeMillis);
        }

        if (content != null)
            content.setAllDelayedMovement(deltaX, deltaY, movementSpeed, startingTimeMillis);
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
        if (renderFullShadows)
            npFullShadow.render(sb);

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
        npFullTrimHighlight.render(sb);
    }
}
