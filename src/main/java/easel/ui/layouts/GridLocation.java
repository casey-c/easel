package easel.ui.layouts;

import java.util.Objects;

/**
 * A simple helper struct for making it easier to work with (int, int) grid indices.
 */
public class GridLocation {
    public int row;
    public int col;

    public GridLocation(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GridLocation that = (GridLocation) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
