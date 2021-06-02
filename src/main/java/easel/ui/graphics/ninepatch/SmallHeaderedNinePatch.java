package easel.ui.graphics.ninepatch;

import easel.utils.textures.TextureAtlasDatabase;
import easel.utils.textures.TextureManager;

public class SmallHeaderedNinePatch extends HeaderedNinePatch<SmallHeaderedNinePatch> {
    private static final int HEADER_HEIGHT = 80;

    public SmallHeaderedNinePatch(float width, float height) {
        super(width,
                height,
                64,
                64,
                HEADER_HEIGHT,
                64,
                TextureManager.getTextureAtlas(TextureAtlasDatabase.SMALL_HEADERED_TOOL_TIP));
    }
}
