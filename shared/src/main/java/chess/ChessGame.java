package chess;

//import java.lang.runtime.TemplateRuntime;
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
        mainBoard.resetBoard();
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
        ChessPiece startPiece = mainBoard.getPiece(startPosition);
        if (startPiece == null) {
            return null;
        }
        Collection<ChessMove> pieceTestMoves = startPiece.pieceMoves(mainBoard,startPosition);
        Collection<ChessMove> pieceValidMoves = new ArrayList<ChessMove>();
        for (ChessMove move : pieceTestMoves){
            if (testMove(move)) {
                pieceValidMoves.add(move);
            }
        }

        return pieceValidMoves;
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
        ChessPiece checkPiece = mainBoard.getPiece(move.getStartPosition());
        if (checkPiece == null) {
            throw new InvalidMoveException("No piece at location");
        }
        TeamColor colorTrying = mainBoard.getPiece(move.getStartPosition()).getTeamColor();
        // Wrong Team trying to move
        if (colorTrying != getTeamTurn()) {
            throw new InvalidMoveException("Not your turn");
        }
        // No possible moves
        if (pieceValidMoves == null) {
            throw new InvalidMoveException("No valid moves");
        }
        for (ChessMove validMove : pieceValidMoves){
            if (validMove.equals(move)){
                moveIsValid = true;
            }
        }
        // Move not valid
        if (!moveIsValid){
            throw new InvalidMoveException("Move not valid");
        }
        // Move is valid
        movePiece(move);
        switch (currentTeamsTurn) {
            case BLACK:
                currentTeamsTurn = TeamColor.WHITE;
                break;
            case WHITE:
                currentTeamsTurn = TeamColor.BLACK;
                break;
        }
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
        if (kingPosition == null) {
            return false;
        }

        // Check opposing piece moves and if contain king position, check is true
        for (int i=1; i<=64; i++){
            int y = ((i-1)/8)+1;
            int x = (i-1)%8+1;
            checkPosition = new ChessPosition(y,x);
            checkPiece = mainBoard.getPiece(checkPosition);
            if (checkPiece != null && checkPiece.getTeamColor() != teamColor) {
                checkMoves = checkPiece.pieceMoves(mainBoard,checkPosition);
                for (ChessMove possibleMove : checkMoves){
                    ChessPosition possible = possibleMove.getEndPosition();
                    if (possible.getRow()==kingPosition.getRow() && possible.getColumn()==kingPosition.getColumn()){
                        isCheck = true;
                        break;
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
        boolean isCheckmate = true;
        ChessPosition checkPosition = null;
        ChessPiece checkPiece = null;

        if (!isInCheck(teamColor)){
            return false;
        }
        for (int i=1; i<=8; i++) {
            for (int j = 1; j <= 8; j++) {
                checkPosition = new ChessPosition(i, j);
                checkPiece = mainBoard.getPiece(checkPosition);
                if (checkPiece != null &&
                        checkPiece.getTeamColor() == teamColor &&
                        !validMoves(checkPosition).isEmpty()) {
                    isCheckmate = false;
                }
            }
        }
        return isCheckmate;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        boolean isStalemate = true;
        ChessPosition checkPosition = null;
        ChessPiece checkPiece = null;

        if (teamColor != currentTeamsTurn || isInCheck(teamColor)) {
            return false;
        }
        for (int i=1; i<=8; i++) {
            for (int j = 1; j <= 8; j++) {
                checkPosition = new ChessPosition(i,j);
                checkPiece = mainBoard.getPiece(checkPosition);
                if (checkPiece!=null &&
                        checkPiece.getTeamColor()==teamColor &&
                        !validMoves(checkPosition).isEmpty()){
                    isStalemate = false;
                }
            }
        }
        return isStalemate;
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
        ChessBoard saveBoard = new ChessBoard(mainBoard);
        boolean isMoveValid = true;
        TeamColor checkColor = mainBoard.getPiece(testMove.getStartPosition()).getTeamColor();
        this.movePiece(testMove);

        if(isInCheck(checkColor)){
            isMoveValid = false;
        }

        mainBoard = saveBoard;
        return isMoveValid;
    }

    public void movePiece(ChessMove move) {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece.PieceType promotionPiece = move.getPromotionPiece();
        ChessPiece startPiece = mainBoard.getPiece(startPosition);
        ChessPiece.PieceType currentPieceType = startPiece.getPieceType();
        TeamColor pieceColor = startPiece.getTeamColor();
        if (promotionPiece == null){
            mainBoard.addPiece(endPosition,new ChessPiece(pieceColor, currentPieceType));
            mainBoard.addPiece(startPosition,null);
        } else {
            mainBoard.addPiece(endPosition,new ChessPiece(pieceColor, promotionPiece));
            mainBoard.addPiece(startPosition,null);
        }
    }
}
