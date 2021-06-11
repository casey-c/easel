package easel.ui.containers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import easel.Easel;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;

// TODO: make sure withView has been called at least once before using, and that withWidget has been made for all views
//   or at least that it doesn't crash if you omit these two critical things!
public class SwapContainer<T extends Enum<T>> extends AbstractWidget<SwapContainer<T>> {
    private AbstractWidget[] widgets;
    private AnchorPosition[] internalAnchors;

    private AnchorPosition defaultChildAnchor = AnchorPosition.CENTER;

    private Class<T> clz;
    private T currentView;
    private AbstractWidget activeWidget = null;

    private boolean isShowing = false;

    private float maxWidth = 0.0f;
    private float maxHeight = 0.0f;

    public SwapContainer(Class<T> clz) {
        int numItems = clz.getEnumConstants().length;
        widgets = new AbstractWidget[numItems];
        internalAnchors = new AnchorPosition[numItems];

        for (int i = 0; i < numItems; ++i) {
            internalAnchors[i] = defaultChildAnchor;
            // TODO make the widgets have placeholder items here as well? - so no nulls?
        }

        this.clz = clz;
    }

    public SwapContainer<T> withWidget(T option, AbstractWidget widget) {
        return withWidget(option, widget, false, defaultChildAnchor);
    }

    public SwapContainer<T> withWidget(T option, AbstractWidget widget, boolean activeView) {
        return withWidget(option, widget, activeView, defaultChildAnchor);
    }

    public SwapContainer<T> withWidget(T view, AbstractWidget widget, boolean activeView, AnchorPosition childAnchor) {
        this.widgets[view.ordinal()] = widget;
        this.internalAnchors[view.ordinal()] = childAnchor;

        if (widget.getWidth() > maxWidth)
            this.maxWidth = widget.getWidth();
        if (widget.getHeight() > maxHeight)
            this.maxHeight = widget.getHeight();

        if (activeView) {
            this.activeWidget = widget;
            this.currentView = view;
        }

        scaleHitboxToContent();

        return this;
    }

    public SwapContainer<T> withDefaultChildAnchor(AnchorPosition defaultChildAnchor) {
        this.defaultChildAnchor = defaultChildAnchor;
        return this;
    }

    public SwapContainer<T> forceChildAnchors(AnchorPosition forcedChildAnchorPosition) {
        for (int i = 0; i < widgets.length; ++i)
            internalAnchors[i] = forcedChildAnchorPosition;
        return this;
    }

    public SwapContainer<T> updateAnchorAt(T view, AnchorPosition newAnchor) {
        internalAnchors[view.ordinal()] = newAnchor;
        return this;
    }

    // --------------------------------------------------------------------------------

    public SwapContainer<T> withView(T choice) {
        AbstractWidget target = widgets[choice.ordinal()];

        // TODO: target may be null here!

        // Hide existing widget if we had been showing it
        if (isShowing && target != activeWidget && activeWidget != null)
            activeWidget.hide();

        this.activeWidget = target;
        this.currentView = choice;

        // Show new widget if we should be showing it
        if (isShowing && activeWidget != null)
            activeWidget.show();

        return this;
    }

    public SwapContainer<T> nextView() {
        int next = (currentView.ordinal() + 1) % widgets.length;
        return withView(clz.getEnumConstants()[next]);
    }

    // --------------------------------------------------------------------------------

    @Override
    public SwapContainer<T> anchoredAt(float x, float y, AnchorPosition anchorPosition, InterpolationSpeed movementSpeed) {
        super.anchoredAt(x, y, anchorPosition, movementSpeed);

        for (int i = 0; i < widgets.length; ++i) {
            AbstractWidget w = widgets[i];

            if (w == null) {
                Easel.logger.warn("Trying to anchor a null widget?");
            }
            else {
                AnchorPosition anchor = internalAnchors[i];

                float wx = anchor.getXFromLeft(getContentLeft(), getContentWidth());
                float wy = anchor.getYFromBottom(getContentBottom(), getContentHeight());

                w.anchoredAt(wx, wy, anchor, movementSpeed);
            }
        }

        return this;
    }

    // --------------------------------------------------------------------------------

    @Override public float getContentWidth() { return maxWidth; }
    @Override public float getContentHeight() { return maxHeight; }

    @Override
    protected void renderWidget(SpriteBatch sb) {
        if (activeWidget != null)
            activeWidget.render(sb);
    }

    @Override
    protected void updateWidget() {
        if (activeWidget != null)
            activeWidget.update();
    }

    @Override
    public void show() {
        if (activeWidget != null)
            activeWidget.show();

        isShowing = true;
    }

    @Override
    public void hide() {
        if (activeWidget != null)
            activeWidget.hide();

        isShowing = false;
    }
}
