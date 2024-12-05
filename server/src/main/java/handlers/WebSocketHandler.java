package handlers;

import chess.ChessGame;
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
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    Server server;
    public static HashMap<Session, Integer> currentGameSessions = new HashMap<>();

    public WebSocketHandler(Server server) {
        this.server = server;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.println("Recieved command: " + message);
        try {
            if (message.contains("\"commandType\":\"LOAD_GAME\"")) {
                LoadGameCommand command = new Gson().fromJson(message, LoadGameCommand.class);
                handleLoadGame(session, command);
            }
            if (message.contains("\"commandType\":\"CONNECT\"")) {
                LoadGameCommand command = new Gson().fromJson(message, LoadGameCommand.class);
                handleConnectSession(session, command);
            }
            if (message.contains("\"commandType\":\"HIGHLIGHT\"")) {
                LoadHighlightCommand command = new Gson().fromJson(message, LoadHighlightCommand.class);
                handleLoadHighlight(session, command);
            }
            if (message.contains("\"commandType\":\"MAKE_MOVE\"")) {
                MakeMoveCommand command = new Gson().fromJson(message, MakeMoveCommand.class);
                handleMakeMove(session, command);
            }
            if (message.contains("\"commandType\":\"RESIGN\"")) {
                ResignCommand command = new Gson().fromJson(message, ResignCommand.class);
                handleResign(session, command);
            }
            if (message.contains("\"commandType\":\"LEAVE\"")) {
                LeaveSessionCommand command = new Gson().fromJson(message, LeaveSessionCommand.class);
                handleLeaveSession(session, command);
            }
        } catch (Exception e) {
            ErrorMessage errorMessage = new ErrorMessage("Bad request: " + e.getMessage());
            sendMessage(session, errorMessage);
        }
    }

    private void handleMakeMove(Session session, MakeMoveCommand command) {
        String token = command.getAuthToken();
        int gameID = command.getGameID();
        ChessMove givenMove = command.getMove();

        try {
            AuthData auth = server.authDAO.getAuthByToken(token);
            GameData gameData = server.gameDAO.getGameByID(gameID);
            if (!gameData.gameActive()) {
                ErrorMessage errorMessage = new ErrorMessage("The game is over. No more moves can be made.");
                sendMessage(session, errorMessage);
                return;
            }
            ChessGame.TeamColor teamToMove = gameData.game().getTeamTurn();
            ChessGame.TeamColor checkColor = null;
            if (gameData.whiteUsername() != null) {
                if (gameData.whiteUsername().equals(auth.username())) {
                    checkColor = ChessGame.TeamColor.WHITE;
                }
            }
            if (gameData.blackUsername() != null) {
                if (gameData.blackUsername().equals(auth.username())) {
                    checkColor = ChessGame.TeamColor.BLACK;
                }
            }
            if (!Objects.equals(checkColor, teamToMove)) {
                ErrorMessage errorMessage = new ErrorMessage("It is not your turn. Please wait for the other player to make their move. You cannot make moves if observing.");
                sendMessage(session, errorMessage);
                return;
            }

            GameData updatedGame = server.gameDAO.makeMove(auth.authToken(), gameData, givenMove);

            LoadGameMessage message = new LoadGameMessage(updatedGame);
            sendBroadcastAll(gameID, message);
            NotificationMessage madeMoveMessage = new NotificationMessage(auth.username()+" made move: " +givenMove);
            sendBroadcastExclude(session,gameID,madeMoveMessage);
            System.out.println("Current Sessions: " + currentGameSessions.size());
        } catch (Exception e) {
            ErrorMessage errorMessage = new ErrorMessage("Unable to make move: " + e.getMessage());
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
            ErrorMessage errorMessage = new ErrorMessage("Unable to load game: " + e.getMessage());
            sendMessage(session, errorMessage);
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
            ErrorMessage errorMessage = new ErrorMessage("Unable to load highlight: " + e.getMessage());
            sendMessage(session, errorMessage);
        }
    }

    private void handleConnectSession(Session session, LoadGameCommand command) throws DataAccessException {
        String token = command.getAuthToken();
        int gameID = command.getGameID();

        try {
            GameData gameData;
            AuthData auth;
            try {
                auth = server.authDAO.getAuthByToken(token);
                gameData = server.gameDAO.getGameByID(gameID);
                currentGameSessions.put(session, gameID);
            } catch (Exception e) {
                throw new Exception(e);
            }

            try {
                LoadGameMessage message = new LoadGameMessage(gameData);
                sendMessage(session, message);
                NotificationMessage notification = new NotificationMessage(auth.username() + " connected");
                sendBroadcastExclude(session, gameID, notification);
                System.out.println("Current Sessions: " + currentGameSessions.size());
            } catch (Exception e) {
                currentGameSessions.remove(session);
                throw new Exception(e);
            }
        } catch (Exception e) {
            ErrorMessage errorMessage = new ErrorMessage("Unable to connect session: " + e.getMessage());
            sendMessage(session, errorMessage);
        }
    }

    private void handleLeaveSession(Session session, LeaveSessionCommand command) throws DataAccessException {
        String token = command.getAuthToken();
        int gameID = command.getGameID();

        try {
            AuthData auth = server.authDAO.getAuthByToken(token);
            GameData gameData = server.gameDAO.getGameByID(gameID);

            ChessGame.TeamColor teamToLeave = null;
            if (gameData.whiteUsername() != null) {
                if (gameData.whiteUsername().equals(auth.username())) {
                    teamToLeave = ChessGame.TeamColor.WHITE;
                }
            }
            if (gameData.blackUsername() != null) {
                if (gameData.blackUsername().equals(auth.username())) {
                    teamToLeave = ChessGame.TeamColor.BLACK;
                }
            }
            if (teamToLeave != null) {
                String leaveTeamString = null;
                if (teamToLeave == ChessGame.TeamColor.WHITE) {
                    leaveTeamString = "WHITE";
                } else {
                    leaveTeamString = "BLACK";
                }
                server.gameDAO.joinGame(gameID, leaveTeamString, null);
            }

            currentGameSessions.remove(session);
            System.out.println("Current Sessions: " + currentGameSessions.size());

            NotificationMessage notification = new NotificationMessage(auth.username()+" disconnected");
            sendBroadcastExclude(session,gameID,notification);
        } catch (Exception e) {
            ErrorMessage errorMessage = new ErrorMessage("Unable to leave session: " + e.getMessage());
            sendMessage(session, errorMessage);
        }
    }

    private void handleResign(Session session, ResignCommand command) {
        String token = command.getAuthToken();
        int gameID = command.getGameID();

        try {
            AuthData auth = server.authDAO.getAuthByToken(token);
            GameData gameData = server.gameDAO.getGameByID(gameID);

            if (!gameData.gameActive()) {
                ErrorMessage errorMessage = new ErrorMessage("The game is over. No more moves can be made.");
                sendMessage(session, errorMessage);
                return;
            }

            String checkColor = null;
            if (gameData.whiteUsername() != null) {
                if (gameData.whiteUsername().equals(auth.username())) {
                    checkColor = "WHITE";
                }
            }
            if (gameData.blackUsername() != null) {
                if (gameData.blackUsername().equals(auth.username())) {
                    checkColor = "BLACK";
                }
            }
            if (checkColor == null) {
                ErrorMessage errorMessage = new ErrorMessage("Observers cannot resign from a game.");
                sendMessage(session, errorMessage);
                return;
            }

            GameData updatedGame = server.gameDAO.resignGame(auth.authToken(), gameData.gameID(),checkColor, auth.username());

            //LoadGameMessage message = new LoadGameMessage(updatedGame);
            //sendBroadcastAll(gameID, message);
            NotificationMessage madeMoveMessage = new NotificationMessage(auth.username()+" has resigned, game over. No more moves can be made.");
            sendBroadcastAll(gameID,madeMoveMessage);
            System.out.println("Current Sessions: " + currentGameSessions.size());
        } catch (Exception e) {
            ErrorMessage errorMessage = new ErrorMessage("Unable to make move: " + e.getMessage());
            sendMessage(session, errorMessage);
        }
    }

    public void sendMessage(Session session, ServerMessage message) {
        try {
            System.out.println("Sent message: " + message.toString());
            session.getRemote().sendString(new Gson().toJson(message));
        } catch (IOException e) {
            ErrorMessage errorMessage = new ErrorMessage("Unable to send message: " + e.getMessage());
            sendMessage(session, errorMessage);
        }
    }

    public void sendBroadcastAll(int gameID, ServerMessage message) {
        currentGameSessions.forEach((session, value)-> {
            try {
                if (gameID == value) {
                    session.getRemote().sendString(new Gson().toJson(message));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void sendBroadcastExclude(Session exclude, int gameID, ServerMessage message) {
        currentGameSessions.forEach((session, value)-> {
            try {
                if (gameID == value && !session.equals(exclude)) {
                    session.getRemote().sendString(new Gson().toJson(message));
                }
            } catch (IOException e) {
                ErrorMessage errorMessage = new ErrorMessage("Unable to send message: " + e.getMessage());
                sendMessage(session, errorMessage);
            }
        });
    }

    public void clearSessions() {
        currentGameSessions.clear();
    }

}
