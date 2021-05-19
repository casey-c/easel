package easel.ui.debug;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;
import easel.ui.interactive.MovableWidget;
import easel.ui.interactive.MoveableWidget;
import easel.ui.layouts.GridLayout;
import easel.utils.GraphicsHelper;

public class PartiallyMoveableWidget extends AbstractWidget<PartiallyMoveableWidget> {
    private GridLayout grid;

    private static final float MOVE_HEIGHT = 40;

    public PartiallyMoveableWidget(float totalWidth, float totalHeight) {
//        MovableWidget moveArea = new MovableWidget() {
//            @Override public float getContentWidth() { return totalWidth; }
//            @Override public float getContentHeight() { return MOVE_HEIGHT; }
//
//            @Override
//            protected void renderWidget(SpriteBatch sb) {
//                GraphicsHelper.drawRect(sb, getContentLeft(), getContentBottom(), getContentWidth(), getContentHeight(), DebugWidget.DEBUG_COLOR_0);
//            }
//        };


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

    @Override public void update() { grid.update(); }
    @Override protected void renderWidget(SpriteBatch sb) { grid.render(sb); }
}
