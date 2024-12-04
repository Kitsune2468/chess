package websocket.commands;

import chess.ChessGame;
import chess.ChessMove;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 *
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class LeaveSessionCommand extends UserGameCommand{

    ChessGame.TeamColor teamToLeave;

    public LeaveSessionCommand(String authToken, Integer gameID, ChessGame.TeamColor teamToLeave) {
        super(CommandType.LEAVE, authToken, gameID);
        this.teamToLeave = teamToLeave;
    }

    public ChessGame.TeamColor getTeamToLeave() {
        return teamToLeave;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaveSessionCommand)) {
            return false;
        }
        LeaveSessionCommand that = (LeaveSessionCommand) o;
        return getCommandType() == that.getCommandType() &&
                Objects.equals(getAuthToken(), that.getAuthToken()) &&
                Objects.equals(getGameID(), that.getGameID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthToken(), getGameID());
    }
}
