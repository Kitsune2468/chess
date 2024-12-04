package client;

import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import handlers.WebSocketHandler;
import model.GameData;
import model.requests.GameListResult;
import model.requests.GameTemplateResult;
import ui.BoardPrinter;
import websocket.commands.LoadGameCommand;
import websocket.commands.LoadHighlightCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadHighlightMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;


public class ServerFacade {
    String authToken;
    String serverUrl;
    String domain;
    String currentUser;
    BoardPrinter printer;

    HttpCommunicator http;
    WebsocketCommunicator ws;

    public ServerFacade(String refDomain) {
        serverUrl = "http://"+refDomain;
        domain = refDomain;
        printer = new BoardPrinter();
        try {
            http = new HttpCommunicator(serverUrl, authToken);
            ws = null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void login(String username, String password) throws Exception {
        authToken = http.login(username, password);
    }

    public void logout() throws Exception {
        http.logout();
    }

    public void register(String username, String password, String email) throws Exception {
        authToken = http.register(username, password, email);
    }

    public GameListResult listGames() throws Exception {
        return http.listGames();
    }

    public void createGame(String gameName) throws Exception {
        http.createGame(gameName);
    }

    public void joinGame(int gameID, String teamToJoin) throws Exception {
        http.joinGame(gameID, teamToJoin);
    }

    protected void clearServer() throws Exception {
        http.clearServer();
    }

    public void connectWS(String username) {
        try {
            ws = new WebsocketCommunicator(domain, this);
            currentUser = username;
        } catch (Exception e) {
            System.out.println("Failed to connect to server");
        }
    }

    public void sendCommand(UserGameCommand command) {
        String message = new Gson().toJson(command);
        try {
            ws.send(message);
        } catch (Exception e) {
            System.out.println("Failed to connect to server, please try again.");
        }
    }

    public void sendRedraw(int gameID) {
        sendCommand(new LoadGameCommand(authToken,gameID));
    }

    public void sendHighlight(int gameID, ChessPosition startPosition) {
        sendCommand(new LoadHighlightCommand(authToken,gameID,startPosition));
    }

    public void drawBoard(GameData gameData) {
        printer.printBoard(gameData,currentUser);
    }

    public void drawHighlightBoard(GameData gameData, Collection<ChessMove> possibleMoves) {
        printer.printBoard(gameData, currentUser, possibleMoves);
    }

}
