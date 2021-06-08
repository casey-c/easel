package easel.ui.containers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.utils.EaselInputHelper;
import easel.utils.EaselMathHelper;
import easel.utils.EaselSoundHelper;
import easel.utils.UpdateSuppressor;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;


public class MoveContainer extends AbstractWidget<MoveContainer> {
    private float width;
    private float height;
    private boolean fullscreen;

    private TreeMap<Integer, AbstractWidget> map = new TreeMap<>();

    public MoveContainer() {
        this.width = Settings.WIDTH;
        this.height = Settings.HEIGHT;
        this.fullscreen = true;
    }

    // --------------------------------------------------------------------------------

    public MoveContainer withChild(AbstractWidget child) {
        map.put(getTopMostIndex() + 1, child);
        return this;
    }

    // --------------------------------------------------------------------------------

    private int getBottomMostIndex() {
        return map.isEmpty() ? 0 : map.firstKey();
    }

    private int getTopMostIndex() {
        return map.isEmpty() ? 0 : map.lastKey();
    }

    private void bringIndexToBottom(int index) {
        AbstractWidget widget = map.get(index);
        map.remove(index);
        map.put(getBottomMostIndex() - 1, widget);
    }

    private void bringIndexToTop(int index) {
        AbstractWidget widget = map.get(index);
        map.remove(index);
        map.put(getTopMostIndex() + 1, widget);
    }

    // --------------------------------------------------------------------------------

    @Override public float getContentWidth() { return width; }
    @Override public float getContentHeight() { return height; }

    // --------------------------------------------------------------------------------

//    @Override
//    public MoveContainer anchoredAt(float x, float y, AnchorPosition anchorPosition, InterpolationSpeed withDelay) {
//        if (fullscreen) {
//            return this;
//        } else {
//            // TODO: move non full screen version
//            return this;
//        }
//    }

    // --------------------------------------------------------------------------------

    private boolean moving;

    private AbstractWidget moveTarget;

    private float startingWidgetLeft;
    private float startingWidgetBottom;

    private int startingMouseX;
    private int startingMouseY;

    private Optional<Map.Entry<Integer, AbstractWidget>> findTopMostWidgetUnderMouse() {
        for (Map.Entry<Integer, AbstractWidget> entry : map.descendingMap().entrySet()) {
            if (entry.getValue().isMouseInBounds())
                return Optional.of(entry);
        }

        return Optional.empty();
    }

    private void updateCurrentlyMoving() {
        // Update the widget position to the mouse
        int currMouseX = EaselInputHelper.getMouseX();
        int currMouseY = EaselInputHelper.getMouseY();

        int deltaX = startingMouseX - currMouseX;
        int deltaY = startingMouseY - currMouseY;

        float newWidgetLeft = startingWidgetLeft - deltaX;
        float newWidgetBottom = startingWidgetBottom - deltaY;

        // Round to nearest multiple of 10
        if (EaselInputHelper.isShiftPressed()) {
            newWidgetLeft = EaselMathHelper.roundToMultipleOf(newWidgetLeft, 10);
            newWidgetBottom = EaselMathHelper.roundToMultipleOf(newWidgetBottom, 10);
        }

        moveTarget.anchoredAt(newWidgetLeft, newWidgetBottom, AnchorPosition.LEFT_BOTTOM, 20);

        // Handle releasing the mouse down
        if (InputHelper.justReleasedClickLeft) {
            this.moving = false;

            if (moveTarget instanceof StyledContainer) {
                ((StyledContainer)moveTarget).withShadows(false);
            }

            UpdateSuppressor.releaseUpdateSuppression();
//            SoundHelper.uiClick2();
        }
    }

    @Override
    protected void updateWidget() {
        // Update all children
        map.values().forEach(AbstractWidget::update);

        // Test for click and drag moves
        if (moving)
            updateCurrentlyMoving();
        else {
            // Figure out the move target
            Optional<Map.Entry<Integer, AbstractWidget>> target = findTopMostWidgetUnderMouse();

            // Nothing under mouse
            if (!target.isPresent()) {
                UpdateSuppressor.releaseAllSuppression();
                return;
            }

            // Left click started (start moving)
            if (InputHelper.justClickedLeft) {
                UpdateSuppressor.suppressAll();

                Map.Entry<Integer, AbstractWidget> validTarget = target.get();
                this.moveTarget = validTarget.getValue();

                if (moveTarget instanceof StyledContainer) {
                    ((StyledContainer)moveTarget).withShadows(true);
                }

                bringIndexToTop(validTarget.getKey());

                // Start the move
                EaselSoundHelper.uiClick1();
                this.moving = true;

                this.startingMouseX = EaselInputHelper.getMouseX();
                this.startingMouseY = EaselInputHelper.getMouseY();

                this.startingWidgetLeft = moveTarget.getLeft();
                this.startingWidgetBottom = moveTarget.getBottom();
            }
            else {
                UpdateSuppressor.suppressTips();
            }
        }

        // Handle regular mouse events (mouse enter/clicks/etc.)
        super.updateInteractivity();
    }


    // --------------------------------------------------------------------------------

    @Override
    protected void renderWidget(SpriteBatch sb) {
        map.values().forEach(w -> w.render(sb));
    }

    // --------------------------------------------------------------------------------
}
