package easel.config.enums;

import easel.config.EaselConfigHelper;

/**
 * Config enums handling integers should implement this interface.
 * @see EaselConfigHelper
 */
public interface ConfigIntegerEnum {
    int getDefault();
    int ordinal();
}
