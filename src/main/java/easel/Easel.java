package easel;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.PostUpdateSubscriber;
import basemod.interfaces.RenderSubscriber;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.containers.StyledContainer;
import easel.ui.debug.DebugWidget;
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

    private AnchorPosition ap = AnchorPosition.LEFT_TOP;

    @Override
    public void receivePostInitialize() {
        TextureLoader.loadTextures();
        EaselFonts.loadFonts();

        widgets.add(
                new StyledContainer(100, 100)
                        .onRightClick(container -> {
                            ap = ap.next();
                            container.withContentAnchor(ap);
                        })
                        .withContent(new Label("No Header"), true)
                        .scaleToContent()
                        .makeMovable()
        );

        widgets.add(
                new StyledContainer(500, 500)
                        .withHeader("Title")
                        .onRightClick(container -> {
                            ap = ap.next();
                            container.withContentAnchor(ap);
                        })
                        .withContent(new Label("Just the title"), true)
                        .scaleToContent()
                        .makeMovable()
        );

        widgets.add(
                new StyledContainer(500, 500)
                        .withHeader("Title", "Subtitle")
                        .onRightClick(container -> {
                            ap = ap.next();
                            container.withContentAnchor(ap);
                        })
                        .withContent(new DebugWidget(100, 100).withMargins(100), false)
                        .scaleToContent()
                        .makeMovable()
        );

        // Make a nice starting location for all these widgets
        HorizontalLayout layout = new HorizontalLayout(100, 40);

        for (AbstractWidget w : widgets) {
            layout.withChild(w);
        }

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