package easel.ui.layouts;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;

import java.util.ArrayList;

public class VerticalLayout extends AbstractWidget<VerticalLayout> {
    private ArrayList<LayoutItem> children = new ArrayList<>();
    private AnchorPosition defaultChildAnchor = AnchorPosition.LEFT_TOP;

    private float desiredWidth;
    private float totalHeight;

    private float spacing = 0.0f;

    public VerticalLayout(float desiredWidth, float spacing) {
        this.desiredWidth = desiredWidth;
        this.spacing = spacing;
    }

    // --------------------------------------------------------------------------------

    public VerticalLayout withDefaultChildAnchorPosition(AnchorPosition anchorPosition) {
        this.defaultChildAnchor = anchorPosition;
        return this;
    }

    public void clear() {
        children.clear();
    }

    public void addChild(AbstractWidget widget, AnchorPosition anchor) {
        children.add(new LayoutItem(widget, anchor));
        this.totalHeight += (spacing + widget.getHeight());
    }

    public VerticalLayout withChild(AbstractWidget widget, AnchorPosition anchor) {
        addChild(widget, anchor);
        return this;
    }

    public void addChild(AbstractWidget widget) {
        addChild(widget, defaultChildAnchor);
    }

    public VerticalLayout withChild(AbstractWidget widget) {
        addChild(widget, defaultChildAnchor);
        return this;
    }

    // --------------------------------------------------------------------------------

    private void anchorChildren(InterpolationSpeed withDelay) {
        float left = getContentLeft();
        float currY = getContentTop();

        for (LayoutItem child : children) {
            AbstractWidget widget = child.widget;
            AnchorPosition anchor = child.anchor;

            float widgetHeight = widget.getHeight();

            float x = anchor.getXFromLeft(left, desiredWidth);
            float y = anchor.getYFromTop(currY, widgetHeight);

            widget.anchoredAt(x, y, anchor, withDelay);

            currY -= (widgetHeight + spacing);
        }
    }

    @Override
    public VerticalLayout anchoredAt(float x, float y, AnchorPosition anchorPosition, InterpolationSpeed withDelay) {
        super.anchoredAt(x, y, anchorPosition, withDelay);
        anchorChildren(withDelay);
        return this;
    }


    // --------------------------------------------------------------------------------

    @Override public float getContentWidth() { return desiredWidth; }
    @Override public float getContentHeight() { return totalHeight - spacing; }

    @Override protected void renderWidget(SpriteBatch sb) { children.forEach(w -> w.widget.render(sb)); }
    @Override public void hide() { children.forEach(w -> w.widget.hide()); }
    @Override public void show() { children.forEach(w -> w.widget.show()); }
}
