package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor currentTeamsTurn;
    private ChessBoard mainBoard;

    public ChessGame() {
        currentTeamsTurn = TeamColor.WHITE;
        mainBoard = new ChessBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTeamsTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTeamsTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        boolean moveIsValid = false;
        Collection<ChessMove> pieceValidMoves = validMoves(move.getStartPosition());
        for (ChessMove validMove : pieceValidMoves){
            if (validMove==move){
                moveIsValid = true;
            }
        }
        if (!moveIsValid){
            throw new InvalidMoveException();
        }
        movePiece(move);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition checkPosition = null;
        ChessPiece checkPiece = null;
        Collection<ChessMove> checkMoves = null;
        boolean isCheck = false;

        // Find Team's King
        ChessPosition kingPosition = findKing(teamColor, mainBoard);

        // Check opposing piece moves and if contain king position, check is true
        for (int i=1; i<=8; i++){
            for (int j=1; j<=8; j++){
                checkPosition = new ChessPosition(i,j);
                checkPiece = mainBoard.getPiece(checkPosition);
                if (checkPiece != null) {
                    if (checkPiece.getTeamColor() != teamColor){
                        checkMoves = checkPiece.pieceMoves(mainBoard,checkPosition);
                        for (ChessMove possibleMove : checkMoves){
                            if (possibleMove.getEndPosition().getRow()==kingPosition.getRow() && possibleMove.getEndPosition().getColumn()==kingPosition.getColumn()){
                                isCheck = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return isCheck;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)){
            return false;
        } else {
            return true;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        mainBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return mainBoard;
    }

    public ChessPosition findKing(TeamColor teamColor, ChessBoard checkBoard) {
        ChessPosition checkPosition = null;
        ChessPiece checkPiece = null;
        Collection<ChessMove> checkMoves = null;
        boolean isCheck = false;

        // Find Team's King
        ChessPosition kingPosition = null;
        for (int i=1; i<=8; i++){
            for (int j=1; j<=8; j++){
                checkPosition = new ChessPosition(i,j);
                checkPiece = checkBoard.getPiece(checkPosition);
                if (checkPiece != null){
                    // If correct color and team
                    if (checkPiece.getTeamColor()==teamColor && checkPiece.getPieceType()== ChessPiece.PieceType.KING){
                        kingPosition = checkPosition;
                        break;
                    }
                }
            }
        }
        return kingPosition;
    }

    public boolean testMove(ChessMove testMove) {
        ChessBoard saveBoard = mainBoard;
        boolean isMoveValid = true;
        this.movePiece(testMove);

        if(isInCheck(this.getTeamTurn())){
            isMoveValid = false;
        }

        mainBoard = saveBoard;
        return isMoveValid;
    }

    public void movePiece(ChessMove move) {
        ChessPiece newEndPiece = mainBoard.getPiece(move.getStartPosition());
        mainBoard.addPiece(move.getEndPosition(),newEndPiece);
        mainBoard.addPiece(move.getStartPosition(),null);
    }
}
