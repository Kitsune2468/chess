package handlers;

import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import server.Server;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import websocket.commands.*;
import websocket.messages.*;

import java.io.IOException;
import java.util.Collection;
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
            if (message.contains("\"commandType\":\"CONNECT\"")) {
                LoadGameCommand command = new Gson().fromJson(message, LoadGameCommand.class);
                handleLoadGame(session, command);
            }
            if (message.contains("\"commandType\":\"HIGHLIGHT\"")) {
                LoadHighlightCommand command = new Gson().fromJson(message, LoadHighlightCommand.class);
                handleLoadHighlight(session, command);
            }
            if (message.contains("\"commandType\":\"MAKE_MOVE\"")) {
                MakeMoveCommand command = new Gson().fromJson(message, MakeMoveCommand.class);
                handleMakeMove(session, command);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handleMakeMove(Session session, MakeMoveCommand command) {
        String token = command.getAuthToken();
        int gameID = command.getGameID();
        ChessMove givenMove = command.getChessMove();

        try {
            AuthData auth = server.authDAO.getAuthByToken(token);
            GameData gameData = server.gameDAO.getGameByID(gameID);
            GameData updatedGame = server.gameDAO.makeMove(auth.authToken(), gameData, givenMove);

            LoadGameMessage message = new LoadGameMessage(updatedGame);
            sendMessage(session,message);
        } catch (Exception e) {
            ErrorMessage errorMessage = new ErrorMessage("Unable to make move");
            sendMessage(session, errorMessage);
        }
    }

    private void handleLoadGame(Session session, LoadGameCommand command) throws DataAccessException {
        String token = command.getAuthToken();
        int gameID = command.getGameID();

        try {
            AuthData auth = server.authDAO.getAuthByToken(token);
            GameData gameData = server.gameDAO.getGameByID(gameID);

            LoadGameMessage message = new LoadGameMessage(gameData);
            sendMessage(session,message);
        } catch (Exception e) {
            throw e;
        }
    }

    private void handleLoadHighlight(Session session, LoadHighlightCommand command) throws DataAccessException {
        String token = command.getAuthToken();
        int gameID = command.getGameID();
        ChessPosition startPosition = command.getStartPosition();

        try {
            AuthData auth = server.authDAO.getAuthByToken(token);
            GameData gameData = server.gameDAO.getGameByID(gameID);
            Collection<ChessMove> possibleMoves = gameData.game().validMoves(startPosition);

            LoadHighlightMessage message = new LoadHighlightMessage(gameData, possibleMoves);
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
