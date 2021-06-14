package easel.config.samples;

import easel.config.EaselConfigHelper;
import easel.config.enums.ConfigBooleanEnum;

/**
 * A sample implementation of the {@link ConfigBooleanEnum} interface. This includes two config options, a BOOL_ONE and a BOOL_TWO. Adding new options should only be APPENDED to the end of the options (not added into the middle) once you publicly release your mod (or deserialization will load the improper values and potentially cause confusion). See {@link EaselConfigHelper} for further details.
 * @see EaselConfigHelper
 */
public enum SampleBooleanChoices implements ConfigBooleanEnum {
    BOOL_ONE(true),
    BOOL_TWO(false);

    boolean defaultValue;

    SampleBooleanChoices(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override public boolean getDefault() { return defaultValue; }
}
