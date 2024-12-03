package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.GameData;
import model.requests.CreateGameRequest;
import model.requests.GameListResult;
import model.requests.JoinGameRequest;
import service.GameService;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import spark.Request;
import spark.Response;

@WebSocket
public class WebSocketHandler {
    public WebSocketHandler() {
    }

    @OnWebSocketConnect
    public void onConnect(Session session, String message) throws Exception {

    }

    @OnWebSocketClose
    public void onClose(Session session, String message) throws Exception {

    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.printf("Received: %s", message);
        session.getRemote().sendString("WebSocket response: " + message);
    }
}
