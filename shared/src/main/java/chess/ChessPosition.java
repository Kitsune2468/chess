package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    int posRow = 0;
    int posCol = 0;

    public ChessPosition(int row, int col) {
        posRow = row;
        posCol = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return posRow;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return posCol;
    }

    public String toString() {
        return "(" + posRow + ", " + posCol + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        ChessPosition that = (ChessPosition) o;
        return posRow == that.posRow && posCol == that.posCol;
    }

    @Override
    public int hashCode() {
        return Objects.hash(posRow, posCol);
    }
}
