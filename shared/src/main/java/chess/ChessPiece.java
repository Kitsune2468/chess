package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pColor;
    private ChessPiece.PieceType pType;
    private ChessPosition pPosition;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        pColor = pieceColor;
        pType = type;
        pPosition = new ChessPosition(0,0);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (board.getPiece(myPosition).getPieceType() == PieceType.BISHOP){
            return BishopMoves(board, myPosition);
        }
        return new ArrayList<ChessMove>();
    }

    public ArrayList<ChessMove> BishopMoves(ChessBoard board, ChessPosition myPosition) {
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();
        ArrayList<ChessMove> possibleMoves = new ArrayList<ChessMove>();

        //Check left-up diagonal
        int counter = 1;
        while(myPosition.getRow()+counter <= 8 && myPosition.getColumn()-counter >= 1) {
            int curRow = startRow + counter;
            int curCol = startCol - counter;
            possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
            /* Accidentally started on next test lol
            if (board.getPiece(new ChessPosition(curRow, curCol)) == null) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
            } else if (board.getPiece(new ChessPosition(curRow, curCol)).getTeamColor() != pColor) {
                possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
                break;
            } else if (board.getPiece(new ChessPosition(curRow, curCol)).getTeamColor() == pColor) {
                break;
            }
            */

            counter++;
        }
        //Check right-up diagonal
        counter = 1;
        while(myPosition.getRow()+counter <= 8 && myPosition.getColumn()+counter <= 8) {
            int curRow = startRow + counter;
            int curCol = startCol + counter;
            possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
            counter++;
        }
        //Check right-down diagonal
        counter = 1;
        while(myPosition.getRow()-counter >= 1 && myPosition.getColumn()+counter <= 8) {
            int curRow = startRow - counter;
            int curCol = startCol + counter;
            possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
            counter++;
        }
        //Check left-down diagonal
        counter = 1;
        while(myPosition.getRow()-counter >= 1 && myPosition.getColumn()-counter >= 1) {
            int curRow = startRow - counter;
            int curCol = startCol - counter;
            possibleMoves.add(new ChessMove(myPosition,new ChessPosition(curRow,curCol),null));
            counter++;
        }
        return possibleMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pColor == that.pColor && pType == that.pType && Objects.equals(pPosition, that.pPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pColor, pType, pPosition);
    }
}
