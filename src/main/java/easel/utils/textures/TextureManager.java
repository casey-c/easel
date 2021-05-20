package easel.utils.textures;

import com.badlogic.gdx.graphics.Texture;
import easel.Easel;

public class TextureManager {

    public static void loadTextures() {
        for (TextureDatabase s : TextureDatabase.values()) {
            s.load();
        }

        Easel.logger.info("TextureManager: loaded " + TextureDatabase.values().length + " textures.");
    }

    public static Texture get(TextureDatabase texture) {
        return texture.get();
    }
}
