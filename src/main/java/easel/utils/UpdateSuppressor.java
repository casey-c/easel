package easel.utils;

import easel.Easel;

public class UpdateSuppressor {
//    private static boolean currentlySuppressing = false;
//
//    /**
//     * Prevents updates from occurring on the main game loop. Always be sure to call {@link #release()} when finished to allow the game to proceed. While suppression is active, the game will not process hitboxes, advance the timers, or do any other computations it does during the standard update step. These actions will not be lost forever and should start up exactly where they paused after the suppression is halted. Since rendering is not part of the update step, the game will continue to render and animate many of its idle animations. Does nothing if already activated.
//     */
//    public static void activate() {
//        if (!currentlySuppressing) {
//            currentlySuppressing = true;
//            Easel.logger.info("Update Suppressor: activated");
//        }
//    }
//
    // --------------------------------------------------------------------------------

    private static boolean suppressingTips = false;
    private static boolean suppressingUpdates = false;

    // --------------------------------------------------------------------------------

    public static void suppressTips() {
        suppressingTips = true;
        Easel.logger.info("Update Suppressor: suppressing tips");
    }

    public static void suppressUpdates() {
        suppressingUpdates = true;
        Easel.logger.info("Update Suppressor: suppressing updates");
    }

    public static void suppressAll() {
        suppressTips();
        suppressUpdates();
    }

    // --------------------------------------------------------------------------------

    public static void releaseTipSuppression() {
        suppressingTips = false;
        Easel.logger.info("Update Suppressor: release tips");
    }

    public static void releaseUpdateSuppression() {
        suppressingUpdates = false;
        Easel.logger.info("Update Suppressor: release updates");
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

    // --------------------------------------------------------------------------------

//    /**
//     * Stop suppressing updates to the main game loop. Updates will resume from where they left off when {@link #activate()} was called. Does nothing if not currently suppressing.
//     */
//    public static void release() {
//        if (currentlySuppressing) {
//            currentlySuppressing = false;
//            Easel.logger.info("Update Suppressor: released");
//        }
//    }
//
//    /**
//     * @return true if an {@link #activate()} has been called and no {@link #release()} has been made yet
//     */
//    public static boolean isActive() {
//        return currentlySuppressing;
//    }
}
