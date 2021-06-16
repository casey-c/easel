package easel.ui.layouts;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;

import java.util.ArrayList;
import java.util.stream.Stream;

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
     * Forcibly sets the anchor position of all current children to the given value. This is useful if you change desired anchors later and have previously added children with different anchors. This function does not update the <code>defaultChildAnchorPosition</code>, so any future children added will continue to use that value if not manually set.
     * @param forcedChildAnchorPosition the new anchor for all existing children
     * @return this layout
     */
    public T forceChildAnchors(AnchorPosition forcedChildAnchorPosition) {
        for (LayoutItem child : children)
            child.anchor = forcedChildAnchorPosition;
        return (T) this;
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
    public T anchoredAt(float x, float y, AnchorPosition anchorPosition, InterpolationSpeed movementSpeed) {
        super.anchoredAt(x, y, anchorPosition, movementSpeed);
        anchorChildren(movementSpeed);
        return (T)this;
    }

    @Override
    protected void setChildrenDelayedMovement(float deltaX, float deltaY, InterpolationSpeed movementSpeed, long startingTimeMillis) {
        iterator().forEach(child -> child.setAllDelayedMovement(deltaX, deltaY, movementSpeed, startingTimeMillis));
    }

    @Override
    protected void cancelMovementQueueForAllChildren(boolean shouldTryAndResolveOneLastTime) {
        iterator().forEach(child -> child.cancelMovementQueue(shouldTryAndResolveOneLastTime));
    }

    /**
     * @return a stream of all children currently handled by this widget, from top to bottom (or left to right)
     */
    public Stream<AbstractWidget> iterator() {
        return this.children.stream().map(item -> item.widget);
    }

    /**
     * A stream of children handled by this widget who are of the specific type, for convenience purposes. Follows the same top to bottom / left to right ordering of the regular iterator. If you know you've built the layout with all objects of a particular type, you can quickly recover them all into a stream that remembers the type. This has a slight performance penalty over {@link #iterator()} due to checking each child against the type for safe casts, but the resulting stream will be properly typed to make it easier to work with. Children managed by this layout who are not of the given type will not be included in the stream, so be wary if using this variant of the iterator.
     * @param clz the class for the type
     * @param <T> the type of child to extract
     * @return a stream of children who fit the given type
     * @see #iterator()
     */
    public <T> Stream<T> iteratorOfType(Class<T> clz) {
        return iterator().filter(clz::isInstance).map(c -> (T)c);
    }

    // --------------------------------------------------------------------------------

    @Override protected void renderWidget(SpriteBatch sb) { children.forEach(w -> w.widget.render(sb)); }
    @Override public void renderTopLevel(SpriteBatch sb) { children.forEach(w -> w.widget.renderTopLevel(sb)); }

    @Override public void hide() { children.forEach(w -> w.widget.hide()); }
    @Override public void show() { children.forEach(w -> w.widget.show()); }
}