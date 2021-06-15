package easel.ui.text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import easel.ui.AbstractWidget;

import java.util.ArrayList;
import java.util.function.Supplier;

public class SmartLabel extends AbstractWidget<SmartLabel> {
    private float textHeight;
    private float textWidth;

    private float lineWidth;
    private float lineSpacing;

    private BitmapFont font;

    private Supplier<Color> colorSupplier = () -> Settings.CREAM_COLOR;

    private static final class TextGroup {
        String text;
        int line;
        Supplier<Color> colorSupplier;
        float width;

        public TextGroup(String text, int line, Supplier<Color> colorSupplier, float width) {
            this.text = text;
            this.line = line;
            this.colorSupplier = colorSupplier;
            this.width = width;
        }
    }

    private ArrayList<TextGroup> groups = new ArrayList<>();

    private float spaceWidth;
    private float lastLeft;
    private int lastLine;

    private float fontLineHeight;
    private boolean startOfLine = true;

    private StringBuilder stringBuilder = new StringBuilder();

    // --------------------------------------------------------------------------------

    public SmartLabel(BitmapFont font, float lineWidth, float lineSpacing) {
        this.font = font;

        this.spaceWidth = FontHelper.getWidth(font, " ", 1.0f) / Settings.scale;
        this.fontLineHeight = FontHelper.getHeight(font) / Settings.scale;

        this.lineWidth = lineWidth;
        this.lineSpacing = lineSpacing;
    }

    // --------------------------------------------------------------------------------

    public SmartLabel withText(String text) {
        appendTextByWords(text);
        return this;
    }

    public SmartLabel withText(Color textColor, String text) {
        Supplier<Color> previousSupplier = this.colorSupplier;
        this.colorSupplier = () -> textColor;

        appendTextByWords(text);

        this.colorSupplier = previousSupplier;
        return this;
    }

    public SmartLabel withText(Supplier<Color> colorSupplier, String text) {
        Supplier<Color> previousSupplier = this.colorSupplier;
        this.colorSupplier = colorSupplier;

        appendTextByWords(text);

        this.colorSupplier = previousSupplier;
        return this;
    }

    // --------------------------------------------------------------------------------

    public SmartLabel withTextColor(Color color) {
        this.colorSupplier = () -> color;
        return this;
    }

    public SmartLabel withTextColor(Supplier<Color> colorSupplier) {
        this.colorSupplier = colorSupplier;
        return this;
    }

    public SmartLabel withNewlines(int count) {
        this.lastLine += count;
        this.lastLeft = 0;

        this.startOfLine = true;

        return this;
    }

    // --------------------------------------------------------------------------------

//    public enum TextJustification {
//        LEFT, CENTER, RIGHT;
//    }
//
//    public SmartLabel withJustification(TextJustification justification) {
//
//    }

//    public SmartLabel withLeftJustify() {
//
//    }
//
//    public SmartLabel withCenterJustify() {
//
//    }
//
//    public SmartLabel withRightJustify() {
//
//    }

    // --------------------------------------------------------------------------------

    private void finalizeTextGroup() {
        String contents = stringBuilder.toString();

        if (!contents.isEmpty()) {
            float width = FontHelper.getWidth(font, contents, 1.0f) / Settings.scale;

            groups.add(new TextGroup(contents, lastLine, colorSupplier, width));

            // Recompute the total textWidth
            if (lastLeft > textWidth)
                textWidth = lastLeft;

            // Reset
            stringBuilder = new StringBuilder();
        }
    }

    private void appendTextByWords(String text) {
        for (String word : text.split(" ")) {
            float wordWidth = FontHelper.getWidth(font, word, 1.0f);

            // Make a new line
            if (lastLeft + spaceWidth + wordWidth > lineWidth) {
                finalizeTextGroup();

                // Start the next text group on a new line
                stringBuilder.append(word);
                lastLeft = wordWidth;
                lastLine = lastLine + 1;
            }
            // Fits on current line
            else {
                if (startOfLine)
                    startOfLine = false;
                else
                    stringBuilder.append(" ");

                stringBuilder.append(word);

                lastLeft += (wordWidth + spaceWidth);
            }
        }

        finalizeTextGroup();
        recomputeTextHeight();
    }

    private void recomputeTextHeight() {
        if (groups.isEmpty())
            textHeight = 0;
        else {
            int numSpacing = groups.get(groups.size() - 1).line;
            textHeight = (numSpacing * lineSpacing) + ((numSpacing + 1) * fontLineHeight);
        }
    }

    // --------------------------------------------------------------------------------

    @Override public float getContentWidth() { return textWidth; }
    @Override public float getContentHeight() { return textHeight; }

    @Override
    protected void renderWidget(SpriteBatch sb) {
        int currLine = 0;

        float contentLeft = getContentLeft();

        float left = contentLeft;
        float top = getContentTop();

        for (TextGroup group : groups) {
            String text = group.text;
            Color color = group.colorSupplier.get();

            while (currLine < group.line) {
                top -= (fontLineHeight + lineSpacing);
                ++currLine;

                left = contentLeft;
            }

            FontHelper.renderFontLeftTopAligned(sb, font, text, left * Settings.xScale, top * Settings.yScale, color);

            left += group.width;
        }

//        EaselGraphicsHelper.drawDebugRects(sb, this);
    }
}