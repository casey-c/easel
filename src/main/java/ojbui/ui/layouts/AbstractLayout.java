package ojbui.ui.layouts;

public interface AbstractLayout {
    /**
     * Remove all widgets from this layout.
     */
    void clear();

    /**
     * (Potentially expensive). Force the layout to update the positions of all tracked children. Ensure that this is called at least once before rendering or using the getContentWidth() and getContentHeight() functions. NOTE: the addChild and addAll methods will NOT recompute the layout automatically since the recompute layout can be expensive. Be sure to call this function after you finish adding child widgets to this layout in order to finalize the layout creation.
     */
    void recomputeLayout();
}
