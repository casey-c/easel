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
import easel.ui.interactive.MoveableWidget;
import easel.ui.layouts.GridLayout;
import easel.utils.SoundHelper;
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

    @Override
    public void receivePostUpdate() {
        widgets.forEach(AbstractWidget::update);
    }

    @Override
    public void receivePostInitialize() {
        logger.info("Hello, world");

        GridLayout grid = new GridLayout();
        widgets.add(
                grid.withRowsCols(500, 500, 2, 2)
                        .withDefaultChildAnchorPosition(AnchorPosition.CENTER)
                        .withChildrenInRow(0,
                                new DebugWidget()
                                        .onMouseEnter(w -> w.setColor(DebugWidget.DEBUG_COLOR_1))
                                        .onMouseLeave(w -> w.setColor(DebugWidget.DEBUG_COLOR_0))
                                        .onLeftClick(w -> SoundHelper.cawCaw()),
                                new DebugWidget()
                                        .onMouseEnter(w -> w.setColor(DebugWidget.DEBUG_COLOR_1))
                                        .onMouseLeave(w -> w.setColor(DebugWidget.DEBUG_COLOR_0))
                                        .onLeftClick(w -> SoundHelper.cawCaw())
                        )
                        .withChildrenInRow(1,
                                new DebugWidget()
                                        .onMouseEnter(w -> w.setColor(DebugWidget.DEBUG_COLOR_1))
                                        .onMouseLeave(w -> w.setColor(DebugWidget.DEBUG_COLOR_0))
                                        .onRightClick(w -> SoundHelper.cawCaw()),
                                new MoveableWidget(100, 100, grid)
                        )
                        //.resizeRowsToFitTallestChildren()
                        .onLeftClick(onClick -> SoundHelper.screenCloseSound())
                        .anchoredCenteredOnScreen()
        );

        widgets.add(
                new DebugWidget(100, 100, DebugWidget.DEBUG_COLOR_1)
                        .onRightClick(w -> {
                            w.setColor(Color.GOLD);
                            SoundHelper.cawCaw();
                        })
                        .onMouseEnter(w -> w.setColor(DebugWidget.DEBUG_COLOR_0))
                        .onMouseLeave(w -> w.setColor(DebugWidget.DEBUG_COLOR_1))
                        .makeMoveable()
                        .anchoredAt(500, 500, AnchorPosition.LEFT_BOTTOM)
//                new InteractiveDebugWidget()
//                        .onLeftClick(w -> SoundHelper.cawCaw())
//                        .onRightClick(w -> w.setColor(Color.GOLD))
//                        .anchoredAt(500, 500, AnchorPosition.LEFT_BOTTOM)
//                new PartiallyMoveableWidget(200, 140)
//                        .anchoredCenteredOnScreen()
        );

        widgets.add(
                new DebugWidget(50, 50)
                        .anchoredAt(100, 100, AnchorPosition.LEFT_BOTTOM)
        );
    }


    @Override
    public void receiveRender(SpriteBatch sb) {
        sb.setColor(Color.BLACK);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0, 0, Settings.WIDTH, Settings.HEIGHT);

        widgets.forEach(w -> w.render(sb));
    }
}