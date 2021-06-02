package easel.utils.textures;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public enum TextureAtlasDatabase {
    SMALL_HEADERED_TOOL_TIP("easel/textures/headeredToolTips/SmallHeaderedToolTip.atlas"),
    LARGE_HEADERED_TOOL_TIP("easel/textures/headeredToolTips/LargeHeaderedToolTip.atlas"),
    NO_HEADERED_TOOL_TIP("easel/textures/headeredToolTips/NoHeaderedToolTip.atlas")
    ;

    private final String internalPath;
    private TextureAtlas atlas;

    /**
     * @param internalPath internal path to an .atlas file (texture PNG itself should be in the same spot)
     */
    TextureAtlasDatabase(String internalPath) {
        this.internalPath = internalPath;
    }

    protected void load() {
        this.atlas = new TextureAtlas(internalPath);
    }

    protected TextureAtlas getAtlas() {
        return atlas;
    }
}
