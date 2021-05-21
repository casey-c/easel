package easel.ui.graphics.pie;

import easel.ui.graphics.ShaderWidget;

/**
 * Not intended for use (yet?). This class needs a LOT of generifying. Currently only allows 4 regions (hardcoded), and isn't ready for production. Will need to spend a lot more effort into tweaking the GLSL and Java code to make it useable beyond this. For now, consider this deprecated as it may be removed or changed entirely at any time.
 */
@Deprecated
public class PieChartWidget extends ShaderWidget<PieChartWidget> {
    private float theta0, theta1, theta2;
    static final float TWO_PI = (float)Math.PI * 2.0f;

    public PieChartWidget(float width, float height) {
        super(width, height, "easel/shaders/pie/vert.glsl", "easel/shaders/pie/frag.glsl");
    }


    public PieChartWidget withCounts(int a, int b, int c, int d) {
        float total = a + b + c + d;

        if (total == 0)
            return this;

        float percentA = (float)a / total;
        float percentB = (float)b / total;
        float percentC = (float)c / total;

        this.theta0 = percentA * TWO_PI;
        this.theta1 = theta0 + (percentB * TWO_PI);
        this.theta2 = theta1 + (percentC * TWO_PI);

        return this;
    }

    @Override
    protected void setUniforms() {
        shaderProgram.setUniformf("borderSize", 2.0f / Math.min(getContentWidth(), getContentHeight()));

        shaderProgram.setUniformf("theta0", theta0);
        shaderProgram.setUniformf("theta1", theta1);
        shaderProgram.setUniformf("theta2", theta2);
    }
}
