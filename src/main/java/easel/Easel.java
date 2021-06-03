package easel;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.PostUpdateSubscriber;
import basemod.interfaces.RenderSubscriber;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.containers.HeaderlessContainer;
import easel.ui.containers.LargeHeaderedContainer;
import easel.ui.containers.SmallHeaderedContainer;
import easel.ui.graphics.ninepatch.headered.SmallHeaderedNinePatch;
import easel.ui.graphics.pie.PieChartWidget;
import easel.ui.text.Label;
import easel.utils.EaselFonts;
import easel.utils.colors.EaselColors;
import easel.utils.textures.TextureLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Random;

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
    public void receivePostInitialize() {
        TextureLoader.loadTextures();
        EaselFonts.loadFonts();

        widgets.add(
                new LargeHeaderedContainer(500, 500)
                        .withHeader("Pie Chart Tests", "Subtitle")
                        //.withHeaderAlignment(AnchorPosition.LEFT_TOP)
                        .withHeaderColor(EaselColors.HEADER_PURPLE())
                        .withContentAnchor(AnchorPosition.LEFT_TOP)
                        .withContent(
                                new PieChartWidget(200, 200)
                                        .withMargins(50)
                                        .withCounts(6, 4, 2, 1, 3)
                                        .withColors(EaselColors.QUAL_RED(), EaselColors.QUAL_GREEN(), EaselColors.QUAL_BLUE(), EaselColors.QUAL_PURPLE(), EaselColors.QUAL_YELLOW())
                                        .onRightClick(pie -> {
                                            Random random = new Random();

                                            int a = random.nextInt(5) + 1;
                                            int b = random.nextInt(5) + 1;
                                            int c = random.nextInt(5) + 1;
                                            int d = random.nextInt(5) + 1;
                                            int e = random.nextInt(5) + 1;

                                            pie.withCounts(a, b, c, d, e);
                                        })
                        )
                        .scaleToContent()
                        .makeMovable()
                        .anchoredCenteredOnScreen()
        );

        widgets.add(
                new SmallHeaderedContainer(400, 200)
                        .withHeader("Smaller", "This has less header height")
                        .makeMovable()
                        .anchoredAt(1920, 100, AnchorPosition.LEFT_BOTTOM, 20)
        );

        widgets.add(
                new HeaderlessContainer(200, 400)
                        .makeMovable()
                        .withContent(new Label("This has no header...").withMargins(40))
                        .scaleToContent()
                        .anchoredAt(300, 300, AnchorPosition.CENTER)
        );
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