package ojbui.ui.layouts;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ojbui.ui.AbstractWidget;
import ojbui.ui.AnchorPosition;

import java.util.*;
import java.util.stream.Stream;

public final class GridLayout<W extends AbstractWidget> extends AbstractWidget<GridLayout<W>> implements AbstractLayout {
    // --------------------------------------------------------------------------------
    // Simple struct for making widget location access convenient
    // --------------------------------------------------------------------------------
    private static final class GridLocation {
        int row;
        int col;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GridLocation that = (GridLocation) o;
            return row == that.row && col == that.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }

    private static final class GridItem<W> {
        W widget;
        GrowthPolicy growthPolicy;
        AnchorPosition anchor;

        public GridItem(W widget, GrowthPolicy policy, AnchorPosition anchor) {
            this.widget = widget;
            this.growthPolicy = policy;
            this.anchor = anchor;
        }
    }

    // --------------------------------------------------------------------------------

    private HashMap<GridLocation, GridItem<W>> children = new HashMap<>();
    private float totalWidth;
    private float totalHeight;

    private int numWidgetsHorizontal;
    private int numWidgetsVertical;

    @Override public float getContentWidth() { return totalWidth; }
    @Override public float getContentHeight() { return totalHeight; }


    // --------------------------------------------------------------------------------

    protected Stream<GridItem<W>> iteratorByRow(int row) {
        return children.entrySet()
                .stream()
                .filter(a -> a.getKey().row == row)
                .map(Map.Entry::getValue);
    }

    protected Stream<GridItem<W>> iteratorByCol(int col) {
        return children.entrySet()
                .stream()
                .filter(a -> a.getKey().col == col)
                .map(Map.Entry::getValue);
    }

    public float getMaxHeightInRow(int row) {
        return iteratorByRow(row)
                .map(item -> item.widget)
                .map(AbstractWidget::getContentHeight)
                .max(Float::compareTo)
                .orElse(0.0f);
    }

    public float getMaxWidthInCol(int col) {
        return iteratorByCol(col)
                .map(item -> item.widget)
                .map(AbstractWidget::getContentWidth)
                .max(Float::compareTo)
                .orElse(0.0f);
    }

    // --------------------------------------------------------------------------------

    public void addChild(W widget, int row, int col, GrowthPolicy growthPolicy) {

    }

    public GridLayout<W> withChild(W widget, int row, int col, GrowthPolicy growthPolicy) {
        addChild(widget, row, col, growthPolicy);
        return this;
    }

    // --------------------------------------------------------------------------------

    @Override
    public void clear() {
        children.clear();
    }

    @Override
    public void recomputeLayout() {

    }

    public GridLayout<W> withRecomputeLayout() {
        recomputeLayout();
        return this;
    }

    // --------------------------------------------------------------------------------

    @Override
    protected void renderWidget(SpriteBatch sb) {
        children.values()
                .stream()
                .map(a -> a.widget)
                .forEach(w -> w.render(sb));
    }

    @Override
    public void update() {
        children.values()
                .stream()
                .map(a -> a.widget)
                .forEach(AbstractWidget::update);
    }

    @Override
    public void show() {
        children.values()
                .stream()
                .map(a -> a.widget)
                .forEach(AbstractWidget::show);
    }

    @Override
    public void hide() {
        children.values()
                .stream()
                .map(a -> a.widget)
                .forEach(AbstractWidget::hide);
    }
}
