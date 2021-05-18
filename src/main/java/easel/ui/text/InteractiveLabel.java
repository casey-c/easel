package easel.ui.text;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import easel.ui.AbstractWidget;

import java.util.function.Consumer;

public class InteractiveLabel extends AbstractWidget<InteractiveLabel> {
    public InteractiveLabel() {
    }

    @Override
    public float getContentWidth() {
        return 0;
    }

    @Override
    public float getContentHeight() {
        return 0;
    }

    @Override
    protected void renderWidget(SpriteBatch sb) {

    }

    public InteractiveLabel withOnClick(Consumer<InteractiveLabel> callback) {
        return this;
    }
}
