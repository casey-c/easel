package easel.ui.graphics.pie;

import com.badlogic.gdx.graphics.Color;
import easel.ui.graphics.ShaderWidget;
import java.util.Arrays;

/**
 * Not intended for use (yet?). This class needs a LOT of generifying. Currently only allows 4 regions (hardcoded), and isn't ready for production. Will need to spend a lot more effort into tweaking the GLSL and Java code to make it useable beyond this. For now, consider this deprecated as it may be removed or changed entirely at any time.
 */
public class PieChartWidget extends ShaderWidget<PieChartWidget> {
    private static final float TWO_PI = (float)Math.PI * 2.0f;

    private int numThetas;
    private float[] thetas;

    private int numColors;
    private float[] colors;

    public PieChartWidget(float width, float height) {
        super(width, height, "easel/shaders/pie/vert.glsl", "easel/shaders/pie/frag.glsl");
    }

    public PieChartWidget withCounts(int... counts) {

        this.numThetas = counts.length - 1;
        this.thetas = new float[numThetas];

        float total = Arrays.stream(counts).sum();

        // Safety check (there should be at least one nonzero count)
        if (total < 1.0f) {
            for (int i = 0; i < numThetas - 1; ++i)
                this.thetas[i] = 0;

            return this;
        }

        float sumThetaSoFar = 0.0f;

        int index = 0;

        for (int i = 0; i < counts.length && i < numThetas; ++i) {
            int curr = counts[i];

            float currTheta = ((float)curr / total) * TWO_PI;
            thetas[index++] = sumThetaSoFar + (currTheta);

            sumThetaSoFar += currTheta;
        }

        return this;
    }

    public PieChartWidget withColors(Color... colors) {
        this.numColors = colors.length;

        this.colors = new float[numColors * 4];

        int index = 0;
        for (Color c : colors) {
            this.colors[index++] = c.r;
            this.colors[index++] = c.g;
            this.colors[index++] = c.b;
            this.colors[index++] = c.a;
        }

        return this;
    }

    @Override
    protected void setUniforms() {
        shaderProgram.setUniformf("borderSize", 2.0f / Math.min(getContentWidth(), getContentHeight()));

        shaderProgram.setUniformi("numThetas", numThetas);
        shaderProgram.setUniform1fv("thetas", thetas, 0, numThetas);

        shaderProgram.setUniformi("numColors", numColors);
        shaderProgram.setUniform4fv("colors", colors, 0, numColors * 4);
    }
}
