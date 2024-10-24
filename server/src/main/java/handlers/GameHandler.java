package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.NotAuthorizedException;
import model.GameData;
import model.UserData;
import service.DataBaseService;
import service.GameService;
import service.Requests.CreateGameRequest;
import spark.Request;
import spark.Response;

public class GameHandler {
    GameService gameService;
    public GameHandler(GameService inputGameService) {
        gameService = inputGameService;
    }

    public Object createGame(Request request, Response result) throws NotAuthorizedException {
        String resultBody;
        String authToken = request.headers("authorization");
        CreateGameRequest newGameRequest = new Gson().fromJson(request.body(), CreateGameRequest.class);

        try {
            String gameName = newGameRequest.gameName();
            int gameID = gameService.createGame(gameName, authToken);
            GameData returnID = new GameData(gameID, null, null, null, null);
            resultBody = new Gson().toJson(returnID);
        } catch (DataAccessException e) {
            if (e.getMessage().toString().equals("unauthorized")) {
                result.status(401);
            } else if (e.getMessage().toString().equals("bad request")) {
                result.status(400);
            } else {
                result.status(500);
            }

            resultBody = "{ \"message\": \"Error: "+e.getMessage()+"\" }";
        }

        return resultBody;
    }
}
