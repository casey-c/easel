package easel.ui.text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import easel.ui.AbstractWidget;

//public class BlockLabel extends AbstractWidget<BlockLabel> {
//    @Override public float getContentWidth() { return 0; }
//    @Override public float getContentHeight() { return 0; }
//
//    @Override
//    protected void renderWidget(SpriteBatch sb) {
//
//    }
//
//    // --------------------------------------------------------------------------------
//    // e.g. new BlockLabel(Settings.CREAM_COLOR, maxLineWidth, lineSpacing)
//    //            .withText("The following text is red: ")
//    //            .withText("red text", Color.RED);
//    //            .withLineBreaks(1)
//    //            .withText("The following text is hoverable: ")
//    //            .withHoverableText("hover me!", onHover -> { cawCaw(); })
//    //            .withLineBreaks(1)
//    //            .withText("The following text is clickable: ")
//    //            .withInteractiveLabel(new InteractiveLabel("click me").onClick(onClick -> {cawCaw();}))
//    //            .withClickableText("click me!", onHover -> { cawCaw(); }, onClick -> { cawCaw(); })
//    //            .anchoredCenteredOnScreen();
//    // QUESTION: should we include the base game's text manip techniques? e.g. #rRed #rText, or " NL " for newlines?
//    //   i'm leaning towards NO, because this is much more flexible and readable; but maybe it's worth including a
//    //   converter? e.g. fromFormatted("Existing formatted #rRed #rText NL second line")
//
//    public BlockLabel withText(String next) {
//        return this;
//    }
//
//    public BlockLabel withText(String next, Color color) {
//        return this;
//    }
//
//    public BlockLabel withLineBreaks(int count) {
//        return this;
//    }
//
//    public BlockLabel withInteractiveLabel(InteractiveLabel label) {
//        return this;
//    }
//
//    // --------------------------------------------------------------------------------
//}
