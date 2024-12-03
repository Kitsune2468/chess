package handlers;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import model.requests.CreateGameRequest;
import model.requests.GameListResult;
import model.requests.JoinGameRequest;
import server.Server;
import service.GameService;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import spark.Spark;
import websocket.commands.*;
import websocket.messages.*;

import java.io.IOException;
import java.util.HashMap;

@WebSocket
public class WebSocketHandler {
    Server server;
    public int sessionCounter;
    public static HashMap<Session, Integer> currentGameSessions = new HashMap<>();

    public WebSocketHandler(Server server) {
        this.server = server;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.println("Recieved message: " + message);
        try {
            if (message.contains("\"commandType\":\"LOAD_GAME\"")) {
                LoadGameCommand command = new Gson().fromJson(message, LoadGameCommand.class);
                handleLoadGame(session, command);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handleLoadGame(Session session, LoadGameCommand command) throws DataAccessException {
        String token = command.getAuthToken();
        int gameID = command.getGameID();

        try {
            AuthData auth = server.authDAO.getAuthByToken(token);
            GameData game = server.gameDAO.getGameByID(gameID);

            LoadGameMessage message = new LoadGameMessage(game);
            sendMessage(session,message);
        } catch (Exception e) {
            throw e;
        }
    }

    public void sendMessage(Session session, ServerMessage message) {
        try {
            session.getRemote().sendString(new Gson().toJson(message));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
