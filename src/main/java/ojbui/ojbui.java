package ojbui;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import ojbui.ui.AbstractWidget;
import ojbui.ui.layouts.GridLayout;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpireInitializer
public class ojbui implements PostInitializeSubscriber {
    public static final Logger logger = LogManager.getLogger(ojbui.class);

    public static void initialize() {
        new ojbui();
    }

    public ojbui() {
        BaseMod.subscribe(this);
    }

    @Override
    public void receivePostInitialize() {
        logger.info("Hello, world");

        GridLayout grid = new GridLayout<>().withAll();

    }
}