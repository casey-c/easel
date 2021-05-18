package easel.ui.interactive;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import easel.Easel;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;

import java.util.function.Consumer;

public abstract class HitboxWidget<T extends HitboxWidget<T>> extends AbstractWidget<T> {
    protected Hitbox hb;

    protected Consumer<T> onLeftClick = x -> {};
    protected Consumer<T> onRightClick;

    protected Consumer<T> onHoverEnter = x -> {};
    protected Consumer<T> onHoverLeave = x -> {};

    protected boolean isHovered = false;

    protected boolean rightClickStarted = false;
    protected boolean leftClickStarted = false;

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

            Easel.logger.info("Hover started");
            Easel.logger.info(this);

            isHovered = true;
        }
        else if (!hb.hovered && isHovered){
            onHoverLeave.accept((T)this);

            Easel.logger.info("Hover finished");
            Easel.logger.info(this);

            isHovered = false;
        }

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

    // --------------------------------------------------------------------------------

    public T onLeftClick(Consumer<T> onLeftClick) {
        this.onLeftClick = onLeftClick;
        return (T)this;
    }

    public T onRightClick(Consumer<T> onRightClick) {
        this.onRightClick = onRightClick;
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
