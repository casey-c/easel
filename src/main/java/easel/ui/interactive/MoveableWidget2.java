package easel.ui.interactive;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;

public class MoveableWidget2 extends AbstractWidget<MoveableWidget2> {
    private final float width;
    private final float height;
    private final AbstractWidget moveTarget;

    private boolean moving;

    private float startingWidgetLeft;
    private float startingWidgetBottom;

    private int startingMouseX;
    private int startingMouseY;

    // --------------------------------------------------------------------------------

    public MoveableWidget2(float width, float height, AbstractWidget moveTarget) {
        this.width = width;
        this.height = height;

        this.moveTarget = moveTarget;

        // Sets up the hitbox
        enableInteractivity();
    }

    public MoveableWidget2(AbstractWidget moveTarget) {
        this.width = moveTarget.getContentWidth();
        this.height = moveTarget.getContentHeight();

        this.moveTarget = moveTarget;
        enableInteractivity();
    }

    // --------------------------------------------------------------------------------

    private void updateCurrentlyMoving() {
        // Update the widget position to the mouse
        int currMouseX = InputHelper.mX;
        int currMouseY = InputHelper.mY;

        int deltaX = startingMouseX - currMouseX;
        int deltaY = startingMouseY - currMouseY;

        float newWidgetLeft = startingWidgetLeft - deltaX;
        float newWidgetBottom = startingWidgetBottom - deltaY;

        moveTarget.anchoredAt(newWidgetLeft, newWidgetBottom, AnchorPosition.LEFT_BOTTOM, 20);

        // Handle releasing the mouse down
        if (InputHelper.justReleasedClickLeft) {
            this.moving = false;
        }
    }
    @Override
    protected void updateInteractivity() {
        // Special move updater
        if (moving) {
            updateCurrentlyMoving();
        }
        else {
            // Left click started (start moving)
            if (isHovered && InputHelper.justClickedLeft) {
                moving = true;

                this.startingMouseX = InputHelper.mX;
                this.startingMouseY = InputHelper.mY;
                this.startingWidgetLeft = moveTarget.getLeft();
                this.startingWidgetBottom = moveTarget.getBottom();
            }
        }

        // Handle regular mouse events (enter/clicks/etc.)
        super.updateInteractivity();
    }

    @Override public float getContentWidth() { return width; }
    @Override public float getContentHeight() { return height; }

    @Override
    protected void renderWidget(SpriteBatch sb) {
        hb.render(sb);
    }
}
