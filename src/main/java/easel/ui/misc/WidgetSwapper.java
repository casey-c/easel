package easel.ui.misc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import easel.Easel;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;

public class WidgetSwapper<T extends Enum<T>> extends AbstractWidget<WidgetSwapper<T>> {
    private AbstractWidget[] widgets;
    private AbstractWidget activeWidget = null;

    private boolean isShowing = false;

    private float maxWidth = 0.0f;
    private float maxHeight = 0.0f;

    public WidgetSwapper(Class<T> clz) {
        int numItems = clz.getEnumConstants().length;
        widgets = new AbstractWidget[numItems];
    }

    public WidgetSwapper<T> withWidget(T option, AbstractWidget widget) {
        return withWidget(option, widget, false);
    }

    public WidgetSwapper<T> withWidget(T option, AbstractWidget widget, boolean activeView) {
        widgets[option.ordinal()] = widget;

        if (widget.getWidth() > maxWidth)
            maxWidth = widget.getWidth();
        if (widget.getHeight() > maxHeight)
            maxHeight = widget.getHeight();

        if (activeView)
            activeWidget = widget;

        return this;
    }

    // --------------------------------------------------------------------------------

    public void setView(T choice) {
        AbstractWidget target = widgets[choice.ordinal()];

        // Hide existing widget if we had been showing it
        if (isShowing && target != activeWidget && activeWidget != null)
            activeWidget.hide();

        activeWidget = target;

        // Show new widget if we should be showing it
        if (isShowing && activeWidget != null)
            activeWidget.show();
    }

    public WidgetSwapper<T> withView(T choice) {
        setView(choice);
        return this;
    }

    // --------------------------------------------------------------------------------

    @Override
    public WidgetSwapper<T> anchoredAt(float x, float y, AnchorPosition anchorPosition, InterpolationSpeed withDelay) {
        for (AbstractWidget w : widgets) {
            if (w == null) {
                Easel.logger.warn("Trying to anchor a null widget?");
            }
            else {
                w.anchoredAt(x, y, anchorPosition, withDelay);
            }
        }

        return super.anchoredAt(x, y, anchorPosition, withDelay);
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
