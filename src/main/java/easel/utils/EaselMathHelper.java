package easel.utils;

public class EaselMathHelper {
    /**
     * @param numberToRound the number to round
     * @param multiple the output will be some multiple of this number
     * @return the multiple of <code>multiple</code> that is closest to <code>numberToRound</code>
     */
    public static int roundToMultipleOf(int numberToRound, int multiple) {
        return Math.round(roundToMultipleOf((float)numberToRound, (float)multiple));
    }

    /**
     * @param numberToRound the number to round
     * @param multiple the output will be some multiple of this number
     * @return the multiple of <code>multiple</code> that is closest to <code>numberToRound</code>
     */
    public static float roundToMultipleOf(float numberToRound, float multiple) {
        return Math.round(numberToRound / multiple) * multiple;
    }
}
