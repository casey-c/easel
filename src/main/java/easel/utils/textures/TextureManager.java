package easel.utils.textures;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import easel.Easel;

public class TextureManager {

    public static void loadTextures() {
        for (TextureDatabase t : TextureDatabase.values())
            t.load();

        for (TextureAtlasDatabase a : TextureAtlasDatabase.values())
            a.load();

        Easel.logger.info("TextureManager: loaded " + TextureDatabase.values().length + " textures.");
        Easel.logger.info("TextureManager: loaded " + TextureAtlasDatabase.values().length + " texture atlases.");
    }

    public static Texture getTexture(TextureDatabase texture) {
        return texture.getTexture();
    }

    public static TextureAtlas getTextureAtlas(TextureAtlasDatabase textureAtlas) {
        return textureAtlas.getAtlas();
    }
}
