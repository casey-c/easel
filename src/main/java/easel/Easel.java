package easel;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.PostUpdateSubscriber;
import basemod.interfaces.RenderSubscriber;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import easel.config.ConfigTester;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;
import easel.ui.containers.MoveContainer;
import easel.ui.containers.StyledContainer;
import easel.ui.debug.DebugWidget;
import easel.ui.layouts.GridLayout;
import easel.ui.layouts.HorizontalLayout;
import easel.ui.text.Label;
import easel.utils.EaselFonts;
import easel.utils.EaselInputHelper;
import easel.utils.textures.TextureLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;

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

    private ArrayList<StyledContainer> containers = new ArrayList<>();

    private float delta = 80;

    private String lastSerialization = "{\"widgets\":[{\"addOrder\":0,\"left\":812.0,\"bottom\":254.0},{\"addOrder\":2,\"left\":697.0,\"bottom\":189.0},{\"addOrder\":1,\"left\":779.0,\"bottom\":115.0},{\"addOrder\":3,\"left\":996.0,\"bottom\":193.0}]}";

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

//        HorizontalLayout layout = new HorizontalLayout(100, 20)
//                .withChild(new StyledContainer(300, 200).withHeader("One").withContent(new Label("ONE"), true))
//                .withChild(new StyledContainer(300, 200).withHeader("Two").withContent(new Label("TWO"), true))
//                .withChild(new StyledContainer(300, 200).withHeader("Three").withContent(new Label("THREE"), true))
//                .anchoredCenteredOnScreen();
        StyledContainer a = new StyledContainer(300, 200)
                .withHeader("One")
                .withContent(new Label("ONE"), true);
        StyledContainer b = new StyledContainer(300, 200)
                .withHeader("Two")
                .withContent(new Label("TWO"), true);
        StyledContainer c = new StyledContainer(300, 200)
                .withHeader("Three")
                .withContent(new Label("THREE"), true);
        StyledContainer d = new StyledContainer(300, 200)
                .withHeader("Four")
                .withContent(new Label("FOUR"), true);

//        DebugWidget a = new DebugWidget(100, 100, DebugWidget.DEBUG_COLOR_0);
//        DebugWidget b = new DebugWidget(100, 100, DebugWidget.DEBUG_COLOR_1);
//        DebugWidget c = new DebugWidget(100, 100, DebugWidget.DEBUG_COLOR_2);
//        DebugWidget d = new DebugWidget(100, 100, DebugWidget.DEBUG_COLOR_3);


        //containers.addAll(Arrays.asList(a, b, c, d));

        HorizontalLayout layout = new HorizontalLayout(100, -40)
                .withDefaultChildAnchorPosition(AnchorPosition.CENTER)
                .withChild(a)
                .withChild(b)
                .withChild(c)
                .withChild(d)
                .scaleToTallestChild()
                .anchoredCenteredOnScreen();

//        widgets.add(layout);

//        GridLayout layout = new GridLayout()
//                .withNEvenlySizedCols(500, 2)
//                .withNEvenlySizedRows(500, 2)
//                .withChild(0, 0, a )
//                .withChild(0, 1, b )
//                .withChild(1, 0, c )
//                .withChild(1, 1, d )
//                .resizeColsToFitWidestChildren()
//                .resizeRowsToFitTallestChildren()
//                .anchoredCenteredOnScreen();

        widgets.add(
                new MoveContainer()
                        .withAllChildrenOfLayout(layout)
                        .onRightClick(container -> {
                            System.out.println("SERIALIZED------------------");
                            System.out.println( container.serialize() );
                            System.out.println("----------------------------");

                            if (EaselInputHelper.isShiftPressed()) {
                                System.out.println("STORED");
                                lastSerialization = container.serialize();
                            }
                            else if (EaselInputHelper.isAltPressed()) {
                                System.out.println("RESTORED");
                                container.deserialize(lastSerialization);
                            }

//                            String vertical = "{\"left\":[1510.0,1510.0,1510.0,1510.0],\"bottom\":[800.0,590.0,380.0,170.0],\"addOrders\":[0,1,2,3]}";
//                            container.deserialize(new Gson().fromJson(vertical));
                        })
                        .anchoredCenteredOnScreen()
        );

//        widgets.add(
//                new MoveContainer()
//                        .withChild(
//                                new StyledContainer(100, 100)
//                                        .withContent(
//                                                new VerticalLayout(100, 20)
//                                                        .withDefaultChildAnchorPosition(AnchorPosition.CENTER)
//                                                        .withChild(new Label("Row 1", EaselColors.SEQ_BLUE_0()))
//                                                        .withChild(new Label("Row 2", EaselColors.SEQ_BLUE_1()))
//                                                        .withChild(new Label("Row 3", EaselColors.SEQ_BLUE_2()))
//                                                        .withChild(new Label("Row 4", EaselColors.SEQ_BLUE_3()))
//                                                        .withChild(new Label("Row 5", EaselColors.SEQ_BLUE_4()))
//                                                        .scaleToWidestChild(),
//                                                true
//                                        )
//                                        .scaleToContent()
//                                        .anchoredCenteredOnScreen()
//                        )
//                        .withChild(
//                                new StyledContainer(100, 100)
//                                        .withHeader("Pie Chart", "Right click to randomize")
//                                        .withHeaderColor(EaselColors.HEADER_SEA_GLASS())
//                                        .withContent(
//                                                new PieChartWidget(200, 200)
//                                                        .withRegion(6, EaselColors.QUAL_RED())
//                                                        .withRegion(5, EaselColors.QUAL_GREEN())
//                                                        .withRegion(1, EaselColors.QUAL_BLUE())
//                                                        .withRegion(1, EaselColors.QUAL_PURPLE())
//                                                        .onRightClick( pie -> {
//                                                            if (EaselInputHelper.isShiftPressed()) {
//                                                                pie
//                                                                        .withCounts(6, 5, 1, 1)
//                                                                        .withColors(
//                                                                                EaselColors.QUAL_RED(),
//                                                                                EaselColors.QUAL_GREEN(),
//                                                                                EaselColors.QUAL_BLUE(),
//                                                                                EaselColors.QUAL_PURPLE());
//                                                            }
//                                                            else {
//                                                                Random r = new Random();
//                                                                int index = r.nextInt(4);
//                                                                pie.updateRegionCount(index, r.nextInt(6) + 1);
//                                                                pie.updateRegionColor(index, EaselColors.rainbow());
//                                                            }
//                                                        } )
////                                                        .withColors(EaselColors.QUAL_RED(), EaselColors.QUAL_GREEN(), EaselColors.QUAL_BLUE(), EaselColors.QUAL_PURPLE())
////                                                        .withCounts(6, 5, 1, 1)
//                                                ,
//                                                true
//                                        )
//                                        .scaleToContent()
//                                        .anchoredCenteredOnScreen()
//                        )
//                        .withChild(
//                                new StyledContainer(100, 100)
//                                        .withHeader("Debug Widgets", "Right click for caw caw")
//                                        .withHeaderColor(EaselColors.HEADER_RED())
//                                        .withContent(
//                                                new DebugWidget(300, 300),
//                                                true
//                                        )
//                                        .onRightClick(container -> {
//                                            EaselSoundHelper.cawCaw();
//                                        })
//                                        .scaleToContent()
//                                        .anchoredCenteredOnScreen()
//                        )
//        );

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
//        HorizontalLayout layout = new HorizontalLayout(100, 40);
//        widgets.forEach(layout::withChild);
//        layout.anchoredCenteredOnScreen();
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

    private long nextUpdateTime;
    private int numShifts;

    @Override
    public void receivePostUpdate() {
        widgets.forEach(AbstractWidget::update);
//
//
//        if (System.currentTimeMillis() > nextUpdateTime) {
//            float delta = (numShifts++ % 2 == 0) ? 20 : -20;
//
//            AtomicLong time = new AtomicLong(200);
//            for (StyledContainer c : containers)
//                c.delayedAnchoredAt(c.getLeft(), c.getBottom() + delta, AnchorPosition.LEFT_BOTTOM, InterpolationSpeed.FAST, time.addAndGet(100));
//
//            nextUpdateTime = System.currentTimeMillis() + 600;
//        }
    }
}