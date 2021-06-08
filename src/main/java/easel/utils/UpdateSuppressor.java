package easel.utils;

public class UpdateSuppressor {
    private static boolean suppressingTips = false;
    private static boolean suppressingUpdates = false;

    // --------------------------------------------------------------------------------

    public static void suppressTips() {
        suppressingTips = true;
    }

    public static void suppressUpdates() {
        suppressingUpdates = true;
    }

    public static void suppressAll() {
        suppressTips();
        suppressUpdates();
    }

    // --------------------------------------------------------------------------------

    public static void releaseTipSuppression() {
        suppressingTips = false;
    }

    public static void releaseUpdateSuppression() {
        suppressingUpdates = false;
    }

    public static void releaseAllSuppression() {
        releaseTipSuppression();
        releaseUpdateSuppression();
    }

    // --------------------------------------------------------------------------------

    public static boolean isSuppressingTips() {
        return suppressingTips;
    }

    public static boolean isSuppressingUpdates() {
        return suppressingUpdates;
    }
}
