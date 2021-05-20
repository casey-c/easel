package easel.ui.layouts;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;

import java.util.ArrayList;

abstract class AbstractOneDimensionalLayout<T extends AbstractOneDimensionalLayout<T>> extends AbstractWidget<T> {
    protected ArrayList<LayoutItem> children = new ArrayList<>();
    protected AnchorPosition defaultChildAnchorPosition = AnchorPosition.LEFT_TOP;

    protected float totalWidth, totalHeight;
    protected float spacing;

    public AbstractOneDimensionalLayout(float spacing) {
        this.spacing = spacing;
    }

    // --------------------------------------------------------------------------------

    /**
     * Sets the AnchorPosition for all future children added to this widget. If this is not set or children are added before this method is called, it defaults to a value of <code>AnchorPosition.LEFT_TOP</code>. Only referred to when using the add child methods that do not specify a specific anchor position (if specified, this default child anchor position is ignored).
     * NOTE: For horizontal layouts, only the vertical position in the anchor matters. This is because children are only allowed to grow horizontally up to their preferred width. However, the total height of the layout is arbitrary and that means that the vertical anchoring can affect the vertical positioning. A similar restriction exists for vertical layouts but for left/right anchors.
     * @param defaultChildAnchorPosition the internal anchor position of each future child
     * @return this layout
     */
    public T withDefaultChildAnchorPosition(AnchorPosition defaultChildAnchorPosition) {
        this.defaultChildAnchorPosition = defaultChildAnchorPosition;
        return (T)this;
    }

    /**
     * Stop managing all widgets previously managed by this layout.
     */
    public void clear() {
        children.clear();
    }

    // --------------------------------------------------------------------------------

    protected abstract void updateSize(AbstractWidget newChild);
    protected abstract void anchorChildren(InterpolationSpeed withDelay);

    // --------------------------------------------------------------------------------

    /**
     * Let this layout manage the given child using the provided internal anchor. The child is added to the end of the layout (right-most for a horizontal layout, bottom-most for a vertical layout).
     * @param child the child widget to be added
     * @param anchor where the child will be anchored internally to the space given (see {@link #withDefaultChildAnchorPosition(AnchorPosition)} for details about the limitations of the internal anchor)
     * @return this layout
     * @see #withChild(AbstractWidget)
     */
    public final T withChild(AbstractWidget child, AnchorPosition anchor) {
        children.add(new LayoutItem(child, anchor));
        updateSize(child);
        return (T)this;
    }

    /**
     * Let this layout manage the given child using the <code>defaultChildAnchorPosition</code> as its internal anchor. This child is added to the end of the layout (right-most for a horizontal layout, bottom-most for a vertical layout). If the <code>defaultChildAnchorPosition</code> hasn't been set previously (i.e. through {@link #withDefaultChildAnchorPosition(AnchorPosition)}), the default value is <code>AnchorPosition.LEFT_TOP</code>.
     * @param child the child widget to be added
     * @return this layout
     * @see #withChild(AbstractWidget, AnchorPosition)
     */
    public final T withChild(AbstractWidget child) {
        return withChild(child, defaultChildAnchorPosition);
    }

    // --------------------------------------------------------------------------------

    @Override
    public T anchoredAt(float x, float y, AnchorPosition anchorPosition, InterpolationSpeed withDelay) {
        super.anchoredAt(x, y, anchorPosition, withDelay);
        anchorChildren(withDelay);
        return (T)this;
    }


    // --------------------------------------------------------------------------------

    @Override protected void renderWidget(SpriteBatch sb) { children.forEach(w -> w.widget.render(sb)); }
    @Override public void renderTopLevel(SpriteBatch sb) { children.forEach(w -> w.widget.renderTopLevel(sb)); }

    @Override public void hide() { children.forEach(w -> w.widget.hide()); }
    @Override public void show() { children.forEach(w -> w.widget.show()); }
}