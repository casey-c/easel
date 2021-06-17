package easel.ui.layouts;

import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;

import java.util.stream.Stream;

/**
 * Layout widgets horizontally from left to right. Use the {@link #withChild(AbstractWidget)} family of methods to manage new widgets. The following example code constructs a new layout with a desired height of 100px and three widgets spaced 20px apart horizontally. From left to right: widget1, 20px spacing, widget2, 20px spacing, and widget3. The default child anchor is set to center, so all children added after this line will be centered vertically inside the 100px height, except for widget3 which specifies that it overrides this anchor and will be aligned to the top.
 * <pre>
 * {@code
 * HorizontalLayout layout = new HorizontalLayout(100.0f, 20.0f)
 *     .withDefaultChildAnchor(AnchorPosition.CENTER)
 *     .withChild(widget1)
 *     .withChild(widget2)
 *     .withChild(widget3, AnchorPosition.CENTER_TOP)
 *     .anchoredAt(x, y, AnchorPosition.LEFT_TOP);
 * }
 * </pre>
 * NOTE: child widgets are only given enough room as their preferred widths allow but can move around vertically within the preferred height; because of this, the child anchor positions only consider the vertical position of the anchor and the horizontal information is ignored.
 * @see VerticalLayout
 */
public class HorizontalLayout extends AbstractOneDimensionalLayout<HorizontalLayout> {

    /**
     * Constructs a new horizontal layout with the given row height and horizontal spacing. Child widgets of height less than this <code>desiredHeight</code> will be able to float around vertically in their assigned slot using their anchor position. You can use the other constructor ({@link #HorizontalLayout(float)}) if you want to automatically scale the heights to the tallest child, but using this version means the height allowed for children will not be changed unless you manually alter it using {@link #scaleToTallestChild()}.
     * @param desiredHeight the height of the horizontal layout (for child anchoring purposes)
     * @param spacing the horizontal gap in between elements of the layout
     * @see #HorizontalLayout(float)
     */
    public HorizontalLayout(float desiredHeight, float spacing) {
        super(spacing);
        this.totalHeight = desiredHeight;
    }

    /**
     * Constructs a new horizontal layout with the given horizontal spacing. This constructor guarantees that {@link #scaleToTallestChild()} will be called the FIRST time that any {@link #anchoredAt(float, float, AnchorPosition)} anchoring occurs (only the first time). This is essentially a convenience method for the usual pattern where the row height set by the other constructor is ignored with a manual {@link #scaleToTallestChild()} before anchoring. This variant just does it automatically. To ensure performance, this auto-scaling only occurs the first time an anchoring occurs (which, presumably is after all children have been added), and must be done manually later if things change beyond that.
     * @param spacing the horizontal gap in between elements of the layout
     * @see #HorizontalLayout(float, float)
     */
    public HorizontalLayout(float spacing) {
        super(spacing);
        this.shouldAutoScaleToContent = true;
    }

    @Override public float getContentWidth() { return totalWidth - spacing; }
    @Override public float getContentHeight() { return totalHeight; }

    @Override
    protected void updateSize(AbstractWidget newChild) {
        this.totalWidth += (spacing + newChild.getWidth());
    }

    @Override protected void autoscale() { scaleToTallestChild(); }

    @Override
    protected void anchorChildren(InterpolationSpeed withDelay) {
        float top = getContentTop();
        float currX = getContentLeft();

        for (LayoutItem child : children) {
            AbstractWidget widget = child.widget;
            AnchorPosition anchor = child.anchor;

            float widgetWidth = widget.getWidth();

            float x = anchor.getXFromLeft(currX, widgetWidth);
            float y = anchor.getYFromTop(top, totalHeight);

            widget.anchoredAt(x, y, anchor, withDelay);

            currX += (widgetWidth + spacing);
        }
    }

    @Override
    public void clear() {
        super.clear();
        this.totalWidth = 0;
    }

    /**
     * Replaces the <code>desiredHeight</code> set in the constructor ({@link #HorizontalLayout(float, float)}) with the height of the tallest child. Useful for dynamically scaling the height of this widget to fit the heights of its children. NOTE: should call this function AFTER adding all children and before anchoring.
     * @return this widget
     */
    public HorizontalLayout scaleToTallestChild() {
        this.totalHeight = children.stream()
                .map(item -> item.widget.getHeight())
                .max(Float::compareTo)
                .orElse(0.0f);

        return this;
    }
}
