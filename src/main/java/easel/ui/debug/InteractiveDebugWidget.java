package easel.ui.debug;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import easel.ui.AnchorPosition;
import easel.ui.HitboxWidget;
import easel.ui.InterpolationSpeed;
import easel.utils.SoundHelper;

public class InteractiveDebugWidget extends HitboxWidget<InteractiveDebugWidget> {
    private DebugWidget dw;

    public InteractiveDebugWidget() {
        this.dw = new DebugWidget(50, 50);

        this.onHoverEnter(w -> setColor(DebugWidget.DEBUG_COLOR_1));
        this.onHoverLeave(w -> setColor(DebugWidget.DEBUG_COLOR_0));
    }

    public void setColor(Color color) {
        dw.setColor(color);
    }

    @Override public float getContentWidth() { return dw.getContentWidth(); }
    @Override public float getContentHeight() { return dw.getContentHeight(); }

    @Override
    public InteractiveDebugWidget anchoredAt(float x, float y, AnchorPosition anchorPosition, InterpolationSpeed withDelay) {
        super.anchoredAt(x, y, anchorPosition, withDelay);
        dw.anchoredAt(x, y, anchorPosition, withDelay);
        return this;
    }

    @Override
    protected void renderWidget(SpriteBatch sb) {
        dw.renderWidget(sb);

        // DEBUG
        this.hb.render(sb);
    }
}
