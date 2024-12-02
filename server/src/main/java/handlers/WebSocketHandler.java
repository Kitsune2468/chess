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

public class WebSocketHandler {
    public WebSocketHandler() {
    }

    public void onConnect(Session session, String message) throws Exception {

    }
}
