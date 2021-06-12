package easel.config.enums;

/**
 * Config enums handling Strings should implement this interface.
 * @see easel.config.ConfigHelper
 */
public interface ConfigStringEnum {
    String getDefault();
    int ordinal();
}
