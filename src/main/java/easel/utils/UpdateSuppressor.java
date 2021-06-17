package easel.utils;

/**
 * <p>
 * Can be used to suppress updates to the base game. This patches into the regular game loop and prevents execution from occurring if suppression is enabled. The {@link easel.ui.containers.MoveContainer} uses this suppressor to ensure that moving widgets around will not interact with the game elements underneath. Usage is done in pairs: first suppress the desired functionality by passing in <code>true</code>, do whatever you need to do, and then be sure to release afterwards by passing in <code>false</code> to the same functions you used to start the suppression. Not releasing suppressed code will mean the game will be stuck and won't update, so be sure to release at the end.
 * </p>
 * <p>
 * Note that these suppression patches affect the base game's update loop but shouldn't affect BaseMod's hooks. If you're using something other than BaseMod's update hooks (e.g. you're hooking directly into the game with some manual patches), you may accidentally suppress your own updates with this suppressor and make it impossible to do anything (or release the suppressor). So be sure to test that this suppressor does what you need it to do, or consider rolling your own variant to do what you need.
 * </p>
 * <p>
 * These functions are not thread-safe and will choose to suppress or allow based on whatever boolean it received most recently.
 * </p>
 */
public class UpdateSuppressor {
    private static boolean suppressingTips = false;
    private static boolean suppressingUpdates = false;

    // --------------------------------------------------------------------------------

    /**
     * Suppress the rendering of tips from the base game's TipHelper.
     * @param shouldSuppressTips if true, starts suppressing tips from being rendered; if false, lets tips render again
     */
    public static void suppressTips(boolean shouldSuppressTips) {
        suppressingTips = shouldSuppressTips;
    }

    /**
     * Suppress any updates to the base game. Works while in a run (but not on the main menu, for example).
     * @param shouldSuppressUpdates if true, starts suppressing updates from happening in the main game loop; if false, lets updates proceed as normal
     */
    public static void suppressUpdates(boolean shouldSuppressUpdates) {
        suppressingUpdates = shouldSuppressUpdates;
    }

    /**
     * Convenience function to suppress/release both tips ({@link #suppressTips(boolean)}) and updates ({@link #suppressUpdates(boolean)}) at the same time.
     * @param shouldSuppressAll if true, start suppressing; if false, release suppression
     */
    public static void suppressAll(boolean shouldSuppressAll) {
        suppressTips(shouldSuppressAll);
        suppressUpdates(shouldSuppressAll);
    }

    // --------------------------------------------------------------------------------

    /**
     * Used by the patches.
     * @return whether tips should be prevented from rendering
     */
    public static boolean isSuppressingTips() {
        return suppressingTips;
    }

    /**
     * Used by the patches
     * @return whether updates should be prevented from occurring
     */
    public static boolean isSuppressingUpdates() {
        return suppressingUpdates;
    }
}
