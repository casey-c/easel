package easel.ui.graphics.ninepatch.headered;

import easel.utils.textures.TextureAtlasDatabase;

public class LargeHeaderedNinePatch extends HeaderedNinePatch<LargeHeaderedNinePatch> {
    private static final int HEADER_HEIGHT = 100;

    public LargeHeaderedNinePatch(float width, float height) {
        super(width,
                height,
                64,
                64,
                HEADER_HEIGHT,
                64,
                TextureAtlasDatabase.LARGE_HEADERED_TOOL_TIP.getAtlas()
        );
    }

    @Override public int getHeaderHeight() { return HEADER_HEIGHT; }
}
