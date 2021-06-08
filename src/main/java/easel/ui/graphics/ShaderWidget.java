package easel.ui.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import easel.Easel;
import easel.ui.AbstractWidget;

/**
 * A base for widgets that use custom shaders to render graphics. This widget is potentially too niche for general use; its use case is primarily for leveraging GLSL code for letting a vertex shader and a fragment shader generate the graphics. By default, this widget attempts to render a white square onto the SpriteBatch, letting the provided shaders overwrite the rendering entirely if necessary. This process is designed for a specific style of customized 2D graphics and isn't set up for 3D. You may wish to look at the source code for this widget and use it as a starting point for more complicated applications, but really this ShaderWidget is simply the abstracted base for the {@link easel.ui.graphics.pie.PieChartWidget} and future as-of-yet unimplemented widgets that may use the same idea. (You probably won't find much use out of this widget as is).
 */
public abstract class ShaderWidget<T extends ShaderWidget<T>> extends AbstractWidget<T> {
    protected Texture tex = ImageMaster.WHITE_SQUARE_IMG;
    protected ShaderProgram shaderProgram;

    protected float width;
    protected float height;

    public ShaderWidget(float width, float height, String vertexShaderPath, String fragmentShaderPath) {
        this.width = width;
        this.height = height;

        this.shaderProgram = new ShaderProgram(
                Gdx.files.internal(vertexShaderPath).readString(),
                Gdx.files.internal(fragmentShaderPath).readString()
        );

        if (shaderProgram.isCompiled()) {
            Easel.logger.info("Shaders compiled successfully.");
        }
        else {
            Easel.logger.error("ERROR: shaders failed to compile");
            Easel.logger.error(shaderProgram.getLog());
        }
    }

    @Override public float getContentWidth() { return width; }
    @Override public float getContentHeight() { return height; }

    protected void setUniforms() { }

    protected void renderTexture(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        sb.draw(tex,
                getContentLeft() * Settings.xScale,
                getContentBottom() * Settings.yScale,
                getContentWidth() * Settings.xScale,
                getContentHeight() * Settings.yScale);
    }

    @Override
    protected void renderWidget(SpriteBatch sb) {
        sb.end();

        ShaderProgram oldShader = sb.getShader();
        sb.setShader(shaderProgram);

        // Do shader based renders
        sb.begin();

        setUniforms();
        renderTexture(sb);

        sb.end();

        // Reset
        sb.setShader(oldShader);
        sb.begin();
    }
}
