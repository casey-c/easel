package easel.ui.interactive;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;

public class MoveableWidget extends HitboxWidget<MoveableWidget> {
    private boolean moving = false;
    private AbstractWidget widgetToMove;

    private float startingWidgetLeft;
    private float startingWidgetBottom;

    private int startingMouseX;
    private int startingMouseY;

    public MoveableWidget(AbstractWidget widgetToMove, float hitboxWidth, float hitboxHeight) {
        this.widgetToMove = widgetToMove;
        this.hb = new Hitbox(hitboxWidth * Settings.xScale, hitboxHeight * Settings.yScale);
    }

    @Override public float getContentWidth() { return hb.width / Settings.xScale; }
    @Override public float getContentHeight() { return hb.height / Settings.yScale; }

    private void updateCurrentlyMoving() {
        // Update the widget position to the mouse (clamped?)
        int currMouseX = InputHelper.mX;
        int currMouseY = InputHelper.mY;

        int deltaX = startingMouseX - currMouseX;
        int deltaY = startingMouseY - currMouseY;

        float newWidgetLeft = startingWidgetLeft - deltaX;
        float newWidgetBottom = startingWidgetBottom - deltaY;

        widgetToMove.anchoredAt(newWidgetLeft, newWidgetBottom, AnchorPosition.LEFT_BOTTOM, 20);

        // Handle releasing the mouse down
        if (InputHelper.justReleasedClickLeft) {
            this.moving = false;
        }
    }

    @Override
    public void updateWidget() {
        hb.update();

        if (moving) {
            updateCurrentlyMoving();
        }
        else {
            // Hover transitions
            updateHoverTransitions();

            // Left click started
            if (isHovered && InputHelper.justClickedLeft) {
                // START MOVING
                moving = true;

                this.startingMouseX = InputHelper.mX;
                this.startingMouseY = InputHelper.mY;
                this.startingWidgetLeft = widgetToMove.getLeft();
                this.startingWidgetBottom = widgetToMove.getBottom();
            }

            updateRightClicks();
        }
    }

    @Override
    protected void renderWidget(SpriteBatch sb) {
        hb.render(sb);
    }
}
