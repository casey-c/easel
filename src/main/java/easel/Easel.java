package easel;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.PostUpdateSubscriber;
import basemod.interfaces.RenderSubscriber;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import easel.config.EaselConfigHelper;
import easel.config.enums.ConfigIntegerEnum;
import easel.config.enums.ConfigStringEnum;
import easel.config.samples.SampleBooleanChoices;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.containers.MoveContainer;
import easel.ui.containers.StyledContainer;
import easel.ui.layouts.HorizontalLayout;
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

    private ArrayList<StyledContainer> containers = new ArrayList<>();

    private EaselConfigHelper<SampleBooleanChoices, ConfigIntegerEnum, TestStringOptions> configHelper;
    private float delta = 80;

    private enum TestStringOptions implements ConfigStringEnum {
        MOVE_CONTAINER_LOCATIONS("");

        String val;
        TestStringOptions(String val) { this.val = val; }
        @Override public String getDefault() { return val; }
    }

    @Override
    public void receivePostInitialize() {
        TextureLoader.loadTextures();
        EaselFonts.loadFonts();

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

        HorizontalLayout layout = new HorizontalLayout(100, -40)
                .withDefaultChildAnchorPosition(AnchorPosition.CENTER)
                .withChild(a)
                .withChild(b)
                .withChild(c)
                .withChild(d)
                .scaleToTallestChild()
                .anchoredCenteredOnScreen();

        configHelper = EaselConfigHelper.fromBooleansStrings("Easel", "EaselMoveTests", SampleBooleanChoices.class, TestStringOptions.class);

        MoveContainer mc = new MoveContainer()
                .withAllChildrenOfLayout(layout)
                .onRightClick(container -> {
                    configHelper.setString(TestStringOptions.MOVE_CONTAINER_LOCATIONS, container.toJsonString());
                })
                .anchoredCenteredOnScreen();

        // Load defaults
        mc.loadFromJsonString(configHelper.getString(TestStringOptions.MOVE_CONTAINER_LOCATIONS));

        widgets.add(mc);

        System.out.println(configHelper);
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