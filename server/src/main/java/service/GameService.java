package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;


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

    public
}
