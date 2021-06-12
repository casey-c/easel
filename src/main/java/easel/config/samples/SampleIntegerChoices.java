package easel.config.samples;

import easel.config.enums.ConfigIntegerEnum;

/**
 * A sample implementation of the {@link ConfigIntegerEnum} interface. This includes two config options, an INT_ONE and an INT_TWO. Adding new options should only be APPENDED to the end of the options (not added into the middle) once you publicly release your mod (or deserialization will load the improper values and potentially cause confusion). See {@link easel.config.ConfigHelper} for further details.
 * @see easel.config.ConfigHelper
 */
public enum SampleIntegerChoices implements ConfigIntegerEnum {
    INT_ONE(1),
    INT_TWO(2);

    int defaultValue;

    SampleIntegerChoices(int defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override public int getDefault() { return defaultValue; }
}
