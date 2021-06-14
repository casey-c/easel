package easel.config.enums;

import easel.config.EaselConfigHelper;

/**
 * Config enums handling Strings should implement this interface.
 * @see EaselConfigHelper
 */
public interface ConfigStringEnum {
    String getDefault();
    int ordinal();
}
