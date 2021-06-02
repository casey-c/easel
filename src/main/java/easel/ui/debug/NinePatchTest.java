package easel.ui.debug;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;
import easel.ui.graphics.ninepatch.DualNinePatchWidget;
import easel.ui.graphics.pie.PieChartWidget;
import easel.ui.layouts.GridLayout;
import easel.utils.SoundHelper;
import easel.utils.textures.TextureDatabase;
import easel.utils.textures.TextureManager;

public class NinePatchTest extends AbstractWidget<NinePatchTest> {
    private DualNinePatchWidget np;
    private GridLayout grid;

    public NinePatchTest() {
        this.grid = new GridLayout()
                .withRowsCols(500, 500, 3, 3)
                .withDefaultChildAnchorPosition(AnchorPosition.CENTER);

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                grid.withChild(row, col,
                        new DebugWidget()
                                .onMouseEnter(w -> w.setColor(DebugWidget.DEBUG_COLOR_1))
                                .onMouseLeave(w -> w.setColor(DebugWidget.DEBUG_COLOR_0))
                                .onLeftClick(w -> SoundHelper.uiClick1())
                                .onRightClick(w -> SoundHelper.uiClick2())
                );
            }
        }

        grid.withChild(1, 1, new PieChartWidget(100, 100).withCounts(6, 4, 0, 1));

        //grid.resizeRowToFitTallestChild(1);

        this.np = new DualNinePatchWidget(100, 100, TextureManager.getTexture(TextureDatabase.TOOLTIP_BASE), TextureManager.getTexture(TextureDatabase.TOOLTIP_TRIM))
                .withColors(Color.BLUE, Color.CYAN)
                .scaleToFullWidget(grid);
    }

    @Override public float getContentWidth() { return grid.getContentWidth(); }
    @Override public float getContentHeight() { return grid.getContentHeight(); }

    @Override
    public NinePatchTest anchoredAt(float x, float y, AnchorPosition anchorPosition, InterpolationSpeed withDelay) {
        super.anchoredAt(x, y, anchorPosition, withDelay);

        np.anchoredAt(getContentCenterX(), getContentCenterY(), AnchorPosition.CENTER, withDelay);
        grid.anchoredAt(getContentCenterX(), getContentCenterY(), AnchorPosition.CENTER, withDelay);

        return this;
    }

    @Override
    protected void renderWidget(SpriteBatch sb) {
        np.render(sb);
        grid.render(sb);
    }

    @Override
    protected void updateWidget() {
        np.update();
        grid.update();
    }
}
