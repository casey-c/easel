package easel;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.PostUpdateSubscriber;
import basemod.interfaces.RenderSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.debug.DebugWidget;
import easel.ui.debug.InteractiveDebugWidget;
import easel.ui.layouts.GridLayout;
import easel.ui.layouts.HorizontalLayout;
import easel.ui.layouts.VerticalLayout;
import easel.ui.misc.WidgetSwapper;
import easel.ui.text.Label;
import easel.utils.SoundHelper;
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
    public void receivePostUpdate() {
        widgets.forEach(AbstractWidget::update);
    }

    @Override
    public void receivePostInitialize() {
        logger.info("Hello, world");

        widgets.add(
                new InteractiveDebugWidget()
                        .onLeftClick(w -> SoundHelper.cawCaw())
                        .onRightClick(w -> w.setColor(Color.GOLD))
                        .anchoredAt(500, 500, AnchorPosition.LEFT_BOTTOM)
        );
    }


    @Override
    public void receiveRender(SpriteBatch sb) {
        sb.setColor(Color.BLACK);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0, 0, Settings.WIDTH, Settings.HEIGHT);

        widgets.forEach(w -> w.render(sb));
    }
}