package client;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.requests.GameTemplateResult;

import java.net.*;
import java.util.ArrayList;
import java.util.Map;

public class ServerFacade {
    String authToken;
    String serverUrl;

    public ServerFacade(int port) {
        serverUrl = "http://localhost:"+port;
    }

    public void login(String username, String password) throws DataAccessException {

    }

    public void register(String username, String password, String email) throws DataAccessException {
        var body = Map.of("username", username, "password", password, "email", email);
        var jsonBody = new Gson().toJson(body);
        Map resp = request("POST", "/user", jsonBody);
        if (resp.containsKey("Error")) {
            throw new DataAccessException("Failed to register");
        }
        authToken = (String) resp.get("authToken");
        return;
    }

    public ArrayList<GameTemplateResult> listGames() {
        return null;
    }

}
