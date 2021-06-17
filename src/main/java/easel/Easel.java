package easel;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.PostUpdateSubscriber;
import basemod.interfaces.RenderSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.containers.MoveContainer;
import easel.ui.containers.StyledContainer;
import easel.ui.layouts.VerticalLayout;
import easel.ui.text.SmartLabel;
import easel.utils.EaselFonts;
import easel.utils.EaselSoundHelper;
import easel.utils.colors.EaselColors;
import easel.utils.textures.TextureLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.stream.Stream;

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

        // Tests
        VerticalLayout layout = new VerticalLayout(10, 20)
                .withChild(
                        new StyledContainer(100, 100)
                                .withHeader("Tip Body Font")
                                .withHeaderColor(EaselColors.HEADER_STRONG_PURPLE())
                                .withContentAnchor(AnchorPosition.LEFT_TOP)
                                .withContent(
                                        new SmartLabel(FontHelper.tipBodyFont, 500, 10)
                                                .withTextColor(Color.GRAY)
                                                .withText(Settings.RED_TEXT_COLOR, "Hello, world.")
                                                .withText(" Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore")
                                                .withText(Settings.BLUE_TEXT_COLOR, " magna aliqua.")
                                                .withText(" Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                                                .onRightClick(label -> {
                                                    EaselSoundHelper.cawCaw();
                                                }),
                                        true
                                )
                                .scaleToContent()
                )
                .withChild(
                        new StyledContainer(700, 100)
                                .withHeader("Easel Italic Font")
                                .withHeaderColor(EaselColors.HEADER_LIGHT_ALGAE())
                                .withContentAnchor(AnchorPosition.LEFT_TOP)
                                .withContent(
                                        new SmartLabel(EaselFonts.MEDIUM_ITALIC, 300, 10)
                                                .withText("Hello, world.")
                                                .withNewlines(1)
                                                .withText(EaselColors::rainbow, "Hello, world.")
                                                .withNewlines(2)
                                                .withText("Hello, world.")
                                                .withNewlines(2)
                                                .withText(" Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.")
                                                .withNewlines(2)
                                                .withText(" Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                                                .onRightClick(label -> {
                                                    EaselSoundHelper.cawCaw();
                                                }),
                                true
                                )
                                .scaleToContentHeight()
                );

        StyledContainer.syncContainerHeights(true, layout.iteratorOfType(StyledContainer.class));

        layout.anchoredCenteredOnScreen();

        widgets.add(
                new MoveContainer().withAllChildrenOfLayout(layout)
        );
    }


    @Override
    public void receiveRender(SpriteBatch sb) {
        widgets.forEach(widget -> {
            widget.render(sb);
        });
    }

    @Override
    public void receivePostUpdate() {
        widgets.forEach(AbstractWidget::update);
    }
}