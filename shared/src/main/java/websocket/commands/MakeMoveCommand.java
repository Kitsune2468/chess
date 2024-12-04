package websocket.commands;

import chess.ChessMove;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 *
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class MakeMoveCommand extends UserGameCommand{

    private final ChessMove chessMove;

    public MakeMoveCommand(String authToken, Integer gameID, ChessMove chessMove) {
        super(CommandType.MAKE_MOVE, authToken, gameID);

        this.chessMove = chessMove;
    }

    public ChessMove getChessMove() {
        return chessMove;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MakeMoveCommand)) {
            return false;
        }
        MakeMoveCommand that = (MakeMoveCommand) o;
        return getCommandType() == that.getCommandType() &&
                Objects.equals(getAuthToken(), that.getAuthToken()) &&
                Objects.equals(getGameID(), that.getGameID()) &&
                Objects.equals(getChessMove(), that.getChessMove());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthToken(), getGameID());
    }
}
