package easel;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.PostUpdateSubscriber;
import basemod.interfaces.RenderSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import easel.config.ConfigTester;
import easel.ui.AbstractWidget;
import easel.ui.containers.MoveContainer;
import easel.ui.containers.StyledContainer;
import easel.ui.debug.DebugWidget;
import easel.ui.graphics.pie.PieChartWidget;
import easel.ui.layouts.HorizontalLayout;
import easel.ui.text.Label;
import easel.utils.EaselFonts;
import easel.utils.EaselSoundHelper;
import easel.utils.colors.EaselColors;
import easel.utils.textures.TextureLoader;
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
//    private AnchorPosition ap = AnchorPosition.LEFT_TOP;
//    private enum SwapWindows { MAIN, SECONDARY };

    @Override
    public void receivePostInitialize() {
        TextureLoader.loadTextures();
        EaselFonts.loadFonts();

        ConfigTester.test();

//        MoveContainer moveContainer =
//                new MoveContainer()
//                        .withChild(new Label("First").withMargins(20))
//                        .withChild(new Label("Second").withMargins(20))
//                        .withChild(new Label("Third").withMargins(20))
//                        .withChild(new Label("Fourth").withMargins(20));
//
//        widgets.add(
//                moveContainer
//        );

        widgets.add(
                new MoveContainer()
                        .withChild(
                                new StyledContainer(100, 100)
                                        .withContent(new Label("No Header"), true)
                                        .scaleToContent()
                                        .anchoredCenteredOnScreen()
                        )
                        .withChild(
                                new StyledContainer(100, 100)
                                        .withHeader("Pie Chart")
                                        .withContent(
                                                new PieChartWidget(200, 200)
                                                        .withColors(Color.WHITE, Color.BLACK)
                                                        .withCounts(1, 2)
                                                ,
                                                true
                                        )
                                        .scaleToContent()
                                        .anchoredCenteredOnScreen()
                        )
                        .withChild(
                                new StyledContainer(100, 100)
                                        .withHeader("Debug Widgets", "Right click for caw caw")
                                        .withHeaderColor(EaselColors.HEADER_DEEP_BLUE())
                                        .withContent(
                                                new DebugWidget(300, 300),
                                                true
                                        )
                                        .onRightClick(container -> {
                                            EaselSoundHelper.cawCaw();
                                        })
                                        .scaleToContent()
                                        .anchoredCenteredOnScreen()
                        )
        );

//        widgets.add(
//                new StyledContainer(100, 100)
//                        .onRightClick(container -> {
//                            ap = ap.next();
//                            container.withContentAnchor(ap);
//                        })
//                        .withContent(new Label("No Header"), true)
//                        .scaleToContent()
//                        .makeMovable()
//        );
//
//        widgets.add(
//                new StyledContainer(500, 500)
//                        .withHeader("Title")
//                        .onRightClick(container -> {
//                            ap = ap.next();
//                            container.withContentAnchor(ap);
//                        })
//                        .withContent(new Label("Just the title"), true)
//                        .scaleToContent()
//                        .makeMovable()
//        );
//
//        widgets.add(
//                new StyledContainer(500, 500)
//                        .withHeader("Swap Tests", "Very long header text compared to content")
//                        .onRightClick(container -> {
//                            ap = ap.next();
//                            container.withContentAnchor(ap).refreshAnchor();
//                        })
//                        .withContent(
//                                new SwapContainer<>(SwapWindows.class)
//                                        .withWidget(SwapWindows.MAIN, new Label("1"), true)
//                                        .withWidget(SwapWindows.SECONDARY, new Label("2"))
//                                        .onLeftClick(container -> {
//                                            if (KeyHelper.isShiftPressed())
//                                                container.nextView();
//                                        }),
//                                true
//                        )
////                        .withContent(
////                                new PieChartWidget(200, 200)
////                                        .withCounts(1, 1)
////                                        .withColors(Color.WHITE, Color.BLACK),
////                                true
////                        )
//                        .scaleToContent()
//                        .makeMovable()
//        );
//
        // Make a nice starting location for all these widgets
        HorizontalLayout layout = new HorizontalLayout(100, 40);
        widgets.forEach(layout::withChild);
        layout.anchoredCenteredOnScreen();
    }


    @Override
    public void receiveRender(SpriteBatch sb) {
//        GraphicsHelper.dimFullScreen(sb, true);

        widgets.forEach(widget -> {
            widget
                    //.anchoredCenteredOnMouse(40, -40, AnchorPosition.LEFT_TOP, 20)
                    .render(sb);
        });
    }

    @Override
    public void receivePostUpdate() {
        widgets.forEach(AbstractWidget::update);
    }
}