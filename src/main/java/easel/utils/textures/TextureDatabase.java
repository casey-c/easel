package easel.utils.textures;

import com.badlogic.gdx.graphics.Texture;

public enum TextureDatabase {
    TOOLTIP_BASE("easel/textures/np_tooltip_base.png"),
    TOOLTIP_TRIM("easel/textures/np_tooltip_trim.png");

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
