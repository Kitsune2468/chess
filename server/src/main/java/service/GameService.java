package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;

public class GameService {
    private GameDAO gameDAO;
    private AuthDAO authDAO;

    public GameService(GameDAO inputGameDAO, AuthDAO inputAuthDAO) {
        gameDAO = inputGameDAO;
        authDAO = inputAuthDAO;
    }
}
