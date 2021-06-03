package easel.ui.graphics.ninepatch.headered;

import easel.utils.textures.TextureAtlasDatabase;

public class NoHeaderedNinePatch extends HeaderedNinePatch<NoHeaderedNinePatch> {
    private static final int HEADER_HEIGHT = 0;

    public NoHeaderedNinePatch(float width, float height) {
        super(width,
                height,
                64,
                64,
                64,
                64,
                TextureAtlasDatabase.NO_HEADERED_TOOL_TIP.getAtlas()
        );

        this.shadows = false;
    }

    @Override public int getHeaderHeight() { return HEADER_HEIGHT; }
}
