package easel.config.enums;

import easel.config.EaselConfigHelper;

/**
 * Config enums handling booleans should implement this interface.
 * @see EaselConfigHelper
 */
public interface ConfigBooleanEnum {
    boolean getDefault();
    int ordinal();
}
