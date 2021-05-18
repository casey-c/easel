package easel.ui.interactive;

import com.megacrit.cardcrawl.helpers.input.InputHelper;
import easel.Easel;
import easel.ui.AnchorPosition;

public abstract class MovableWidget<T extends MovableWidget<T>> extends HitboxWidget<T> {

    private boolean moving = false;

    private float startingWidgetLeft;
    private float startingWidgetBottom;

    private int startingMouseX;
    private int startingMouseY;

    @Override
    public void update() {
        hb.update();

        if (moving) {
            // Update the widget position to the mouse (clamped?)
            int currMouseX = InputHelper.mX;
            int currMouseY = InputHelper.mY;

            int deltaX = startingMouseX - currMouseX;
            int deltaY = startingMouseY - currMouseY;

            float newWidgetLeft = startingWidgetLeft - deltaX;
            float newWidgetBottom = startingWidgetBottom - deltaY;

            anchoredAt(newWidgetLeft, newWidgetBottom, AnchorPosition.LEFT_BOTTOM, 20);

            // Handle releasing the mouse down
            if (InputHelper.justReleasedClickLeft) {
                this.moving = false;
            }
        }
        else {
            // Hover transitions
            if (hb.hovered && !isHovered) {
                onHoverEnter.accept((T)this);

                Easel.logger.info("Hover started");
                Easel.logger.info(this);

                isHovered = true;
            }
            else if (!hb.hovered && isHovered){
                onHoverLeave.accept((T)this);

                Easel.logger.info("Hover finished");
                Easel.logger.info(this);

                isHovered = false;
            }

            // Left click started
            if (isHovered && InputHelper.justClickedLeft) {
                leftClickStarted = true;

                // START MOVING
                moving = true;

                this.startingMouseX = InputHelper.mX;
                this.startingMouseY = InputHelper.mY;
                this.startingWidgetLeft = getLeft();
                this.startingWidgetBottom = getBottom();
            }

            // Left click ended (ignored?)
            if (leftClickStarted && InputHelper.justReleasedClickLeft) {
                if (isHovered) {
                    onLeftClick.accept((T)this);

                    Easel.logger.info("Clicked (regular)");
                    Easel.logger.info(this);
                }

                leftClickStarted = false;
            }

            // Right click started
            if (isHovered && InputHelper.justClickedRight) {
                rightClickStarted = true;
            }

            // Right click ended
            if (rightClickStarted && InputHelper.justReleasedClickRight) {
                if (isHovered) {
                    onRightClick.accept((T)this);

                    Easel.logger.info("Right Clicked (regular)");
                    Easel.logger.info(this);
                }

                rightClickStarted = false;
            }
        }
    }
}
