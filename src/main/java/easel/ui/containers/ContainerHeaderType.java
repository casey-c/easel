package easel.ui.containers;

enum ContainerHeaderType {
    NONE,
    TITLE,
    TITLE_SUBTITLE,
    CUSTOM;

    public boolean hasHeader() {
        return this != NONE;
    }
}
