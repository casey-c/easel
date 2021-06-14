package easel.ui.containers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.layouts.GridLayout;
import easel.ui.layouts.HorizontalLayout;
import easel.ui.layouts.VerticalLayout;
import easel.utils.EaselInputHelper;
import easel.utils.EaselMathHelper;
import easel.utils.EaselSoundHelper;
import easel.utils.UpdateSuppressor;

import java.util.*;
import java.util.stream.Stream;

@SuppressWarnings("rawtypes")
public class MoveContainer extends AbstractWidget<MoveContainer> {
    private final float width;
    private final float height;

    //private final TreeMap<Integer, AbstractWidget> map = new TreeMap<>();
    private final TreeMap<Integer, MapItem> map = new TreeMap<>();

    private int addOrder = 0;

    private static class MapItem {
        AbstractWidget widget;
        int addOrder;

        public MapItem(AbstractWidget widget, int addOrder) {
            this.widget = widget;
            this.addOrder = addOrder;
        }
    }

    public MoveContainer() {
        this.width = Settings.WIDTH;
        this.height = Settings.HEIGHT;
    }

    // --------------------------------------------------------------------------------

    public MoveContainer withChild(AbstractWidget child) {
        map.put(getTopMostIndex() + 1, new MapItem(child, addOrder++));
        return this;
    }

    public MoveContainer withAllChildrenOfLayout(VerticalLayout layout) {
        layout.iterator().forEach( child -> {
            map.put(getTopMostIndex() + 1, new MapItem(child, addOrder++));
        });

        return this;
    }

    public MoveContainer withAllChildrenOfLayout(HorizontalLayout layout) {
        layout.iterator().forEach( child -> {
            map.put(getTopMostIndex() + 1, new MapItem(child, addOrder++));
        });

        return this;
    }

    public MoveContainer withAllChildrenOfLayout(GridLayout layout) {
        layout.iterator().forEach( child -> {
            map.put(getTopMostIndex() + 1, new MapItem(child, addOrder++));
        });

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
        MapItem item = map.get(index);
        map.remove(index);
        map.put(getBottomMostIndex() - 1, item);
    }

    private void bringIndexToTop(int index) {
        MapItem item = map.get(index);
        map.remove(index);
        map.put(getTopMostIndex() + 1, item);
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

    /**
     * @return a stream containing all children managed by this widget, in their render order from bottom to top
     */
    public Stream<AbstractWidget> iterator() {
        return map.values().stream().map(item -> item.widget);
    }

    // --------------------------------------------------------------------------------

    private boolean moving;

    private AbstractWidget moveTarget;

    private float startingWidgetLeft;
    private float startingWidgetTop;

    private int startingMouseX;
    private int startingMouseY;

    private Optional<Map.Entry<Integer, MapItem>> findTopMostWidgetUnderMouse() {
        for (Map.Entry<Integer, MapItem> item : map.descendingMap().entrySet()) {
            if (item.getValue().widget.isMouseInBounds())
                return Optional.of(item);
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
        float newWidgetTop = startingWidgetTop - deltaY;

        // Round to nearest multiple of 10
        if (EaselInputHelper.isShiftPressed()) {
            newWidgetLeft = EaselMathHelper.roundToMultipleOf(newWidgetLeft, 10);
            newWidgetTop = EaselMathHelper.roundToMultipleOf(newWidgetTop, 10);
        }

        moveTarget.anchoredAtClamped(newWidgetLeft, newWidgetTop, AnchorPosition.LEFT_TOP, 20);

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
//        map.values().forEach(AbstractWidget::update);
        iterator().forEach(AbstractWidget::update);

        // Test for click and drag moves
        if (moving)
            updateCurrentlyMoving();
        else {
            // Figure out the move target
            Optional<Map.Entry<Integer, MapItem>> target = findTopMostWidgetUnderMouse();

            // Nothing under mouse
            if (!target.isPresent()) {
                UpdateSuppressor.releaseAllSuppression();
                return;
            }

            // Left click started (start moving)
            if (InputHelper.justClickedLeft) {
                UpdateSuppressor.suppressAll();

                Map.Entry<Integer, MapItem> validTarget = target.get();
                this.moveTarget = validTarget.getValue().widget;

                moveTarget.cancelMovementQueue(true);

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
                this.startingWidgetTop = moveTarget.getTop();
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
        iterator().forEach(w -> w.render(sb));
    }

    // --------------------------------------------------------------------------------
    // Serialization / Deserialization
    // --------------------------------------------------------------------------------

    private boolean forcePosition(int orderedPosition, float newLeft, float newBottom) {
        for (Map.Entry<Integer, MapItem> entry : map.entrySet()) {
            MapItem item = entry.getValue();

            if (item.addOrder == orderedPosition) {
                item.widget.anchoredAtClamped(newLeft, newBottom, AnchorPosition.LEFT_BOTTOM, 20);

                // Recover stacking order?
                bringIndexToTop(entry.getKey());
                return true;
            }
        }

        return false;
    }

    private static class SerializationHelperWidget {
        int addOrder;
        float left;
        float bottom;

        public SerializationHelperWidget(int addOrder, AbstractWidget widget) {
            this.addOrder = addOrder;
            this.left = widget.getLeft();
            this.bottom = widget.getBottom();
        }
    }

    private static class SerializationHelperContainer {
        List<SerializationHelperWidget> widgets;
    }

    /**
     * <p>
     * Forcibly update all widget positions based on a previously serialized string. Note that the serialization writes out information about widget positions to a JSON formatted string, with the ability to identify widgets based on the order they are added to this container. Thus, this deserialization step only works correctly if you've added the same widgets in the same order as the time when {@link #serialize()} was called - i.e. unless you're doing something very weird and fancy by adding a variable number of widgets in a dynamic way, this deserialization / serialization step should work out of the box.
     * </p>
     * <p>
     * In the backend, this deserialization works by looping through each saved (addOrder, left, bottom) triplet in the serialized string, and trying to match up the widgets currently in the map. If the current map contains a widget which was added with the same addOrder as the triplet, that particular widget will be {@link AbstractWidget#anchoredAt(float, float, AnchorPosition)} with the (left, bottom) coordinates. This function will also attempt to restore the stacking order correctly - the serialization step stores information in order from bottom to top, and the deserialization loops through that same order and will bring elements to the top if they're valid. If all widgets in the map can be linked 1:1 with the elements in the serialized string, this function will return true.
     * </p>
     * <p>
     * Note that if you aren't using this function as intended (i.e. if you're getting anything other than true as the output as it fails to 1:1 update each widget), there are no guarantees that this function will be stable and not crash. So if you're doing something custom enough for this to return false, then you really shouldn't be using this at all and should roll your own serialization/deserialization code to fit your particular needs. Scary warning aside, if you're just using this in a predictable way (whenever a deserialize gets called, the container has the same basic structure of widgets added to it when the serialize was called), then this should be safe to use.
     * </p>
     * @param jsonString a string generated by a previous call of {@link #serialize()}
     * @return true if the container was able to link all elements of the serialized input string to all elements of the map and update them (successful 1 to 1 update of all tracked widgets).
     * @see #serialize()
     */
    public boolean deserialize(String jsonString) {
        if (jsonString.isEmpty())
            return (map.size() == 0);

        int numValuesUpdated = 0;

        Gson gson = new Gson();
        SerializationHelperContainer container = gson.fromJson(jsonString, SerializationHelperContainer.class);

        if (container != null && container.widgets != null) {
            for (SerializationHelperWidget w : container.widgets) {
                if (forcePosition(w.addOrder, w.left, w.bottom))
                    ++numValuesUpdated;
            }
        }

        return (numValuesUpdated == map.size());
    }

    /**
     * @return a JSON formatted string storing the positions of each element in the map, for recovering later with {@link #deserialize(String)}
     * @see #deserialize(String)
     */
    public String serialize() {
        SerializationHelperContainer container = new SerializationHelperContainer();
        container.widgets = new ArrayList<>(map.size());

        for (MapItem item : map.values()) {
            container.widgets.add(new SerializationHelperWidget(item.addOrder, item.widget));
        }

        return new Gson().toJson(container);
    }
}
