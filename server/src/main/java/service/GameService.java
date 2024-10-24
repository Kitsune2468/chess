package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import service.requests.GameListResult;
import service.requests.GameTemplateResult;

import java.util.ArrayList;
import java.util.Objects;


public class GameService {
    private GameDAO gameDAO;
    private AuthDAO authDAO;

    public GameService(GameDAO inputGameDAO, AuthDAO inputAuthDAO) {
        gameDAO = inputGameDAO;
        authDAO = inputAuthDAO;
    }

    public GameData findGameByGameName(String gameName) {
        return gameDAO.getGameByString(gameName);
    }

    public int createGame(String newGameName, String authToken) throws DataAccessException {
        if (authDAO.getAuthByToken(authToken) == null) {
            throw new DataAccessException("unauthorized");
        }

        GameData foundGame = gameDAO.getGameByString(newGameName);
        if (foundGame != null) {
            throw new DataAccessException("bad request");
        }

        try {
            int gameID = gameDAO.addGame(newGameName);
            return gameID;
        } catch (DataAccessException e) {
            throw e;
        }
    }

    public GameListResult listGames(String authToken) throws DataAccessException {
        if (authDAO.getAuthByToken(authToken) == null) {
            throw new DataAccessException("unauthorized");
        }
        ArrayList<GameTemplateResult> games = gameDAO.getAllGames();
        return new GameListResult(games);
    }

    public void joinGame(int gameID, String joinColor, String authToken) throws DataAccessException {
        AuthData authData = authDAO.getAuthByToken(authToken);
        if (authData == null) {
            throw new DataAccessException("unauthorized");
        }
        GameData gameToJoin = gameDAO.getGameByID(gameID);
        if (gameToJoin == null || joinColor == null) {
            throw new DataAccessException("bad request");
        }
        String teamTaken;
        if (joinColor.equals("BLACK")) {
            teamTaken = gameToJoin.blackUsername();
            if (teamTaken != null) {
                throw new DataAccessException("already taken");
            }
        } else if (joinColor.equals("WHITE")) {
            teamTaken = gameToJoin.whiteUsername();
            if (teamTaken != null) {
                throw new DataAccessException("already taken");
            }
        }
        gameDAO.joinGame(gameID, joinColor, authData.username());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameService that = (GameService) o;
        return Objects.equals(gameDAO, that.gameDAO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameDAO, authDAO);
    }

    @Override
    public String toString() {
        return "GameService{" +
                "gameDAO=" + gameDAO +
                ", authDAO=" + authDAO +
                '}';
    }
}
