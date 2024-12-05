package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import model.GameData;
import model.requests.GameListResult;
import model.requests.GameTemplateResult;
import ui.BoardPrinter;
import ui.GamePlayUI;
import websocket.commands.*;

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
    GamePlayUI gamePlayUI;

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
            System.out.println(e.getMessage());
        }
    }

    public void sendConnect(int gameID) {
        sendCommand(new UserGameCommand(UserGameCommand.CommandType.CONNECT,authToken,gameID));
    }

    public void sendRedraw(int gameID) {
        sendCommand(new LoadGameCommand(authToken,gameID));
    }

    public void sendHighlight(int gameID, ChessPosition startPosition) {
        sendCommand(new LoadHighlightCommand(authToken,gameID,startPosition));
    }

    public void sendMakeMove(int gameID, ChessMove move) {
        sendCommand(new MakeMoveCommand(authToken,gameID,move));
    }

    public void sendLeaveSession(int gameID) {
        sendCommand(new LeaveSessionCommand(authToken,gameID));
    }

    public void connectGameplayUI(GamePlayUI gamePlayUI) {
        this.gamePlayUI = gamePlayUI;
    }

    public void sendResign(int gameID) {
        sendCommand(new ResignCommand(authToken,gameID));
    }

    public void drawBoard(GameData gameData) {
        gamePlayUI.setGameDataForUI(gameData);
        printer.printBoard(gameData,currentUser);
    }

    public void drawHighlightBoard(GameData gameData, Collection<ChessMove> possibleMoves) {
        gamePlayUI.setGameDataForUI(gameData);
        printer.printBoard(gameData, currentUser, possibleMoves);
    }

}
