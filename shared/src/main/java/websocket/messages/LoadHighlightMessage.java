package websocket.messages;

import chess.ChessMove;
import model.GameData;

import java.util.Collection;
import java.util.Objects;

public class LoadHighlightMessage extends ServerMessage {

    GameData gameData;
    Collection<ChessMove> possibleMoves;

    public LoadHighlightMessage(GameData gameData, Collection<ChessMove> possibleMoves) {
        super(ServerMessageType.HIGHLIGHT);
        this.possibleMoves = possibleMoves;
        this.gameData = gameData;
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    public GameData getGameData() {
        return this.gameData;
    }

    public Collection<ChessMove> getPossibleMoves() {
        return this.possibleMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoadHighlightMessage)) {
            return false;
        }
        LoadHighlightMessage that = (LoadHighlightMessage) o;
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}
