package easel.ui;

import java.util.Random;

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
     * Return the left-most point of a region with the given width where x is a point inside that region determined by this anchor. Implementation wise, this is: returns x if this anchor is left, x minus half the width if this anchor is centered, or x minus the full width if this anchor is on the right. If this doesn't make sense, you can ignore this function as it is just convenience for internal use (and it's surprisingly difficult to describe concisely).
     * @param x a point placed inside the region according to this anchor
     * @param width the width of the region
     * @return the left-most point of the region
     */
    public float getLeft(float x, float width) {
        return isLeft() ? x : (isCenterX() ? x - 0.5f * width : x - width);
    }

    /**
     * Return the bottom-most point of a region with the given height where y is a point inside that region determined by this anchor. Implementation wise, this is: returns y if this anchor is bottom, y minus half the height if this anchor is centered, or y minus the full height if this anchor is on the top. If this doesn't make sense, you can ignore this function as it is just convenience for internal use (and it's surprisingly difficult to describe concisely).
     * @param y a point placed inside the region according to this anchor
     * @param height the height of the region
     * @return the bottom-most point of the region
     */
    public float getBottom(float y, float height) {
        return isBottom() ? y : (isCenterY() ? y - 0.5f * height : y - height);
    }

    /**
     * Determines how much the coordinates move horizontally from a starting point to the ending point. This function allows computations to occur when the two x values are not the same anchor (if they were, you'd just be able to do <code>newX - startX</code>). This lets you quickly find the horizontal delta between ALL points in the region, given any anchored point in the original position compared to any anchored point in the new position. This assumes the width of the region stays constant, and the move is a strict translation.
     * @param startX a point placed inside the starting region with its local position determined by startAnchor
     * @param startAnchor the position of startX inside the starting region (is it on the left side of the region, the center, or the right)
     * @param newX a point placed inside the ending region with its local position determined by newAnchor
     * @param newAnchor the position of newX inside the ending region (is it on the left side of the region, the center, or the right)
     * @param width the width of the region being translated (assumed constant)
     * @return the horizontal delta between any same anchor position points in the region (e.g. how much the LEFT_BOTTOM point moves horizontally to become the new LEFT_BOTTOM, or the CENTER_TOP moves to become the new CENTER_TOP, etc.)
     * @see #deltaY(float, AnchorPosition, float, AnchorPosition, float)
     */
    public static float deltaX(float startX, AnchorPosition startAnchor, float newX, AnchorPosition newAnchor, float width) {
        float oldLeft = startAnchor.getLeft(startX, width);
        float newLeft = newAnchor.getLeft(newX, width);
        return newLeft - oldLeft;
    }

    /**
     * Determines how much the coordinates move vertically from a starting point to the ending point. This function allows computations to occur when the two y values are not the same anchor (if they were, you'd just be able to do <code>newY - startY</code>). This lets you quickly find the vertical delta between ALL points in the region, given any anchored point in the original position compared to any anchored point in the new position. This assumes the height of the region stays constant, and the move is a strict translation.
     * @param startY a point placed inside the starting region with its local position determined by startAnchor
     * @param startAnchor the position of startY inside the starting region (is it on the bottom of the region, the center, or the top)
     * @param newY a point placed inside the ending region with its local position determined by newAnchor
     * @param newAnchor the position of newY inside the ending region (is it on the bottom of the region, the center, or the top)
     * @param height the height of the region being translated (assumed constant)
     * @return the vertical delta between any same anchor position points in the region (e.g. how much the LEFT_BOTTOM point moves vertically to become the new LEFT_BOTTOM, or the CENTER_TOP moves to become the new CENTER_TOP, etc.)
     * @see #deltaX(float, AnchorPosition, float, AnchorPosition, float)
     */
    public static float deltaY(float startY, AnchorPosition startAnchor, float newY, AnchorPosition newAnchor, float height) {
        float oldBottom = startAnchor.getBottom(startY, height);
        float newBottom = newAnchor.getBottom(newY, height);
        return newBottom - oldBottom;
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

    /**
     * DEBUG.
     * @return a random anchor position out of the nine possibilities
     * @see #randomAnchor(AnchorPosition)
     */
    public static AnchorPosition randomAnchor() {
        Random random = new Random();
        int index = random.nextInt(values().length);
        return values()[index];
    }

    /**
     * DEBUG. Note: will loop until it randomly finds an anchor that isn't the one given - consider using {@link #next()} instead, which will cycle through the enum and not have to loop.
     * @param previous the anchor position to skip (can't be in the output)
     * @return a random anchor position excluding the <code>previous</code>
     * @see #next()
     * @see #randomAnchor()
     */
    public static AnchorPosition randomAnchor(AnchorPosition previous) {
        Random random = new Random();

        while (true) {
            int index = random.nextInt(values().length);
            AnchorPosition next = values()[index];

            if (next != previous)
                return next;
        }
    }

    /**
     * DEBUG.
     * @return the next anchor in the enum (will loop back around and cycle forever)
     */
    public AnchorPosition next() {
        return values()[(ordinal() + 1) % values().length];
    }
}