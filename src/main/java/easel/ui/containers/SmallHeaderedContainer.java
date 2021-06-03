package easel.ui.containers;

import easel.ui.graphics.ninepatch.headered.SmallHeaderedNinePatch;

public class SmallHeaderedContainer extends AbstractHeaderedContainer<SmallHeaderedContainer, SmallHeaderedNinePatch> {
    public SmallHeaderedContainer(float width, float height) {
        super(width, height);
    }

    @Override
    protected void buildBackgroundWithHeader() {
        this.backgroundWithHeader = new SmallHeaderedNinePatch(totalWidth, totalHeight)
                .withHeaderColor(this.headerColor);
    }
}
