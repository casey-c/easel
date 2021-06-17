package easel.ui.text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import easel.ui.AbstractWidget;

import java.util.ArrayList;
import java.util.function.Supplier;

/**
 * <p>
 * A more flexible version of {@link Label} which allows for automatic text wrapping, inline colors for specific pieces of text, and convenient builder methods to make producing larger paragraphs of text a bit more convenient. Like other widgets, this SmartLabel requires anchoring before being rendered, and this anchoring should occur AFTER setting all text.
 * </p>
 * <p>
 * Example use:
 * </p>
 * <pre>
 * {@code
 * new SmartLabel(EaselFonts.MEDIUM_ITALIC, 300, 10)
 *   .withTextColor(Color.GREEN)
 *   .withText(Color.GRAY, "Hello, world")
 *   .withNewlines(2)
 *   .withText(EaselColors::rainbow, "Dynamic rainbow text!")
 *   .withNewlines(1)
 *   .withText("The rest of the text is in green and will automatically wrap once a line passes 300px in width")
 *   .anchoredCenteredOnScreen();
 * }
 * </pre>
 */
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

    /**
     * Convenience constructor for {@link #SmartLabel(BitmapFont, float, float)} with the base game's <code>FontHelper.tipBodyFont</code> and no automatic line wrapping.
     */
    public SmartLabel() {
        this(FontHelper.tipBodyFont);
    }

    /**
     * Convenience constructor for {@link #SmartLabel(BitmapFont, float, float)} with the base game's <code>FontHelper.tipBodyFont</code> and <code>lineSpacing = 10</code>.
     * @param lineWidth the width before the line breaks automatically
     */
    public SmartLabel(float lineWidth) {
        this(FontHelper.tipBodyFont, lineWidth);
    }

    /**
     * Convenience constructor for {@link #SmartLabel(BitmapFont, float, float)} with no automatic line wrapping.
     * @param font the font of all text on this label
     */
    public SmartLabel(BitmapFont font) {
        this(font, 1000000, 10);
    }

    /**
     * Convenience constructor for {@link #SmartLabel(BitmapFont, float, float)} with a <code>lineSpacing = 10</code>.
     * @param font the font of all text on this label
     * @param lineWidth the width before the line breaks automatically
     */
    public SmartLabel(BitmapFont font, float lineWidth) {
        this(font, lineWidth, 10);
    }

    /**
     * The primary constructor for a smart label. The entire label will use the given font and will wrap text once a line would exceed the given line width. The line spacing is the gap added between lines and is independent of the height of the actual font (this behavior differs from the way the base game's FontHelper handles it, where the line spacing IS dependent on the font height). Setting the lineWidth to something large, e.g. 10000000, will result in no text wrapping except for manually added newlines using {@link #withNewlines(int)} (although if text wrapping isn't desired, you may find yourself content with a simple {@link Label} instead, as it can be slightly more efficient to construct).
     * @param font the font of all text on this label
     * @param lineWidth the width before the line breaks automatically
     * @param lineSpacing the vertical spacing between each line, i.e. the space between the bottom of a line and the top of the line directly beneath it
     */
    public SmartLabel(BitmapFont font, float lineWidth, float lineSpacing) {
        this.font = font;

        this.spaceWidth = FontHelper.getWidth(font, " ", 1.0f) / Settings.scale;
        this.fontLineHeight = FontHelper.getHeight(font) / Settings.scale;

        this.lineWidth = lineWidth;
        this.lineSpacing = lineSpacing;
    }

    // --------------------------------------------------------------------------------

    /**
     * Appends the given text to the end of the label. The color of this text will be the one last set by {@link #withTextColor(Color)}. If that function has not been used, it will fallback to the base game's <code>Settings.CREAM_COLOR</code> pale white color. The given text string will be split by words (words are determined by calling <code>text.split(" ")</code> to split on spaces, skipping over empty strings), and then added onto the block at the end of the current line until a new line needs to be formed. A new line is automatically formed whenever adding a word will cause the current line's width to exceed the <code>lineWidth</code> set by the constructor.
     * @param text the text to append
     * @return this widget
     */
    public SmartLabel withText(String text) {
        appendTextByWords(text);
        return this;
    }

    /**
     * Appends the given text to the end of the label, with a specific (temporary) color. After adding all the text, the temporary color will revert back to the one set by the last call of {@link #withTextColor(Color)}, or the base game's <code>Settings.CREAM_COLOR</code> pale white color, if that function has not been used. The given text string will be split by words (words are determined by calling <code>text.split(" ")</code> to split on spaces, skipping over empty strings), and then added onto the block at the end of the current line until a new line needs to be formed. A new line is automatically formed whenever adding a word will cause the current line's width to exceed the <code>lineWidth</code> set by the constructor.
     * @param textColor the color for this block of text
     * @param text the text to append
     * @return this widget
     */
    public SmartLabel withText(Color textColor, String text) {
        Supplier<Color> previousSupplier = this.colorSupplier;
        this.colorSupplier = () -> textColor;

        appendTextByWords(text);

        this.colorSupplier = previousSupplier;
        return this;
    }

    /**
     * Appends the given text to the end of the label, with a specific (temporary) color. This variant uses a supplier so that you can have dynamically updated colors by including a reference to a function handle. After adding all the text, the temporary color will revert back to the one set by the last call of {@link #withTextColor(Color)}, or the base game's <code>Settings.CREAM_COLOR</code> pale white color, if that function has not been used. The given text string will be split by words (words are determined by calling <code>text.split(" ")</code> to split on spaces, skipping over empty strings), and then added onto the block at the end of the current line until a new line needs to be formed. A new line is automatically formed whenever adding a word will cause the current line's width to exceed the <code>lineWidth</code> set by the constructor.
     * @param colorSupplier the color for this block of text
     * @param text the text to append
     * @return this widget
     */
    public SmartLabel withText(Supplier<Color> colorSupplier, String text) {
        Supplier<Color> previousSupplier = this.colorSupplier;
        this.colorSupplier = colorSupplier;

        appendTextByWords(text);

        this.colorSupplier = previousSupplier;
        return this;
    }

    // --------------------------------------------------------------------------------

    /**
     * Sets the color of all future text appended via {@link #withText(String)}. If this function has not been used or if text was added prior to setting the color, the previously added text will fallback to the base game's <code>Settings.CREAM_COLOR</code> pale white color. If using one of the overloads that specifies a specific color, e.g. {@link #withText(Color, String)}, then this color is temporarily ignored for that block of text, before reverting back to this color after it finishes that block.
     * @param color the color for all future appended text
     * @return this widget
     */
    public SmartLabel withTextColor(Color color) {
        this.colorSupplier = () -> color;
        return this;
    }

    /**
     * Sets the color of all future text appended via {@link #withText(String)} with a dynamic supplier. If this function has not been used or if text was added prior to setting the color, the previously added text will fallback to the base game's <code>Settings.CREAM_COLOR</code> pale white color. If using one of the overloads that specifies a specific color, e.g. {@link #withText(Color, String)}, then this color is temporarily ignored for that block of text, before reverting back to this color after it finishes that block.
     * @param colorSupplier the color for all future appended text
     * @return this widget
     */
    public SmartLabel withTextColor(Supplier<Color> colorSupplier) {
        this.colorSupplier = colorSupplier;
        return this;
    }

    /**
     * Adds a certain number of newlines to the ongoing text block. This won't do anything visible until the next {@link #withText(String)} is called. Calling this function with a <code>count = 1</code> means the next block of text appended will have its first word at the start of its own line, while a <code>count = 2</code> leaves an empty line in between any previously added text and the next. Undefined behavior if <code>count</code> is less than or equal to zero. This function is intended to be used between two distinct calls of the {@link #withText(String)} family of methods.
     * @param count the number of new lines to add before the next text block is appended
     * @return this widget
     */
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
            if (word.isEmpty())
                continue;

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