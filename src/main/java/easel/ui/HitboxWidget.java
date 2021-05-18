package easel.ui;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import java.util.function.Consumer;

public abstract class HitboxWidget<T extends HitboxWidget<T>> extends AbstractWidget<T> {
    protected Hitbox hb;

    private Consumer<T> onLeftClick = x -> {};
    private Consumer<T> onRightClick;

    private Consumer<T> onHoverEnter = x -> {};
    private Consumer<T> onHoverLeave = x -> {};

    private boolean isHovered = false;

    private boolean rightClickStarted = false;
    private boolean leftClickStarted = false;

    private void resizeHitboxToContent() {
        float scaledContentWidth = getContentWidth() * Settings.xScale;
        float scaledContentHeight = getContentHeight() * Settings.yScale;

        if (hb == null) {
            hb = new Hitbox(scaledContentWidth, scaledContentHeight);
        }
        else {
            hb.width = scaledContentWidth;
            hb.height = scaledContentHeight;
        }
    }

    private void moveHitboxToTarget(float targetLeft, float targetBottom) {
        float centerX = targetLeft + (0.5f * getContentWidth());
        float centerY = targetBottom + (0.5f * getContentHeight());

        hb.move(centerX * Settings.xScale,
                centerY * Settings.yScale);
    }

    @Override
    public T anchoredAt(float x, float y, AnchorPosition anchorPosition, InterpolationSpeed withDelay) {
        super.anchoredAt(x, y, anchorPosition, withDelay);

        resizeHitboxToContent();

        float targetX = anchorPosition.isLeft() ? x : (anchorPosition.isCenterX() ? x - 0.5f * getWidth() : x - getWidth());
        float targetY = anchorPosition.isBottom() ? y : (anchorPosition.isCenterY() ? y - 0.5f * getHeight() : y - getHeight());

        moveHitboxToTarget(targetX, targetY);

        return (T)this;
    }

    @Override
    public void update() {
        hb.update();

        // NOTE: (poorly) rewriting code that already exists in the hitbox
        // (e.g. clickStarted/clicked/hoverStarted/justHovered etc. all exist in Hitbox already - I just
        //   want full control)
        // Really should have just found a way to work around the Hitbox code; but I'm a control freak...

        // Hover transitions
        if (hb.hovered && !isHovered) {
            onHoverEnter.accept((T)this);
            isHovered = true;
        }
        else if (isHovered){
            onHoverLeave.accept((T)this);
            isHovered = false;
        }

        // Left click started
        if (isHovered && InputHelper.justClickedLeft) {
            leftClickStarted = true;
        }
        else if (hb.hovered && CInputActionSet.select.isJustPressed()) {
            CInputActionSet.select.unpress();
            onLeftClick.accept((T)this);
        }

        // Left click ended
        if (leftClickStarted && InputHelper.justReleasedClickLeft) {
            if (isHovered) {
                onLeftClick.accept((T)this);
            }

            leftClickStarted = false;
        }
    }

    // --------------------------------------------------------------------------------

    public T onLeftClick(Consumer<T> onLeftClick) {
        this.onLeftClick = onLeftClick;
        return (T)this;
    }

    public T onHoverEnter(Consumer<T> onHoverEnter) {
        this.onHoverEnter = onHoverEnter;
        return (T)this;
    }

    public T onHoverLeave(Consumer<T> onHoverLeave) {
        this.onHoverLeave = onHoverLeave;
        return (T)this;
    }
}
