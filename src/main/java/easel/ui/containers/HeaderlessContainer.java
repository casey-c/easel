package easel.ui.containers;

import easel.ui.graphics.ninepatch.headered.NoHeaderedNinePatch;

public class HeaderlessContainer extends AbstractHeaderedContainer<HeaderlessContainer, NoHeaderedNinePatch> {
    public HeaderlessContainer(float width, float height) {
        super(width, height);
    }

    @Override
    protected void buildBackgroundWithHeader() {
        this.backgroundWithHeader = new NoHeaderedNinePatch(totalWidth, totalHeight)
                .withHeaderColor(this.headerColor);
    }
}
