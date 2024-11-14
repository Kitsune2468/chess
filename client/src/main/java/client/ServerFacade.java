package client;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.requests.GameListResult;
import service.requests.GameTemplateResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class ServerFacade {
    String authToken;
    String serverUrl;

    public ServerFacade(int port) {
        serverUrl = "http://localhost:"+port;
    }

    public void login(String username, String password) throws DataAccessException {
        var body = Map.of("username", username, "password", password);
        var jsonBody = new Gson().toJson(body);
        Map resp = request("POST", "/session", jsonBody);
        if (resp.containsKey("Error")) {
            throw new DataAccessException("Failed to login");
        }
        authToken = (String)resp.get("authToken");
    }

    public void logout() throws DataAccessException {
        Map resp = request("DELETE", "/session", null);
        if (resp.containsKey("Error")) {
            throw new DataAccessException("Failed to logout");
        }
    }

    public void register(String username, String password, String email) throws DataAccessException {
        var body = Map.of("username", username, "password", password, "email", email);
        var jsonBody = new Gson().toJson(body);
        Map resp = request("POST", "/user", jsonBody);
        if (resp.containsKey("Error")) {
            throw new DataAccessException("Failed to register");
        }
        authToken = (String)resp.get("authToken");
    }

    public GameListResult listGames() {
//        Map resp = request("GET", "/game",null);
//        if (resp.containsKey("Error")) {
//            return null;
//        }
//        GameListResult games = new Gson().fromJson(resp, GameListResult.class);
//
//        return games;
        return null;
    }

    private Map request(String method, String endpoint, String jsonBody) {
        Map respMap;
        try {
            URI uri = new URI(serverUrl + endpoint);
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod(method);

            if (authToken != null) {
                http.addRequestProperty("authorization", authToken);
            }

            if (jsonBody != null) {
                http.setDoOutput(true);
                http.addRequestProperty("Content-Type", "application/json");
                try (var outputStream = http.getOutputStream()) {
                    outputStream.write(jsonBody.getBytes());
                }
            }

            http.connect();

            try {
                if (http.getResponseCode() == 401) {
                    return Map.of("Error", 401);
                }
            } catch (IOException e) {
                return Map.of("Error", 401);
            }


            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                respMap = new Gson().fromJson(inputStreamReader, Map.class);
            }

        } catch (URISyntaxException | IOException e) {
            return Map.of("Error", e.getMessage());
        }

        return respMap;
    }

    private String readerToString(InputStreamReader reader) {
        StringBuilder sb = new StringBuilder();
        try {
            for (int ch; (ch = reader.read()) != -1; ) {
                sb.append((char) ch);
            }
            return sb.toString();
        } catch (IOException e) {
            return "";
        }
    }

}
