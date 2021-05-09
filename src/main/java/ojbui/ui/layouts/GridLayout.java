package ojbui.ui.layouts;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import ojbui.ui.AbstractWidget;
import ojbui.ui.AnchorPosition;
import ojbui.ui.InterpolationSpeed;

import java.util.*;
import java.util.stream.Stream;

public final class GridLayout extends AbstractWidget<GridLayout> {
    // --------------------------------------------------------------------------------
    // Simple struct for making widget location access convenient
    // --------------------------------------------------------------------------------
    private static final class GridLocation {
        int row;
        int col;

        public GridLocation(int row, int col) {
            this.row = row;
            this.col = col;
        }

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

    private static final class GridItem {
        AbstractWidget widget;
        AnchorPosition anchor;

        public GridItem(AbstractWidget widget, AnchorPosition anchor) {
            this.widget = widget;
            this.anchor = anchor;
        }
    }

    // --------------------------------------------------------------------------------
    private HashMap<GridLocation, GridItem> children = new HashMap<>();
    private float totalWidth;
    private float totalHeight;

    private int numWidgetsHorizontal;
    private int numWidgetsVertical;

    private ArrayList<Float> rowHeights = new ArrayList<>();
    private ArrayList<Float> colWidths = new ArrayList<>();

    private AnchorPosition defaultChildAnchor = AnchorPosition.LEFT_TOP;

    private float preferredWidth = 0.0f;
    private float preferredHeight = 0.0f;

    public GridLayout() { }

    public GridLayout(float preferredWidth, float preferredHeight) {
        this.preferredWidth = preferredWidth;
        this.preferredHeight = preferredHeight;
    }

    // --------------------------------------------------------------------------------

    public GridLayout withDefaultChildAnchorPosition(AnchorPosition anchorPosition) {
        this.defaultChildAnchor = anchorPosition;
        return this;
    }

    public void clear() {
        children.clear();
    }

    // --------------------------------------------------------------------------------

    private ArrayList<Float> buildExactSizeArray(float pref, float... values) {
        ArrayList<Float> sizes = new ArrayList<>();

        float sum = 0.0f;
        int numNegative = 0;

        for (float v : values) {
            if (v < 0.0f)
                ++numNegative;
            else
                sum += v;
        }

        float remainder = pref - sum;
        float sizePerDynamicElement = (numNegative > 0) ? remainder / (float)numNegative : 0.0f;

        for (float v : values) {
            if (v < 0.0f)
                sizes.add(sizePerDynamicElement);
            else
                sizes.add(v);
        }

        return sizes;
    }

    private ArrayList<Float> buildRelativeSizeArray(float pref, float... values) {
        ArrayList<Float> sizes = new ArrayList<>();

        if (values.length == 0)
            return sizes;

        float sumPositive = 0.0f;
        float sumNegative = 0.0f;

        int numPositive = 0;
        int numNegative = 0;

        for (float v : values) {
            if (v < 0.0f) {
                numNegative++;
                sumNegative -= v;
            }
            else {
                numPositive++;
                sumPositive += v;
            }
        }

        float totalSum = sumPositive + sumNegative;

        // Sanity check
        if (totalSum == 0.0f)
            return sizes;

        if (numNegative > 0) {
            float positivePercent = sumPositive / totalSum;
            float negativePercent = sumNegative / totalSum;

            for (float v : values) {
                if (v < 0.0f)
                    sizes.add(negativePercent * pref * (-v / sumNegative));
                else
                    sizes.add(positivePercent * pref * (v / sumPositive));
            }
        }
        else {
            for (float v : values) {
                sizes.add((v / sumPositive) * pref);
            }
        }

        return sizes;
    }

    private void updateTotalHeight() {
        this.totalHeight = rowHeights.stream().reduce(Float::sum).orElse(0.0f);
    }

    private void updateTotalWidth() {
        this.totalWidth = colWidths.stream().reduce(Float::sum).orElse(0.0f);
    }

    public GridLayout withExactRows(float... heights) {
        this.rowHeights = buildExactSizeArray(preferredHeight, heights);
        updateTotalHeight();
        return this;
    }

    public GridLayout withExactCols(float... widths) {
        this.colWidths = buildExactSizeArray(preferredWidth, widths);
        updateTotalWidth();
        return this;
    }

    public GridLayout withRelativeRows(float... heights) {
        this.rowHeights = buildRelativeSizeArray(preferredHeight, heights);
        updateTotalHeight();
        return this;
    }

    public GridLayout withRelativeCols(float... widths) {
        this.colWidths = buildRelativeSizeArray(preferredWidth, widths);
        updateTotalWidth();
        return this;
    }

    // --------------------------------------------------------------------------------


    @Override public float getContentWidth() { return totalWidth; }
    @Override public float getContentHeight() { return totalHeight; }


    // --------------------------------------------------------------------------------

    protected Stream<GridItem> iteratorByRow(int row) {
        return children.entrySet()
                .stream()
                .filter(a -> a.getKey().row == row)
                .map(Map.Entry::getValue);
    }

    protected Stream<GridItem> iteratorByCol(int col) {
        return children.entrySet()
                .stream()
                .filter(a -> a.getKey().col == col)
                .map(Map.Entry::getValue);
    }

    // Originally used for growth policy stuff (which got cut for simplicity)
//    public float getMaxHeightInRow(int row) {
//        return iteratorByRow(row)
//                .map(item -> item.widget)
//                .map(AbstractWidget::getContentHeight)
//                .max(Float::compareTo)
//                .orElse(0.0f);
//    }
//
//    public float getMaxWidthInCol(int col) {
//        return iteratorByCol(col)
//                .map(item -> item.widget)
//                .map(AbstractWidget::getContentWidth)
//                .max(Float::compareTo)
//                .orElse(0.0f);
//    }

    // --------------------------------------------------------------------------------
    // Add child
    // --------------------------------------------------------------------------------

    public void addChild(AbstractWidget widget, int row, int col, AnchorPosition anchorPosition) {
        children.put(new GridLocation(row, col), new GridItem(widget, anchorPosition));
    }

    public void addChild(AbstractWidget widget, int row, int col) {
        addChild(widget, row, col, defaultChildAnchor);
    }

    public GridLayout withChild(AbstractWidget widget, int row, int col) {
        addChild(widget, row, col, defaultChildAnchor);
        return this;
    }

    public GridLayout withChild(AbstractWidget widget, int row, int col, AnchorPosition anchorPosition) {
        addChild(widget, row, col, anchorPosition);
        return this;
    }

    // --------------------------------------------------------------------------------
    // Convenient row / col position access
    // --------------------------------------------------------------------------------

    private float getColLeft(int col)  {
        float pos = 0.0f;
        for (int i = 0; i < col && i < colWidths.size(); ++i)
            pos += colWidths.get(i);
        return pos;
    }

    private float getRowTop(int row) {
        float pos = 0.0f;
        for (int i = 0; i < row && i < rowHeights.size(); ++i)
            pos -= rowHeights.get(i);
        return pos;
    }

    private float getColWidth(int col) {
        return (col < colWidths.size()) ? colWidths.get(col) : 0.0f;
    }

    private float getRowHeight(int row) {
        return (row < rowHeights.size()) ? rowHeights.get(row) : 0.0f;
    }

    // --------------------------------------------------------------------------------

    private void anchorChild(AbstractWidget child, int row, int col, AnchorPosition target, InterpolationSpeed withDelay) {
        float colLeft = getColLeft(col);
        float rowTop = getRowTop(row);

        float colWidth = getColWidth(col);
        float rowHeight = getRowHeight(row);

        float x = colLeft;
        if (target.isCenterX())
            x += colWidth * 0.5f;
        else if (target.isRight())
            x += colWidth;

        float y = rowTop;
        if (target.isCenterY())
            y -= rowHeight * 0.5f;
        else if (target.isBottom())
            y -= rowHeight;

        child.anchoredAt(x + getContentLeft(), y + getContentTop(), target, withDelay);
    }

    private void anchorAllChildren(InterpolationSpeed withDelay) {
        for (Map.Entry<GridLocation, GridItem> gridEntry : children.entrySet()) {
            GridLocation location = gridEntry.getKey();
            GridItem item = gridEntry.getValue();

            anchorChild(item.widget, location.row, location.col, item.anchor, withDelay);
        }
    }

    // --------------------------------------------------------------------------------

    @Override
    public GridLayout anchoredAt(float x, float y, AnchorPosition anchorPosition, InterpolationSpeed withDelay) {
        super.anchoredAt(x, y, anchorPosition, withDelay);
        anchorAllChildren(withDelay);
        return this;
    }

    @Override
    public GridLayout anchoredAt(float x, float y, AnchorPosition anchorPosition) {
        return anchoredAt(x, y, anchorPosition, InterpolationSpeed.INSTANT);
    }

    @Override
    public GridLayout anchorCenteredOnScreen(InterpolationSpeed withDelay) {
        super.anchorCenteredOnScreen(withDelay);
        anchorAllChildren(withDelay);
        return this;
    }

    @Override
    public GridLayout anchorCenteredOnScreen() {
        return anchorCenteredOnScreen(InterpolationSpeed.INSTANT);
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
