package easel.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;

public abstract class AbstractWidget<T extends AbstractWidget<T>> {
    private float marginLeft, marginRight, marginTop, marginBottom;

    private float x, y;
    private float targetX, targetY;
    private InterpolationSpeed interpolationSpeed = InterpolationSpeed.INSTANT;

    public abstract float getContentWidth();
    public abstract float getContentHeight();

    // --------------------------------------------------------------------------------
    // Some basic builder methods. (Widgets will probably add some of their own)
    // --------------------------------------------------------------------------------

    public void setMargins(float all) { this.marginLeft = this.marginRight = this.marginTop = this.marginBottom = all; }
    public T withMargins(float all) {
        this.setMargins(all);
        return (T)this;
    }

    public void setMargins(float horizontal, float vertical) {
        this.marginLeft = this.marginRight = horizontal;
        this.marginBottom = this.marginTop = vertical;
    }
    public T withMargins(float horizontal, float vertical) {
        this.setMargins(horizontal, vertical);
        return (T)this;
    }

    public void setMargins(float left, float right, float bottom, float top) {
        this.marginLeft = left;
        this.marginRight = right;
        this.marginBottom = bottom;
        this.marginTop = top;
    }

    public T withMargins(float left, float right, float bottom, float top) {
        this.setMargins(left, right, bottom, top);
        return (T)this;
    }

    // --------------------------------------------------------------------------------
    // The final step of the pseudo-builder pattern. Required for rendering.
    // --------------------------------------------------------------------------------
    public T anchoredAt(float x, float y, AnchorPosition anchorPosition) {
        return anchoredAt(x, y, anchorPosition, InterpolationSpeed.INSTANT);
    }

    public T anchoredAt(float x, float y, AnchorPosition anchorPosition, InterpolationSpeed withDelay) {
        this.targetX = anchorPosition.isLeft() ? x : (anchorPosition.isCenterX() ? x - 0.5f * getWidth() : x - getWidth());
        this.targetY = anchorPosition.isBottom() ? y : (anchorPosition.isCenterY() ? y - 0.5f * getHeight() : y - getHeight());

        this.interpolationSpeed = withDelay;

        if (withDelay == InterpolationSpeed.INSTANT) {
            this.x = targetX;
            this.y = targetY;
        }

        return (T)this;
    }

    public T anchorCenteredOnScreen() {
        return anchorCenteredOnScreen(InterpolationSpeed.INSTANT);
    }

    public T anchorCenteredOnScreen(InterpolationSpeed withDelay) {
        float screenCenterX = (Settings.WIDTH / 2.0f) / Settings.xScale;
        float screenCenterY = (Settings.HEIGHT / 2.0f) / Settings.yScale;

        return anchoredAt(screenCenterX, screenCenterY, AnchorPosition.CENTER, withDelay);

//        float screenCenterX = (Settings.WIDTH / 2.0f) / Settings.xScale;
//        float screenCenterY = (Settings.HEIGHT / 2.0f) / Settings.yScale;
//
//        this.targetX = screenCenterX - (0.5f * getWidth());
//        this.targetY = screenCenterY - (0.5f * getHeight());
//
//        this.interpolationSpeed = withDelay;
//
//        if (withDelay == InterpolationSpeed.INSTANT) {
//            this.x = targetX;
//            this.y = targetY;
//        }

        //return (T)this;
    }

    /*
      Prevent the left, right, top, bottom edges from going outside the screen, e.g.
       +--------+          +--------+
       |       xxx    ->   |     xxx|
       +--------+          +--------+
    */
    /**
     * Instantly snap the edges back into the viewing area. Note: the top right snapping takes priority if the widget
     * is larger than the screen, although that is undefined behavior (widgets should fit inside the view area).
     * @param border How close to the edge of the screen can be to this widget. E.g. a value of 0 means the left most
     *               edge of the widget can align exactly with the left most edge of the screen, but it cannot start
     *               rendering further to the left of the screen and get cut off.
     * @return this widget
     */
    public T instantClampIntoScreen(int border) {
        // TODO this function needs to account for xScale, yScale, as x and y are both in 1080p space, and
        //   Settings.WIDTH etc. may not be in the same coordinate system. (Need to verify / do testing here!)

        // Bottom left
        if (targetX < border)
            this.targetX = border;
        if (targetY < border)
            this.targetY = border;

        // Top right
        if (targetX + getWidth() > (Settings.WIDTH - border))
            this.targetX = Settings.WIDTH - border - getWidth();
        if (targetY + getHeight() > (Settings.HEIGHT - border))
            this.targetY = Settings.HEIGHT - border - getHeight();

        this.x = targetX;
        this.y = targetY;

        this.interpolationSpeed = InterpolationSpeed.INSTANT;

        return (T)this;
    }

    // --------------------------------------------------------------------------------

    // These can be obtained before (x,y) are set by the anchor
    public float getWidth() { return getContentWidth() + marginLeft + marginRight; }
    public float getHeight() { return getContentHeight() + marginBottom + marginTop; }

    // These should only be used after setting the anchor position
    public float getContentLeft() { return x + marginLeft; }
    public float getContentRight() { return x + getContentWidth() + marginLeft + marginRight; }

    public float getContentBottom() { return y + marginBottom; }
    public float getContentTop() { return y + getContentHeight() + marginBottom + marginTop; }

    public float getContentCenterX() { return x + marginLeft + 0.5f * getContentWidth(); }
    public float getContentCenterY() { return y + marginBottom + 0.5f * getContentHeight(); }

    // --------------------------------------------------------------------------------

    protected void moveTowardsTarget() {
        if (x != targetX || y != targetY) {
            this.x = interpolationSpeed.interpolate(x, targetX);
            this.y = interpolationSpeed.interpolate(y, targetY);
        }
    }

    public final void render(SpriteBatch sb) {
        moveTowardsTarget();
        renderWidget(sb);
    }

    protected abstract void renderWidget(SpriteBatch sb);
    public void update() {}

    // --------------------------------------------------------------------------------
    // Usually for hitboxes, but can be used to enable/disable computations required each frame - these should recurse
    // down the hierarchy when appropriate

    public void show() {}
    public void hide() {}
}
