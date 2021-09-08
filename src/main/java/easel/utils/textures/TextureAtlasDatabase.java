package easel.utils.textures;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Contains all texture atlases used by Easel.
 * @see TextureDatabase
 * @see TextureLoader
 */
public enum TextureAtlasDatabase implements ITextureAtlasDatabaseEnum {
    SMALL_HEADERED_TOOL_TIP("easel/textures/headeredToolTips/SmallHeadered.atlas"),
    LARGE_HEADERED_TOOL_TIP("easel/textures/headeredToolTips/LargeHeadered.atlas"),
    //STYLED_CONTAINER("easel/textures/container/StyledContainer.atlas"),
    STYLED_CONTAINER("easel/textures/container/StyledContainerMessy.atlas"),
    NO_HEADERED_TOOL_TIP("easel/textures/headeredToolTips/NoHeadered.atlas")
    ;

    private final String internalPath;
    private TextureAtlas atlas;

    /**
     * @param internalPath internal path to an .atlas file (texture PNG itself should be in the same spot)
     */
    TextureAtlasDatabase(String internalPath) {
        this.internalPath = internalPath;
    }

    public void load() {
        this.atlas = new TextureAtlas(internalPath);
    }

    public TextureAtlas getTextureAtlas() {
        return atlas;
    }
}
