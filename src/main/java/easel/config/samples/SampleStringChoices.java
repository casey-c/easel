package easel.config.samples;

import easel.config.enums.ConfigStringEnum;

/**
 * A sample implementation of the {@link ConfigStringEnum} interface. This includes two config options, a STRING_ONE and a STRING_TWO. Adding new options should only be APPENDED to the end of the options (not added into the middle) once you publicly release your mod (or deserialization will load the improper values and potentially cause confusion). See {@link easel.config.ConfigHelper} for further details.
 * @see easel.config.ConfigHelper
 */
public enum SampleStringChoices implements ConfigStringEnum {
    STRING_ONE("one"),
    STRING_TWO("two");

    String defaultValue;

    SampleStringChoices(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override public String getDefault() { return defaultValue; }
}