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

//    public JsonObject serialize() {
//        JsonObject obj = new JsonObject();
//
////        JsonArray left = new JsonArray(map.size());
////        JsonArray bottom = new JsonArray(map.size());
////        JsonArray addOrders = new JsonArray(map.size());
//
//        JsonArray left = new JsonArray();
//        JsonArray bottom = new JsonArray();
//        JsonArray addOrders = new JsonArray();
//
//        map.values().forEach(item -> {
//            left.add(item.widget.getLeft());
//            bottom.add(item.widget.getBottom());
//            addOrders.add(item.addOrder);
//        });
//
//        obj.add("left", left);
//        obj.add("bottom", bottom);
//        obj.add("addOrders", addOrders);
//
//        return obj;
//    }

    private boolean updatePosition(int orderedPosition, float newLeft, float newBottom) {
        for (MapItem item : map.values()) {
            if (item.addOrder == orderedPosition) {
                item.widget.anchoredAt(newLeft, newBottom, AnchorPosition.LEFT_BOTTOM);
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

//    public boolean deserialize(JsonObject obj) {
//        int numberValuesProperlyUpdated = 0;
//
//        JsonArray left = obj.get("left").getAsJsonArray();
//        JsonArray bottom = obj.get("bottom").getAsJsonArray();
//        JsonArray addOrders = obj.get("addOrders").getAsJsonArray();
//
//        for (int i = 0; i < left.size() && i < bottom.size() && i < addOrders.size(); ++i) {
//            float l = left.get(i).getAsFloat();
//            float b = bottom.get(i).getAsFloat();
//            int order = addOrders.get(i).getAsInt();
//
//            if (updatePosition(order, l, b))
//                ++numberValuesProperlyUpdated;
//        }
//
//        return (numberValuesProperlyUpdated == map.size());
//    }

    public boolean deserialize(String jsonString) {
        int numValuesUpdated = 0;

        Gson gson = new Gson();
        SerializationHelperContainer container = gson.fromJson(jsonString, SerializationHelperContainer.class);

        if (container != null && container.widgets != null) {
            for (SerializationHelperWidget w : container.widgets) {
                if (updatePosition(w.addOrder, w.left, w.bottom))
                    ++numValuesUpdated;
            }
        }

        return (numValuesUpdated == map.size());
    }

    public String serialize() {
        SerializationHelperContainer container = new SerializationHelperContainer();
        container.widgets = new ArrayList<>(map.size());

        for (MapItem item : map.values()) {
            container.widgets.add(new SerializationHelperWidget(item.addOrder, item.widget));
        }

        return new Gson().toJson(container);
    }
}
