package client;

import com.google.gson.Gson;
import websocket.messages.NotificationMessage;

import javax.websocket.Endpoint;
import javax.websocket.*;
import java.net.URI;
import java.util.Scanner;

public class WebsocketCommunicator extends Endpoint {

    public Session session;

    public WebsocketCommunicator(String domain) throws Exception {
        URI uri = new URI("ws://"+domain+"/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                System.out.println(message);
            }
        });
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    private void handleMessage(String msg) {
        if (msg.contains("\"serverMessageType\":\"NOTIFICATION\"")) {
            NotificationMessage notificationMessage = new Gson().fromJson(msg, NotificationMessage.class);
            System.out.println(notificationMessage.getMessage());
        }
        if (msg.contains("\"serverMessageType\":\"ERROR\"")) {

        }
        if (msg.contains("\"serverMessageType\":\"LOAD_GAME\"")) {

        }
    }


}
