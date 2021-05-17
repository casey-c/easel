package easel.ui.layouts;

import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;

/**
 * Layout widgets vertically from top to bottom. Use the {@link #withChild(AbstractWidget)} family of methods to manage new widgets. The following example code constructs a new layout with a desired width of 100px and three widgets spaced 20px apart vertically. From top to bottom: widget1, 20px spacing, widget2, 20px spacing, and widget3. The default child anchor is set to center, so all children added after this line will be centered horizontally inside the 100px width, except for widget3 which specifies that it overrides this anchor and will be aligned to the right most side.
 * <pre>
 * {@code
 * VerticalLayout vlayout = new VerticalLayout(100.0f, 20.0f)
 *     .withDefaultChildAnchor(AnchorPosition.CENTER)
 *     .withChild(widget1)
 *     .withChild(widget2)
 *     .withChild(widget3, AnchorPosition.RIGHT_CENTER)
 *     .anchoredAt(x, y, AnchorPosition.LEFT_TOP);
 * }
 * </pre>
 * NOTE: child widgets are only given enough room as their preferred heights allow but can move around horizontally within the preferred width; because of this, the child anchor positions only consider the horizontal position of the anchor and the vertical information is ignored.
 * @see HorizontalLayout
 */
public class VerticalLayout extends AbstractOneDimensionalLayout<VerticalLayout> {
    /**
     * Constructs a new vertical layout with a total desired width and the vertical spacing between each widget.
     * @param desiredWidth how much space the
     * @param spacing vertical space in between widgets (in pixels)
     */
    public VerticalLayout(float desiredWidth, float spacing) {
        super(spacing);
        this.totalWidth = desiredWidth;
    }

    @Override public float getContentWidth() { return totalWidth; }
    @Override public float getContentHeight() { return totalHeight - spacing; }

    @Override
    protected void updateSize(AbstractWidget newChild) {
        this.totalHeight += (spacing + newChild.getHeight());
    }

    @Override
    protected void anchorChildren(InterpolationSpeed withDelay) {
        float left = getContentLeft();
        float currY = getContentTop();

        for (LayoutItem child : children) {
            AbstractWidget widget = child.widget;
            AnchorPosition anchor = child.anchor;

            float widgetHeight = widget.getHeight();

            float x = anchor.getXFromLeft(left, totalWidth);
            float y = anchor.getYFromTop(currY, widgetHeight);

            widget.anchoredAt(x, y, anchor, withDelay);

            currY -= (widgetHeight + spacing);
        }
    }

    /**
     * Replaces the <code>desiredWidth</code> set in the constructor ({@link #VerticalLayout(float, float)}) with the width of the widest child. Useful for dynamically scaling the width of this widget to fit the widths of its children. NOTE: should call this function AFTER adding all children and before anchoring.
     * @return this widget
     */
    public VerticalLayout resizeWidthToWidestChild() {
        this.totalWidth = children.stream()
                .map(item -> item.widget.getWidth())
                .max(Float::compareTo)
                .orElse(0.0f);

        return this;
    }
}
