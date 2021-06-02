package easel.ui.interactive;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;

/**
 * A special interactive widget that helps make it easier to move widgets around with the mouse.
 */
// TODO: special options to adjust which mouse controls work as moving?
//   (e.g. right click / drag vs. left click / drag, or require SHIFT/ALT to be held, etc.
//   right now only considers left click and drag
public class MoveableWidget extends AbstractWidget<MoveableWidget> {
    private final float width;
    private final float height;
    private final AbstractWidget moveTarget;

    private boolean moving;

    private float startingWidgetLeft;
    private float startingWidgetBottom;

    private int startingMouseX;
    private int startingMouseY;

    // --------------------------------------------------------------------------------

    /**
     * <p>
     * Setup a new hitbox widget with the given width and height to control the movement of the given <code>moveTarget</code> widget. You'll want to make sure that this new widget created by this constructor is some descendant of the <code>moveTarget</code> (it doesn't have to be a direct child - grand children etc. will also work - it just needs to be synchronously moved along with the <code>moveTarget</code>). As usual, you'll want to anchor this attached widget somewhere where it makes sense (i.e. some piece inside or alongside the <code>moveTarget</code>) - use a layout widget!
     * </p>
     * <p>
     * The typical use case for this constructor is to make some specified portion of a larger layout be a designated "move area". The initial design was to support a simple windowing system, where users would click and drag only on the title bar of the window to move the entire window around. This particular setup can be mimicked by creating a simple VerticalLayout. The first child of the vertical layout can be constructed with this particular constructor, giving the desired width/height of this top piece and sending in the vertical layout itself as the <code>moveTarget</code>. The remaining children of the layout could be anything else; attaching this moveable widget to the layout this way enables users to click and drag only on the top most piece (the remaining children wouldn't be click/draggable) and move the entire VerticalLayout along with it.
     * </p>
     * <p>
     * Obviously, this approach requires a bit more manual effort to make it look nice (you'll need to add textures or some overall indication of this being a moveable area, since this widget alone does not do any rendering and will be invisible), but it's provided to help make some of the burden of moving widgets a lot easier.
     * </p>
     * @param width the width of the hitbox area
     * @param height the height of the hitbox area
     * @param moveTarget the widget which will be moved as users click and drag inside the hitbox area
     * @see #MoveableWidget(AbstractWidget)
     */
    public MoveableWidget(float width, float height, AbstractWidget moveTarget) {
        this.width = width;
        this.height = height;

        this.moveTarget = moveTarget;

        // Sets up the hitbox
        initializeInteractivity();
    }

    /**
     * A simplified version of {@link #MoveableWidget(float, float, AbstractWidget)}, which takes the hitbox size information directly from the <code>moveTarget</code>. This version is used by the {@link AbstractWidget#makeMoveable()} convenience function to attach the moveable area to the entire source widget.
     * @param moveTarget the widget which will be moved as users click and drag inside the hitbox area
     */
    public MoveableWidget(AbstractWidget moveTarget) {
        this.width = moveTarget.getContentWidth();
        this.height = moveTarget.getContentHeight();

        this.moveTarget = moveTarget;
        initializeInteractivity();
    }

    // --------------------------------------------------------------------------------

    private void updateCurrentlyMoving() {
        // Update the widget position to the mouse
        int currMouseX = (int)((float)InputHelper.mX / Settings.xScale);
        int currMouseY = (int)((float)InputHelper.mY / Settings.yScale);

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
