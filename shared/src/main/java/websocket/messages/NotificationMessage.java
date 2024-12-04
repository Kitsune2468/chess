package websocket.messages;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class NotificationMessage extends ServerMessage {

    String message;

    public NotificationMessage(String refMessage) {
        super(ServerMessage.ServerMessageType.NOTIFICATION);
        message = refMessage;
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
