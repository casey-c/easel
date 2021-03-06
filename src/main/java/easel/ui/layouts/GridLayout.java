package easel.ui.layouts;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import easel.Easel;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;

import java.util.*;
import java.util.stream.Stream;

/**
 * UNSTABLE / API subject to change. A more powerful layout to aid arranging things in a grid. Since this class is probably going to be redesigned before the first official release, this documentation will remain unfinished until the API is more stable. For now, the general pattern to use this layout is to construct a new Grid, set up the rows/columns by calling one of {@link #withExactRows(float...)}, {@link #withNEvenlySizedRows(float, int)}, etc. and one of {@link #withExactCols(float...)}, {@link #withRelativeCols(float, float...)}, etc. (use one of the row builders to initialize the rows and one of the column builders for the columns). After that, you may add children using the convenience methods {@link #withChildrenInRow(int, AbstractWidget[])} or by directly specifying the cell {@link #withChild(int, int, AbstractWidget)} it falls into. (Row 0, Column 0) is the top left corner and the grid layout grows down and to the right of that, and when specifying widgets to go into certain cells you must be careful to place them into positions that actually exist from your initialized version in order for the grid to function properly. Finally, when you call an {@link #anchoredAt(float, float, AnchorPosition)}, the children are all moved into place. Like other layouts, you can automatically scale rows or columns using the various helper methods, (e.g. {@link #resizeRowToFitTallestChild(int)}), and managing the usual suspects (render, update, etc.) is as simple as calling those functions on the layout itself (as everything will trickle down the hierarchy as usual).
 */
public final class GridLayout extends AbstractWidget<GridLayout> {
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

    /**
     * Makes evenly distributed rows/cols for a certain width and height. Convenience function for performing both {@link #withNEvenlySizedRows(float, int)} and {@link #withNEvenlySizedCols(float, int)} in one function call.
     * @param totalWidth the total width of the grid
     * @param totalHeight the total height of the grid
     * @param numRows the number of rows in the grid
     * @param numCols the number of cols in the grid
     * @return this layout
     * @see #withNEvenlySizedRows(float, int)
     * @see #withNEvenlySizedCols(float, int)
     */
    public GridLayout withRowsCols(float totalWidth, float totalHeight, int numRows, int numCols) {
        withNEvenlySizedRows(totalHeight, numRows);
        withNEvenlySizedCols(totalWidth, numCols);
        return this;
    }

    // --------------------------------------------------------------------------------


    @Override public float getContentWidth() { return totalWidth; }
    @Override public float getContentHeight() { return totalHeight; }


    // --------------------------------------------------------------------------------

    /**
     * Returns all children in the given row, in no particular order.
     * @param row the row to pull children from
     * @return a stream containing any widget that sits in this row
     */
    public Stream<LayoutItem> iteratorByRow(int row) {
        return children.entrySet()
                .stream()
                .filter(a -> a.getKey().row == row)
                .map(Map.Entry::getValue);
    }

    /**
     * Returns all children in the given column, in no particular order.
     * @param col the column to pull children from
     * @return a stream containing any widget that sits in this column
     */
    public Stream<LayoutItem> iteratorByCol(int col) {
        return children.entrySet()
                .stream()
                .filter(a -> a.getKey().col == col)
                .map(Map.Entry::getValue);
    }

    /**
     * Returns all children in the given column of a particular type, in no particular order. Children in this column which are not instances of the given type are not added to the stream. This is mostly for convenience as recovering the type inside the stream can make some code cleaner. This variant of {@link #iteratorByCol(int)} has a slight performance penalty due to making sure the casts are safe.
     * @param col the column to pull children from
     * @param clz the class of widget caught by the filter
     * @param <T> the type of widget that will be in the final stream
     * @return a stream containing any widget of type <code>T</code> that sits in this column
     * @see #iteratorByCol(int)
     */
    public <T> Stream<T> iteratorByColOfType(int col, Class<T> clz) {
        return iteratorByCol(col).filter(clz::isInstance).map(w -> (T)w);
    }

    /**
     * Returns all children in the given row of a particular type, in no particular order. Children in this row which are not instances of the given type are not added to the stream. This is mostly for convenience as recovering the type inside the stream can make some code cleaner. This variant of {@link #iteratorByRow(int)} has a slight performance penalty due to making sure the casts are safe.
     * @param row the row to pull children from
     * @param clz the class of widget caught by the filter
     * @param <T> the type of widget that will be in the final stream
     * @return a stream containing any widget of type <code>T</code> that sits in this row
     * @see #iteratorByRow(int)
     */
    public <T> Stream<T> iteratorByRowOfType(int row, Class<T> clz) {
        return iteratorByRow(row).filter(clz::isInstance).map(w -> (T)w);
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
     * <p>
     * Let this grid manage the given widget. This function uses the specified anchor (ignoring <code>defaultChildAnchor</code>) to position this child inside its grid cell. The widget is moved into the proper position next time {@link #anchoredAt(float, float, AnchorPosition)} (or another in the <code>anchoredAt</code> family) is called.
     * </p>
     * <p>
     * NOTE: This widget will replace any existing widget at the same (row, col) position if it already exists. It also assumes that (row,col) will be a valid position in the grid, but no bounds checking is provided. The grid will add this widget to its tracked HashMap regardless.
     * </p>
     * @param row the row the widget will be placed in (0 is the top-most row)
     * @param col the col the widget will be placed in (0 is the left-most column)
     * @param widget the widget to track
     * @param anchorPosition how the child will be situated inside the (row, col) grid cell [which may be wider and taller than the widget's own width/height]
     * @see #withChild(int, int, AbstractWidget)
     * @return this layout
     */
    public GridLayout withChild(int row, int col, AbstractWidget widget, AnchorPosition anchorPosition) {
        children.put(new GridLocation(row, col), new LayoutItem(widget, anchorPosition));
        return this;
    }

    /**
     * <p>
     * Let this grid manage the given widget. This function uses the current <code>defaultChildAnchor</code> position to set this child's position inside its grid cell (see {@link #withDefaultChildAnchorPosition(AnchorPosition)} for details). The widget is moved into the proper position next time {@link #anchoredAt(float, float, AnchorPosition)} (or another in the <code>anchoredAt</code> family) is called.
     * </p>
     * <p>
     * NOTE: This widget will replace any existing widget at the same (row, col) position if it already exists. It also assumes that (row,col) will be a valid position in the grid, but no bounds checking is provided. The grid will add this widget to its tracked HashMap regardless.
     * </p>
     * @param row the row the widget will be placed in (0 is the top-most row)
     * @param col the col the widget will be placed in (0 is the left-most column)
     * @param widget the widget to track
     * @see #withChild(int, int, AbstractWidget, AnchorPosition)
     * @see #withDefaultChildAnchorPosition(AnchorPosition)
     * @return this layout
     */
    public GridLayout withChild(int row, int col, AbstractWidget widget) {
        return withChild(row, col, widget, defaultChildAnchor);
    }

    /**
     * Adds the provided children to the specified row, starting at column 0. E.g. the first widget provided will be placed in the row at column 0, the second in the row at column 1, etc.
     * @param row the row to add the children into
     * @param widgets a list of child widgets
     * @return this layout
     * @see #withChild(int, int, AbstractWidget, AnchorPosition)
     */
    public GridLayout withChildrenInRow(int row, AbstractWidget... widgets) {
        for (int col = 0; col < widgets.length; ++col) {
            withChild(row, col, widgets[col]);
        }

        return this;
    }

    /**
     * Adds the provided children to the specified column, starting at row 0. E.g. the first widget provided will be placed in the column in row 0, the second in the column in row 1, etc.
     * @param col the column to add the children into
     * @param widgets a list of child widgets
     * @return this layout
     * @see #withChild(int, int, AbstractWidget, AnchorPosition)
     */
    public GridLayout withChildrenInCol(int col, AbstractWidget... widgets) {
        for (int row = 0; row < widgets.length; ++row) {
            withChild(row, col, widgets[row]);
        }

        return this;
    }

//    /**
//     * Convenience function.
//     * @param row the row the widget will be placed in (0 is the top-most row)
//     * @param col the col the widget will be placed in (0 is the left-most column)
//     * @param widget the widget to track
//     * @see #addChild(int, int, AbstractWidget)
//     * @return this
//     */
//    public GridLayout withChild(int row, int col, AbstractWidget widget) {
//        addChild(row, col, widget, defaultChildAnchor);
//        return this;
//    }

//    /**
//     * Convenience function.
//     * @param row the row the widget will be placed in (0 is the top-most row)
//     * @param col the col the widget will be placed in (0 is the left-most column)
//     * @param widget the widget to track
//     * @param anchorPosition how the child will be situated inside the (row, col) grid cell [which may be wider and taller than the widget's own width/height]
//     * @see #addChild(int, int, AbstractWidget, AnchorPosition)
//     * @return this
//     */
//    public GridLayout withChild(int row, int col, AbstractWidget widget, AnchorPosition anchorPosition) {
//        addChild(row, col, widget, anchorPosition);
//        return this;
//    }

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

    public GridLayout resizeRowToFitTallestChild(int row) {
        if (row > rowHeights.size())
            return this;

        float maxHeightInRow = children.entrySet().stream()
                .filter(entry -> entry.getKey().row == row)
                .map(entry -> entry.getValue().widget.getHeight())
                .max(Float::compareTo)
                .orElse(0.0f);

        rowHeights.set(row, maxHeightInRow);
        updateTotalHeight();

        return this;
    }

    public GridLayout resizeRowsToFitTallestChildren() {
        for (int row = 0; row < rowHeights.size(); ++row)
            resizeRowToFitTallestChild(row);

        return this;
    }

    public GridLayout resizeColToFitWidestChild(int col) {
        if (col > colWidths.size())
            return this;

        float maxWidthInCol = children.entrySet().stream()
                .filter(entry -> entry.getKey().col == col)
                .map(entry -> entry.getValue().widget.getWidth())
                .max(Float::compareTo)
                .orElse(0.0f);

        colWidths.set(col, maxWidthInCol);
        updateTotalWidth();

        return this;
    }

    public GridLayout resizeColsToFitWidestChildren() {
        for (int col = 0; col < colWidths.size(); ++col)
            resizeColToFitWidestChild(col);

        return this;
    }

    // --------------------------------------------------------------------------------

    /**
     * The ordering of this stream is dependent on the underlying map (which does not have a predictable order).
     * @return a stream of all children currently handled by this widget, in no particular order
     */
    public Stream<AbstractWidget> iterator() {
        return this.children.values().stream().map(item -> item.widget);
    }

    /**
     * A stream of children handled by this widget who are of the specific type, for convenience purposes. Like {@link #iterator()}, the resulting stream is in no particular order. If you know you've built the layout with all objects of a particular type, you can quickly recover them all into a stream that remembers the type. This has a slight performance penalty over {@link #iterator()} due to checking each child against the type for safe casts, but the resulting stream will be properly typed to make it easier to work with. Children managed by this layout who are not of the given type will not be included in the stream, so be wary if using this variant of the iterator.
     * @param clz the class for the type
     * @param <T> the type of child to extract
     * @return a stream of children who fit the given type
     * @see #iterator()
     */
    public <T> Stream<T> iteratorOfType(Class<T> clz) {
        return iterator().filter(clz::isInstance).map(c -> (T)c);
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

    @Override
    protected void cancelMovementQueueForAllChildren(boolean shouldTryAndResolveOneLastTime) {
        iterator().forEach(child -> child.cancelMovementQueue(shouldTryAndResolveOneLastTime));
    }

    // --------------------------------------------------------------------------------

    @Override
    public GridLayout anchoredAt(float x, float y, AnchorPosition anchorPosition, InterpolationSpeed movementSpeed) {
        super.anchoredAt(x, y, anchorPosition, movementSpeed);
        anchorAllChildren(movementSpeed);
        return this;
    }

    @Override
    protected void setChildrenDelayedMovement(float deltaX, float deltaY, InterpolationSpeed movementSpeed, long startingTimeMillis) {
        iterator().forEach(child -> child.setAllDelayedMovement(deltaX, deltaY, movementSpeed, startingTimeMillis));
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
//        sb.setColor(DebugWidget.DEBUG_COLOR_1);
//        sb.draw(ImageMaster.WHITE_SQUARE_IMG,
//                getContentLeft() * Settings.xScale,
//                getContentBottom() * Settings.yScale,
//                getContentWidth() * Settings.xScale,
//                getContentHeight() * Settings.yScale);

        children.values().forEach(w -> w.widget.render(sb));
    }

    @Override
    public void renderTopLevel(SpriteBatch sb) {
        children.values().forEach(c -> c.widget.renderTopLevel(sb));
    }

    @Override
    public void updateWidget() {
        children.values().forEach(w -> w.widget.update());
    }

    @Override
    public void show() {
        children.values().forEach(w -> w.widget.show());
    }

    @Override
    public void hide() {
        children.values().forEach(w -> w.widget.hide());
    }
}
