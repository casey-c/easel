package ojbui;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.RenderSubscriber;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import ojbui.ui.AnchorPosition;
import ojbui.ui.debug.DebugWidget;
import ojbui.ui.layouts.GridLayout;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpireInitializer
public class ojbui implements PostInitializeSubscriber, RenderSubscriber {
    public static final Logger logger = LogManager.getLogger(ojbui.class);

    public static void initialize() {
        new ojbui();
    }

    public ojbui() {
        BaseMod.subscribe(this);
    }

    private GridLayout grid;
    private DebugWidget debugWidget;

    @Override
    public void receivePostInitialize() {
        logger.info("Hello, world");

        this.grid = new GridLayout()
                //.withDefaultChildAnchorPosition(AnchorPosition.LEFT_BOTTOM)
                .withExactRows(120.0f, 120.0f)
                .withExactCols(120.0f, 120.0f)
                .withChild(new DebugWidget(50, 50), 0, 0)
                .withChild(new DebugWidget(50, 50), 0, 1, AnchorPosition.CENTER)
                .withChild(new DebugWidget(50, 50), 1, 0, AnchorPosition.LEFT_BOTTOM)
                .withChild(new DebugWidget(50, 50), 1, 1, AnchorPosition.RIGHT_TOP)
                .anchoredAt(500, 500, AnchorPosition.CENTER);

        this.debugWidget = new DebugWidget(240, 240, DebugWidget.DEBUG_COLOR_1)
                .anchoredAt(500, 500, AnchorPosition.CENTER);
    }

    @Override
    public void receiveRender(SpriteBatch sb) {
        debugWidget.render(sb);
        grid.render(sb);
    }
}