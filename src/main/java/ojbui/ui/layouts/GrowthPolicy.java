package ojbui.ui.layouts;

public enum GrowthPolicy {
    FIXED,
    EXPANDING_HORIZONTAL,
    EXPANDING_VERTICAL,
    EXPANDING_BOTH;

    public boolean isDynamicSize() {
        return this != FIXED;
    }
}
