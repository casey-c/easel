package ojbui.ui;

import com.megacrit.cardcrawl.helpers.MathHelper;

public enum InterpolationSpeed {
    INSTANT, // No delay
    FAST,    // 20x (mouseLerpSnap)
    MEDIUM,  // 12x (fadelerpSnap)
    SLOW;    // 6x (cardlerpSnap)

    public float interpolate(float start, float target) {
        switch (this) {
            case INSTANT:
                return target;
            case FAST:
                return MathHelper.mouseLerpSnap(start, target);
            case MEDIUM:
                return MathHelper.fadeLerpSnap(start, target);
            default:
                return MathHelper.cardLerpSnap(start, target);
        }
    }
}
