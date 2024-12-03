package client;

import com.google.gson.Gson;
import handlers.WebSocketHandler;
import model.requests.GameListResult;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Map;


public class ServerFacade {
    String authToken;
    String serverUrl;
    String domain;

    HttpCommunicator http;
    WebsocketCommunicator ws;

    public ServerFacade(String refDomain) {
        serverUrl = "http://"+refDomain;
        domain = refDomain;
        try {
            http = new HttpCommunicator(serverUrl, authToken);
            ws = null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void login(String username, String password) throws Exception {
//        var body = Map.of("username", username, "password", password);
//        var jsonBody = new Gson().toJson(body);
//        Map resp = request("POST", "/session", jsonBody);
//        if (resp.containsKey("Error")) {
//            throw new Exception("Failed to login");
//        }
//        authToken = (String)resp.get("authToken");
        http.login(username, password);
    }

    public void logout() throws Exception {
//        Map resp = request("DELETE", "/session", null);
//        if (resp.containsKey("Error")) {
//            throw new Exception("Failed to logout"+resp.get("Error").toString());
//        }
        http.logout();
    }

    public void register(String username, String password, String email) throws Exception {
//        var body = Map.of("username", username, "password", password, "email", email);
//        var jsonBody = new Gson().toJson(body);
//        Map resp = request("POST", "/user", jsonBody);
//        if (resp.containsKey("Error")) {
//            throw new Exception("Failed to register");
//        }
//        authToken = (String)resp.get("authToken");
        http.register(username, password, email);
    }

    public GameListResult listGames() throws Exception {
//        Map resp = request("GET", "/game",null);
//        if (resp.containsKey("Error")) {
//            return null;
//        }
//        String stringResp = new Gson().toJson(resp);
//        GameListResult games = new Gson().fromJson(stringResp, GameListResult.class);
//
//        if (games.games().isEmpty()) {
//            throw new Exception("No games found");
//        }
        GameListResult games = http.listGames();

        return games;
    }

    public void createGame(String gameName) throws Exception {
//        var body = Map.of("gameName", gameName);
//        var jsonBody = new Gson().toJson(body);
//        Map resp = request("POST", "/game", jsonBody);
//        if (resp.containsKey("Error")) {
//            throw new Exception("Failed to create game: "+resp.get("Error").toString());
//        }
        http.createGame(gameName);
    }

    public void joinGame(int gameID, String teamToJoin) throws Exception {
//        var body = Map.of("gameID", gameID,"playerColor",teamToJoin);
//        var jsonBody = new Gson().toJson(body);
//        Map resp = request("PUT", "/game", jsonBody);
//        if (resp.containsKey("Error")) {
//            throw new Exception("Failed to join game: "+resp.get("Error").toString());
//        }
        http.joinGame(gameID, teamToJoin);
    }

    protected void clearServer() throws Exception {
//        request("DELETE", "/db", null);
        http.clearServer();
    }

//    private Map request(String method, String endpoint, String jsonBody) {
//        Map respMap;
//        try {
//            URI uri = new URI(serverUrl + endpoint);
//            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
//            http.setRequestMethod(method);
//
//            if (authToken != null) {
//                http.addRequestProperty("authorization", authToken);
//            }
//
//            if (jsonBody != null) {
//                http.setDoOutput(true);
//                http.addRequestProperty("Content-Type", "application/json");
//                try (var outputStream = http.getOutputStream()) {
//                    outputStream.write(jsonBody.getBytes());
//                }
//            }
//
//            http.connect();
//
//            try {
//                if (http.getResponseCode() == 401) {
//                    return Map.of("Error", 401);
//                }
//            } catch (IOException e) {
//                return Map.of("Error", 401);
//            }
//
//
//            try (InputStream respBody = http.getInputStream()) {
//                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
//                respMap = new Gson().fromJson(inputStreamReader, Map.class);
//            }
//
//        } catch (URISyntaxException | IOException e) {
//            return Map.of("Error", e.getMessage());
//        }
//
//        return respMap;
//    }

    public void connectWS() {
        try {
            ws = new WebsocketCommunicator(domain);
        } catch (Exception e) {
            System.out.println("Failed to connect to server");
        }
    }

    public void sendMessage(UserGameCommand command) {
        String message = new Gson().toJson(command);
        try {
            ws.send(message);
        } catch (Exception e) {
            System.out.println("Failed to connect to server, please try again.");
        }
    }

}
