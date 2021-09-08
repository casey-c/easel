package easel.utils.textures;

import com.badlogic.gdx.graphics.Texture;

/**
 * Contains the direct textures used by Easel.
 * @see TextureAtlasDatabase
 * @see TextureLoader
 */
public enum TextureDatabase implements ITextureDatabaseEnum {
    TOOLTIP_BASE("easel/textures/np_tooltip_base.png"),
    TOOLTIP_TRIM("easel/textures/np_tooltip_trim.png"),
    BLACK_GRADIENT_VERTICAL("easel/textures/black_gradient_vertical.png")
    ;

    private final String internalPath;
    private Texture texture;

    TextureDatabase(String internalPath) {
        this.internalPath = internalPath;
    }

    public void load() {
        this.texture = new Texture(internalPath);
    }

    public Texture getTexture() {
        return texture;
    }
}
