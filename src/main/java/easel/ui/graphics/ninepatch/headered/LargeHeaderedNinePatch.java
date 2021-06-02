package easel.ui.graphics.ninepatch.headered;

import easel.utils.textures.TextureAtlasDatabase;
import easel.utils.textures.TextureManager;

public class LargeHeaderedNinePatch extends HeaderedNinePatch<LargeHeaderedNinePatch> {
    private static final int HEADER_HEIGHT = 100;

    public LargeHeaderedNinePatch(float width, float height) {
        super(width,
                height,
                64,
                64,
                HEADER_HEIGHT,
                64,
                TextureManager.getTextureAtlas(TextureAtlasDatabase.LARGE_HEADERED_TOOL_TIP));
    }
}
