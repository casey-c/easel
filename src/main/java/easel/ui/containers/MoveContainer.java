package easel.ui.containers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;
import easel.ui.text.Label;

import java.util.*;

public class MoveContainer extends AbstractWidget<MoveContainer> {
    private float width;
    private float height;
    private boolean fullscreen;

    private static class MoveObject {
        private AbstractWidget widget;
        private int index;

        public MoveObject(AbstractWidget w, int index) {
            this.widget = w;
            this.index = index;
        }
    }

    //private PriorityQueue<MoveObject> widgets;
//    private TreeSet<MoveObject> widgets;

    private TreeMap<Integer, AbstractWidget> map;

    public MoveContainer() {
        this.width = Settings.WIDTH;
        this.height = Settings.HEIGHT;
        this.fullscreen = true;

        //this.widgets = new PriorityQueue<>(Comparator.comparingInt(moveObject -> moveObject.index));
//        this.widgets = new PriorityQueue<>(new Comparator<MoveObject>() {
//            @Override
//            public int compare(MoveObject moveObject, MoveObject t1) {
//                //return Integer.compare(moveObject.index, t1.index);
//                return Integer.compare(t1.index, moveObject.index);
//            }
//        });

//        this.widgets = new TreeSet<>(new Comparator<MoveObject>() {
//            @Override
//            public int compare(MoveObject moveObject, MoveObject t1) {
//                return Integer.compare(moveObject.index, t1.index);
//            }
//        });

        this.map = new TreeMap<>();
    }

    private void leftMouseDown() {
        
    }

    public MoveContainer withChild(AbstractWidget child) {
//        if (widgets.isEmpty())
//            widgets.add(new MoveObject(child, 1));
//        else {
//            int head = widgets.last().index;
//            System.out.println("The head of the queue is: " + head);
//            widgets.add(new MoveObject(child, head + 1));
//            System.out.println();
//            printQueue();
//        }

        if (map.isEmpty()) {
            map.put(1, child);
        }
        else {
            int top = map.lastKey();
            System.out.println("The top-most item of the queue is @ index " + top);
            map.put(top + 1, child);
            printQueue();
        }

        return this;
    }

    public void printQueue() {
        System.out.println("The map has " + map.size() + " tracked widgets.");
        if (map.isEmpty())
            return;

        for (Map.Entry<Integer, AbstractWidget> entry : map.entrySet()) {
            int index = entry.getKey();
            AbstractWidget widget = entry.getValue();

            System.out.println("\t" + index + ": " + ((Label)widget).getText());
        }
        System.out.println();

        System.out.println("The bottom most widget is: " + ((Label)map.firstEntry().getValue()).getText());
        System.out.println("The top most widget is: " + ((Label)map.lastEntry().getValue()).getText());

        System.out.println();
    }

    private int getBottomMostIndex() {
        return map.isEmpty() ? 0 : map.firstKey();
    }

    private int getTopMostIndex() {
        return map.isEmpty() ? 0 : map.lastKey();
    }

    public void bringIndexToBottom(int index) {
        if (map.containsKey(index)) {
            AbstractWidget widget = map.get(index);
            map.remove(index);
            int bottom = getBottomMostIndex();
            map.put(bottom - 1, widget);
        }
    }

    public void bringIndexToTop(int index) {
        if (map.containsKey(index)) {
            AbstractWidget widget = map.get(index);
            map.remove(index);
            int top = getTopMostIndex();
            map.put(top + 1, widget);
        }
    }


    @Override public float getContentWidth() { return width; }
    @Override public float getContentHeight() { return height; }

    // --------------------------------------------------------------------------------

    @Override
    public MoveContainer anchoredAt(float x, float y, AnchorPosition anchorPosition, InterpolationSpeed withDelay) {
        if (fullscreen)
            return this;
        else {
            // TODO: move non full screen version
            return this;
        }
    }

    // --------------------------------------------------------------------------------

    @Override
    protected void renderWidget(SpriteBatch sb) {

    }
}
