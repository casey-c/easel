package easel.ui.layouts;
//
//import ojbui.ui.AbstractWidget;
//
//import java.util.Arrays;
//import java.util.List;
//
//public abstract class AbstractOneDimensionalLayout<T extends AbstractOneDimensionalLayout<T, W>, W extends AbstractWidget> extends AbstractWidget<T> implements AbstractLayout {
//    /**
//     * Let this layout manage the given widget.
//     * @param widget the child to be managed
//     */
//    public abstract void addChild(W widget);
//
//    /**
//     * Convenience method for addChild.
//     * @param widget the child to be managed.
//     * @return this layout
//     */
//    public final T withChild(W widget) {
//        return (T)this;
//    }
//
//    /**
//     * Adds all widgets to be managed by this layout. The particular display order is managed by the layout type.
//     * @param widgets a list of widgets to be managed
//     */
//    public abstract void addAll(List<W> widgets);
//
//    /**
//     * Convenience method for allAll.
//     * @param widgets a variadic list of widgets to be managed
//     */
//    public final void addAll(W... widgets) {
//        addAll(Arrays.asList(widgets));
//    }
//
//    /**
//     * Convenience method for allAll.
//     * @param widgets a list of widgets to be managed
//     * @return this layout
//     */
//    public final T withChildren(List<W> widgets) {
//        addAll(widgets);
//        return (T)this;
//    }
//
//    /**
//     * Convenience method for allAll.
//     * @param widgets a variadic list of widgets to be managed
//     * @return
//     */
//    public final T withChildren(W... widgets) {
//        addAll(widgets);
//        return (T)this;
//    }
//
//    /**
//     * Convenience method for recomputeLayout().
//     * @return this layout
//     */
//    public final T withRecomputeLayout() {
//        recomputeLayout();
//        return (T)this;
//    }
//}
