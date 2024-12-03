package websocket.messages;

import model.GameData;

import java.util.Objects;

public class LoadGameMessage extends ServerMessage {

    GameData gameData;

    public LoadGameMessage(GameData gameData) {
        super(ServerMessageType.LOAD_GAME);
        this.gameData = gameData;
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    public GameData getGameData() {
        return this.gameData;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoadGameMessage)) {
            return false;
        }
        LoadGameMessage that = (LoadGameMessage) o;
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}
