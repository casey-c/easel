package easel.utils;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import easel.Easel;

@SpireInitializer
public class EaselFonts implements PostInitializeSubscriber {
    public static void initialize() { new EaselFonts(); }

    public EaselFonts() {
        BaseMod.subscribe(this);
    }

    /**
     * A slightly smaller version of {@link com.megacrit.cardcrawl.helpers.FontHelper#tipBodyFont}.
     */
    public static BitmapFont SMALLER_TIP_BODY;

    @Override
    public void receivePostInitialize() {
        SMALLER_TIP_BODY = new BitmapFontBuilder()
                .withSize(18)
                .build();

        Easel.logger.info("Initialized " + 1 + " extra font(s).");
    }
}
