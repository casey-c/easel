package easel;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.PostUpdateSubscriber;
import basemod.interfaces.RenderSubscriber;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;
import easel.ui.graphics.ninepatch.headered.SmallHeaderedNinePatch;
import easel.utils.GraphicsHelper;
import easel.utils.KeyHelper;
import easel.utils.colors.EaselColors;
import easel.utils.textures.TextureManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpireInitializer
public class Easel implements PostInitializeSubscriber, RenderSubscriber, PostUpdateSubscriber {
    public static final Logger logger = LogManager.getLogger(Easel.class);

    public static void initialize() {
        new Easel();
    }

    public Easel() {
        BaseMod.subscribe(this);
    }

    private SmallHeaderedNinePatch widget;

    @Override
    public void receivePostInitialize() {
        TextureManager.loadTextures();

        widget = new SmallHeaderedNinePatch(500, 500)
                .onRightClick(s -> {
                    s.withHeaderColor(EaselColors.HEADER_RED())
                            .anchoredAt(0, 0, AnchorPosition.CENTER, InterpolationSpeed.MEDIUM, 20);
                })
                .onLeftClick(s -> {
                    if (KeyHelper.isShiftPressed()) {
                        s.withHeaderColor(EaselColors.HEADER_DEEP_BLUE())
                                .anchoredCenteredOnScreen(InterpolationSpeed.FAST);
                    }
                    else {
                        if (KeyHelper.isAltPressed()) {
                            s.withHeaderColor(EaselColors.HEADER_BLUE())
                                    .anchoredCenteredOnScreen(InterpolationSpeed.MEDIUM);
                        }
                        else {
                            s.withHeaderColor(EaselColors.HEADER_DARK_ALGAE())
                                    .anchoredCenteredOnScreen(InterpolationSpeed.SLOW);
                        }
                    }
                })
                .anchoredCenteredOnScreen()
        ;
    }


    @Override
    public void receiveRender(SpriteBatch sb) {
        GraphicsHelper.dimFullScreen(sb, true);
        widget.render(sb);
    }

    @Override
    public void receivePostUpdate() {
        widget.update();
    }
}