package easel;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.PostUpdateSubscriber;
import basemod.interfaces.RenderSubscriber;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.containers.LargeHeaderedContainer;
import easel.utils.GraphicsHelper;
import easel.utils.KeyHelper;
import easel.utils.colors.EaselColors;
import easel.utils.textures.TextureManager;
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
    public void receivePostInitialize() {
        TextureManager.loadTextures();

        widgets.add(
                new LargeHeaderedContainer(500, 500)
                        .withHeader("Title")
                        .withHeaderColor(EaselColors.TOOLTIP_BASE())
                        .withHeaderHorizontalAlignment(AnchorPosition.CENTER)
                        //.makeMoveable()
                        .anchoredCenteredOnScreen()
        );
    }


    @Override
    public void receiveRender(SpriteBatch sb) {
        GraphicsHelper.dimFullScreen(sb, true);

        widgets.forEach(widget -> {
            widget
                    .anchoredCenteredOnMouse(40, -40, AnchorPosition.LEFT_TOP, 20)
                    .render(sb);
        });
    }

    @Override
    public void receivePostUpdate() {
        widgets.forEach(AbstractWidget::update);
    }
}