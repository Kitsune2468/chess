package service;

import dataaccess.GameDAO;

public class GameService {
    private GameDAO gameDAO;

    public GameService(GameDAO inputGameDAO) {
        this.gameDAO = inputGameDAO;
    }
}
