package easel.ui.containers;

import easel.ui.graphics.ninepatch.headered.LargeHeaderedNinePatch;

public class LargeHeaderedContainer extends AbstractHeaderedContainer<LargeHeaderedContainer, LargeHeaderedNinePatch> {
    public LargeHeaderedContainer(float width, float height) {
        super(width, height);
    }

    @Override
    protected void buildBackgroundWithHeader() {
        this.backgroundWithHeader = new LargeHeaderedNinePatch(totalWidth, totalHeight)
                .withHeaderColor(this.headerColor);
    }
}
