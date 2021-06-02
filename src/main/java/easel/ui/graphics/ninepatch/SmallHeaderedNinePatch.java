package easel.ui.graphics.ninepatch;

import easel.utils.textures.TextureAtlasDatabase;
import easel.utils.textures.TextureManager;

public class SmallHeaderedNinePatch extends HeaderedNinePatch<SmallHeaderedNinePatch> {
    public SmallHeaderedNinePatch(float width, float height) {
        super(width,
                height,
                64,
                64,
                80,
                64,
                TextureManager.getTextureAtlas(TextureAtlasDatabase.SMALL_HEADERED_TOOL_TIP));
    }
}
