package easel;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.PostUpdateSubscriber;
import basemod.interfaces.RenderSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.debug.DebugWidget;
import easel.ui.layouts.GridLayout;
import easel.ui.misc.WidgetSwapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

@SpireInitializer
public class Easel implements PostInitializeSubscriber, RenderSubscriber, PostUpdateSubscriber {
    public static final Logger logger = LogManager.getLogger(Easel.class);

    public static void initialize() {
        new Easel();
    }

    public Easel() {
        BaseMod.subscribe(this);
    }

    private ArrayList<AbstractWidget> widgets = new ArrayList<>();

    private long lastUpdateTime = 0;
    private boolean isMain = false;
    @Override
    public void receivePostUpdate() {
        long currUpdateTime = System.currentTimeMillis();
        if (currUpdateTime - lastUpdateTime > 3000) {
            lastUpdateTime = currUpdateTime;

            if (isMain)
                widget.setView(TestEnum.SECONDARY);
            else
                widget.setView(TestEnum.MAIN);

            isMain = !isMain;
        }
    }

    enum TestEnum {
        MAIN, SECONDARY
    }
    private WidgetSwapper<TestEnum> widget;

    @Override
    public void receivePostInitialize() {
        logger.info("Hello, world");

        widget = new WidgetSwapper<>(TestEnum.class)
                .withWidget(TestEnum.MAIN, new DebugWidget(100, 100, new Color(1.0f, 1.0f, 1.0f, 1.0f)))
                .withWidget(TestEnum.SECONDARY, new DebugWidget(100, 100, new Color(1.0f, 0.0f, 0.0f, 1.0f)))
                .anchorCenteredOnScreen();

        widgets.add(widget);
    }

    @Override
    public void receiveRender(SpriteBatch sb) {
        sb.setColor(Color.BLACK);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0, 0, Settings.WIDTH, Settings.HEIGHT);

        widgets.forEach(w -> w.render(sb));
    }
}