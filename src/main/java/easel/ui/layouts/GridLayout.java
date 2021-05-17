package easel.ui.layouts;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import easel.Easel;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;
import easel.ui.debug.DebugWidget;

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


    // --------------------------------------------------------------------------------
    private HashMap<GridLocation, LayoutItem> children = new HashMap<>();
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

    private ArrayList<Float> buildNSizeArray(float total, int count) {
        ArrayList<Float> sizes = new ArrayList<>();

        for (int i = 0; i < count; ++i)
            sizes.add(total / count);

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
     * @see #withRelativeRows(float, float...)
     * @see #withNEvenlySizedRows(float, int)
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
     * @see #withRelativeCols(float, float...)
     * @see #withNEvenlySizedCols(float, int)
     * @param widths a list of widths for column 0, column 1, column 2, etc., where column 0 is the left-most column. Widths are assumed to be in 1080p space.
     * @return this
     */
    public GridLayout withExactCols(float... widths) {
        this.colWidths = buildExactSizeArray(widths);
        updateTotalWidth();
        return this;
    }

    /**
     * Sets the row heights relative to each other as a fraction of some given <code>totalHeight</code>. Each argument in the <code>heightRatios</code> variadic list produces a row in the final grid.
     * The following example makes the grid have three rows, where the top-most row is considered the "baseline" height with the middle row being twice as tall and the bottom row being half as tall. With a <code>totalHeight</code> of 350px, this results in row heights being 100px, 200px, and 50px respectively.
     * <pre>
     * {@code
     * grid.withRelativeRows(350.0f, 1f, 2f, 0.5f);
     * }
     * </pre>
     * The portion of the total height allotted to each row is entirely relative, so these will both produce 100px tall rows:
     * <pre>
     * {@code
     * grid.withRelativeRows(300.0f, 0.2f, 0.2f, 0.2f);
     * grid.withRelativeRows(300.0f, 1f, 1f, 1f);
     * }
     * </pre>
     * Given ratios (x, y, z), the space allotted for row x is simply <code>totalHeight * (x / (x + y + z))</code>
     * Note: undefined behavior if supplied negative or zero ratios
     * @param totalHeight the total space allotted for all rows combined
     * @param heightRatios a list of ratios used to divide up the totalHeight in a relative manner
     * @see #withRelativeCols(float, float...)
     * @see #withExactRows(float...)
     * @see #withNEvenlySizedRows(float, int)
     * @return this
     */
    public GridLayout withRelativeRows(float totalHeight, float... heightRatios) {
        this.rowHeights = buildRelativeSizeArray(totalHeight, heightRatios);
        updateTotalHeight();
        return this;
    }

    /**
     * Sets the column widths relative to each other as a fraction of some given <code>totalWidth</code>. Each argument in the <code>widthRatios</code> variadic list produces a column in the final grid.
     * The following example makes the grid have three columns, where the left-most column is considered the "baseline" width with the middle column being twice as wide and the bottom column being half as wide. With a <code>totalWidth</code> of 350px, this results in column widths being 100px, 200px, and 50px respectively.
     * <pre>
     * {@code
     * grid.withRelativeCols(350.0f, 1f, 2f, 0.5f);
     * }
     * </pre>
     * The portion of the total width allotted to each row is entirely relative, so these will both produce 100px wide columns:
     * <pre>
     * {@code
     * grid.withRelativeCols(300.0f, 0.2f, 0.2f, 0.2f);
     * grid.withRelativeCols(300.0f, 1f, 1f, 1f);
     * }
     * </pre>
     * Given ratios (x, y, z), the space allotted for column x is simply <code>totalWidth * (x / (x + y + z))</code>
     * NOTE: undefined behavior if supplied negative or zero ratios
     * @param totalWidth the total space allotted for all columns combined
     * @param widthRatios a list of ratios used to divide up the totalWidth in a relative manner
     * @see #withRelativeRows(float, float...)
     * @see #withExactCols(float...)
     * @see #withNEvenlySizedCols(float, int)
     * @return this
     */
    public GridLayout withRelativeCols(float totalWidth, float... widthRatios) {
        this.colWidths = buildRelativeSizeArray(totalWidth, widthRatios);
        updateTotalWidth();
        return this;
    }

    /**
     * Makes <code>numRows</code> evenly sized rows. Each row will be given a height of <code>totalHeight / numRows</code> pixels.
     * NOTE: undefined behavior if numRows is less than or equal to 0 or if totalHeight is less than 0
     * @param totalHeight the combined height of all rows
     * @param numRows the number of rows to construct
     * @return this
     * @see #withNEvenlySizedCols(float, int)
     * @see #withExactRows(float...)
     * @see #withRelativeRows(float, float...)
     */
    public GridLayout withNEvenlySizedRows(float totalHeight, int numRows) {
        this.rowHeights = buildNSizeArray(totalHeight, numRows);
        updateTotalHeight();
        return this;
    }

    /**
     * Makes <code>numCols</code> evenly sized columns. Each column will be given a width of <code>totalWidth / numCols</code> pixels.
     * NOTE: undefined behavior if numCols is less than or equal to 0 or if totalWidth is less than 0
     * @param totalWidth the combined width of all columns
     * @param numCols the number of columns to construct
     * @return this
     * @see #withNEvenlySizedRows(float, int)
     * @see #withExactCols(float...)
     * @see #withRelativeCols(float, float...)
     */
    public GridLayout withNEvenlySizedCols(float totalWidth, int numCols) {
        this.colWidths = buildNSizeArray(totalWidth, numCols);
        updateTotalWidth();
        return this;
    }

    // --------------------------------------------------------------------------------


    @Override public float getContentWidth() { return totalWidth; }
    @Override public float getContentHeight() { return totalHeight; }


    // --------------------------------------------------------------------------------

    protected Stream<LayoutItem> iteratorByRow(int row) {
        return children.entrySet()
                .stream()
                .filter(a -> a.getKey().row == row)
                .map(Map.Entry::getValue);
    }

    protected Stream<LayoutItem> iteratorByCol(int col) {
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

    /**
     * Let this grid manage the given widget. The widget will be placed at the specified anchor position at the desired (row, col) position in the grid next time {@link #anchoredAt(float, float, AnchorPosition)} is called. This widget will replace any existing widget at the same (row, col) position if it already exists.
     * NOTE: assumes that (row,col) will be a valid position in the grid, but no bounds checking is provided. The grid will add this widget to its tracked HashMap regardless.
     * @param row the row the widget will be placed in (0 is the top-most row)
     * @param col the col the widget will be placed in (0 is the left-most column)
     * @param widget the widget to track
     * @param anchorPosition how the child will be situated inside the (row, col) grid cell [which may be wider and taller than the widget's own width/height]
     * @see #addChild(int, int, AbstractWidget, AnchorPosition)
     * @see #withChild(int, int, AbstractWidget)
     * @see #withDefaultChildAnchorPosition(AnchorPosition)
     */
    public void addChild(int row, int col, AbstractWidget widget, AnchorPosition anchorPosition) {
        children.put(new GridLocation(row, col), new LayoutItem(widget, anchorPosition));
    }

    /**
     * Convenience function for {@link #addChild(int, int, AbstractWidget, AnchorPosition)}. Uses the <code>defaultChildAnchor</code> anchor set by the {@link #withDefaultChildAnchorPosition(AnchorPosition)} method (if that method isn't used when building, the <code>defaultChildAnchor</code> defaults to LEFT_TOP).
     * @param row the row the widget will be placed in (0 is the top-most row)
     * @param col the col the widget will be placed in (0 is the left-most column)
     * @param widget the widget to track
     * @see #addChild(int, int, AbstractWidget, AnchorPosition)
     */
    public void addChild(int row, int col, AbstractWidget widget) {
        addChild(row, col, widget, defaultChildAnchor);
    }

    /**
     * Convenience function.
     * @param row the row the widget will be placed in (0 is the top-most row)
     * @param col the col the widget will be placed in (0 is the left-most column)
     * @param widget the widget to track
     * @see #addChild(int, int, AbstractWidget)
     * @return this
     */
    public GridLayout withChild(int row, int col, AbstractWidget widget) {
        addChild(row, col, widget, defaultChildAnchor);
        return this;
    }

    /**
     * Convenience function.
     * @param row the row the widget will be placed in (0 is the top-most row)
     * @param col the col the widget will be placed in (0 is the left-most column)
     * @param widget the widget to track
     * @param anchorPosition how the child will be situated inside the (row, col) grid cell [which may be wider and taller than the widget's own width/height]
     * @see #addChild(int, int, AbstractWidget, AnchorPosition)
     * @return this
     */
    public GridLayout withChild(int row, int col, AbstractWidget widget, AnchorPosition anchorPosition) {
        addChild(row, col, widget, anchorPosition);
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
        // Ensure the child position is indeed tracked by this grid
        if (row >= rowHeights.size() || col >= colWidths.size()) {
            Easel.logger.warn("Warning: attempt to anchor child " + child + " to GridLayout " + this + " failed: (row, col) index out of bounds.");
        }

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
        for (Map.Entry<GridLocation, LayoutItem> gridEntry : children.entrySet()) {
            GridLocation location = gridEntry.getKey();
            LayoutItem item = gridEntry.getValue();

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

//    @Override
//    public GridLayout anchoredAt(float x, float y, AnchorPosition anchorPosition) {
//        return anchoredAt(x, y, anchorPosition, InterpolationSpeed.INSTANT);
//    }
//
//    @Override
//    public GridLayout anchorCenteredOnScreen(InterpolationSpeed withDelay) {
//        super.anchorCenteredOnScreen(withDelay);
//        anchorAllChildren(withDelay);
//        return this;
//    }
//
//    @Override
//    public GridLayout anchorCenteredOnScreen() {
//        return anchorCenteredOnScreen(InterpolationSpeed.INSTANT);
//    }
//
    // --------------------------------------------------------------------------------

    @Override
    protected void renderWidget(SpriteBatch sb) {
        // TODO: make debug only
        sb.setColor(DebugWidget.DEBUG_COLOR_1);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG,
                getContentLeft() * Settings.xScale,
                getContentBottom() * Settings.yScale,
                getContentWidth() * Settings.xScale,
                getContentHeight() * Settings.yScale);

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
