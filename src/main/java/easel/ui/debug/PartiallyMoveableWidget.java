package easel.ui.debug;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;
import easel.ui.interactive.MoveableWidget;
import easel.ui.layouts.GridLayout;

public class PartiallyMoveableWidget extends AbstractWidget<PartiallyMoveableWidget> {
    private GridLayout grid;

    private static final float MOVE_HEIGHT = 40;

    public PartiallyMoveableWidget(float totalWidth, float totalHeight) {
        this.grid = new GridLayout();
        MoveableWidget moveArea = new MoveableWidget(grid, totalWidth, MOVE_HEIGHT);

        grid.withExactRows(MOVE_HEIGHT, totalHeight - MOVE_HEIGHT)
                .withNEvenlySizedCols(totalWidth, 1)
                .withChild(0, 0, moveArea)
                .withChild(1, 0, new DebugWidget(totalWidth, totalHeight - MOVE_HEIGHT));
    }

    @Override
    public PartiallyMoveableWidget anchoredAt(float x, float y, AnchorPosition anchorPosition, InterpolationSpeed withDelay) {
        super.anchoredAt(x, y, anchorPosition, withDelay);
        grid.anchoredAt(x, y, anchorPosition, withDelay);
        return this;
    }

    @Override public float getContentWidth() { return grid.getContentWidth(); }
    @Override public float getContentHeight() { return grid.getContentHeight(); }

    @Override public void updateWidget() { grid.update(); }
    @Override protected void renderWidget(SpriteBatch sb) { grid.render(sb); }
}
