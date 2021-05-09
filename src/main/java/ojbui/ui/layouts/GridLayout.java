package ojbui.ui.layouts;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    private ArrayList<Float> rowHeights = new ArrayList<>();
    private ArrayList<Float> colWidths = new ArrayList<>();

    private AnchorPosition defaultChildAnchor = AnchorPosition.LEFT_TOP;


    public GridLayout() { }


    // --------------------------------------------------------------------------------

    public GridLayout withDefaultChildAnchorPosition(AnchorPosition anchorPosition) {
        this.defaultChildAnchor = anchorPosition;
        return this;
    }

    public void clear() {
        children.clear();
    }

    // --------------------------------------------------------------------------------

    private void updateTotalHeight() {
        this.totalHeight = rowHeights.stream().reduce(Float::sum).orElse(0.0f);
    }

    private void updateTotalWidth() {
        this.totalWidth = colWidths.stream().reduce(Float::sum).orElse(0.0f);
    }

    // --------------------------------------------------------------------------------

    private ArrayList<Float> buildExactSizeArray(float... values) {
        ArrayList<Float> sizes = new ArrayList<>();

        for (float v : values)
            sizes.add(v);

        return sizes;
    }

    private ArrayList<Float> buildExactSizeArrayOverflow(float overflow, float... values) {
        ArrayList<Float> sizes = new ArrayList<>();

        int numNegative = 0;

        for (float v : values) {
            if (v < 0.0f)
                ++numNegative;
        }

        float sizePerOverflowElement = (numNegative > 0) ? overflow / (float)numNegative : 0.0f;

        for (float v : values) {
            if (v < 0.0f)
                sizes.add(sizePerOverflowElement);
            else
                sizes.add(v);
        }

        return sizes;
    }

    private ArrayList<Float> buildRelativeSizeArray(float total, float... values) {
        ArrayList<Float> sizes = new ArrayList<>();

        if (values.length == 0)
            return sizes;

        float sum = 0.0f;
        for (float v : values)
            sum += v;

        if (sum == 0.0f)
            return sizes;

        for (float v : values)
            sizes.add((v / sum) * total);

        return sizes;
    }

    private ArrayList<Float> buildNSizeArray(float pref, int count) {
        ArrayList<Float> sizes = new ArrayList<>();

        for (int i = 0; i < count; ++i)
            sizes.add(pref / count);

        return sizes;
    }

    // --------------------------------------------------------------------------------

    /**
     * Sets the heights of each row to an exact pixel value. Each argument to this function produces a row in the grid with the given height.
     * The following example makes the grid have three rows, where the top-most row is given a height of 100px, the middle row 200px, and the bottom row 300px:
     * <pre>
     * {@code
     * grid.withExactRows(100.0f, 200.0f, 300.0f);
     * }
     * </pre>
     * NOTE: using negative values for a row height lets the layout overlap multiple rows (usually undesired behavior).
     * @see #withExactCols(float...)
     * @see #withExactRowsOverflow(float, float...)
     * @param heights a list of heights for row 0, row 1, row 2, etc., where row 0 is the top-most row. Heights are assumed to be in 1080p space.
     * @return this
     */
    public GridLayout withExactRows(float... heights) {
        this.rowHeights = buildExactSizeArray(heights);
        updateTotalHeight();
        return this;
    }

    /**
     * Sets the widths of each column to an exact pixel value. Each argument to this function produces a column in the grid with the given width.
     * The following example makes the grid have three columns, where the left-most column is given a width of 100px, the middle column 200px, and the right-most row 300px:
     * <pre>
     * {@code
     * grid.withExactCols(100.0f, 200.0f, 300.0f);
     * }
     * </pre>
     * NOTE: using negative values for a column width lets the layout overlap multiple columns (usually undesired behavior).
     * @see #withExactRows(float...)
     * @see #withExactColsOverflow(float, float...)
     * @param widths a list of widths for column 0, column 1, column 2, etc., where column 0 is the left-most row. Widths are assumed to be in 1080p space.
     * @return this
     */
    public GridLayout withExactCols(float... widths) {
        this.colWidths = buildExactSizeArray(widths);
        updateTotalWidth();
        return this;
    }

    /**
     * Sets the heights of each row to an exact pixel value allowing for "overflow" rows . Each argument to this function produces a row in the grid with the given height.
     * The following example makes the grid have three rows, where the top-most row is given a height of 100px, the middle row 200px, and the bottom row 300px:
     * <pre>
     * {@code
     * grid.withExactRows(100.0f, 200.0f, 300.0f);
     * }
     * </pre>
     * NOTE: using negative values for a row height lets the layout overlap multiple rows (usually undesired behavior).
     * @see #withExactRows(float...)
     * @param overflowHeight how much space should be allotted for ALL overflow rows combined; with N overflow rows, each row will be provided <code>overflowHeight / N</code> pixels of height.
     * @param heights a list of heights for row 0, row 1, row 2, etc., where row 0 is the top-most row. Heights are assumed to be in 1080p space.
     * @return this
     */
    public GridLayout withExactRowsOverflow(float overflowHeight, float... heights) {
        this.rowHeights = buildExactSizeArrayOverflow(overflowHeight, heights);
        updateTotalHeight();
        return this;
    }

    public GridLayout withExactColsOverflow(float overflowWidth, float... widths) {
        this.colWidths = buildExactSizeArrayOverflow(overflowWidth, widths);
        updateTotalWidth();
        return this;
    }

    public GridLayout withRelativeRows(float totalHeight, float... heights) {
        this.rowHeights = buildRelativeSizeArray(totalHeight, heights);
        updateTotalHeight();
        return this;
    }

    public GridLayout withRelativeCols(float totalWidth, float... widths) {
        this.colWidths = buildRelativeSizeArray(totalWidth, widths);
        updateTotalWidth();
        return this;
    }

    public GridLayout withNEvenlyDistributedRows(float totalHeight, int numRows) {
        this.rowHeights = buildNSizeArray(totalHeight, numRows);
        updateTotalHeight();
        return this;
    }

    public GridLayout withNEvenlyDistributedCols(float totalWidth, int numCols) {
        this.colWidths = buildNSizeArray(totalWidth, numCols);
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
