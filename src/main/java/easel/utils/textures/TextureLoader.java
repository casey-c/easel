package easel.utils.textures;

import easel.Easel;

public class TextureLoader {

    public static void loadTextures() {
        for (TextureDatabase t : TextureDatabase.values())
            t.load();

        for (TextureAtlasDatabase a : TextureAtlasDatabase.values())
            a.load();

        Easel.logger.info("TextureManager: loaded " + TextureDatabase.values().length + " textures.");
        Easel.logger.info("TextureManager: loaded " + TextureAtlasDatabase.values().length + " texture atlases.");
    }
}
