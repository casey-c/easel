package easel.config;

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import easel.Easel;
import easel.config.enums.ConfigBooleanEnum;
import easel.config.enums.ConfigIntegerEnum;
import easel.config.enums.ConfigStringEnum;

import java.io.IOException;

/**
 * <p>
 * EXPERIMENTAL / UNSTABLE (API subject to change until easel 1.0.0)
 * </p>
 * <p>
 * An alternative system for managing config settings. It's mostly designed for more complicated systems where the amount of config options isn't known up front but may increase rapidly throughout development. Virtually all the effort for using this system is front-loaded, and adding new config options later is as simple as adding a row to an enum. With this system, there is no need to manually produce getter or setters for each config item, write (de)serialization code, etc. - it's all abstracted away through customizable enumerations. It has the added benefit of being partially type safe: allowing suggestions, code completion, and seeing fallback default values directly through your IDE. Additionally, the computational cost of config getting/setting is O(1), which gives it some added benefit over other systems that may rely on tree or hash structures. However, these benefits do not come free: there are some significant upfront development costs and limitations to this approach.
 * </p>
 * <p>
 * This system is NOT intended for simple or smaller use cases - if you only have a few config options, it is recommended that you do not follow this pattern as the boilerplate is substantial. The limitations of Java generics mixed with the verbosity required to make it work tend to make this method a bit too over-engineered for the vast majority of projects; however, larger projects with more complicated config reliance (e.g. FilterTheSpire) can reap substantial benefits from using this setup.
 * </p>
 * <p>
 * To use this ConfigHelper, you need to first construct an enum for a particular type (that is: a Java type, such as a boolean or a String); all config options that share this Java type will be added inside this enum as you create them. E.g., all of your config options that determine a boolean value will be stored in a boolean specific enum. These enums are special, and should implement one of the Config(TYPE_HERE)Enum interfaces (i.e. an enum storing booleans should implement the {@link ConfigBooleanEnum} interface, an enum storing integers should implement the {@link ConfigIntegerEnum} interface, and an enum storing strings should implement the {@link ConfigStringEnum} interface. There are examples showing this implementation included in the {@link easel.config.samples} package, e.g. {@link easel.config.samples.SampleBooleanChoices}, which you can find browsing the source code for Easel on Github.
 * </p>
 * <p>
 * The values in these enums are laid out with the following pattern: OPTION_NAME(defaultValue), where defaultValue is what will get loaded the first time the mod is installed, or as a fallback if the mod cannot save/load config items for some reason (e.g. unable to save/load to/from disk due to the filesystem being READ-ONLY, out of space, etc.). By extending these interfaces, you are forced into making a <code>getDefault()</code> function for the ConfigHelper to hook into. Note that these interfaces include an <code>int ordinal()</code>, but this does not need to be explicitly written out. Making an enum will inherit the definition of that automatically (and since <code>ordinal()</code> is critical to the indexing scheme of the ConfigHelper, it is NOT something you should do manually and should be deleted if your IDE automatically generates it for some reason).
 * </p>
 * <p>
 * Once you have an enum for your options, you can proceed to construct the actual ConfigHelper using one of the included static factory methods. Here's an example where we use one of the sample implementations offered for boolean values ({@link easel.config.samples.SampleBooleanChoices}), which has two slots in its enum: a {@link easel.config.samples.SampleBooleanChoices#BOOL_ONE} that defaults to true, and {@link easel.config.samples.SampleBooleanChoices#BOOL_TWO} which defaults to false. We want to make sure our ConfigHelper can automatically setup all of these options from this {@link easel.config.samples.SampleBooleanChoices} class, so we construct it from the factory helper that takes from booleans only. Because the actual type of our configHelper variable is going to be generic (and extremely messy), it's best to let your IDE figure out the type automatically for you. Using IntelliJ-IDEA, you can type the following line as is, and it will alert you that it "cannot resolve symbol" with some scary red text / squiggles on the configHelper variable itself (since it hasn't been declared at all yet). If you mouse over it and hit ALT+ENTER, you'll be able to select the option "create local variable" and have it figure out the type.
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
 * Note that any of the pieces we did not set with a particular enum are given the placeholder types of the base interfaces, e.g. {@link ConfigIntegerEnum}. The argument(s) to these various static factory methods require you to take your particular enum and take its <code>.class</code> in order for the compiler to construct things nicely. Now, let's see how to use this config helper.
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
 * Note that we get some type safety here - we're only allowed to pass in values from our enum which lists out all the possible config options we've defined, and the IDE / compiler can let us know if we screw something up. However, we're not fully protected here due to a limitation of this particular design: the other enum types were never initialized passed the placeholders ({@link ConfigIntegerEnum} etc.) and thus this particular ConfigHelper should ONLY be used for booleans (since that's all we chose in the factory method). Since IntelliJ-IDEA will suggest that FOO in the following example should be of type {@link ConfigIntegerEnum}, you may think you're safe because that placeholder doesn't have any options to choose from (and the compiler will prevent you from picking anything):
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
 * As stated previously, as your project evolves and begins to require more config options, all you need to do is to go into your custom enum with the choices and add rows to it. Make sure that rows are only appended to the end (and not placed inside the middle somewhere), due to the ConfigHelper's dependence on <code>.ordinal()</code> for figuring out which element is which; if you fail to follow this pattern, a user of your mod who loads up a previously saved config into a version where the enum rows are re-arranged or come in a different order will have their choices re-arranged as well, and will no longer correspond to the same enum option they originally were created from (causing confusion). Now, this isn't a catastrophic error (it won't crash), but it is certainly not user-friendly. So just remember to append new options and not place them into the middle.
 * </p>
 * <p>
 * The ConfigHelper itself can easily serialize/deserialize its settings to be saved and loaded between game boots. See {@link #asJsonObject()} and {@link #fromJsonObject(JsonObject)} for more details.
 * </p>
 * <p>
 * For general design suggestions: you'd typically want to make your ConfigHelper a public static from inside your main class and initialize (and potentially deserialize saved previous versions) within a "postInitialize" subscription hook inside BaseMod. Then, other classes that need access to config options can just call <code>MyMod.configHelper.getBoolean(...)</code> etc. as they see fit. As a fair warning, this config helper is not thread safe, so if you're trying to set config options from various threads you may run into race conditions.
 * </p>
 * @param <B>
 * @param <I>
 * @param <S>
 */
public class EaselConfigHelper<B extends ConfigBooleanEnum, I extends ConfigIntegerEnum, S extends ConfigStringEnum> {
    @SerializedName("booleans")
    private boolean[] booleanOptions;

    @SerializedName("integers")
    private int[] integerOptions;

    @SerializedName("strings")
    private String[] stringOptions;

    private String modName;
    private String configName;

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
    public static <B extends ConfigBooleanEnum>
    EaselConfigHelper<B, ConfigIntegerEnum, ConfigStringEnum>
    fromBooleansOnly(String modName, String configName, Class<? extends B> booleanClz) {
        return new EaselConfigHelper<>(modName, configName, booleanClz, ConfigIntegerEnum.class, ConfigStringEnum.class);
    }

    // INTEGERS
    public static <I extends ConfigIntegerEnum>
    EaselConfigHelper<ConfigBooleanEnum, I, ConfigStringEnum>
    fromIntegersOnly(String modName, String configName, Class<? extends I> integerClz) {
        return new EaselConfigHelper<>(modName, configName, ConfigBooleanEnum.class, integerClz, ConfigStringEnum.class);
    }

    // STRINGS
    public static <S extends ConfigStringEnum>
    EaselConfigHelper<ConfigBooleanEnum, ConfigIntegerEnum, S>
    fromStringsOnly(String modName, String configName, Class<? extends S> stringClz) {
        return new EaselConfigHelper<>(modName, configName, ConfigBooleanEnum.class, ConfigIntegerEnum.class, stringClz);
    }

    // BOOLEANS, INTEGERS
    public static <B extends ConfigBooleanEnum, I extends ConfigIntegerEnum>
    EaselConfigHelper<B, I, ConfigStringEnum>
    fromBooleansIntegers(String modName, String configName, Class<? extends B> booleanClz, Class<? extends I> integerClz) {
        return new EaselConfigHelper<>(modName, configName, booleanClz, integerClz, ConfigStringEnum.class);
    }

    // BOOLEANS, STRINGS
    public static <B extends ConfigBooleanEnum, S extends ConfigStringEnum>
    EaselConfigHelper<B, ConfigIntegerEnum, S>
    fromBooleansStrings(String modName, String configName, Class<? extends B> booleanClz, Class<? extends S> stringClz) {
        return new EaselConfigHelper<>(modName, configName, booleanClz, ConfigIntegerEnum.class, stringClz);
    }

    // INTEGERS, STRINGS
    public static <I extends ConfigIntegerEnum, S extends ConfigStringEnum>
    EaselConfigHelper<ConfigBooleanEnum, I, S>
    fromIntegersStrings(String modName, String configName, Class<? extends I> integerClz, Class<? extends S> stringClz) {
        return new EaselConfigHelper<>(modName, configName, ConfigBooleanEnum.class, integerClz, stringClz);
    }

    // --------------------------------------------------------------------------------

    private void initializeBooleans(Class<? extends B> booleans) {
        if (booleans.getEnumConstants() != null) {
            booleanOptions = new boolean[booleans.getEnumConstants().length];

            for (int i = 0; i < booleanOptions.length; ++i)
                booleanOptions[i] = booleans.getEnumConstants()[i].getDefault();
        }
    }

    private void initializeIntegers(Class<? extends I> integers) {
        if (integers.getEnumConstants() != null) {
            integerOptions = new int[integers.getEnumConstants().length];

            for (int i = 0; i < integerOptions.length; ++i)
                integerOptions[i] = integers.getEnumConstants()[i].getDefault();
        }
    }

    private void initializeStrings(Class<? extends S> strings) {
        if (strings.getEnumConstants() != null) {
            stringOptions = new String[strings.getEnumConstants().length];

            for (int i = 0; i < stringOptions.length; ++i)
                stringOptions[i] = strings.getEnumConstants()[i].getDefault();
        }
    }

    // --------------------------------------------------------------------------------

    public boolean getBoolean(B choice) {
        if (booleanOptions == null) {
            Easel.logger.warn("WARNING: ConfigHelper#getBoolean has null boolean options; returning default value for " + choice);
            return choice.getDefault();
        } else
            return booleanOptions[choice.ordinal()];
    }

    public void setBoolean(B choice, boolean value) {
        boolean needsSaving = (booleanOptions[choice.ordinal()] != value);
        booleanOptions[choice.ordinal()] = value;

        if (needsSaving)
            save();
    }

    // --------------------------------------------------------------------------------

    public int getInt(I choice) {
        if (integerOptions == null) {
            Easel.logger.warn("WARNING: ConfigHelper#getInt has null integer options; returning default value for " + choice);
            return choice.getDefault();
        }
        else
            return integerOptions[choice.ordinal()];
    }

    public void setInt(I choice, int value) {
        boolean needsSaving = (integerOptions[choice.ordinal()] != value);

        integerOptions[choice.ordinal()] = value;

        if (needsSaving)
            save();
    }

    // --------------------------------------------------------------------------------

    public String getString(S choice) {
        if (stringOptions == null) {
            Easel.logger.warn("WARNING: ConfigHelper#getString has null string options; returning default value for " + choice);
            return choice.getDefault();
        }
        else
            return stringOptions[choice.ordinal()];
    }

    public void setString(S choice, String value) {
        boolean needsSaving = !(stringOptions[choice.ordinal()].equals(value));
        stringOptions[choice.ordinal()] = value;

        if (needsSaving)
            save();
    }

    // --------------------------------------------------------------------------------

    public JsonObject toJson() {
        return new Gson().toJsonTree(this).getAsJsonObject();
    }

    public void loadFromJson(JsonObject obj) {
        EaselConfigHelper other = new Gson().fromJson(obj, EaselConfigHelper.class);

        this.booleanOptions = other.booleanOptions;
        this.integerOptions = other.integerOptions;
        this.stringOptions = other.stringOptions;
    }

    public void loadFromJsonString(String jsonString) {
        EaselConfigHelper other = new Gson().fromJson(jsonString, EaselConfigHelper.class);

        this.booleanOptions = other.booleanOptions;
        this.integerOptions = other.integerOptions;
        this.stringOptions = other.stringOptions;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    // --------------------------------------------------------------------------------

    private void save() {
        System.out.println("EaselConfigHelper saving...");
        try {
            SpireConfig spireConfig = new SpireConfig(modName, configName);
            spireConfig.setString("json", toString());
            spireConfig.save();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void load() {
        System.out.println("EaselConfigHelper loading...");
        try {
            SpireConfig spireConfig = new SpireConfig(modName, configName);
            spireConfig.load();

            if (spireConfig.has("json")) {
                loadFromJsonString(spireConfig.getString("json"));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            // No config yet?
        }
    }

}
