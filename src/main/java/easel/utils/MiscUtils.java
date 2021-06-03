package easel.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class MiscUtils {
    public static float[] toPrimitive(ArrayList<Float> list) {
        int numNonNull = (int)list.stream().filter(Objects::nonNull).count();
        float[] results = new float[numNonNull];

        int index = 0;
        for (Float f : list) {
            if (f != null) {
                results[index++] = f;
            }
        }

        return results;
    }

    public static float[] toPrimitive(Float... floats) {
        return toPrimitive(new ArrayList<>(Arrays.asList(floats)));
    }
}
