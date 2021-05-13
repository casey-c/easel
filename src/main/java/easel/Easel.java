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
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.debug.DebugWidget;
import easel.ui.layouts.GridLayout;
import easel.ui.layouts.VerticalLayout;
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
                swapperWidget.setView(TestEnum.SECONDARY);
            else
                swapperWidget.setView(TestEnum.MAIN);

            isMain = !isMain;
        }
    }

    enum TestEnum {
        MAIN, SECONDARY
    }
    private WidgetSwapper<TestEnum> swapperWidget;

    private GridLayout grid;

    @Override
    public void receivePostInitialize() {
        logger.info("Hello, world");

        swapperWidget = new WidgetSwapper<>(TestEnum.class)
                .withWidget(TestEnum.MAIN, new DebugWidget(100, 100, new Color(1.0f, 1.0f, 1.0f, 1.0f)))
                .withWidget(TestEnum.SECONDARY, new DebugWidget(100, 100, new Color(1.0f, 0.0f, 0.0f, 1.0f)))
                .anchoredCenteredOnScreen();

        widgets.add(swapperWidget);

        widgets.add(
                new VerticalLayout(400, 40)
                        .withChild(new DebugWidget(40, 40))
                        .withChild(new DebugWidget(40, 40, DebugWidget.DEBUG_COLOR_1), AnchorPosition.LEFT_CENTER)
                        .withChild(new DebugWidget(40, 40), AnchorPosition.LEFT_BOTTOM)
                        .withChild(new DebugWidget(40, 40), AnchorPosition.CENTER_BOTTOM)
                        .withChild(new DebugWidget(40, 40, DebugWidget.DEBUG_COLOR_2), AnchorPosition.CENTER)
                        .withChild(new DebugWidget(40, 40), AnchorPosition.CENTER_TOP)
                        .withChild(new DebugWidget(40, 40), AnchorPosition.RIGHT_BOTTOM)
                        .withChild(new DebugWidget(40, 40), AnchorPosition.RIGHT_CENTER)
                        .withChild(new DebugWidget(40, 40), AnchorPosition.RIGHT_BOTTOM)
                        .withChild(new DebugWidget(400, 40))
                        .withChild(new DebugWidget(400, 40))
                        .anchoredAt(Settings.WIDTH - 20, Settings.HEIGHT - 20, AnchorPosition.RIGHT_TOP)
        );

        grid = new GridLayout()
                .withNEvenlySizedCols(300.0f, 3)
                .withNEvenlySizedRows(300.0f, 3)
                .withChild(0, 0, new DebugWidget(40, 40), AnchorPosition.LEFT_TOP)
                .withChild(0, 1, new DebugWidget(40, 40), AnchorPosition.CENTER_TOP)
                .withChild(0, 2, new DebugWidget(40, 40), AnchorPosition.RIGHT_TOP)
                .withChild(1, 0, new DebugWidget(40, 40), AnchorPosition.LEFT_CENTER)
                .withChild(1, 1, new DebugWidget(40, 40), AnchorPosition.CENTER)
                .withChild(1, 2, new DebugWidget(40, 40), AnchorPosition.RIGHT_CENTER)
                .withChild(2, 0, new DebugWidget(40, 40), AnchorPosition.LEFT_BOTTOM)
                .withChild(2, 1, new DebugWidget(40, 40), AnchorPosition.CENTER_BOTTOM)
                .withChild(2, 2, new DebugWidget(40, 40), AnchorPosition.RIGHT_BOTTOM)
                .anchoredCenteredOnScreen();

        widgets.add(grid);
    }

    @Override
    public void receiveRender(SpriteBatch sb) {
        sb.setColor(Color.BLACK);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0, 0, Settings.WIDTH, Settings.HEIGHT);

        grid.anchoredAt(InputHelper.mX, InputHelper.mY, AnchorPosition.CENTER)
                .clampedOntoScreen(20);

        widgets.forEach(w -> w.render(sb));
    }
}