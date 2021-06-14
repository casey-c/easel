package easel.config;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import easel.config.enums.ConfigBooleanEnum;
import easel.config.enums.ConfigIntegerEnum;
import easel.config.enums.ConfigStringEnum;
import easel.ui.containers.MoveContainer;

import java.io.IOException;
import java.util.HashMap;

/**
 * <p>
 * EXPERIMENTAL / UNSTABLE (API subject to change until easel 1.0.0)
 * </p>
 * <p>
 * An alternative system for managing config settings. It's mostly designed for more complicated systems where the amount of config options isn't known up front but may increase rapidly throughout development. Virtually all the effort for using this system is front-loaded, and adding new config options later is as simple as adding a row to an enum. With this system, there is no need to manually produce getter or setters for each config item, write (de)serialization code, etc. - it's all abstracted away through customizable enumerations. It has the added benefit of being partially type safe: allowing suggestions, code completion, and seeing fallback default values directly through your IDE. The backend of this config helper relies on hash maps to store and manage the data: getting a config option is O(1), while setting a config option to a different value is slightly more expensive as it will automatically save the entire group of options to disk when things change. The convenience of these benefits do not come free: there are some significant upfront development costs and limitations to this approach.
 * </p>
 * <p>
 * This system is NOT intended for simple or smaller use cases - if you only have a few config options, it is recommended that you do not follow this pattern as the boilerplate is substantial. The limitations of Java generics mixed with the verbosity required to make it work tend to make this method a bit too over-engineered for the vast majority of projects; however, larger projects with more complicated config reliance (e.g. the FilterTheSpire mod) can reap substantial benefits from using this setup.
 * </p>
 * <p>
 * To use this ConfigHelper, you need to first construct an enum for a particular type (that is: a Java type, such as a boolean or a String); all config options that share this Java type will be added inside this enum as you create them. E.g., all of your config options that determine a boolean value will be stored in a boolean specific enum. These enums are special, and should implement one of the Config(TYPE_HERE)Enum interfaces (i.e. an enum storing booleans should implement the {@link ConfigBooleanEnum} interface, an enum storing integers should implement the {@link ConfigIntegerEnum} interface, and an enum storing strings should implement the {@link ConfigStringEnum} interface. The string interface can be used to store much more complex serialization due to the ease of using a library like Gson to convert java classes into JSON formatted strings. For example, the {@link easel.ui.containers.MoveContainer} can {@link MoveContainer#toJsonString()} and {@link MoveContainer#loadFromJsonString(String)} to/from a string that can then be managed by this config. There are examples showing how to implement the enum types included in the {@link easel.config.samples} package, e.g. {@link easel.config.samples.SampleBooleanChoices}, which you can find browsing the source code for Easel on Github.
 * </p>
 * <p>
 * The constants in these enums are laid out with the following pattern: OPTION_NAME(defaultValue), where defaultValue is what will get loaded the first time the mod is installed, or as a fallback if the mod cannot save/load config items for some reason (e.g. unable to save/load to/from disk due to the filesystem being READ-ONLY, out of space, etc.). By extending these interfaces, you are forced into making a <code>getDefault()</code> function for the ConfigHelper to hook into.
 * </p>
 * <p>
 * Once you have an enum for your options, you can proceed to construct the actual ConfigHelper using one of the included static factory methods. Here's an example where we use one of the sample implementations offered for boolean values ({@link easel.config.samples.SampleBooleanChoices}), which has two slots in its enum: a {@link easel.config.samples.SampleBooleanChoices#BOOL_ONE} that defaults to true, and {@link easel.config.samples.SampleBooleanChoices#BOOL_TWO} which defaults to false. We want to make sure our ConfigHelper can automatically setup all of these options from this {@link easel.config.samples.SampleBooleanChoices} class, so we construct it from the factory helper that takes from booleans only. Because the actual type of our configHelper variable is going to rely on generics (and essentially become unreadably messy), it's best to let your IDE figure out the type automatically for you. Using IntelliJ-IDEA, you can type the following line as is, and it will alert you that it "cannot resolve symbol" with some scary red text / squiggles on the configHelper variable itself (since it hasn't been declared yet). If you mouse over it and hit ALT+ENTER, you'll be able to select the option "create local variable" and have it figure out the type.
 * </p>
 * <pre>
 * {@code
 * configHelper = ConfigHelper.fromBooleansOnly(SampleBooleanChoices.class);
 * }
 * </pre>
 * <p>
 * Letting it figure out the type allows it to fill out the entire line:
 * </p>
 * <pre>
 * {@code
 * ConfigHelper<SampleBooleanChoices, ConfigIntegerEnum, ConfigStringEnum> configHelper = ConfigHelper.fromBooleansOnly(SampleBooleanChoices.class);
 * }
 * </pre>
 * <p>
 * Note that any of the pieces we did not set with a particular enum are given the placeholder types of the base interfaces, e.g. {@link ConfigIntegerEnum}. The argument(s) to these various static factory methods require you to take your particular enum and take its <code>.class</code> in order for the compiler to construct things nicely. Now, let's examine how to use this config helper to manage these config options.
 * </p>
 * <p>
 * To access a particular config option, we'll pass in an option from our enum into one of the getter functions:
 * </p>
 * <pre>
 * {@code
 * boolean booleanOne = configHelper.getBoolean(SampleBooleanChoices.BOOL_ONE); // returns true, since that was the default value for BOOL_ONE
 * }
 * </pre>
 * <p>
 * Note that we get some type safety here - we're only allowed to pass in values from our enum which lists out all the possible config options we've defined, and the IDE / compiler can let us know if we screw something up. However, we're not fully protected here due to a limitation of this particular design: the other enum types were never initialized passed the placeholders ({@link ConfigIntegerEnum} etc.) and thus this particular ConfigHelper should ONLY be used for booleans (since that's all we chose in the factory method). Since IntelliJ-IDEA will suggest that "FOO" in the following example should be of type {@link ConfigIntegerEnum}, you may think you're safe because that placeholder doesn't have any options to choose from (and the compiler will prevent you from picking anything):
 * </p>
 * <pre>
 * {@code
 * int intOne = configHelper.getInt(FOO);
 * }
 * </pre>
 * <p>
 * However, this isn't fully type safe, as you can pass in an option from something that DOES extend the {@link ConfigIntegerEnum} interface and the compiler will be totally fine with it, even though we never set up the ConfigHelper to refer to it as being a valid source! Thus, this will compile:
 * </p>
 * <pre>
 * {@code
 * int intOne = configHelper.getInt(SampleIntegerChoices.INT_ONE);
 * }
 * </pre>
 * <p>
 * ... but will result in a runtime crash due to an attempt to access a position in an array that hasn't been initialized. So to re-iterate: only use the ConfigHelper's getters and setters for the enum types you've actually setup when you constructed the ConfigHelper with the factory method, or you will crash. This limitation is why the ConfigHelper is only "partially" type-safe.
 * </p>
 * <p>
 * Setting a value is similar to getting one:
 * </p>
 * <pre>
 * {@code
 * configHelper.setBoolean(SampleBooleanChoices.BOOL_ONE, false);
 * }
 * </pre>
 * <p>
 * As stated previously, as your project evolves and begins to require more config options, all you need to do is to go into your custom enums and add rows to them. Since the config helper relies on maps in the backend, you'll be able to easily add new options, stop referring to old options, and update your config cleanly as your project grows and your needs evolve. Should your config change dramatically enough between public releases, there are even helper functions like {@link #resetAllToDefaults(Class, Class, Class)} which can automatically clean up previously saved data and remove everything but the currently used options.
 * </p>
 * <p>
 * For general design suggestions: you'd typically want to make your ConfigHelper a public static from inside your main class, and build it using the factory methods within a "postInitialize" subscription hook inside BaseMod. The factory methods will automatically attempt to load previously saved data from the disk (and then using the setters later will update and save the files as needed). With this pattern, other classes that need access to config options can just call <code>MyMod.configHelper.getBoolean(...)</code> etc. as they see fit. As a fair warning: this config helper is not thread safe so if you're trying to set config options from various threads you may run into race conditions.
 * </p>
 * <p>
 * The place this config gets saved to / loaded from is defined by the <code>modName</code> and <code>configName</code> options used by the factory methods: the <code>modName</code> should uniquely identify your mod (e.g. the id from your ModTheSpire.json), and the <code>configName</code> will define this particular set of config values. You may find it convenient to use multiple <code>configName</code> configs for a single mod (i.e. one <code>modName</code> for all, but multiple different ConfigHelpers each with a different <code>configName</code>), but usually a singular config is enough.
 * </p>
 * @param <B> the type of the enum storing boolean options (defaults to {@link ConfigBooleanEnum} if not specified as an implementer of that interface)
 * @param <I> the type of the enum storing integer options (defaults to {@link ConfigIntegerEnum} if not specified as an implementer of that interface)
 * @param <S> the type of the enum storing string options (defaults to {@link ConfigStringEnum} if not specified as an implementer of that interface)
 */
public class EaselConfigHelper<B extends ConfigBooleanEnum, I extends ConfigIntegerEnum, S extends ConfigStringEnum> {
    @SerializedName("booleans")
    private HashMap<String, Boolean> booleanMap = new HashMap<>();

    @SerializedName("integers")
    private HashMap<String, Integer> integerMap = new HashMap<>();

    @SerializedName("strings")
    private HashMap<String, String> stringMap = new HashMap<>();

    private final String modName;
    private final String configName;

    // --------------------------------------------------------------------------------

    private EaselConfigHelper(String modName, String configName, Class<? extends B> booleans, Class<? extends I> integers, Class<? extends S> strings) {
        initializeBooleans(booleans);
        initializeIntegers(integers);
        initializeStrings(strings);

        this.modName = modName;
        this.configName = configName;

        load();
    }

    // Convenience functions from here on down

    // BOOLEANS

    /**
     * Factory method to build from a boolean enum only. Will automatically attempt to load existing values from disk during creation. See {@link EaselConfigHelper} for more details about this class.
     * @param modName the unique name of your mod (e.g. from your ModTheSpire.json's modid)
     * @param configName the name of this particular config (you can have multiple config helpers per mod, each with a unique configName)
     * @param booleanClz the class of your enum which extends {@link ConfigBooleanEnum}
     * @param <B> the type of your enum
     * @return a usable config helper
     */
    public static <B extends ConfigBooleanEnum>
    EaselConfigHelper<B, ConfigIntegerEnum, ConfigStringEnum>
    fromBooleansOnly(String modName, String configName, Class<? extends B> booleanClz) {
        return new EaselConfigHelper<>(modName, configName, booleanClz, ConfigIntegerEnum.class, ConfigStringEnum.class);
    }

    // INTEGERS
    /**
     * Factory method to build from an integer enum only. Will automatically attempt to load existing values from disk during creation. See {@link EaselConfigHelper} for more details about this class.
     * @param modName the unique name of your mod (e.g. from your ModTheSpire.json's modid)
     * @param configName the name of this particular config (you can have multiple config helpers per mod, each with a unique configName)
     * @param integerClz the class of your enum which extends {@link ConfigIntegerEnum}
     * @param <I> the type of your enum
     * @return a usable config helper
     */
    public static <I extends ConfigIntegerEnum>
    EaselConfigHelper<ConfigBooleanEnum, I, ConfigStringEnum>
    fromIntegersOnly(String modName, String configName, Class<? extends I> integerClz) {
        return new EaselConfigHelper<>(modName, configName, ConfigBooleanEnum.class, integerClz, ConfigStringEnum.class);
    }

    // STRINGS
    /**
     * Factory method to build from a string enum only. Will automatically attempt to load existing values from disk during creation. See {@link EaselConfigHelper} for more details about this class.
     * @param modName the unique name of your mod (e.g. from your ModTheSpire.json's modid)
     * @param configName the name of this particular config (you can have multiple config helpers per mod, each with a unique configName)
     * @param stringClz the class of your enum which extends {@link ConfigStringEnum}
     * @param <S> the type of your enum
     * @return a usable config helper
     */
    public static <S extends ConfigStringEnum>
    EaselConfigHelper<ConfigBooleanEnum, ConfigIntegerEnum, S>
    fromStringsOnly(String modName, String configName, Class<? extends S> stringClz) {
        return new EaselConfigHelper<>(modName, configName, ConfigBooleanEnum.class, ConfigIntegerEnum.class, stringClz);
    }

    // BOOLEANS, INTEGERS
    /**
     * Factory method to build from a boolean enum and an integer enum. Will automatically attempt to load existing values from disk during creation. See {@link EaselConfigHelper} for more details about this class.
     * @param modName the unique name of your mod (e.g. from your ModTheSpire.json's modid)
     * @param configName the name of this particular config (you can have multiple config helpers per mod, each with a unique configName)
     * @param booleanClz the class of your boolean enum which extends {@link ConfigBooleanEnum}
     * @param integerClz the class of your integer enum which extends {@link ConfigIntegerEnum}
     * @param <B> the type of your boolean enum
     * @param <I> the type of your integer enum
     * @return a usable config helper
     */
    public static <B extends ConfigBooleanEnum, I extends ConfigIntegerEnum>
    EaselConfigHelper<B, I, ConfigStringEnum>
    fromBooleansIntegers(String modName, String configName, Class<? extends B> booleanClz, Class<? extends I> integerClz) {
        return new EaselConfigHelper<>(modName, configName, booleanClz, integerClz, ConfigStringEnum.class);
    }

    // BOOLEANS, STRINGS
    /**
     * Factory method to build from a boolean enum and a string enum. Will automatically attempt to load existing values from disk during creation. See {@link EaselConfigHelper} for more details about this class.
     * @param modName the unique name of your mod (e.g. from your ModTheSpire.json's modid)
     * @param configName the name of this particular config (you can have multiple config helpers per mod, each with a unique configName)
     * @param booleanClz the class of your boolean enum which extends {@link ConfigBooleanEnum}
     * @param stringClz the class of your string enum which extends {@link ConfigStringEnum}
     * @param <B> the type of your boolean enum
     * @param <S> the type of your string enum
     * @return a usable config helper
     */
    public static <B extends ConfigBooleanEnum, S extends ConfigStringEnum>
    EaselConfigHelper<B, ConfigIntegerEnum, S>
    fromBooleansStrings(String modName, String configName, Class<? extends B> booleanClz, Class<? extends S> stringClz) {
        return new EaselConfigHelper<>(modName, configName, booleanClz, ConfigIntegerEnum.class, stringClz);
    }

    // INTEGERS, STRINGS
    /**
     * Factory method to build from a boolean enum and a string enum. Will automatically attempt to load existing values from disk during creation. See {@link EaselConfigHelper} for more details about this class.
     * @param modName the unique name of your mod (e.g. from your ModTheSpire.json's modid)
     * @param configName the name of this particular config (you can have multiple config helpers per mod, each with a unique configName)
     * @param integerClz the class of your integer enum which extends {@link ConfigIntegerEnum}
     * @param stringClz the class of your string enum which extends {@link ConfigStringEnum}
     * @param <I> the type of your integer enum
     * @param <S> the type of your string enum
     * @return a usable config helper
     */
    public static <I extends ConfigIntegerEnum, S extends ConfigStringEnum>
    EaselConfigHelper<ConfigBooleanEnum, I, S>
    fromIntegersStrings(String modName, String configName, Class<? extends I> integerClz, Class<? extends S> stringClz) {
        return new EaselConfigHelper<>(modName, configName, ConfigBooleanEnum.class, integerClz, stringClz);
    }

    // BOOLEANS, INTEGERS, STRINGS
    /**
     * Factory method to build from a boolean enum, an integer enum, and a string enum. Will automatically attempt to load existing values from disk during creation. See {@link EaselConfigHelper} for more details about this class.
     * @param modName the unique name of your mod (e.g. from your ModTheSpire.json's modid)
     * @param configName the name of this particular config (you can have multiple config helpers per mod, each with a unique configName)
     * @param booleanClz the class of your boolean enum which extends {@link ConfigBooleanEnum}
     * @param integerClz the class of your integer enum which extends {@link ConfigIntegerEnum}
     * @param stringClz the class of your string enum which extends {@link ConfigStringEnum}
     * @param <B> the type of your boolean enum
     * @param <I> the type of your integer enum
     * @param <S> the type of your string enum
     * @return a usable config helper
     */
    public static <B extends ConfigBooleanEnum, I extends ConfigIntegerEnum, S extends ConfigStringEnum>
    EaselConfigHelper<B, I, S>
    fromBooleansIntegersStrings(String modName, String configName, Class<? extends B> booleanClz, Class<? extends I> integerClz, Class<? extends S> stringClz) {
        return new EaselConfigHelper<>(modName, configName, booleanClz, integerClz, stringClz);
    }

    // --------------------------------------------------------------------------------

    private void initializeBooleans(Class<? extends B> booleans) {
        booleanMap = new HashMap<>();
        if (booleans.getEnumConstants() != null) {
            for (B choice : booleans.getEnumConstants())
                booleanMap.put(choice.toString(), choice.getDefault());
        }
    }

    private void initializeIntegers(Class<? extends I> integers) {
        integerMap = new HashMap<>();
        if (integers.getEnumConstants() != null) {
            for (I choice : integers.getEnumConstants())
                integerMap.put(choice.toString(), choice.getDefault());
        }
    }

    private void initializeStrings(Class<? extends S> strings) {
        stringMap = new HashMap<>();

        if (strings.getEnumConstants() != null) {
            for (S choice : strings.getEnumConstants())
                stringMap.put(choice.toString(), choice.getDefault());
        }
    }

    // --------------------------------------------------------------------------------

    /**
     * @param choice a choice in the boolean enum
     * @return the current value of this particular choice or the default value if it doesn't exist yet
     */
    public boolean getBoolean(B choice) {
        return booleanMap.getOrDefault(choice.toString(), choice.getDefault());
    }

    /**
     * Set the given boolean option to this value. Automatically attempts to save if the value is different than the current one, and then returns true if the save was successful.
     * @param choice a choice in the boolean enum
     * @param value the new value of this particular choice
     * @return true if the value is new and the config was successfully saved; false if the value existed already or if something went wrong when saving (there will be a stack trace if IO failed)
     */
    public boolean setBoolean(B choice, boolean value) {
        Boolean existing = booleanMap.get(choice.toString());

        if (existing == null || !existing.equals(value)) {
            booleanMap.put(choice.toString(), value);
            return save();
        }
        else
            return false;
    }

    // --------------------------------------------------------------------------------

    /**
     * @param choice a choice in the integer enum
     * @return the current value of this particular choice or the default value if it doesn't exist yet
     */
    public int getInt(I choice) {
        return integerMap.getOrDefault(choice.toString(), choice.getDefault());
    }

    /**
     * Set the given int option to this value. Automatically attempts to save if the value is different than the current one, and then returns true if the save was successful.
     * @param choice a choice in the integer enum
     * @param value the new value of this particular choice
     * @return true if the value is new and the config was successfully saved; false if the value existed already or if something went wrong when saving (there will be a stack trace if IO failed)
     */
    public boolean setInt(I choice, int value) {
        Integer existing = integerMap.get(choice.toString());

        if (existing == null || !existing.equals(value)) {
            integerMap.put(choice.toString(), value);
            return save();
        }
        else
            return false;
    }

    // --------------------------------------------------------------------------------

    /**
     * @param choice a choice in the string enum
     * @return the current value of this particular choice or the default value if it doesn't exist yet
     */
    public String getString(S choice) {
        return stringMap.getOrDefault(choice.toString(), choice.getDefault());
    }

    /**
     * Set the given string option to this value. Automatically attempts to save if the value is different than the current one, and then returns true if the save was successful.
     * @param choice a choice in the string enum
     * @param value the new value of this particular choice
     * @return true if the value is new and the config was successfully saved; false if the value existed already or if something went wrong when saving (there will be a stack trace if IO failed)
     */
    public boolean setString(S choice, String value) {
        String existing = stringMap.get(choice.toString());

        if (existing == null || !existing.equals(value)) {
            stringMap.put(choice.toString(), value);
            return save();
        }
        else
            return false;
    }

    // --------------------------------------------------------------------------------

    /**
     * A version of {@link #setBoolean(ConfigBooleanEnum, boolean)} which does NOT automatically save the config. You can use this variant to set multiple options in bulk before saving, instead of saving after each individual change. This pattern can be used to improve performance in some cases. Remember to call {@link #save()} later in order for these set values to persist.
     * @param choice a choice in the boolean enum
     * @param value the new value of this particular choice
     * @return true if the new value is different than the one previously stored by the map
     */
    public boolean setBooleanWithoutSaving(B choice, boolean value) {
        Boolean existing = booleanMap.get(choice.toString());

        if (existing == null || !existing.equals(value)) {
            booleanMap.put(choice.toString(), value);
            return true;
        }
        else
            return false;
    }

    /**
     * A version of {@link #setInt(ConfigIntegerEnum, int)} which does NOT automatically save the config. You can use this variant to set multiple options in bulk before saving, instead of saving after each individual change. This pattern can be used to improve performance in some cases. Remember to call {@link #save()} later in order for these set values to persist.
     * @param choice a choice in the integer enum
     * @param value the new value of this particular choice
     * @return true if the new value is different than the one previously stored by the map
     */
    public boolean setIntWithoutSaving(I choice, int value) {
        Integer existing = integerMap.get(choice.toString());

        if (existing == null || !existing.equals(value)) {
            integerMap.put(choice.toString(), value);
            return true;
        }
        else
            return false;
    }

    /**
     * A version of {@link #setString(ConfigStringEnum, String)} which does NOT automatically save the config. You can use this variant to set multiple options in bulk before saving, instead of saving after each individual change. This pattern can be used to improve performance in some cases. Remember to call {@link #save()} later in order for these set values to persist.
     * @param choice a choice in the string enum
     * @param value the new value of this particular choice
     * @return true if the new value is different than the one previously stored by the map
     */
    public boolean setStringWithoutSaving(S choice, String value) {
        String existing = stringMap.get(choice.toString());

        if (existing == null || !existing.equals(value)) {
            stringMap.put(choice.toString(), value);
            return true;
        }
        else
            return false;
    }

    // --------------------------------------------------------------------------------

//    public JsonObject toJson() {
//        return new Gson().toJsonTree(this).getAsJsonObject();
//    }
//
//    public void loadFromJson(JsonObject obj) {
//        EaselConfigHelper other = new Gson().fromJson(obj, EaselConfigHelper.class);
//
//        this.booleanOptions = other.booleanOptions;
//        this.integerOptions = other.integerOptions;
//        this.stringOptions = other.stringOptions;
//    }

    private void loadFromJsonString(String jsonString) {
        EaselConfigHelper other = new Gson().fromJson(jsonString, EaselConfigHelper.class);

        if (other.booleanMap != null)
            this.booleanMap = other.booleanMap;
        else
            this.booleanMap.clear();

        if (other.integerMap != null)
            this.integerMap = other.integerMap;
        else
            this.integerMap.clear();

        if (other.stringMap != null)
            this.stringMap = other.stringMap;
        else
            this.stringMap.clear();
    }

    /**
     * Serializes the entire config (and all current values) as a JSON string.
     * @return json formatted string auto-generated by Gson
     */
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    // --------------------------------------------------------------------------------

    /**
     * Attempt to save this config to disk. Not needed if you use the regular setters (as the saving will occur automatically). Only necessary to call this after using something like {@link #setBooleanWithoutSaving(ConfigBooleanEnum, boolean)}, which are intended to let you set the values in bulk and save only at a designated time.
     * @return true if the save is successful; if it fails and returns false, there should be an IOException stack trace
     */
    public boolean save() {
        try {
            SpireConfig spireConfig = new SpireConfig(modName, configName);
            spireConfig.setString("json", toString());
            spireConfig.save();
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Attempt to load in this config from disk. Called automatically by the factory methods and almost never needed to be called manually. Uses the <code>modName</code> and <code>configName</code> set by the factory methods to identify which file to load.
     * @return true if was able to find and successfully load the file
     */
    public boolean load() {
        try {
            SpireConfig spireConfig = new SpireConfig(modName, configName);
            spireConfig.load();

            if (spireConfig.has("json")) {
                loadFromJsonString(spireConfig.getString("json"));
                return true;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            // No config yet?
        }

        return false;
    }

    // --------------------------------------------------------------------------------

    /**
     * Rebuilds the entire boolean storage structure and re-initializes them to their default values. Attempts to save the new settings afterwards.
     * @param booleanClz the enum class for the boolean options
     * @return true if the save is completed successfully
     */
    public boolean resetBooleansToDefaults(Class<B> booleanClz) {
        initializeBooleans(booleanClz);
        return save();
    }

    /**
     * Rebuilds the entire integer storage structure and re-initializes them to their default values. Attempts to save the new settings afterwards.
     * @param integerClz the enum class for the integer options
     * @return true if the save is completed successfully
     */
    public boolean resetIntegersToDefaults(Class<I> integerClz) {
        initializeIntegers(integerClz);
        return save();
    }

    /**
     * Rebuilds the entire string storage structure and re-initializes them to their default values. Attempts to save the new settings afterwards.
     * @param stringClz the enum class for the string options
     * @return true if the save is completed successfully
     */
    public boolean resetStringsToDefaults(Class<S> stringClz) {
        initializeStrings(stringClz);
        return save();
    }

    /**
     * Convenience: calls {@link #resetBooleansToDefaults(Class)}, {@link #resetIntegersToDefaults(Class)}, and {@link #resetStringsToDefaults(Class)} all at once to completely rebuild the entire config structure.
     * @param booleanClz the enum class for the boolean options
     * @param integerClz the enum class for the integer options
     * @param stringClz the enum class for the string options
     * @return true if the save is completed successfully
     */
    public boolean resetAllToDefaults(Class<B> booleanClz, Class<I> integerClz, Class<S> stringClz) {
        boolean hasSaved = false;
        hasSaved = hasSaved || resetBooleansToDefaults(booleanClz);
        hasSaved = hasSaved || resetIntegersToDefaults(integerClz);
        hasSaved = hasSaved || resetStringsToDefaults(stringClz);
        return hasSaved;
    }

}
