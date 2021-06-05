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
import easel.ui.containers.StyledContainer;
import easel.ui.debug.DebugWidget;
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

    private AnchorPosition ap = AnchorPosition.LEFT_TOP;

    @Override
    public void receivePostInitialize() {
        TextureLoader.loadTextures();
        EaselFonts.loadFonts();

        widgets.add(
                new StyledContainer(400, 400)
                        .withHeader("Pie Chart Tests", "Subtitle")
//                        .withHeader(new DebugWidget(40, 40), true)
                        .onRightClick(container -> {
                            ap = ap.next();
                            container.withContentAnchor(ap);
                            container.refreshAnchor();
                        })
                        .withContent(
                                new PieChartWidget(200, 200)
                                        .withColors(EaselColors.QUAL_RED(), EaselColors.QUAL_GREEN(), EaselColors.QUAL_BLUE(), EaselColors.QUAL_PURPLE(), EaselColors.QUAL_YELLOW())
                                        .withCounts(6, 4, 3, 2, 1),
                                true)
                        .scaleToContent()
                        .makeMovable()
                        .anchoredCenteredOnScreen()
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