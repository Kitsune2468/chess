package client;

import com.google.gson.Gson;
import ui.GamePlayUI;
import websocket.commands.LoadGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import javax.websocket.Endpoint;
import javax.websocket.*;
import java.net.URI;
import java.util.Scanner;

public class WebsocketCommunicator extends Endpoint {

    public Session session;
    ServerFacade facade;

    public WebsocketCommunicator(String domain, ServerFacade facade) throws Exception {
        URI uri = new URI("ws://"+domain+"/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);
        this.facade = facade;

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                handleMessage(message);
            }
        });
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    private void handleMessage(String message) {
        if (message.contains("\"serverMessageType\":\"NOTIFICATION\"")) {
            NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
            System.out.println(notificationMessage.getMessage());
        }
        if (message.contains("\"serverMessageType\":\"ERROR\"")) {

        }
        if (message.contains("\"serverMessageType\":\"LOAD_GAME\"")) {
            LoadGameMessage result = new Gson().fromJson(message, LoadGameMessage.class);
            handleLoadGame(result);
        }
    }

    private void handleLoadGame(LoadGameMessage result) {
        facade.drawBoard(result.getGameData());
    }

}
