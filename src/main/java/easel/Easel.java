package easel;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import easel.utils.EaselFonts;
import easel.utils.textures.TextureAtlasDatabase;
import easel.utils.textures.TextureDatabase;
import easel.utils.textures.TextureLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpireInitializer
public class Easel implements PostInitializeSubscriber {
    public static final Logger logger = LogManager.getLogger(Easel.class);

    public static void initialize() {
        new Easel();
    }

    public Easel() {
        BaseMod.subscribe(this);
    }

    @Override
    public void receivePostInitialize() {
        TextureLoader.loadTextures(TextureDatabase.values());
        Easel.logger.info("TextureManager: loaded " + TextureDatabase.values().length + " textures.");

        TextureLoader.loadTextureAtlases(TextureAtlasDatabase.values());
        Easel.logger.info("TextureManager: loaded " + TextureAtlasDatabase.values().length + " texture atlases.");

        EaselFonts.loadFonts();
    }
}