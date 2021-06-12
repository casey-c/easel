package easel.config.enums;

/**
 * Config enums handling booleans should implement this interface.
 * @see easel.config.ConfigHelper
 */
public interface ConfigBooleanEnum {
    boolean getDefault();
    int ordinal();
}
