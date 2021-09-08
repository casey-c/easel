package easel.utils.textures;

/**
 * Loads up a texture database (or texture atlas database) enum to allow the textures to be used in game. The
 * {@link #loadTextures(Enum[])} function must be called before using any texture. This should be done in your
 * postInitialize hook. See the documentation on {@link #loadTextures(Enum[])} for a bit more background on how to
 * use this system. You'll need a implementation similar to {@link TextureDatabase} to use this in your own code
 * . Using a more complex system like this enum-based texture loader is not recommended for small projects!
 */
public class TextureLoader {

//    public static void loadTextures() {
//        for (TextureDatabase t : TextureDatabase.values())
//            t.load();
//
//        for (TextureAtlasDatabase a : TextureAtlasDatabase.values())
//            a.load();
//
//        Easel.logger.info("TextureManager: loaded " + TextureDatabase.values().length + " textures.");
//        Easel.logger.info("TextureManager: loaded " + TextureAtlasDatabase.values().length + " texture atlases.");
//    }

    /**
     * <p>
     * Use this to initialize your custom texture enums once the game loads in your mod's postInitialize hook. This
     * approach is NOT recommended unless your mod has scaled to the point where the number of textures you're
     * handling has become unreasonable without a more formal system.
     * </p>
     *
     * <p>
     * In most common situations, you won't find any benefits to using this enum centric approach over something like
     * manually managing your textures in a static accessor. This {@link TextureLoader} system instead relies on you
     * creating a custom enum which implements {@link ITextureDatabaseEnum} and calling this loadAdditionalTextures
     * function in your postInitialize hook using your enum's <code>.values()</code> method as the input.
     * </p>
     *
     * <p>
     * Then, using your specific textures later becomes as simple as calling YourEnum.NAME_OF_THE_TEXTURE_YOU_WANT
     * .getTexture() whenever you need it. The primary benefit of this approach (why it was used in the backend
     * of Easel, and why it might be nice enough to expose access to it for your own mods) is for much of the same
     * reason as why {@link easel.config.EaselConfigHelper} is so messy and enum centric: with a little bit of
     * initial work, you can scale up adding new textures easily (just add a line into your enum) and gain a bit of
     * type safety (allowing your IDE to suggest auto-completions incredibly easily).
     * </p>
     *
     * <p>
     * If you wish to see this technique in action, please take a look at Easel's source code. Whenever a texture is
     * used by Easel, it goes through an enum that implements {@link ITextureDatabaseEnum}. Check out
     * {@link TextureDatabase} for the main enum used.
     * </p>
     * @param src pass in your enum's <code>.values()</code> here
     * @param <T> the type for your enum
     */
    public static <T extends Enum<T> & ITextureDatabaseEnum> void loadTextures(T[] src) {
        for (T x : src)
            x.load();
    }

    /**
     * Similar to {@link #loadTextures(Enum[])} but for handling TextureAtlas objects instead of just plain
     * Texture objects.
     * @param src pass in your enum's <code>.values()</code> here
     * @param <T> the type for your enum
     * @see #loadTextures(Enum[])
     */
    public static <T extends Enum<T> & ITextureAtlasDatabaseEnum> void loadTextureAtlases(T[] src) {
        for (T x : src)
            x.load();
    }
}
