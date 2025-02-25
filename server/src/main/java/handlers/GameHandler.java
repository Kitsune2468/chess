package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.GameData;
import service.GameService;
import model.requests.CreateGameRequest;
import model.requests.GameListResult;
import model.requests.JoinGameRequest;
import spark.Request;
import spark.Response;

public class GameHandler {
    GameService gameService;
    public GameHandler(GameService inputGameService) {
        gameService = inputGameService;
    }

    public Object createGame(Request request, Response result) throws DataAccessException {
        String resultBody;
        String authToken = request.headers("authorization");
        CreateGameRequest newGameRequest = new Gson().fromJson(request.body(), CreateGameRequest.class);

        try {
            String gameName = newGameRequest.gameName();
            int gameID = gameService.createGame(gameName, authToken);
            GameData returnID = new GameData(gameID, gameName, null, null, null, true);
            resultBody = new Gson().toJson(returnID);
        } catch (DataAccessException e) {
            if (e.getMessage().toString().equals("Unauthorized")) {
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

    public Object listGames(Request request, Response result) throws DataAccessException {
        String resultBody;
        String authToken = request.headers("authorization");

        try {
            GameListResult games = gameService.listGames(authToken);
            resultBody = new Gson().toJson(games);
        } catch (DataAccessException e) {
            if (e.getMessage().toString().equals("bad request")) {
                result.status(400);
            } else if (e.getMessage().toString().equals("Unauthorized")) {
                result.status(401);
            } else {
                result.status(500);
            }

            resultBody = "{ \"message\": \"Error: "+e.getMessage()+"\" }";
        }

        return resultBody;
    }

    public Object joinGame(Request request, Response result) throws DataAccessException {
        String resultBody = "{}";
        String authToken = request.headers("authorization");

        try {
            JoinGameRequest joinGameData = new Gson().fromJson(request.body(), JoinGameRequest.class);
            gameService.joinGame(joinGameData.gameID(),joinGameData.playerColor(),authToken);
        } catch (DataAccessException e) {
            if (e.getMessage().toString().equals("bad request")) {
                result.status(400);
            } else if (e.getMessage().toString().equals("Unauthorized")) {
                result.status(401);
            } else if (e.getMessage().toString().equals("already taken")) {
                result.status(403);
            } else {
                result.status(200);
            }

            resultBody = "{ \"message\": \"Error: "+e.getMessage()+"\" }";
        }

        return resultBody;
    }
}
