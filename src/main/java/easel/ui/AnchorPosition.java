package easel.ui;

/**
 * Enum specifying nine anchorable positions which are mostly used for aligning widgets into the proper location. Contains some helper methods to make working with the anchors a bit easier.
 */
public enum AnchorPosition {
    LEFT_TOP, CENTER_TOP, RIGHT_TOP,
    LEFT_CENTER, CENTER, RIGHT_CENTER,
    LEFT_BOTTOM, CENTER_BOTTOM, RIGHT_BOTTOM;

    /**
     * Returns true if the anchor is horizontally left.
     * @return true if this is <code>LEFT_TOP</code>, <code>LEFT_CENTER</code>, or <code>LEFT_BOTTOM</code>
     */
    public boolean isLeft() { return this == LEFT_BOTTOM || this == LEFT_CENTER || this == LEFT_TOP; }

    /**
     * Returns true if the anchor is horizontally right.
     * @return true if this is <code>RIGHT_TOP</code>, <code>RIGHT_CENTER</code>, or <code>RIGHT_BOTTOM</code>
     */
    public boolean isRight() { return this == RIGHT_BOTTOM || this == RIGHT_CENTER || this == RIGHT_TOP; }

    /**
     * Returns true if the anchor is vertically bottom.
     * @return true if this is <code>LEFT_BOTTOM</code>, <code>CENTER_BOTTOM</code>, or <code>RIGHT_BOTTOM</code>
     */
    public boolean isBottom() { return this == LEFT_BOTTOM || this == CENTER_BOTTOM || this == RIGHT_BOTTOM; }

    /**
     * Returns true if the anchor is vertically top.
     * @return true if this is <code>LEFT_TOP</code>, <code>CENTER_TOP</code>, or <code>RIGHT_TOP</code>
     */
    public boolean isTop() { return this == LEFT_TOP || this == CENTER_TOP || this == RIGHT_TOP; }

    /**
     * Returns true if the anchor is horizontally centered.
     * @return true if this is <code>CENTER_TOP</code>, <code>CENTER</code>, or <code>CENTER_BOTTOM</code>
     */
    public boolean isCenterX() { return this == CENTER_TOP || this == CENTER || this == CENTER_BOTTOM; }

    /**
     * Returns true if the anchor is vertically centered.
     * @return true if this is <code>LEFT_CENTER</code>, <code>CENTER</code>, or <code>RIGHT_CENTER</code>
     */
    public boolean isCenterY() { return this == LEFT_CENTER || this == CENTER || this == RIGHT_CENTER; }

    // --------------------------------------------------------------------------------

    /**
     * Computes an x value adjusted by this anchor, given a leftmost point and the width of the area. If the anchor is LEFT_*, the returned x is just the left point. If the anchor is CENTER_*, the returned x is the left plus half the width. Otherwise, returns the rightmost point (computed by the left + width).
     * @param left leftmost point of the region to anchor into
     * @param width width of the region to anchor into
     * @return an x coordinate contained inside the region <code>[left, left + width]</code> with the horizontal position determined by this anchor
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
     * @param right rightmost point of the region to anchor into
     * @param width width of the region to anchor into
     * @return an x coordinate contained inside the region <code>[right - width, right]</code> with the horizontal position determined by this anchor
     * @see #getXFromLeft(float, float)
     */
    public float getXFromRight(float right, float width) {
        return getXFromLeft(right - width, width);
    }

    /**
     * Computes a y value adjusted by this anchor, given a bottom point and the height of the area. If the anchor is *_BOTTOM, the returned y is just the bottom point. If the anchor is *_CENTER, the returned y is the bottom plus half the height. Otherwise, returns the topmost point (computed by the bottom + height).
     * @param bottom lowest point of the region to anchor into
     * @param height height of the region to anchor into
     * @return a y coordinate contained inside the region <code>[bottom, bottom + height]</code> with the vertical position determined by this anchor
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
     * Similar to {@link #getYFromBottom(float, float)}, except the range is inside <code>[top - height, top]</code>.
     * @param top highest point of the region to anchor into
     * @param height height of the region to anchor into
     * @return a y coordinate contained inside the region <code>[top - height, top]</code> with the vertical position determined by this anchor
     * @see #getYFromBottom(float, float)
     */
    public float getYFromTop(float top, float height) {
        return getYFromBottom(top - height, height);
    }

    /**
     * Produces a new anchor position which takes the horizontal position from <code>horizontal</code> and the vertical position from <code>vertical</code>.
     * @param horizontal determines if the output anchor is <code>LEFT_</code>, <code>CENTER_</code>, or <code>RIGHT_</code>.
     * @param vertical determines if the output anchor is <code>_BOTTOM</code>, <code>_CENTER</code>, or <code>_TOP</code>.
     * @return a new anchor
     */
    public static AnchorPosition combine(AnchorPosition horizontal, AnchorPosition vertical) {
        if (horizontal.isLeft()) {
            if (vertical.isBottom())
                return LEFT_BOTTOM;
            else if (vertical.isCenterY())
                return LEFT_CENTER;
            else return LEFT_TOP;
        }
        else if (horizontal.isCenterX()) {
            if (vertical.isBottom())
                return CENTER_BOTTOM;
            else if (vertical.isCenterY())
                return CENTER;
            else return CENTER_TOP;
        }
        else {
            if (vertical.isBottom())
                return RIGHT_BOTTOM;
            else if (vertical.isCenterY())
                return RIGHT_CENTER;
            else return RIGHT_TOP;
        }
    }
}