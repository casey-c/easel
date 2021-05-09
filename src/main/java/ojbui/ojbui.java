package ojbui;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.RenderSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import ojbui.ui.AbstractWidget;
import ojbui.ui.AnchorPosition;
import ojbui.ui.debug.DebugWidget;
import ojbui.ui.layouts.GridLayout;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

@SpireInitializer
public class ojbui implements PostInitializeSubscriber, RenderSubscriber {
    public static final Logger logger = LogManager.getLogger(ojbui.class);

    public static void initialize() {
        new ojbui();
    }

    public ojbui() {
        BaseMod.subscribe(this);
    }

    private ArrayList<AbstractWidget> widgets = new ArrayList<>();

    @Override
    public void receivePostInitialize() {
        logger.info("Hello, world");

        widgets.add(new GridLayout()
                .withDefaultChildAnchorPosition(AnchorPosition.CENTER)
                .withExactCols(120.0f, 120.0f)
                .withRelativeRows(240.0f, 1f, 1f)
                .withNEvenlySizedCols(240.0f, 2)
                .withChild(0, 0, new DebugWidget())
                .withChild(0, 1, new DebugWidget())
                .withChild(1, 0, new DebugWidget())
                .withChild(1, 1, new DebugWidget())
                .anchorCenteredOnScreen()
        );

        widgets.add(new GridLayout()
                .withNEvenlySizedRows(500.0f, 3)
                .withNEvenlySizedCols(500.0f, 3)
                .withChild(0, 0, new DebugWidget(), AnchorPosition.LEFT_TOP)
                .withChild(0, 1, new DebugWidget(), AnchorPosition.CENTER_TOP)
                .withChild(0, 2, new DebugWidget(), AnchorPosition.RIGHT_TOP)
                .withChild(1, 0, new DebugWidget(), AnchorPosition.LEFT_CENTER)
                .withChild(1, 1, new DebugWidget(), AnchorPosition.CENTER)
                .withChild(1, 2, new DebugWidget(), AnchorPosition.RIGHT_CENTER)
                .withChild(2, 0, new DebugWidget(), AnchorPosition.LEFT_BOTTOM)
                .withChild(2, 1, new DebugWidget(), AnchorPosition.CENTER_BOTTOM)
                .withChild(2, 2, new DebugWidget(), AnchorPosition.RIGHT_BOTTOM)
                .anchoredAt(20.0f, 1000.0f, AnchorPosition.LEFT_TOP)
        );

        widgets.add(new GridLayout()
                .withRelativeRows(700.0f, 1f, 2f, 0.5f)
                .withRelativeCols(350.0f, 0.33f, 0.66f)
                .withDefaultChildAnchorPosition(AnchorPosition.LEFT_BOTTOM)
                .withChild(0, 0, new DebugWidget())
                .withChild(0, 1, new DebugWidget())
                .withChild(1, 0, new DebugWidget())
                .withChild(1, 1, new DebugWidget())
                .withChild(2, 0, new DebugWidget())
                .withChild(2, 1, new DebugWidget())
                .anchoredAt(1500.0f, 200.0f, AnchorPosition.CENTER_BOTTOM)
        );
    }

    @Override
    public void receiveRender(SpriteBatch sb) {
        sb.setColor(Color.BLACK);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0, 0, Settings.WIDTH, Settings.HEIGHT);

        widgets.forEach(w -> w.render(sb));
    }
}