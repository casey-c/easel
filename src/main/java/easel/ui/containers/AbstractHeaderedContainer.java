package easel.ui.containers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;
import easel.ui.graphics.ninepatch.headered.HeaderedNinePatch;
import easel.ui.graphics.ninepatch.headered.NoHeaderedNinePatch;
import easel.ui.layouts.VerticalLayout;
import easel.ui.text.Label;
import easel.utils.colors.EaselColors;

public abstract class AbstractHeaderedContainer<T extends AbstractHeaderedContainer<T, H>, H extends HeaderedNinePatch<H>> extends AbstractWidget<T> {
    protected float totalWidth;
    protected float totalHeight;

    //protected AnchorPosition headerHorizontalAlignment = AnchorPosition.LEFT_CENTER;
    protected AnchorPosition headerAnchor = AnchorPosition.CENTER;
    protected ContainerHeaderType headerType = ContainerHeaderType.NONE;

    protected Color headerColor = EaselColors.HEADER_SLATE();
    protected AbstractWidget customHeaderWidget;

    protected NoHeaderedNinePatch backgroundWithoutHeader;
    protected H backgroundWithHeader;

//    protected float mainContentPaddingLeft;
//    protected float mainContentPaddingRight;
//    protected float mainContentPaddingBottom;
//    protected float mainContentPaddingTop;

    protected AbstractWidget mainContentWidget;
    protected AnchorPosition contentAnchor = AnchorPosition.CENTER;

    protected VerticalLayout titleSubtitleLayout;

    // For left / right aligned headers, allows the titleSubtitleLayout to not touch the sides
    private static final float HEADER_HORIZONTAL_PADDING = 20;

    // --------------------------------------------------------------------------------

    public AbstractHeaderedContainer(float width, float height) {
        this.totalWidth = width;
        this.totalHeight = height;

        titleSubtitleLayout = new VerticalLayout(totalWidth, 20)
                .withMargins(20, 10)
                .withDefaultChildAnchorPosition(AnchorPosition.LEFT_CENTER);

        buildBackgroundWithoutHeader();
    }

    // --------------------------------------------------------------------------------

    protected abstract void buildBackgroundWithHeader();

    protected void buildBackgroundWithoutHeader() {
        this.backgroundWithoutHeader = new NoHeaderedNinePatch(totalWidth, totalHeight);
    }

    protected float getHeaderHeight() {
        if (hasHeader())
            return backgroundWithHeader.getHeaderHeight();
        else
            return backgroundWithoutHeader.getHeaderHeight();
    }

    // --------------------------------------------------------------------------------

    protected boolean hasHeader() {
        return headerType.hasHeader() && backgroundWithHeader != null;
    }


    public T withHeader(String title) {
        this.headerType = ContainerHeaderType.TITLE;
        buildBackgroundWithHeader();

        titleSubtitleLayout.clear();
        titleSubtitleLayout.withChild(new Label(title)).resizeWidthToWidestChild();

        return (T)this;
    }

    public T withHeader(String title, String subtitle) {
        this.headerType = ContainerHeaderType.TITLE_SUBTITLE;
        buildBackgroundWithHeader();

        titleSubtitleLayout.clear();

        titleSubtitleLayout
                .withChild(new Label(title))
                .withChild(new Label(subtitle))
                .resizeWidthToWidestChild();

        return (T)this;
    }

    public T withHeader(AbstractWidget customHeaderWidget) {
        this.headerType = ContainerHeaderType.CUSTOM;
        buildBackgroundWithHeader();

        this.customHeaderWidget = customHeaderWidget;

        return (T)this;
    }

    public T withHeaderColor(Color headerColor) {
        // Lets us cache the header color if the user calls withHeaderColor before a withHeader() build
        this.headerColor = headerColor;

        // If it already exists (e.g. they called withHeader() first), update the header layer's color
        if (hasHeader()) {
            backgroundWithHeader.withHeaderColor(headerColor);
        }

        return (T)this;
    }

//    public T withHeaderHorizontalAlignment(AnchorPosition anchorPosition) {
//        this.headerHorizontalAlignment = anchorPosition;
//        titleSubtitleLayout.withDefaultChildAnchorPosition(anchorPosition);
//        return (T)this;
//    }

    public T withHeaderAlignment(AnchorPosition headerAnchor) {
        this.headerAnchor = headerAnchor;
        titleSubtitleLayout.withDefaultChildAnchorPosition(headerAnchor);
        return (T) this;
    }


    // --------------------------------------------------------------------------------

//    /**
//     * Set the padding around all sides of the main content widget. This is a convenience function to call {@link #withMainContentPadding(float, float, float, float)} with all padding being the same amount.
//     * @param all the width (or height) of the amount of padding between the main content and the sides of the overall container, in pixels
//     * @return this widget
//     * @see #withMainContentPadding(float, float, float, float)
//     */
//    public T withMainContentPadding(float all) {
//        return withMainContentPadding(all, all, all, all);
//    }
//
//    /**
//     * Set the horizontal padding on the left and right sides of the main content widget. This is a convenience function to call {@link #withMainContentPadding(float, float, float, float)} with the given value for the left and right and reusing the existing bottom and top padding.
//     * @param horizontalPadding the width of the spacing allotted between the main content's left hand side and the left of the container, and similarly between its right hand side and the right side of the overall container, in pixels
//     * @return this widget
//     * @see #withMainContentPadding(float, float, float, float)
//     */
//    public T withMainContentHorizontalPadding(float horizontalPadding) {
//        return withMainContentPadding(horizontalPadding, horizontalPadding, mainContentPaddingBottom, mainContentPaddingTop);
//    }
//
//    /**
//     * Set the vertical padding on the bottom and top sides of the main content widget. This is a convenience function to call {@link #withMainContentPadding(float, float, float, float)} with the given value for the bottom and top and reusing the existing left and right padding.
//     * @param verticalPadding the height of the spacing allotted between the main content's bottom side and the bottom of the container, and similarly between its top side and the bottom of the header, in pixels
//     * @return this widget
//     * @see #withMainContentPadding(float, float, float, float)
//     */
//    public T withMainContentVerticalPadding(float verticalPadding) {
//        return withMainContentPadding(mainContentPaddingLeft, mainContentPaddingRight, verticalPadding, verticalPadding);
//    }
//
//    /**
//     * Set the padding around each side of the main content widget. Note that the main content widget is what is in the primary area of the container, and it gets set by {@link #withContent(AbstractWidget)}. With this function, you can easily set the amount of space between the left edge of the total widget and the left edge of the main content widget, and the spacing between the top of the main content widget and the bottom of the header, etc.
//     * @param left how much space between the left of the main content and the left side of the overall container
//     * @param right how much space between the right of the main content and the right side of the overall container
//     * @param bottom how much space between the bottom of the main content and the bottom of the overall container
//     * @param top how much space between the top of the main content and the bottom of the header
//     * @return this widget
//     * @see #withMainContentPadding(float)
//     * @see #withMainContentHorizontalPadding(float)
//     * @see #withMainContentVerticalPadding(float)
//     */
//    public T withMainContentPadding(float left, float right, float bottom, float top) {
//        this.mainContentPaddingLeft = left;
//        this.mainContentPaddingRight = right;
//        this.mainContentPaddingBottom = bottom;
//        this.mainContentPaddingTop = top;
//        return (T)this;
//    }

    // --------------------------------------------------------------------------------

    /**
     * Set the main content of this overall container. This main widget serves as the primary thing that this container renders/updates/etc., and sits throughout the entire area under the header (if it exists). You typically want the main widget to be some sort of layout widget (e.g. {@link easel.ui.layouts.GridLayout}) to easily manage a hierarchy of widgets.
     * @param mainWidget
     * @return
     */
    public T withContent(AbstractWidget mainWidget) {
        this.mainContentWidget = mainWidget;
        return (T)this;
    }

    /**
     * <p>
     * The positioning of the content widget inside the main area. Useful for aligning the main content (see {@link #withContent(AbstractWidget)}) if the space it is given exceeds the space required by the widget itself. I.e., if you construct this container with a large preferred width and height (more than what's needed to render the header and content), this positioning lets you align the internal content widget effectively. This anchor will be ignored if you use {@link #scaleToContent()}, as there will no longer be room for the main content to "float" around inside the overall container. If not set, the content anchor defaults to {@link AnchorPosition#CENTER}.
     * </p>
     * <p>
     * Note that you almost ALWAYS want to add margins to your main content widget in order for it to not touch the sides of the full container (e.g. on your main content widget, you'll want something like <code>mainContentWidget.withMargins(20)</code>, using {@link AbstractWidget#withMargins(float)}, before attaching it to this container using {@link #withContent(AbstractWidget)}). Keep in mind that using asymmetrical margins may negatively affect the anchoring set by this function.
     * </p>
     * @param contentAnchor where to attach the main content widget when the main content area is larger than the widget itself
     * @return this widget
     */
    public T withContentAnchor(AnchorPosition contentAnchor) {
        this.contentAnchor = contentAnchor;
        return (T)this;
    }

    public T scaleToContent() {
        // TODO
        return (T)this;
    }

    public T scaleToContentWidth() {
        // TODO
        return (T)this;
    }

    public T scaleToContentHeight() {
        // TODO
        return (T)this;
    }

    // --------------------------------------------------------------------------------

    // --------------------------------------------------------------------------------

//    /**
//     * The width of the main content area, after subtracting out the left and right main horizontal padding.
//     * @return the width of the main content area
//     */
//    public float getMainAreaWidth() {
//        return getContentWidth() - (mainContentPaddingLeft + mainContentPaddingRight);
//    }
//
    /**
     * The height of the main content area beneath the header. If no header exists, this just devolves to {@link #getContentHeight()}.
     * @return the height of the main content area
     */
    public float getContentAreaHeight() {
        return getContentHeight() - getHeaderHeight();
    }

//    /**
//     * The left-most point of the main content area. Depends on {@link #getContentLeft()} and the main left horizontal padding, so should only be considered accurate after an {@link #anchoredAt(float, float, AnchorPosition)} has taken place.
//     * @return the left-most point of the main content area
//     */
//    public float getMainAreaLeft() {
//        return getContentLeft() + mainContentPaddingLeft;
//    }
//
//    /**
//     * The right-most point of the main content area. Depends on {@link #getContentRight()} and the main right horizontal padding, so should only be considered accurate after an {@link #anchoredAt(float, float, AnchorPosition)} has taken place.
//     * @return the right-most point of the main content area
//     */
//    public float getMainAreaRight() {
//        return getContentRight() - mainContentPaddingRight;
//    }
//
//    /**
//     * The bottom-most point of the main content area. Depends on {@link #getContentBottom()} and the main bottom vertical padding, so should only be considered accurate after an {@link #anchoredAt(float, float, AnchorPosition)} has taken place.
//     * @return the bottom-most point of the main content area
//     */
//    public float getMainAreaBottom() {
//        return getContentBottom() + mainContentPaddingBottom;
//    }
//
    /**
     * The top-most point of the main content area. Depends on {@link #getContentBottom()} and {@link #getContentAreaHeight()}, so should only be considered accurate after an {@link #anchoredAt(float, float, AnchorPosition)} has taken place.
     * @return the top-most point of the main content area
     */
    public float getContentAreaTop() {
        return getContentBottom() + getContentAreaHeight();
    }

    // --------------------------------------------------------------------------------

    @Override public float getContentWidth() { return totalWidth; }
    @Override public float getContentHeight() { return totalHeight; }

    // --------------------------------------------------------------------------------

    @Override
    public T anchoredAt(float x, float y, AnchorPosition anchorPosition, InterpolationSpeed withDelay) {
        super.anchoredAt(x, y, anchorPosition, withDelay);

        // Anchor the background
        if (hasHeader())
            backgroundWithHeader.anchoredAt(x, y, anchorPosition, withDelay);
        else
            backgroundWithoutHeader.anchoredAt(x, y, anchorPosition, withDelay);

        // Anchor everything inside the header (title/subtitle or custom widget)
        if (headerType.hasHeader()) {
            final float hX = headerAnchor.getXFromLeft(getContentLeft(), getContentWidth());
            final float hY = headerAnchor.getYFromTop(getContentTop(), getHeaderHeight());

            if (headerType == ContainerHeaderType.TITLE || headerType == ContainerHeaderType.TITLE_SUBTITLE) {
                titleSubtitleLayout.anchoredAt(hX, hY, headerAnchor, withDelay);
            }
            else if (headerType == ContainerHeaderType.CUSTOM) {
                customHeaderWidget.anchoredAt(hX, hY, headerAnchor, withDelay);
            }
        }

        // Anchor content
        if (mainContentWidget != null) {
            final float cX = contentAnchor.getXFromLeft(getContentLeft(), getContentWidth());
            final float cY = contentAnchor.getYFromTop(getContentAreaTop(), getContentAreaHeight());

            mainContentWidget.anchoredAt(cX, cY, contentAnchor, withDelay);
        }

        return (T) this;
    }


    // --------------------------------------------------------------------------------

    @Override
    protected void updateWidget() {
        super.updateWidget();

        if (mainContentWidget != null)
            mainContentWidget.update();
    }


    // --------------------------------------------------------------------------------

    protected void renderBackground(SpriteBatch sb) {
        if (hasHeader())
            backgroundWithHeader.render(sb);
        else
            backgroundWithoutHeader.render(sb);
    }

    protected void renderHeader(SpriteBatch sb) {
        switch (headerType) {
            case NONE:
                break;
            case TITLE:
            case TITLE_SUBTITLE:
                titleSubtitleLayout.render(sb);
                break;
            case CUSTOM:
                customHeaderWidget.render(sb);
                break;
        }
    }

    protected void renderContent(SpriteBatch sb) {
        if (mainContentWidget != null)
            mainContentWidget.render(sb);
    }

    @Override
    protected void renderWidget(SpriteBatch sb) {
        renderBackground(sb);
        renderHeader(sb);
        renderContent(sb);
    }

    // --------------------------------------------------------------------------------
}
