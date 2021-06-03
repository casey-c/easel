package easel.utils;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import easel.Easel;
import easel.utils.colors.EaselColors;

public class EaselFonts {
    /**
     * A slightly smaller version of {@link com.megacrit.cardcrawl.helpers.FontHelper#tipBodyFont}.
     */
    public static BitmapFont SMALLER_TIP_BODY;

    /**
     * A medium sized italic font.
     */
    public static BitmapFont MEDIUM_ITALIC;

    /**
     * Initializes the fonts. Automatically called by the Easel API at game boot.
     */
    public static void loadFonts() {
        // Setup all fonts
        SMALLER_TIP_BODY = new BitmapFontBuilder()
                .withSize(18)
                .build();

        MEDIUM_ITALIC = new BitmapFontBuilder()
                .withSize(18)
                .withItalic()
                .withShadow(EaselColors.ONE_TENTH_TRANSPARENT_BLACK)
                .build();

        Easel.logger.info("Initialized " + 2 + " extra font(s).");
    }
}
