package easel.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

public class EaselInputHelper {
    /**
     * @return true if either left shift or right shift is pressed
     */
    public static boolean isShiftPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
    }

    /**
     * @return true if either left alt or right alt is pressed
     */
    public static boolean isAltPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.ALT_RIGHT);
    }

    /**
     * @return true if either left control or right control is pressed
     */
    public static boolean isControlPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT);
    }

    /**
     * @return the X coordinate of the mouse, in 1080p space (scaled in a way to be used with widgets)
     */
    public static int getMouseX() {
        return (int)((float) InputHelper.mX / Settings.xScale);
    }

    /**
     * @return the Y coordinate of the mouse, in 1080p space (scaled in a way to be used with widgets)
     */
    public static int getMouseY() {
        return (int)((float) InputHelper.mY / Settings.yScale);
    }
}
