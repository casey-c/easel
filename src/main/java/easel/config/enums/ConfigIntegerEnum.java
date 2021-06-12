package easel.config.enums;

/**
 * Config enums handling integers should implement this interface.
 * @see easel.config.ConfigHelper
 */
public interface ConfigIntegerEnum {
    int getDefault();
    int ordinal();
}
