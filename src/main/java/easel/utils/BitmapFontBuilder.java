package easel.utils;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.LocalizedStrings;

import java.util.HashMap;

// this probably isn't ok lol
import static com.megacrit.cardcrawl.ui.panels.ExhaustPanel.fontScale;

public class BitmapFontBuilder {
    // Defaults mimic tipBodyFont

    private float size = 22.0f;
    private boolean isLinearFiltering = false;
    private float gamma = 0.9f;
    private int spaceX = 0;
    private int spaceY = 0;

    private static final Color DEFAULT_BORDER_COLOR = new Color(0.67F, 0.06F, 0.22F, 1.0F);
    private Color borderColor = DEFAULT_BORDER_COLOR;

    private float borderWidth = 0.0f;
    private float borderGamma = 0.9f;

    private Color shadowColor = Settings.QUARTER_TRANSPARENT_BLACK_COLOR;
    private int shadowOffsetX = (int)(3.0f * Settings.scale);
    private int shadowOffsetY = (int)(3.0f * Settings.scale);

    private static final String KREON_REGULAR_LOCATION = "font/Kreon-Regular.ttf";
    private static final String KREON_BOLD_LOCATION = "font/Kreon-Bold.ttf";
    private static final String ITALIC_REGULAR_LOCATION = "font/ZillaSlab-RegularItalic.otf";

    private String font = KREON_REGULAR_LOCATION;

    // --------------------------------------------------------------------------------
    // modify with builders

    public BitmapFontBuilder withSize(float fontSize) {
        this.size = fontSize;
        return this;
    }

    public BitmapFontBuilder withSize(float fontSize, boolean linearFiltering) {
        this.size = fontSize;
        this.isLinearFiltering = linearFiltering;
        return this;
    }

    public BitmapFontBuilder withKreonRegular() {
        this.font = KREON_REGULAR_LOCATION;
        return this;
    }

    public BitmapFontBuilder withKreonBold() {
        this.font = KREON_BOLD_LOCATION;
        return this;
    }

    public BitmapFontBuilder withItalic() {
        this.font = ITALIC_REGULAR_LOCATION;
        return this;
    }

    /**
     * Use for large font sizes. Base game seems like it uses linear filtering for fonts of size 30 and above, so it's probably a good idea to copy that practice. To be completely honest, I don't know what this does but you can try and change it and report back your results if you understand it.
     * @param linearFiltering whether to use linear filtering
     * @return this builder
     */
    public BitmapFontBuilder withLinearFiltering(boolean linearFiltering) {
        this.isLinearFiltering = linearFiltering;
        return this;
    }

    public BitmapFontBuilder withShadow(Color shadowColor) {
        this.shadowColor = shadowColor;
        return this;
    }

    public BitmapFontBuilder withShadow(Color shadowColor, int unscaledOffsetX, int unscaledOffsetY) {
        this.shadowColor = shadowColor;
        this.shadowOffsetX = (int)(unscaledOffsetX * Settings.scale);
        this.shadowOffsetY = (int)(unscaledOffsetY * Settings.scale);
        return this;
    }

    public BitmapFontBuilder withBorder(Color borderColor, float borderWidth) {
        this.borderColor = borderColor;
        this.borderWidth = borderWidth;
        return this;
    }

    public BitmapFontBuilder withBorder(Color borderColor, float borderWidth, float borderGamma) {
        this.borderColor = borderColor;
        this.borderWidth = borderWidth;
        this.borderGamma = borderGamma;
        return this;
    }

    // --------------------------------------------------------------------------------

    /**
     * Constructs the final bitmap font after setting all the parameters with the builder methods.
     * @return the constructed font
     */
    public BitmapFont build() {
        return prepFont(size,
                isLinearFiltering,
                gamma,
                spaceX,
                spaceY,
                borderColor,
                false,
                borderWidth,
                borderGamma,
                shadowColor,
                shadowOffsetX,
                shadowOffsetY,
                font);
    }

    // --------------------------------------------------------------------------------

    private static BitmapFont prepFont(float size,
                                       boolean isLinearFiltering,
                                       float gamma,
                                       int spaceX,
                                       int spaceY,
                                       Color borderColor,
                                       boolean borderStraight,
                                       float borderWidth,
                                       float borderGamma,
                                       Color shadowColor,
                                       int shadowOffsetX,
                                       int shadowOffsetY,
                                       String font
    ) {
        FreeTypeFontGenerator g;

        HashMap<String, FreeTypeFontGenerator> generators = (HashMap<String, FreeTypeFontGenerator>) ReflectionHacks.getPrivateStatic(FontHelper.class, "generators");
        //FileHandle fontFile = (FileHandle) ReflectionHacks.getPrivateStatic(FontHelper.class, "fontFile");

        FileHandle fontFile = Gdx.files.internal(font);

        if (generators.containsKey(fontFile.path())) {
            g = (FreeTypeFontGenerator)generators.get(fontFile.path());
        } else {
            System.out.println("ERROR: this shouldn't occur!");
            // TODO: throw an exception I guess

            g = new FreeTypeFontGenerator(fontFile);
            generators.put(fontFile.path(), g);
        }

        if (Settings.BIG_TEXT_MODE) {
            size *= 1.2F;
        }

        return prepFont(g, size, isLinearFiltering, gamma, spaceX, spaceY, borderColor, borderStraight, borderWidth, borderGamma, shadowColor, shadowOffsetX, shadowOffsetY);
    }


    private static BitmapFont prepFont(FreeTypeFontGenerator g,
                                       float size,
                                       boolean isLinearFiltering,
                                       float gamma,
                                       int spaceX,
                                       int spaceY,
                                       Color borderColor,
                                       boolean borderStraight,
                                       float borderWidth,
                                       float borderGamma,
                                       Color shadowColor,
                                       int shadowOffsetX,
                                       int shadowOffsetY
    ) {
        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.characters = "";
        p.incremental = true;
        p.size = Math.round(size * fontScale * Settings.scale);

        p.gamma = gamma;
        p.spaceX = spaceX;
        p.spaceY = spaceY;

        p.borderColor = borderColor;
        p.borderStraight = borderStraight;
        p.borderWidth = borderWidth;
        p.borderGamma = borderGamma;

        p.shadowColor = shadowColor;
        p.shadowOffsetX = shadowOffsetX;
        p.shadowOffsetY = shadowOffsetY;

        if (isLinearFiltering) {
            p.minFilter = Texture.TextureFilter.Linear;
            p.magFilter = Texture.TextureFilter.Linear;
        } else {
            p.minFilter = Texture.TextureFilter.Nearest;
            p.magFilter = Texture.TextureFilter.MipMapLinearNearest;
        }

        g.scaleForPixelHeight(p.size);
        BitmapFont font = g.generateFont(p);
        font.setUseIntegerPositions(!isLinearFiltering);
        font.getData().markupEnabled = true;
        if (LocalizedStrings.break_chars != null) {
            font.getData().breakChars = LocalizedStrings.break_chars.toCharArray();
        }

        return font;
    }

}
