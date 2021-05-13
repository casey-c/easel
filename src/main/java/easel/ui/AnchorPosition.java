package easel.ui;

public enum AnchorPosition {
    LEFT_TOP, CENTER_TOP, RIGHT_TOP,
    LEFT_CENTER, CENTER, RIGHT_CENTER,
    LEFT_BOTTOM, CENTER_BOTTOM, RIGHT_BOTTOM;

    public boolean isLeft() { return this == LEFT_BOTTOM || this == LEFT_CENTER || this == LEFT_TOP; }
    public boolean isRight() { return this == RIGHT_BOTTOM || this == RIGHT_CENTER || this == RIGHT_TOP; }

    public boolean isBottom() { return this == LEFT_BOTTOM || this == CENTER_BOTTOM || this == RIGHT_BOTTOM; }
    public boolean isTop() { return this == LEFT_TOP || this == CENTER_TOP || this == RIGHT_TOP; }

    public boolean isCenterX() { return this == CENTER_TOP || this == CENTER || this == CENTER_BOTTOM; }
    public boolean isCenterY() { return this == LEFT_CENTER || this == CENTER || this == RIGHT_CENTER; }

    // --------------------------------------------------------------------------------

    /**
     * Helper / Convenience function. Computes an x value adjusted by this anchor, given a leftmost point and the width of the area. If the anchor is LEFT_*, the returned x is just the left point. If the anchor is CENTER_*, the returned x is the left plus half the width. Otherwise, returns the rightmost point (computed by the left + width).
     * @param left leftmost point of the region to anchor into
     * @param width width of the region to anchor into
     * @return an x coordinate contained inside the region <code>[left, left+width]</code> with the horizontal position determined by this anchor
     */
    public float getXFromLeft(float left, float width) {
        if (isLeft())
            return left;
        else if (isCenterX())
            return left + 0.5f * width;
        else
            return left + width;
    }

    /**
     * Similar to {@link #getXFromLeft(float, float)}, except the range is inside <code>[right - width, right]</code>.
     * @see #getXFromLeft(float, float)
     */
    public float getXFromRight(float right, float width) {
        return getXFromLeft(right - width, width);
    }

    /**
     * Helper / Convenience function. Computes a y value adjusted by this anchor, given a bottom point and the height of the area. If the anchor is *_BOTTOM, the returned y is just the bottom point. If the anchor is *_CENTER, the returned y is the bottom plus half the height. Otherwise, returns the topmost point (computed by the bottom + height).
     * @param bottom lowest point of the region to anchor into
     * @param height height of the region to anchor into
     * @return a y coordinate contained inside the region <code>[bottom, bottom+height]</code> with the vertical position determined by this anchor
     */
    public float getYFromBottom(float bottom, float height) {
        if (isBottom())
            return bottom;
        else if (isCenterY())
            return bottom + 0.5f * height;
        else
            return bottom + height;
    }

    /**
     * Similar to {@link #getYFromBottom(float, float)}, except the range is inside <code>[top-height, top]</code>.
     * @see #getYFromBottom(float, float)
     */
    public float getYFromTop(float top, float height) {
        return getYFromBottom(top - height, height);
    }
}