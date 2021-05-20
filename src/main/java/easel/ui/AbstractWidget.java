package easel.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import easel.Easel;
import easel.ui.interactive.MoveableWidget2;

import java.util.function.Consumer;

/**
 * <p>
 * Widgets are the primary feature of the easel library. Widgets are typically arranged in a tree-like hierarchy, where the root of the tree receives the notification to perform an action (e.g. <code>render()</code> or <code>update()</code>) and passes that information down to all descendants.
 * </p>
 * <p>
 * Proper widget use is essentially a three-stage process: initialization, anchoring, and rendering. It is worth noting that interactive widgets (e.g. buttons) follow the same general pattern, but also need to consider an update step as well. The initialization step lets you construct the widget hierarchy, assign widgets to layout managers, and give each widget the information it would require to render "in a void". The anchoring step lets you position the widget on the screen (moving from the idea of being able to render "in a void" to "in the proper place on screen"). Finally, the rendering step simply ensures every widget in the tree renders to the position that was previously set by the anchoring step.
 * </p>
 * <ul>
 *     <li>Initialization: often performed by constructors or initialization methods; the typical pattern is to visualize the tree from outside-in, from parent widgets to children. By the time initialization ends, you can use functions like {@link #getContentWidth()} and have meaningful information about a widget. </li>
 *     <li>Anchoring: must be used at least once before rendering in order for the widget to be placed on screen in the proper place. For more dynamic widgets that move around from frame to frame, anchoring can often be considered a part of the rendering step, and it is often convenient to call an <code>anchorAt</code> right before the render call. Most anchoring is aided by Layout widgets (e.g. {@link easel.ui.layouts.GridLayout}, {@link easel.ui.layouts.HorizontalLayout}, etc.). When using these layout helper classes, you just need to tell the layout where to position itself and it will handle its children automatically. Layouts are extremely convenient: use them! By the end of the anchoring step, widgets are in the proper place on screen - letting you use functions like {@link #getContentLeft()}.</li>
 *     <li>Rendering: draws the widget onto the screen using a SpriteBatch. For "static" widgets that don't move, you typically anchor one time (directly after the initialization step, often inside the constructor itself), and the render loop just reuses that positioning information over and over. There is no need to recall the <code>anchoredAt</code> family of methods each frame, unless the widget moves again. For "dynamic" widgets that move (e.g. tooltips that follow the mouse cursor), you typically want to anchor right before you render, e.g. <code>widget.anchoredAt(x, y, AnchorPosition.CENTER).render(sb)</code>. </li>
 * </ul>
 * <p>
 * Let's explore some example code to hopefully make these ideas a bit more concrete:
 * </p>
 * <pre>
 * {@code
 * public class Foo implements RenderSubscriber {
 *     private HorizontalLayout child;
 *
 *     public Foo() {
 *         // Since this Foo widget can be considered the "top" of the widget hierarchy, it's okay to subscribe to the
 *         //   render method of BaseMod so that we get told when to render.
 *         BaseMod.subscribe(this);
 *
 *         // Here we make a new horizontal layout containing three labels. Each label will be aligned along the bottom
 *         //   of the horizontal layout (which is 40 px tall) and spaced 20px apart.
 *         //
 *         // We also perform the anchoring step right here as well, since it's convenient and the widget won't move.
 *         child = new HorizontalLayout(40, 20)
 *                     .withDefaultChildAnchor(AnchorPosition.CENTER_BOTTOM)
 *                     .withChild(new Label("Left"))
 *                     .withChild(new Label("Center"))
 *                     .withChild(new Label("Right"))
 *                     .anchoredCenteredOnScreen();
 *     }
 *
 *     // This function gets called every frame thanks to our BaseMod event subscription.
 *     public void receiveRender(SpriteBatch sb) {
 *         // We're going to simply hide the actual game by painting a black rectangle which covers the entire screen
 *         //  to make it easier to see our widgets in action
 *         sb.setColor(Color.BLACK);
 *         sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0, 0, Settings.WIDTH, Settings.HEIGHT);
 *
 *         // Then, this is all we need to do to render our centered layout. Easy.
 *         child.render(sb);
 *     }
 * }
 * }
 * </pre>
 * <p>
 * Note how easy this was to get up and running - in just a few lines of code, we've built a pretty useful way of displaying information to the screen! The HorizontalLayout does most of the alignment fiddling for us (we just told it to render on the center of the screen, and it figured out where to put the labels accordingly). We followed our three step process: we built all the widgets in our constructor, anchored our root widget, and then our render loop made sure to render down the hierarchy.
 * </p>
 * <p>
 * Let's consider one more modification to this contrived example by letting the labels follow the mouse. First, we can take out the <code>anchoredAt</code> call inside the constructor since we're going to anchor in our render loop instead:
 * </p>
 * <pre>
 * {@code
 *         child = new HorizontalLayout(40, 20)
 *                     .withDefaultChildAnchor(AnchorPosition.CENTER_BOTTOM)
 *                     .withChild(new Label("Left"))
 *                     .withChild(new Label("Center"))
 *                     .withChild(new Label("Right"));
 * }
 * </pre>
 * <p>
 * Then, we can rewrite our render function a bit to anchor to the mouse position:
 * </p>
 * <pre>
 * {@code
 *         // ... (other code drawing a black rectangle to see the widgets better) ...
 *
 *         // Update the anchor to the mouse position each frame (this uses the clamping
 *         //   override to ensure we stay at least 20px away from the screen border)
 *         child.anchoredAt(InputHelper.mX, InputHelper.mY, AnchorPosition.CENTER, 20)
 *              .render(sb);
 * }
 * </pre>
 * <p>
 * Now when we run this sample code, the labels will still be arranged nicely (aligned along the bottom with 20px spacing in between each) but the whole thing will follow the mouse cursor. We've even used the {@link #anchoredAt(float, float, AnchorPosition, float)} override that ensures that nothing will render off-screen!
 * </p>
 * <p>
 * Building more complicated widget hierarchies never really gets more complex than this as long as you follow the 3 step process. Often it is convenient to make your own classes extend <code>AbstractWidget</code> in order to take advantage of the various automatic layout managers; this approach is recommended once you find yourself having to deal with excessive manual placement of widgets, since the layouts were originally designed to make handling many widgets as painless as possible. As long as you stick to convention, it is pretty easy to add new functionality and compose widgets together in new and exciting ways.
 * </p>
 * <p>
 * Eventually, we hope to include some additional samples to study which provide more practical examples to follow. For now, please explore the various classes on this javadoc and take a look at the functions to see what's available.
 * </p>
 * @param <T> a self-type pattern; when extending AbstractWidget with your own custom class, make sure to extend it like: <code>public class MyWidget extends AbstractWidget{@literal <}MyWidget{@literal >} { ... }</code>. This lets the builder pattern-esque functions work nicely and be easily chainable without explicit casts.
 */
public abstract class AbstractWidget<T extends AbstractWidget<T>> {
    private float marginLeft, marginRight, marginTop, marginBottom;

    private float x, y;
    private float targetX, targetY;
    private InterpolationSpeed interpolationSpeed = InterpolationSpeed.INSTANT;

    // --------------------------------------------------------------------------------

    protected boolean hasInteractivity;

    protected Hitbox hb;
    protected boolean leftClickStarted;
    protected boolean rightClickStarted;
    protected boolean isHovered;

    private final Consumer<T> NOOP = x -> {};

    protected Consumer<T> onLeftClick = NOOP;
    protected Consumer<T> onRightClick = NOOP;
    protected Consumer<T> onMouseEnter = NOOP;
    protected Consumer<T> onMouseLeave = NOOP;

    private boolean hasMoveable;
    private MoveableWidget2 moveable;

    // --------------------------------------------------------------------------------

    /**
     * The internal content width of the widget (excludes margins). Safe to use before anchoring (values should be accurate after constructor).
     * @return the width of this widget (excluding margins)
     * @see #getWidth()
     * @see #getContentHeight()
     */
    public abstract float getContentWidth();

    /**
     * The internal content height of the widget (excludes margins). Safe to use before anchoring (values should be accurate after constructor).
     * @return the height of this widget (excluding margins)
     * @see #getHeight()
     * @see #getContentWidth()
     */
    public abstract float getContentHeight();

    // --------------------------------------------------------------------------------
    // Some basic builder methods. (Widgets will probably add some of their own)
    // --------------------------------------------------------------------------------

    /**
     * Sets all margins to the same value.
     * @param all the value to set each margin, in pixels
     * @return this widget
     * @see #withMargins(float, float, float, float)
     */
    public T withMargins(float all) {
        this.marginLeft = this.marginBottom = this.marginRight = this.marginTop = all;
        return (T)this;
    }

    /**
     * Sets the left and right margins to <code>horizontal</code> and the bottom and top margins to <code>vertical</code>.
     * @param horizontal value to set left and right margins, in pixels
     * @param vertical value to set bottom and top margins, in pixels
     * @return this widget
     * @see #withMargins(float, float, float, float)
     */
    public T withMargins(float horizontal, float vertical) {
        this.marginLeft = this.marginRight = horizontal;
        this.marginBottom = this.marginTop = vertical;
        return (T)this;
    }

    /**
     * <p>
     * Sets each margin independently. The margins refer to how much space is allotted to the left, right, above, and below the widget before rendering. Essentially, widgets contain two distinct bounding boxes: the inner content (see: {@link #getContentWidth()} and {@link #getContentHeight()}), and the full area (see: {@link #getWidth()} and {@link #getHeight()}). With margins set to zero (the default value), these two areas are the same (e.g. <code>getContentWidth() == getWidth()</code>. As you grow the margins, the total width and height grows as well (but the inner content remains untouched).
     * </p>
     *
     * <p>
     * The inner content dimensions are kept constant based on the widget itself - as it can be considered how much area the widget needs to render itself in its entirety. Thus, any <code>render()</code> calls will just refer to the content locations and dimensions (e.g. {@link #getContentLeft()}), while any layout specific code will require the full dimensions including the margins to align things correctly.
     * </p>
     *
     * Margins are simply a tool to make it easier to offset widgets away from edges in an intuitive fashion. Most of the time you probably won't need to use margins at the individual widget level but instead reserve them for layout widget(s) to have some sort of outer padding. TL;DR: margins can often make it much easier to align things in an aesthetic manner.
     * @param left left margin, in pixels
     * @param right right margin, in pixels
     * @param bottom bottom margin, in pixels
     * @param top top margin, in pixels
     * @return this widget
     * @see #withMargins(float, float)
     * @see #withMargins(float)
     */
    public T withMargins(float left, float right, float bottom, float top) {
        this.marginLeft = left;
        this.marginRight = right;
        this.marginBottom = bottom;
        this.marginTop = top;
        return (T)this;
    }

    // --------------------------------------------------------------------------------
    // The final step of the pseudo-builder pattern. Required for rendering.
    // --------------------------------------------------------------------------------

    /**
     * <p>
     * Moves the widget towards a specific spot. The <code>anchorPosition</code> defines the point on the widget that will be placed at the position <code>(x, y)</code>.
     * </p>
     * <p>
     * For example, <code>anchoredAt(100, 200, AnchorPosition.LEFT_BOTTOM, InterpolationSpeed.FAST)</code> will set the bottom-left corner of the widget to 100 pixels from the left side of the screen and 200 pixels from the bottom of the screen. The widget then renders up and to the right of this point since we anchored at an <code>AnchorPosition.LEFT_BOTTOM</code>. As a second example, using <code>AnchorPosition.CENTER</code> ensures that <code>(x, y)</code> will be the center of the widget.
     * </p>
     * <p>
     * The interpolation speed <code>withDelay</code> determines how quickly the widget moves to the target location. Using a speed other than <code>InterpolationSpeed.INSTANT</code> makes the widget move towards the desired position over the next few frames in a smoothly animated manner. For convenience since instant moving is often the desired effect, see {@link #anchoredAt(float, float, AnchorPosition)}.
     * </p>
     * <p>
     * Note: you should always anchor at least once before rendering. For more dynamic widgets that move a lot (e.g. a tooltip dependent on the mouse cursor location using <code>InputHelper.mX</code> and <code>InputHelper.mY</code>), a general pattern is to call <code>anchoredAt</code> right before rendering in your main render function, e.g.:
     * </p>
     * <pre>
     * {@code
     * widget.anchoredAt(x, y, AnchorPosition.CENTER, InterpolationSpeed.INSTANT)
     *       .render(sb);
     * }
     * </pre>
     * @param x the x position in pixels from the left edge of the screen
     * @param y the y position in pixels from the bottom edge of the screen
     * @param anchorPosition what piece of the widget will be moved to <code>(x, y)</code>
     * @param withDelay how quickly the widget will move towards the desired position
     * @return this widget
     * @see #anchoredAt(float, float, AnchorPosition)
     * @see #anchoredAt(float, float, AnchorPosition, float)
     * @see #anchoredAt(float, float, AnchorPosition, InterpolationSpeed, float)
     */
    public T anchoredAt(float x, float y, AnchorPosition anchorPosition, InterpolationSpeed withDelay) {
        this.targetX = anchorPosition.isLeft() ? x : (anchorPosition.isCenterX() ? x - 0.5f * getWidth() : x - getWidth());
        this.targetY = anchorPosition.isBottom() ? y : (anchorPosition.isCenterY() ? y - 0.5f * getHeight() : y - getHeight());

        this.interpolationSpeed = withDelay;

        if (withDelay == InterpolationSpeed.INSTANT) {
            this.x = targetX;
            this.y = targetY;
        }

        moveHitboxToTarget(targetX, targetY);

        return (T)this;
    }

    /**
     * <p>
     * Instantly moves the widget to a specific spot. The <code>anchorPosition</code> defines the point on the widget that will be placed at the position <code>(x, y)</code>.
     * </p>
     * <p>
     * For example, <code>anchoredAt(100, 200, AnchorPosition.LEFT_BOTTOM)</code> will set the bottom-left corner of the widget to 100 pixels from the left side of the screen and 200 pixels from the bottom of the screen. The widget then renders up and to the right of this point since we anchored at an <code>AnchorPosition.LEFT_BOTTOM</code>. As a second example, using <code>AnchorPosition.CENTER</code> ensures that <code>(x, y)</code> will be the center of the widget.
     * </p>
     * <p>
     * Note: you should always anchor at least once before rendering. For more dynamic widgets that move a lot (e.g. a tooltip dependent on the mouse cursor location using <code>InputHelper.mX</code> and <code>InputHelper.mY</code>), a general pattern is to call <code>anchoredAt</code> right before rendering in your main render function, e.g.:
     * </p>
     * <pre>
     * {@code
     * widget.anchoredAt(x, y, AnchorPosition.CENTER)
     *       .render(sb);
     * }
     * </pre>
     * @param x the x position in pixels from the left edge of the screen
     * @param y the y position in pixels from the bottom edge of the screen
     * @param anchorPosition what piece of the widget will be moved to <code>(x, y)</code>
     * @return this widget
     * @see #anchoredAt(float, float, AnchorPosition, InterpolationSpeed)
     * @see #anchoredAt(float, float, AnchorPosition, float)
     * @see #anchoredAt(float, float, AnchorPosition, InterpolationSpeed, float)
     */
    public final T anchoredAt(float x, float y, AnchorPosition anchorPosition) {
        return anchoredAt(x, y, anchorPosition, InterpolationSpeed.INSTANT);
    }

    /**
     * <p>
     * Moves the widget towards a specific spot, ensuring that the target location is clamped inside the viewing area. The <code>anchorPosition</code> defines the point on the widget that will (attempt) to be placed at the position <code>(x, y)</code>, unless putting it there would render part of the widget outside the screen.
     * </p>
     * <p>
     * This method uses the <code>clampedBorder</code> parameter to restrict the positioning such that an edge of the widget's final bounding box is at least <code>clampedBorder</code> pixels away from the absolute edge of the screen. The most common reason to use this clamping variant of the <code>anchoredAt</code> family is to ensure tooltips (or other widgets) can attach to the mouse position but remain entirely on screen. If you do not clamp, portions of your tooltip may be unreadable and rendered offscreen. The <code>clampedBorder</code> should be non-negative.
     * </p>
     * <p>
     * If the widget is larger than the screen in some dimension, (e.g. the widget is wider than the total width of the screen minus twice how much border spacing is allocated with <code>clampedBorder</code>), the behavior of this method should be considered undefined.
     * </p>
     * <p>
     * The interpolation speed <code>withDelay</code> determines how quickly the widget moves to the target location. Using a speed other than <code>InterpolationSpeed.INSTANT</code> makes the widget move towards the desired position over the next few frames in a smoothly animated manner. For convenience since instant moving is often the desired effect, see {@link #anchoredAt(float, float, AnchorPosition, float)}.
     * </p>
     * <p>
     * Note: you should always anchor at least once before rendering. For more dynamic widgets that move a lot (e.g. a tooltip dependent on the mouse cursor location using <code>InputHelper.mX</code> and <code>InputHelper.mY</code>), a general pattern is to call <code>anchoredAt</code> right before rendering in your main render function, e.g.:
     * </p>
     * <pre>
     * {@code
     * widget.anchoredAt(x, y, AnchorPosition.CENTER, InterpolationSpeed.INSTANT, 20)
     *       .render(sb);
     * }
     * </pre>
     * @param x the x position in pixels from the left edge of the screen
     * @param y the y position in pixels from the bottom edge of the screen
     * @param anchorPosition what piece of the widget will be moved to <code>(x, y)</code>
     * @param withDelay how quickly the widget will move towards the desired position
     * @param clampedBorder how close this widget is allowed to get to the outer edge of the screen
     * @return this widget
     * @see #anchoredAt(float, float, AnchorPosition, float)
     * @see #anchoredAt(float, float, AnchorPosition)
     */
    public final T anchoredAt(float x, float y, AnchorPosition anchorPosition, InterpolationSpeed withDelay, float clampedBorder) {
        this.targetX = anchorPosition.isLeft() ? x : (anchorPosition.isCenterX() ? x - 0.5f * getWidth() : x - getWidth());
        this.targetY = anchorPosition.isBottom() ? y : (anchorPosition.isCenterY() ? y - 0.5f * getHeight() : y - getHeight());

        // Clamp bottom left
        if (targetX < clampedBorder)
            this.targetX = clampedBorder;
        if (targetY < clampedBorder)
            this.targetY = clampedBorder;

        // Clamp top right
        if (targetX + getWidth() > (Settings.WIDTH - clampedBorder))
            this.targetX = Settings.WIDTH - clampedBorder - getWidth();
        if (targetY + getHeight() > (Settings.HEIGHT - clampedBorder))
            this.targetY = Settings.HEIGHT - clampedBorder - getHeight();

        // Do the anchoring
        return anchoredAt(targetX, targetY, AnchorPosition.LEFT_BOTTOM, withDelay);
    }

    /**
     * <p>
     * Instantly moves the widget towards a specific spot, ensuring that the target location is clamped inside the viewing area. The <code>anchorPosition</code> defines the point on the widget that will (attempt) to be placed at the position <code>(x, y)</code>, unless putting it there would render part of the widget outside the screen.
     * </p>
     * <p>
     * This method uses the <code>clampedBorder</code> parameter to restrict the positioning such that an edge of the widget's final bounding box is at least <code>clampedBorder</code> pixels away from the absolute edge of the screen. The most common reason to use this clamping variant of the <code>anchoredAt</code> family is to ensure tooltips (or other widgets) can attach to the mouse position but remain entirely on screen. If you do not clamp, portions of your tooltip may be unreadable and rendered offscreen. The <code>clampedBorder</code> should be non-negative.
     * </p>
     * <p>
     * If the widget is larger than the screen in some dimension, (e.g. the widget is wider than the total width of the screen minus twice how much border spacing is allocated with <code>clampedBorder</code>), the behavior of this method should be considered undefined.
     * </p>
     * <p>
     * Note: you should always anchor at least once before rendering. For more dynamic widgets that move a lot (e.g. a tooltip dependent on the mouse cursor location using <code>InputHelper.mX</code> and <code>InputHelper.mY</code>), a general pattern is to call <code>anchoredAt</code> right before rendering in your main render function, e.g.:
     * </p>
     * <pre>
     * {@code
     * widget.anchoredAt(x, y, AnchorPosition.CENTER, 20)
     *       .render(sb);
     * }
     * </pre>
     * @param x the x position in pixels from the left edge of the screen
     * @param y the y position in pixels from the bottom edge of the screen
     * @param anchorPosition what piece of the widget will be moved to <code>(x, y)</code>
     * @param clampedBorder how close an edge of the widget is allowed to get to an edge of the screen (in pixels)
     * @return this widget
     * @see #anchoredAt(float, float, AnchorPosition, float)
     * @see #anchoredAt(float, float, AnchorPosition)
     */
    public final T anchoredAt(float x, float y, AnchorPosition anchorPosition, float clampedBorder) {
        return anchoredAt(x, y, anchorPosition, InterpolationSpeed.INSTANT, clampedBorder);
    }

    /**
     * Instantly move the widget such that the widget's center point is centered on the screen's center point.
     * @return this widget
     * @see #anchoredCenteredOnScreen(InterpolationSpeed)
     * @see #anchoredAt(float, float, AnchorPosition)
     */
    public final T anchoredCenteredOnScreen() {
        return anchoredCenteredOnScreen(InterpolationSpeed.INSTANT);
    }

    /**
     * Moves the widget such that the widget's center point is centered on the screen's center point. The interpolation speed <code>withDelay</code> determines how quickly the move happens; if it is not <code>InterpolationSpeed.INSTANT</code>, the move will smoothly animate over the next few frames.
     * @param withDelay how quickly to move the widget
     * @return this widget
     * @see #anchoredCenteredOnScreen()
     * @see #anchoredAt(float, float, AnchorPosition, InterpolationSpeed)
     */
    public T anchoredCenteredOnScreen(InterpolationSpeed withDelay) {
        float screenCenterX = (Settings.WIDTH / 2.0f) / Settings.xScale;
        float screenCenterY = (Settings.HEIGHT / 2.0f) / Settings.yScale;

        return anchoredAt(screenCenterX, screenCenterY, AnchorPosition.CENTER, withDelay);
    }

    // --------------------------------------------------------------------------------
    // These can be obtained before (x,y) are set by the anchor
    // --------------------------------------------------------------------------------

    /**
     * The total width of this widget (includes the content width and both left and right margins). Safe to use before anchoring (values should be accurate after constructor).
     * @return the total width of this widget (including margins)
     * @see #getContentWidth()
     * @see #getHeight()
     */
    public float getWidth() { return marginLeft + getContentWidth() + marginRight; }

    /**
     * The total height of this widget (includes the content height and both bottom and top margins). Safe to use before anchoring (values should be accurate after constructor).
     * @return the total height of this widget (including margins)
     * @see #getContentHeight()
     * @see #getWidth()
     */
    public float getHeight() { return marginBottom + getContentHeight() + marginTop; }

    // --------------------------------------------------------------------------------
    // These should only be used after setting the anchor position
    // --------------------------------------------------------------------------------
    /**
     * The left-most point of the inner content area, useful for internal widget rendering. Undefined behavior if used before anchoring at least once.
     * @return the left-most point (past the left margin)
     * @see #getLeft()
     */
    public float getContentLeft() { return x + marginLeft; }

    /**
     * The right-most point of the inner content area, useful for internal widget rendering. Undefined behavior if used before anchoring at least once.
     * @return the right-most point (minus the right margin)
     * @see #getRight()
     */
    public float getContentRight() { return x + marginLeft + getContentWidth(); }

    /**
     * The bottom-most point of the inner content area, useful for internal widget rendering. Undefined behavior if used before anchoring at least once.
     * @return the bottom-most point (above the bottom margin)
     * @see #getBottom()
     */
    public float getContentBottom() { return y + marginBottom; }

    /**
     * The upper-most point of the inner content area, useful for internal widget rendering. Undefined behavior if used before anchoring at least once.
     * @return the top-most point (below the top margin)
     * @see #getTop()
     */
    public float getContentTop() { return y + marginBottom + getContentHeight(); }

    /**
     * The horizontal center of the inner content area, useful for internal widget rendering. Computed as <code>getLeft() + marginLeft + 0.5f * getContentWidth()</code>. Undefined behavior if used before anchoring at least once.
     * @return the horizontal center of this widget
     * @see #getContentCenterY()
     */
    public float getContentCenterX() { return x + marginLeft + 0.5f * getContentWidth(); }

    /**
     * The vertical center of the inner content area, useful for internal widget rendering. Computed as <code>getBottom() + marginBottom + 0.5f * getContentHeight()</code>. Undefined behavior if used before anchoring at least once.
     * @return the vertical center of this widget
     * @see #getContentCenterX()
     */
    public float getContentCenterY() { return y + marginBottom + 0.5f * getContentHeight(); }

    /**
     * The absolute left most point of the widget, useful for layout managers. Undefined behavior if used before anchoring at least once.
     * @return the left-most point (ignores margins)
     * @see #getContentLeft()
     */
    public float getLeft() { return x; }


    /**
     * The absolute bottom-most point of the widget, useful for layout managers. Undefined behavior if used before anchoring at least once.
     * @return the bottom-most point (ignores margins)
     * @see #getContentBottom()
     */
    public float getBottom() { return y; }

    /**
     * The absolute top-most point of the widget, useful for layout managers. Undefined behavior if used before anchoring at least once.
     * @return the top-most point (ignores margins)
     * @see #getContentTop()
     */
    public float getTop() { return y + getHeight(); }

    /**
     * The absolute right-most point of the widget, useful for layout managers. Undefined behavior if used before anchoring at least once.
     * @return the right-most point (ignores margins)
     * @see #getContentRight()
     */
    public float getRight() { return x + getWidth(); }

    // --------------------------------------------------------------------------------

    /**
     * Moves the widget to the target anchor position. This occurs when an <code>anchorAt</code> is called with an <code>InterpolationSpeed</code> other than <code>InterpolationSpeed.INSTANT</code>.
     */
    protected void moveTowardsTarget() {
        if (x != targetX || y != targetY) {
            this.x = interpolationSpeed.interpolate(x, targetX);
            this.y = interpolationSpeed.interpolate(y, targetY);
        }
    }

    /**
     * Renders this widget onto the SpriteBatch. The position it is rendered at is determined by previously calling one of the anchoring methods, e.g. {@link #anchoredAt(float, float, AnchorPosition)}. If this widget changes the SpriteBatch parameters at all mid render (e.g. adding a shader, calling a sb.end(), etc.), these changes will be reset by the end of the render call so that the SpriteBatch has the same settings at the end as it did when entering this function. Container widgets (e.g. {@link easel.ui.layouts.VerticalLayout} etc.) will render all children that they manage.
     * @param sb the SpriteBatch to render this widget upon
     */
    public final void render(SpriteBatch sb) {
        moveTowardsTarget();
        renderWidget(sb);

        if (hasInteractivity)
            hb.render(sb);

        if (hasMoveable)
            moveable.render(sb);
    }

    /**
     * <p>
     * Custom widgets should implement this method for rendering. Use the inner content positions (e.g. {@link #getContentLeft()}, {@link #getContentWidth()}, etc.) to determine any position information necessary for rendering at a specific location. If the library is used as intended, these content locations should be accurate to where the widget needs to be rendered, as they reflect the most up to date location set by an anchoredAt call (this automatically will be interpolated if the anchorAt move is set to occur over several frames).
     * </p>
     * <p>
     * Note: you NEED to revert any changes you make to the SpriteBatch (e.g. setting a shader, changing the perspective matrix, etc.) by the time this function returns, as the SpriteBatch will be reused for rendering other widgets which will not expect those changes. You also don't technically need to render to this particular SpriteBatch (e.g. you can render to your own batch if you know what you're doing), as long as you follow the general intent of this function to render the widget.
     * </p>
     * @param sb the SpriteBatch the widget should be rendered on
     */
    protected abstract void renderWidget(SpriteBatch sb);

    // --------------------------------------------------------------------------------
    // Interactivity
    // --------------------------------------------------------------------------------

    protected void scaleHitboxToContent() {
        if (this.hb == null)
            this.hb = new Hitbox(getContentWidth() * Settings.xScale, getContentHeight() * Settings.yScale);
        else
            this.hb.resize(getContentWidth() * Settings.xScale, getContentHeight() * Settings.yScale);

        if (hasMoveable) {
            this.moveable.hb.resize(getContentWidth() * Settings.xScale, getContentHeight() * Settings.yScale);
        }

    }

    protected void enableInteractivity() {
        this.hasInteractivity = true;
        scaleHitboxToContent();
    }

    public T onLeftClick(Consumer<T> onLeftClick) {
        this.onLeftClick = onLeftClick;
        enableInteractivity();
        return (T)this;
    }

    public T onRightClick(Consumer<T> onRightClick) {
        this.onRightClick = onRightClick;
        enableInteractivity();
        return (T)this;
    }

    public T onMouseEnter(Consumer<T> onMouseEnter) {
        this.onMouseEnter = onMouseEnter;
        enableInteractivity();
        return (T)this;
    }

    public T onMouseLeave(Consumer<T> onMouseLeave) {
        this.onMouseLeave = onMouseLeave;
        enableInteractivity();
        return (T)this;
    }

    /**
     * Update this widget if it requires any logic updates each frame. Mostly for interactive widgets (as this is the spot to update hitboxes to see if they're moused over or clicked, etc.) which need updates each frame. Container widgets (e.g. {@link easel.ui.layouts.VerticalLayout}) will pass updates to all their children. The top-most widget in the hierarchy can subscribe to BaseMod's post update subscriber (or via some other SpirePatch), but everything else lower down should NOT subscribe and instead just receive their update notifications from their parent widget. The update() function for non-interactive widgets with no children is essentially a NO-OP.
     *
     * As this is called once per frame - for more complicated interactive widgets, you should try and avoid recomputing expensive things here unless absolutely needed. For many scenarios, it is better for custom widgets to compute information at one designated time and cache the results to be displayed later, instead of re-computing it again and again.
     */
    public final void update() {
        updateInteractivity();
        updateWidget();
    }

    private void moveHitboxToTarget(float targetLeft, float targetBottom) {
        float centerX = targetLeft + (0.5f * getContentWidth());
        float centerY = targetBottom + (0.5f * getContentHeight());

        if (hasInteractivity)
            hb.move(centerX * Settings.xScale,
                    centerY * Settings.yScale);

        if (hasMoveable)
            moveable.hb.move(centerX * Settings.xScale,
                    centerY * Settings.yScale);
    }

    protected void updateInteractivity() {
        // Update interactive pieces of this widget
        if (hasInteractivity) {
            hb.update();

            // Hover (mouse enter / leave)
            updateHoverTransitions();

            // Mouse button down / up
            updateLeftClicks();
            updateRightClicks();
        }

        if (hasMoveable) {
            moveable.update();
        }
    }

    private void updateHoverTransitions() {
        if (hb.hovered && !isHovered) {
            onMouseEnter.accept((T)this);

            Easel.logger.info("Hover started");
            Easel.logger.info(this);

            isHovered = true;
        }
        else if (!hb.hovered && isHovered){
            onMouseLeave.accept((T)this);

            Easel.logger.info("Hover finished");
            Easel.logger.info(this);

            isHovered = false;
        }
    }

    private void updateLeftClicks() {
        // Left click started
        if (isHovered && InputHelper.justClickedLeft) {
            leftClickStarted = true;
        }
        else if (hb.hovered && CInputActionSet.select.isJustPressed()) {
            CInputActionSet.select.unpress();
            onLeftClick.accept((T)this);

            Easel.logger.info("Clicked (using CInputActionSet)");
            Easel.logger.info(this);
        }

        // Left click ended
        if (leftClickStarted && InputHelper.justReleasedClickLeft) {
            if (isHovered) {
                onLeftClick.accept((T)this);

                Easel.logger.info("Clicked (regular)");
                Easel.logger.info(this);
            }

            leftClickStarted = false;
        }
    }

    private void updateRightClicks() {
        // Right click started
        if (isHovered && InputHelper.justClickedRight) {
            rightClickStarted = true;
        }
        // Should look into how to use input action set for right clicks - e.g. what is the key used when previewing card upgrades in shops?
//        else if (hb.hovered && CInputActionSet.???.isJustPressed()) {
//            CInputActionSet.select.unpress();
//            onLeftClick.accept((T)this);
//
//            Easel.logger.info("Clicked (using CInputActionSet)");
//            Easel.logger.info(this);
//        }

        // Right click ended
        if (rightClickStarted && InputHelper.justReleasedClickRight) {
            if (isHovered) {
                onRightClick.accept((T)this);

                Easel.logger.info("Right Clicked (regular)");
                Easel.logger.info(this);
            }

            rightClickStarted = false;
        }
    }

    public T makeMoveable() {
        this.hasMoveable = true;
        this.moveable = new MoveableWidget2(this);

        return (T)this;
    }

    /**
     * Required for interactive components. For containers, make sure to call <code>update()</code> on all children. For widgets that require some sort of updates each frame, you can do so here.
     */
    protected void updateWidget() {

    }

    // --------------------------------------------------------------------------------
    // Usually for hitboxes, but can be used to enable/disable computations required each frame - these should recurse
    // down the hierarchy when appropriate

    public void show() {}
    public void hide() {}
}
