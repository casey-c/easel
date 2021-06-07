package easel;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.PostUpdateSubscriber;
import basemod.interfaces.RenderSubscriber;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.containers.MoveContainer;
import easel.ui.layouts.HorizontalLayout;
import easel.ui.layouts.VerticalLayout;
import easel.ui.text.Label;
import easel.utils.EaselFonts;
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

        MoveContainer moveContainer =
                new MoveContainer()
                        .withChild(new Label("First"))
                        .withChild(new Label("Second"))
                        .withChild(new Label("Third"))
                        .withChild(new Label("Fourth"));

        System.out.println("------------------------");
        System.out.println("The final move container:");
        moveContainer.printQueue();
        System.out.println("------------------------");

//        System.out.println("Move index 2 to bottom: ");
//        moveContainer.bringIndexToBottom(2);
//        moveContainer.printQueue();
//        System.out.println("------------------------");
//        System.out.println("Move index 1 to top: ");
//        moveContainer.bringIndexToTop(1);
//        moveContainer.printQueue();
//        System.out.println("------------------------");
//
        widgets.add(
                moveContainer
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