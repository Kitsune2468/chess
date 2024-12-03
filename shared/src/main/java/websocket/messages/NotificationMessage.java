package websocket.messages;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class NotificationMessage extends ServerMessage {
    ServerMessageType serverMessageType;
    String message;

    public NotificationMessage(ServerMessage.ServerMessageType type, String refMessage) {
        super(type);
        message = refMessage;
    }

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationMessage)) {
            return false;
        }
        NotificationMessage that = (NotificationMessage) o;
        return (getServerMessageType() == that.getServerMessageType() &&
                Objects.equals(getMessage(), that.getMessage()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}
