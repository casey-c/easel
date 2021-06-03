package easel.ui.graphics.ninepatch.headered;

import easel.utils.textures.TextureAtlasDatabase;

public class SmallHeaderedNinePatch extends HeaderedNinePatch<SmallHeaderedNinePatch> {
    private static final int HEADER_HEIGHT = 80;

    public SmallHeaderedNinePatch(float width, float height) {
        super(width,
                height,
                64,
                64,
                HEADER_HEIGHT,
                64,
                TextureAtlasDatabase.SMALL_HEADERED_TOOL_TIP.getAtlas()
        );
    }

    @Override public int getHeaderHeight() { return HEADER_HEIGHT; }
}
