package easel.ui.graphics.ninepatch.headered;

import easel.utils.textures.TextureAtlasDatabase;
import easel.utils.textures.TextureManager;

public class NoHeaderedNinePatch extends HeaderedNinePatch<NoHeaderedNinePatch> {
    private static final int HEADER_HEIGHT = 0;

    public NoHeaderedNinePatch(float width, float height) {
        super(width,
                height,
                64,
                64,
                64,
                64,
                TextureManager.getTextureAtlas(TextureAtlasDatabase.NO_HEADERED_TOOL_TIP));
    }

    @Override public int getHeaderHeight() { return HEADER_HEIGHT; }
}
