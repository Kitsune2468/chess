package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.GameData;
import model.requests.CreateGameRequest;
import model.requests.GameListResult;
import model.requests.JoinGameRequest;
import server.Server;
import service.GameService;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import spark.Request;
import spark.Response;

@WebSocket
public class WebSocketHandler {
    Server server;
    public int sessionCounter;


    public WebSocketHandler(Server refServer) {
        server = refServer;
        sessionCounter = 0;
    }

    @OnWebSocketConnect
    public void onConnect(Session session) throws Exception {
        server.currentGameSessions.put(session, sessionCounter);
    }

    @OnWebSocketClose
    public void onClose(Session session, String message) throws Exception {
        server.currentGameSessions.remove(session);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.printf("Received: %s", message);
        session.getRemote().sendString("WebSocket response: " + message);
    }
}
