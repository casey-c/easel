package easel;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.PostUpdateSubscriber;
import basemod.interfaces.RenderSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.containers.LargeHeaderedContainer;
import easel.ui.graphics.pie.PieChartWidget;
import easel.utils.GraphicsHelper;
import easel.utils.colors.EaselColors;
import easel.utils.textures.TextureManager;
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
        TextureManager.loadTextures();

        widgets.add(
//                new LargeHeaderedContainer(500, 500)
//                        .withHeader("Title")
//                        .withHeaderColor(EaselColors.TOOLTIP_BASE())
//                        .withHeaderHorizontalAlignment(AnchorPosition.CENTER)
//                        //.makeMovable()
//                        .anchoredCenteredOnScreen()
                new PieChartWidget(200, 200)
                        .withMargins(100, 50)
                        .withCounts(6, 4, 2, 1)
                        .withColors(EaselColors.QUAL_RED(), EaselColors.QUAL_GREEN(), EaselColors.QUAL_BLUE(), EaselColors.QUAL_PURPLE())
                        .onRightClick(pie -> {
                            Random random = new Random();

                            int a = random.nextInt(5) + 1;
                            int b = random.nextInt(5) + 1;
                            int c = random.nextInt(5) + 1;
                            int d = random.nextInt(5) + 1;

                            pie.withCounts(a, b, c, d);
                        })
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