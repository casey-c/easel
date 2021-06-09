package easel.ui.graphics.pie;

import com.badlogic.gdx.graphics.Color;
import easel.ui.graphics.ShaderWidget;

import java.util.ArrayList;

/**
 * Renders a circular pie chart using shaders given dynamically specified regions. There are two distinct ways to build the chart: in bulk, using the {@link #withCounts(int...)} and {@link #withColors(Color...)} methods, or region by region with repeated applications of {@link #withRegion(int, Color)}. The order the regions are rendered in the pie chart (counter-clockwise starting at the top right) is determined by the order they are inserted into this widget. You can update the count or color of a particular region if you know the index at which it was entered into the chart. When constructing the pie chart using the bulk methods, make sure you have the same number of colors as region counts (otherwise it is undefined behavior).
 */
public class PieChartWidget extends ShaderWidget<PieChartWidget> {
    private static final float TWO_PI = (float)Math.PI * 2.0f;

    private boolean shouldRebuildThetas = true;
    private boolean shouldRebuildColors = true;

    private final ArrayList<Integer> countsList = new ArrayList<>();
    private final ArrayList<Float> colorsList = new ArrayList<>();

    private float[] thetasArray;
    private float[] colorsArray;

    // --------------------------------------------------------------------------------

    /**
     * Constructs a new pie-chart that will render with the given width or height. The pie chart will be rendered as a square up to the smaller dimension. Note: the pie chart itself will be a little bit smaller than the given region as the radius used internally does not stretch all the way to the sides by design.
     * @param width the width of the widget
     * @param height the height of the widget
     */
    public PieChartWidget(float width, float height) {
        super(width, height, "easel/shaders/pie/vert.glsl", "easel/shaders/pie/frag.glsl");
    }

    // --------------------------------------------------------------------------------

    /**
     * Sets all counts in the pie chart at once. Each count determines how much relative space that particular region gets as compared to the whole area, e.g. counts of <code>withCounts(1, 1)</code> will result in two regions each taking up exactly 50% of the final area. The order of the variadic list determines the order in which the regions are constructed. This function automatically clears out any previously existing counts, but will not clear out colors. This is intended to be used alongside {@link #withColors(Color...)} with the same number of arguments to each, and not doing so should be considered undefined behavior.
     * @param counts a list of relative counts determining the size of each region
     * @return this widget
     * @see #withColors(Color...)
     * @see #withRegion(int, Color)
     */
    public PieChartWidget withCounts(int... counts) {
        this.countsList.clear();

        for (int c : counts)
            this.countsList.add(c);

        rebuildThetaArray();
        return this;
    }

    /**
     * Sets all colors in the pie chart at once. This function will clear out any existing colors added previously and is intended to be used alongside {@link #withCounts(int...)} with the same number of parameters to each. The order in the variadic list determines the order rendered on the pie chart, going counter-clockwise from the top right.
     * @param colors a list of colors for each region
     * @return this widget
     * @see #withCounts(int...)
     * @see #withRegion(int, Color)
     */
    public PieChartWidget withColors(Color... colors) {
        this.colorsList.clear();

        for (Color c : colors) {
            colorsList.add(c.r);
            colorsList.add(c.g);
            colorsList.add(c.b);
            colorsList.add(c.a);
        }

        rebuildColorArray();
        return this;
    }

    // --------------------------------------------------------------------------------

    /**
     * Clears out all previously stored region sizes and colors. Not necessary to be called unless you're using the {@link #withRegion(int, Color)} mutators and wish to reset any previously added regions. The next {@link #withRegion(int, Color)} call after this function will be the first region in the chart.
     * @return this widget
     * @see #withRegion(int, Color)
     */
    public PieChartWidget clear() {
        countsList.clear();
        colorsList.clear();

        shouldRebuildColors = true;
        shouldRebuildThetas = true;

        return this;
    }

    /**
     * Set the relative count and color of the next region in the pie chart. This function is intended to be repeatedly chained until all regions are defined, and serves as a way to individually add regions instead of doing them in bulk (e.g. {@link #withCounts(int...) etc.}. The first region constructed by this function will be the top right, with the remaining regions all relative to each other and proceeding counter-clockwise.
     * @param count the relative size of this region (compared to other uses of this function)
     * @param color the color for this region
     * @return this widget
     * @see #clear()
     * @see #withCounts(int...)
     * @see #withColors(Color...)
     */
    public PieChartWidget withRegion(int count, Color color) {
        countsList.add(count);

        colorsList.add(color.r);
        colorsList.add(color.g);
        colorsList.add(color.b);
        colorsList.add(color.a);

        shouldRebuildColors = true;
        shouldRebuildThetas = true;

        return this;
    }

    /**
     * Updates the relative size of the given region, if it exists. The index is determined by the order in which the region is added (starting at 0) using the bulk input functions (e.g. {@link #withCounts(int...)}) or the particular input function (e.g. {@link #withRegion(int, Color)}).
     * @param index the zero-based index referring to the particular region to update
     * @param count the new relative size of the region
     * @return true if there is a region at that particular index which was updated; false if the index given is out of bounds
     */
    public boolean updateRegionCount(int index, int count) {
        if (index < 0 || index >= countsList.size())
            return false;

        countsList.set(index, count);

        // O(n) since counts are relative (need to rebuild the whole thing!)
        shouldRebuildThetas = true;

        return true;
    }

    /**
     * Updates the color of the given region, if it exists. The index is determined by the order in which the region is added (starting at 0) using the bulk input functions (e.g. {@link #withCounts(int...)}) or the particular input function (e.g. {@link #withRegion(int, Color)}).
     * @param index the zero-based index referring to the particular region to update
     * @param color the new color of the region
     * @return true if there is a region at that particular index which was updated; false if the index given is out of bounds
     */
    public boolean updateRegionColor(int index, Color color) {
        if (index >= colorsList.size() / 4)
            return false;

        // 0       1           (0-1), size 2
        // r g b a r g b a     (0-7), size 8
        index = index * 4;

        colorsList.set(index, color.r);
        colorsList.set(index + 1, color.g);
        colorsList.set(index + 2, color.b);
        colorsList.set(index + 3, color.a);

        // As colors aren't relative to each other, if we've already built it we can update the array directly
        if (!shouldRebuildColors) {
            colorsArray[index] = color.r;
            colorsArray[index + 1] = color.g;
            colorsArray[index + 2] = color.b;
            colorsArray[index + 3] = color.a;
        }

        return true;
    }

    // --------------------------------------------------------------------------------

    /**
     * Converts the array list of counts into a basic array of cumulative floats for the GPU to use.
     */
    private void rebuildThetaArray() {
        shouldRebuildThetas = false;

        thetasArray = new float[countsList.size()];

        float sum = countsList.stream().reduce(0, Integer::sum);
        float sumThetaSoFar = 0.0f;

        for (int i = 0; i < countsList.size(); ++i) {
            float currTheta = ((float) countsList.get(i) / sum) * TWO_PI;
            sumThetaSoFar += currTheta;
            thetasArray[i] = sumThetaSoFar;
        }
    }

    /**
     * Converts the array list of color data into a basic array for the GPU to use.
     */
    private void rebuildColorArray() {
        shouldRebuildColors = false;

        colorsArray = new float[colorsList.size()];
        for (int i = 0; i < colorsList.size(); ++i)
            colorsArray[i] = colorsList.get(i);
    }

    // --------------------------------------------------------------------------------

    @Override
    protected void setUniforms() {
        // Construct the arrays, if we haven't yet (or if they're dirty)
        if (shouldRebuildColors)
            rebuildColorArray();

        if (shouldRebuildThetas)
            rebuildThetaArray();

        // Set the uniforms used by the shader
        shaderProgram.setUniformf("borderSize", 2.0f / Math.min(getContentWidth(), getContentHeight()));

        shaderProgram.setUniformi("numThetas", thetasArray.length);
        shaderProgram.setUniform1fv("thetas", thetasArray, 0, thetasArray.length);

        shaderProgram.setUniformi("numColors", colorsArray.length / 4);
        shaderProgram.setUniform4fv("colors", colorsArray, 0, colorsArray.length);
    }
}
