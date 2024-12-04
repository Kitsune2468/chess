package websocket.commands;

import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 *
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class LoadHighlightCommand extends UserGameCommand{

    private ChessPosition startPosition;

    public LoadHighlightCommand(String authToken, Integer gameID, ChessPosition startPosition) {
        super(CommandType.HIGHLIGHT, authToken, gameID);
        this.startPosition = startPosition;
    }

    public ChessPosition getStartPosition() {
        return startPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoadHighlightCommand)) {
            return false;
        }
        LoadHighlightCommand that = (LoadHighlightCommand) o;
        return getCommandType() == that.getCommandType() &&
                Objects.equals(getAuthToken(), that.getAuthToken()) &&
                Objects.equals(getGameID(), that.getGameID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthToken(), getGameID());
    }
}
